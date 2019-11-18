package fr.index.cloud.ens.customizer.plugin.cms;

import fr.toutatice.portail.cms.nuxeo.api.ContextualizationHelper;
import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoDocumentContext;
import fr.toutatice.portail.cms.nuxeo.api.portlet.PortletModule;
import org.apache.commons.lang.StringUtils;
import org.osivia.portal.api.Constants;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.cms.Permissions;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.locator.Locator;
import org.osivia.portal.api.urls.IPortalUrlFactory;
import org.osivia.portal.api.urls.PortalUrlType;
import org.osivia.portal.api.windows.PortalWindow;
import org.osivia.portal.api.windows.WindowFactory;
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
     * Constructor.
     *
     * @param portletContext portlet context
     */
    public FileDocumentModule(PortletContext portletContext) {
        super(portletContext);

        // Portal URL factory
        this.portalUrlFactory = Locator.findMBean(IPortalUrlFactory.class, IPortalUrlFactory.MBEAN_NAME);
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
                // Permissions
                Permissions permissions = documentContext.getPermissions();

                // Mutualize URL
                String mutualizeUrl = this.getMutualizeUrl(portalControllerContext, path);
                request.setAttribute("mutualizeUrl", mutualizeUrl);

                if (permissions.isEditable()) {
                    // Rename URL
                    String renameUrl = this.getRenameUrl(portalControllerContext, path);
                    request.setAttribute("renameUrl", renameUrl);

                    // Edit URL
                    String editUrl = this.getEditUrl(portalControllerContext, nuxeoController, path);
                    request.setAttribute("editUrl", editUrl);
                }

                if (permissions.isDeletable()) {
                    // Delete URL
                    String deleteUrl = this.getDeleteUrl(portalControllerContext, path);
                    request.setAttribute("deleteUrl", deleteUrl);
                }
            }
        }
    }


    /**
     * Get mutualize URL.
     *
     * @param portalControllerContext portal controller context
     * @param path                    document path
     * @return URL
     */
    private String getMutualizeUrl(PortalControllerContext portalControllerContext, String path) throws PortletException {
        // Window properties
        Map<String, String> properties = new HashMap<>();
        properties.put("osivia.mutualize.path", path);

        // URL
        String url;
        try {
            url = this.portalUrlFactory.getStartPortletUrl(portalControllerContext, "index-cloud-ens-mutualization-instance", properties, PortalUrlType.MODAL);
        } catch (PortalException e) {
            throw new PortletException(e);
        }

        return url;
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


    /**
     * Get delete URL.
     *
     * @param portalControllerContext portal controller context
     * @param path                    document path
     * @return URL
     */
    private String getDeleteUrl(PortalControllerContext portalControllerContext, String path) throws PortletException {
        // Window properties
        Map<String, String> properties = new HashMap<>();
        properties.put("osivia.delete.path", path);

        // URL
        String url;
        try {
            url = this.portalUrlFactory.getStartPortletUrl(portalControllerContext, "osivia-services-widgets-delete-instance", properties, PortalUrlType.MODAL);
        } catch (PortalException e) {
            throw new PortletException(e);
        }

        return url;
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
