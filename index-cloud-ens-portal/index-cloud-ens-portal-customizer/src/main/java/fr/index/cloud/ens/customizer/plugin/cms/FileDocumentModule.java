package fr.index.cloud.ens.customizer.plugin.cms;

import fr.toutatice.portail.cms.nuxeo.api.ContextualizationHelper;
import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoDocumentContext;
import fr.toutatice.portail.cms.nuxeo.api.portlet.PortletModule;
import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.Constants;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.cms.DocumentType;
import org.osivia.portal.api.cms.Permissions;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.locator.Locator;
import org.osivia.portal.api.urls.IPortalUrlFactory;
import org.osivia.portal.api.urls.PortalUrlType;
import org.osivia.portal.api.windows.PortalWindow;
import org.osivia.portal.api.windows.WindowFactory;
import org.osivia.portal.core.cms.CMSException;
import org.osivia.portal.core.cms.CMSServiceCtx;
import org.osivia.portal.core.cms.ICMSService;
import org.osivia.portal.core.cms.ICMSServiceLocator;
import org.osivia.portal.core.page.PageProperties;

import javax.portlet.*;
import java.util.HashMap;
import java.util.Map;

/**
 * File document module.
 *
 * @author Cédric Krommenhoek
 * @see PortletModule
 */
public class FileDocumentModule extends PortletModule {

    /**
     * Portal URL factory.
     */
    private final IPortalUrlFactory portalUrlFactory;

    /**
     * CMS service locator.
     */
    private final ICMSServiceLocator cmsServiceLocator;


    /**
     * Constructor.
     *
     * @param portletContext portlet context
     */
    public FileDocumentModule(PortletContext portletContext) {
        super(portletContext);

        // Portal URL factory
        this.portalUrlFactory = Locator.findMBean(IPortalUrlFactory.class, IPortalUrlFactory.MBEAN_NAME);
        // CMS service locator
        this.cmsServiceLocator = Locator.findMBean(ICMSServiceLocator.class, ICMSServiceLocator.MBEAN_NAME);
    }


    @Override
    protected void doView(RenderRequest request, RenderResponse response, PortletContext portletContext) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(portletContext, request, response);
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // Document path
        String path = getDocumentPath(nuxeoController);

        if (StringUtils.isNotBlank(path)) {
            // Document context
            NuxeoDocumentContext documentContext = nuxeoController.getDocumentContext(path);

            if (ContextualizationHelper.isCurrentDocContextualized(portalControllerContext)) {
                // Document type
                DocumentType type = documentContext.getDocumentType();
                // Permissions
                Permissions permissions = documentContext.getPermissions();

                if (permissions.isEditable()) {
                    // Rename URL
                    String renameUrl = this.getRenameUrl(portalControllerContext, path);
                    request.setAttribute("renameUrl", renameUrl);

                    // Edit URL
                    String editUrl = this.getEditUrl(portalControllerContext, nuxeoController, path);
                    request.setAttribute("editUrl", editUrl);
                }

                // Deletable indicator
                request.setAttribute("deletable", permissions.isDeletable());
            }
        }
    }


    /**
     * Get rename URL.
     *
     * @param portalControllerContext portal controller context
     * @param path                    document path
     * @return URL
     */
    private String getRenameUrl(PortalControllerContext portalControllerContext, String path) throws PortletException {
        // Window properties
        Map<String, String> properties = new HashMap<>();
        properties.put("osivia.rename.path", path);

        // URL
        String url;
        try {
            url = this.portalUrlFactory.getStartPortletUrl(portalControllerContext, "osivia-services-widgets-rename-instance", properties,
                    PortalUrlType.MODAL);
        } catch (PortalException e) {
            throw new PortletException(e);
        }

        return url;
    }


    /**
     * Get edit URL.
     *
     * @param portalControllerContext portal controller context
     * @param nuxeoController         Nuxeo controller
     * @param path                    document path
     * @return URL
     */
    private String getEditUrl(PortalControllerContext portalControllerContext, NuxeoController nuxeoController, String path) throws PortletException {
        // Window properties
        Map<String, String> properties = new HashMap<>();
        properties.put("osivia.document.edition.base-path", nuxeoController.getBasePath());
        properties.put("osivia.document.edition.path", path);

        // URL
        String url;
        try {
            url = this.portalUrlFactory.getStartPortletUrl(portalControllerContext, "osivia-services-document-edition-instance", properties,
                    PortalUrlType.MODAL);
        } catch (PortalException e) {
            throw new PortletException(e);
        }

        return url;
    }


    @Override
    protected void processAction(ActionRequest request, ActionResponse response, PortletContext portletContext) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(portletContext, request, response);
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // Document path
        String path = this.getDocumentPath(nuxeoController);

        // Action name
        String action = request.getParameter(ActionRequest.ACTION_NAME);

        if ("mutualize".equals(action)) {
            // Nuxeo command
            INuxeoCommand command = new MutualizeDocumentCommand(path);
            nuxeoController.executeNuxeoCommand(command);

            // Refresh
            PageProperties.getProperties().setRefreshingPage(true);
        } else if ("delete".equals(action)) {
            // CMS service
            ICMSService cmsService = this.cmsServiceLocator.getCMSService();
            // CMS context
            CMSServiceCtx cmsContext = new CMSServiceCtx();
            cmsContext.setPortalControllerContext(portalControllerContext);

            // Document context
            NuxeoDocumentContext documentContext = nuxeoController.getDocumentContext(path);
            // Document
            Document document = documentContext.getDocument();

            try {
                cmsService.putDocumentInTrash(cmsContext, document.getId());
            } catch (CMSException e) {
                throw new PortletException(e);
            }
        }
    }


    /**
     * Get document path.
     *
     * @param nuxeoController Nuxeo controller
     * @return path
     */
    private String getDocumentPath(NuxeoController nuxeoController) {
        // Current window
        PortalWindow window = WindowFactory.getWindow(nuxeoController.getRequest());

        return nuxeoController.getComputedPath(window.getProperty(Constants.WINDOW_PROP_URI));
    }

}
