package fr.index.cloud.ens.customizer.plugin.cms;

import fr.toutatice.portail.cms.nuxeo.api.ContextualizationHelper;
import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoDocumentContext;
import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoPublicationInfos;
import fr.toutatice.portail.cms.nuxeo.api.domain.DocumentDTO;
import fr.toutatice.portail.cms.nuxeo.api.domain.RemotePublishedDocumentDTO;
import fr.toutatice.portail.cms.nuxeo.api.portlet.PortletModule;
import fr.toutatice.portail.cms.nuxeo.api.services.dao.DocumentDAO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.Documents;
import org.osivia.portal.api.Constants;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.cms.Permissions;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.locator.Locator;
import org.osivia.portal.api.urls.IPortalUrlFactory;
import org.osivia.portal.api.urls.PortalUrlType;
import org.osivia.portal.api.windows.PortalWindow;
import org.osivia.portal.api.windows.WindowFactory;

import javax.portlet.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * File document module.
 *
 * @author Cédric Krommenhoek
 * @see PortletModule
 */
public class FileDocumentModule extends PortletModule {

    /**
     * Mutualized space path.
     */
    private static final String MUTUALIZED_SPACE_PATH = System.getProperty("config.mutualized.path");


    /**
     * Portal URL factory.
     */
    private final IPortalUrlFactory portalUrlFactory;

    /**
     * Document DAO.
     */
    private final DocumentDAO documentDao;


    /**
     * Constructor.
     *
     * @param portletContext portlet context
     */
    public FileDocumentModule(PortletContext portletContext) {
        super(portletContext);

        // Portal URL factory
        this.portalUrlFactory = Locator.findMBean(IPortalUrlFactory.class, IPortalUrlFactory.MBEAN_NAME);
        // Document DAO
        this.documentDao = DocumentDAO.getInstance();
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
            // Publication infos
            NuxeoPublicationInfos publicationInfos = documentContext.getPublicationInfos();

            if (publicationInfos.isPublished()) {
                // Read only indicator
                request.setAttribute("readOnly", true);

                // Copy
                String copyUrl = this.getCopyUrl(portalControllerContext, path);
                request.setAttribute("copyUrl", copyUrl);

            } else if (ContextualizationHelper.isCurrentDocContextualized(portalControllerContext)) {
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

                // Desynchronized indicator
                boolean desynchronized = this.isDesynchronized(portalControllerContext, documentContext);
                request.setAttribute("desynchronized", desynchronized);

                // Source
                DocumentDTO source = this.getSource(nuxeoController, documentContext);
                request.setAttribute("source", source);
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
     * Get copy URL.
     *
     * @param portalControllerContext portal controller context
     * @param path                    document path
     * @return URL
     */
    private String getCopyUrl(PortalControllerContext portalControllerContext, String path) throws PortletException {
        // Window properties
        Map<String, String> properties = new HashMap<>();
        properties.put("osivia.copy.path", path);

        // URL
        String url;
        try {
            url = this.portalUrlFactory.getStartPortletUrl(portalControllerContext, "index-cloud-ens-mutualization-copy-instance", properties,
                    PortalUrlType.MODAL);
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
     * Check if mutualized document is desynchronized.
     *
     * @param portalControllerContext portal controller context
     * @param documentContext         document context
     * @return true if document is desynchronized
     */
    private boolean isDesynchronized(PortalControllerContext portalControllerContext, NuxeoDocumentContext documentContext) {
        // Portlet request
        PortletRequest request = portalControllerContext.getRequest();

        // Document
        DocumentDTO document = (DocumentDTO) request.getAttribute("document");

        // Published documents
        List<RemotePublishedDocumentDTO> publishedDocuments;
        if (document == null) {
            publishedDocuments = null;
        } else {
            publishedDocuments = document.getPublishedDocuments();
        }

        // Mutualized document
        RemotePublishedDocumentDTO mutualizedDocument = null;
        if (CollectionUtils.isNotEmpty(publishedDocuments)) {
            Iterator<RemotePublishedDocumentDTO> iterator = publishedDocuments.iterator();
            while ((mutualizedDocument == null) && iterator.hasNext()) {
                RemotePublishedDocumentDTO publishedDocument = iterator.next();

                // Published document path
                String publishedDocumentPath = publishedDocument.getPath();
                if (!StringUtils.startsWith(publishedDocumentPath, "/")) {
                    publishedDocumentPath = "/" + publishedDocumentPath;
                }

                if (StringUtils.startsWith(publishedDocumentPath, MUTUALIZED_SPACE_PATH)) {
                    mutualizedDocument = publishedDocument;
                }
            }
        }


        // Desynchronized indicator
        boolean desynchronized;

        if (mutualizedDocument == null) {
            desynchronized = false;
        } else {
            // Publication infos
            NuxeoPublicationInfos publicationInfos = documentContext.getPublicationInfos();

            desynchronized = !StringUtils.equals(mutualizedDocument.getVersionLabel(), publicationInfos.getLiveVersion());
        }

        return desynchronized;
    }


    /**
     * Get source document DTO.
     *
     * @param nuxeoController Nuxeo controller
     * @param documentContext document context
     * @return document DTO
     */
    private DocumentDTO getSource(NuxeoController nuxeoController, NuxeoDocumentContext documentContext) {
        // Document
        Document document = documentContext.getDocument();

        // Source webId
        String sourceWebId = document.getString("mtz:sourceWebId");

        // Source
        DocumentDTO source;
        if (StringUtils.isEmpty(sourceWebId)) {
            source = null;
        } else {
            // Nuxeo command
            INuxeoCommand command = new GetSourceByWebIdCommand(sourceWebId);
            Documents documents = (Documents) nuxeoController.executeNuxeoCommand(command);

            if (documents.size() == 1) {
                source = this.documentDao.toDTO(documents.get(0));
            } else {
                source = null;
            }
        }

        return source;
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
