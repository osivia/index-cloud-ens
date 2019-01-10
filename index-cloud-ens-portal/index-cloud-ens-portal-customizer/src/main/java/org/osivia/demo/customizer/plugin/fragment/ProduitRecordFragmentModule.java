package org.osivia.demo.customizer.plugin.fragment;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.activation.MimeType;
import javax.activation.MimeTypeParseException;
import javax.portlet.ActionRequest;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.RenderResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.portlet.PortletFileUpload;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.PropertyList;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.demo.customizer.plugin.DemoUtils;
import org.osivia.portal.api.Constants;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.internationalization.IInternationalizationService;
import org.osivia.portal.api.locator.Locator;
import org.osivia.portal.api.notifications.INotificationsService;
import org.osivia.portal.api.notifications.NotificationsType;
import org.osivia.portal.api.portlet.model.UploadedFile;
import org.osivia.portal.api.portlet.model.UploadedFileMetadata;
import org.osivia.portal.api.windows.PortalWindow;
import org.osivia.portal.api.windows.WindowFactory;
import org.springframework.web.multipart.MultipartFile;

import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoDocumentContext;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterException;
import fr.toutatice.portail.cms.nuxeo.api.forms.IFormsService;
import fr.toutatice.portail.cms.nuxeo.api.fragment.FragmentModule;
import fr.toutatice.portail.cms.nuxeo.api.services.dao.DocumentDAO;

/**
 * @author Dorian Licois
 */
public class ProduitRecordFragmentModule extends FragmentModule {

    /** Record property fragment identifier. */
    public static final String ID = "record_property";

    /** Nuxeo path window property name. */
    public static final String NUXEO_PATH_WINDOW_PROPERTY = Constants.WINDOW_PROP_URI;

    private static final String LAUNCH_PROCEDURE_PROPERTY = "osivia.launch.procedure.webid";

    private static final String SUPPORT_PROCEDURE_WEBID = "procedure_demande_support";

    private static final String SUPPORT_PROCEDURE_SUBMIT_ACTION_ID = "00";

    /** Temporary file prefix. */
    private static final String TEMPORARY_FILE_PREFIX = "procedure-file-";
    /** Temporary file suffix. */
    private static final String TEMPORARY_FILE_SUFFIX = ".tmp";

    /** JSP name. */
    private static final String JSP_NAME = "product";

    private INotificationsService notificationsService;

    private IBundleFactory bundleFactory;

    public ProduitRecordFragmentModule(PortletContext portletContext) {
        super(portletContext);

        notificationsService = Locator.findMBean(INotificationsService.class, INotificationsService.MBEAN_NAME);

        IInternationalizationService internationalizationService = Locator.findMBean(IInternationalizationService.class,
                IInternationalizationService.MBEAN_NAME);
        bundleFactory = internationalizationService.getBundleFactory(this.getClass().getClassLoader());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void doView(PortalControllerContext portalControllerContext) throws PortletException {
        // Request
        PortletRequest request = portalControllerContext.getRequest();
        // Response
        RenderResponse response = (RenderResponse) portalControllerContext.getResponse();
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(request, response, portalControllerContext.getPortletCtx());

        // Current window
        PortalWindow window = WindowFactory.getWindow(request);
        // Nuxeo path
        String nuxeoPath = window.getProperty(NUXEO_PATH_WINDOW_PROPERTY);

        if (StringUtils.isNotEmpty(nuxeoPath)) {
            // Computed path
            nuxeoPath = nuxeoController.getComputedPath(nuxeoPath);

            // Nuxeo document
            NuxeoDocumentContext documentContext = nuxeoController.getDocumentContext(nuxeoPath);
            Document document = documentContext.getDenormalizedDocument();

            PropertyMap docProperties = document.getProperties();
            PropertyMap dataMap = docProperties.getMap(DemoUtils.RECORD_PROPERTY_DATA);
            if (dataMap != null) {
                // title
                request.setAttribute("title", dataMap.getString(DemoUtils.RECORD_PROPERTY_TITLE));

                // title
                request.setAttribute("produitWebid", docProperties.getString("ttc:webid"));

                // visuel
                PropertyMap visuelMap = dataMap.getMap(DemoUtils.PRODUCT_PROPERTY_VISUEL);
                if (visuelMap != null) {
                    request.setAttribute("visuelUrl", DemoUtils.getFileUrl(nuxeoController, docProperties, visuelMap.getString("digest"), document.getPath()));
                    request.setAttribute("visuelFilename", visuelMap.getString("fileName"));
                }

                // description
                request.setAttribute("description", dataMap.getString(DemoUtils.PRODUCT_PROPERTY_DESCRIPTION));

                // documents
                PropertyList documentsList = dataMap.getList(DemoUtils.PRODUCT_PROPERTY_DOCUMENTS);
                if (documentsList != null) {
                    List<ProduitDocument> files = new ArrayList<ProduitRecordFragmentModule.ProduitDocument>();
                    for (Object documentO : documentsList.list()) {
                        PropertyMap documentMap = (PropertyMap) documentO;
                        PropertyMap documentDataMap = documentMap.getMap(DemoUtils.RECORD_PROPERTY_DATA);
                        if (documentDataMap != null) {
                            files.add(buildProduitDocument(nuxeoController, documentMap, documentDataMap));
                        }
                    }
                    request.setAttribute("files", files);
                }
            }
        }
    }

    /**
     * Creates a ProduitDocument from metadata
     *
     * @param nuxeoController
     * @param documentMap
     * @param documentDataMap
     * @return
     */
    private ProduitDocument buildProduitDocument(NuxeoController nuxeoController, PropertyMap documentMap,
            PropertyMap documentDataMap) {
        String documentPath = documentMap.getString(DemoUtils.DOCUMENT_PROPERTY_PATH);

        PropertyMap fichierMap = documentDataMap.getMap(DemoUtils.DOCUMENTS_PROPERTY_FILES);
        String fileDigest = fichierMap.getString("digest");
        String fileUrl = DemoUtils.getFileUrl(nuxeoController, documentMap, fileDigest, documentPath);

        ProduitDocument upFile = new ProduitDocument();
        upFile.setUrl(fileUrl);
        upFile.setIcon(DemoUtils.getFileIcon(documentMap, fileDigest));
        upFile.setFileName(fichierMap.getString("fileName"));
        upFile.setDocumentTitle(documentDataMap.getString(DemoUtils.RECORD_PROPERTY_TITLE));
        return upFile;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void doAdmin(PortalControllerContext portalControllerContext) throws PortletException {
        // Request
        PortletRequest request = portalControllerContext.getRequest();

        // Current window
        PortalWindow window = WindowFactory.getWindow(request);

        // Nuxeo path
        String nuxeoPath = window.getProperty(NUXEO_PATH_WINDOW_PROPERTY);
        request.setAttribute("nuxeoPath", nuxeoPath);

        String procedureWebid = window.getProperty(LAUNCH_PROCEDURE_PROPERTY);
        request.setAttribute("procedureWebid", procedureWebid);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void processAction(PortalControllerContext portalControllerContext) throws PortletException {
        PortletRequest request = portalControllerContext.getRequest();
        PortletResponse response = portalControllerContext.getResponse();

        if ("admin".equals(request.getPortletMode().toString()) && "save".equals(request.getParameter(ActionRequest.ACTION_NAME))) {
            // Current window
            PortalWindow window = WindowFactory.getWindow(request);

            window.setProperty(NUXEO_PATH_WINDOW_PROPERTY, StringUtils.trimToNull(request.getParameter("nuxeoPath")));
            window.setProperty(LAUNCH_PROCEDURE_PROPERTY, StringUtils.trimToNull(request.getParameter("procedureWebid")));
        } else if ("view".equals(request.getPortletMode().toString()) && "submit".equals(request.getParameter(ActionRequest.ACTION_NAME))) {
            NuxeoController nuxeoController = new NuxeoController(request, response, portalControllerContext.getPortletCtx());
            IFormsService formsService = nuxeoController.getNuxeoCMSService().getFormsService();

            if (request instanceof ActionRequest) {
                DiskFileItemFactory fileItemFactory = new DiskFileItemFactory();
                PortletFileUpload fileUpload = new PortletFileUpload(fileItemFactory);

                try {
                    List<FileItem> parseRequest = fileUpload.parseRequest((ActionRequest) request);

                    // procedure variables
                    Map<String, String> variables = new HashMap<>(2);
                    Map<String, UploadedFile> uploadedFiles = new HashMap<>(1);
                    for (FileItem fileItem : parseRequest) {

                        if (fileItem.isFormField()) {
                            if (StringUtils.equals("supportText", fileItem.getFieldName())) {
                                variables.put("demande", fileItem.getString());
                            } else if (StringUtils.equals("produit", fileItem.getFieldName())) {
                                variables.put("produit", fileItem.getString());
                            }
                        } else {
                            // uploaded files
                            addUploadedFile(uploadedFiles, fileItem);
                        }
                    }

                    // start procedure
                    formsService.start(portalControllerContext, SUPPORT_PROCEDURE_WEBID, SUPPORT_PROCEDURE_SUBMIT_ACTION_ID, variables, uploadedFiles);

                    // success notification
                    Bundle bundle = bundleFactory.getBundle(request.getLocale());
                    notificationsService.addSimpleNotification(portalControllerContext, bundle.getString("FRAGMENT_PRODUCT_RECORD_SUPPORT_SUCCESS"),
                            NotificationsType.SUCCESS);
                } catch (PortalException | IOException | FileUploadException e) {
                    throw new PortletException(e);
                } catch (FormFilterException e) {
                    notificationsService.addSimpleNotification(portalControllerContext, e.getMessage(), NotificationsType.ERROR);
                }
            }
        }
    }

    private void addUploadedFile(Map<String, UploadedFile> uploadedFiles, FileItem fileItem) throws IOException {

        if (fileItem.getSize() > 0) {
            ProcedureUploadedFile uploadedFile = new ProcedureUploadedFile();
            uploadedFile.setUpload(null);
            File temporaryFile = File.createTempFile(TEMPORARY_FILE_PREFIX, TEMPORARY_FILE_SUFFIX);
            temporaryFile.deleteOnExit();
            FileUtils.writeByteArrayToFile(temporaryFile, fileItem.get());
            uploadedFile.setTemporaryFile(temporaryFile);
            uploadedFile.setTemporaryMetadata(getFileMetadata(fileItem));
            uploadedFile.setDeleted(false);
            uploadedFiles.put("pj", uploadedFile);
        }
    }

    private ProcedureUploadedFileMetadata getFileMetadata(FileItem fileItem) {
        ProcedureUploadedFileMetadata metadata = new ProcedureUploadedFileMetadata();

        // File name
        metadata.setFileName(FilenameUtils.getName(fileItem.getName()));
        // Mime type
        MimeType mimeType;
        try {
            mimeType = new MimeType(fileItem.getContentType());
        } catch (MimeTypeParseException e) {
            mimeType = null;
        }
        metadata.setMimeType(mimeType);
        // Icon
        String icon = DocumentDAO.getInstance().getIcon(mimeType);
        metadata.setIcon(icon);
        return metadata;
    }


    @Override
    public String getViewJSPName() {
        return JSP_NAME;
    }

    @Override
    public String getAdminJSPName() {
        return JSP_NAME;
    }

    @Override
    public boolean isDisplayedInAdmin() {
        return true;
    }

    private class ProcedureUploadedFileMetadata implements UploadedFileMetadata {

        /** File name. */
        private String fileName;
        /** File MIME type. */
        private MimeType mimeType;
        /** File type icon. */
        private String icon;


        /**
         * Constructor.
         */
        public ProcedureUploadedFileMetadata() {
            super();
        }


        /**
         * {@inheritDoc}
         */
        @Override
        public String getFileName() {
            return this.fileName;
        }


        /**
         * {@inheritDoc}
         */
        @Override
        public MimeType getMimeType() {
            return this.mimeType;
        }


        /**
         * {@inheritDoc}
         */
        @Override
        public String getIcon() {
            return this.icon;
        }


        /**
         * Setter for fileName.
         *
         * @param fileName the fileName to set
         */
        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        /**
         * Setter for mimeType.
         *
         * @param mimeType the mimeType to set
         */
        public void setMimeType(MimeType mimeType) {
            this.mimeType = mimeType;
        }

        /**
         * Setter for icon.
         *
         * @param icon the icon to set
         */
        public void setIcon(String icon) {
            this.icon = icon;
        }

    }

    /**
     * Document in a product
     *
     * @author Dorian Licois
     */
    public class ProduitDocument {

        /** documentTitle */
        private String documentTitle;
        /** url */
        private String url;
        /** File name. */
        private String fileName;
        /** File type icon. */
        private String icon;

        /**
         * Getter for url.
         *
         * @return the url
         */
        public String getUrl() {
            return url;
        }

        /**
         * Setter for url.
         *
         * @param url the url to set
         */
        public void setUrl(String url) {
            this.url = url;
        }

        /**
         * Getter for fileName.
         *
         * @return the fileName
         */
        public String getFileName() {
            return fileName;
        }

        /**
         * Setter for fileName.
         *
         * @param fileName the fileName to set
         */
        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        /**
         * Getter for icon.
         *
         * @return the icon
         */
        public String getIcon() {
            return icon;
        }

        /**
         * Setter for icon.
         *
         * @param icon the icon to set
         */
        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getDocumentTitle() {
            return documentTitle;
        }

        public void setDocumentTitle(String documentTitle) {
            this.documentTitle = documentTitle;
        }

    }

    private class ProcedureUploadedFile implements UploadedFile {

        /** Uploaded multipart file. */
        private MultipartFile upload;

        /** Original file URL. */
        private String url;
        /** Original file index. */
        private Integer index;
        /** Original file metadata. */
        private ProcedureUploadedFileMetadata originalMetadata;
        /** Uploaded temporary file. */
        private File temporaryFile;
        /** Temporary file metadata. */
        private ProcedureUploadedFileMetadata temporaryMetadata;
        /** Deleted file indicator. */
        private boolean deleted;


        /**
         * Constructor.
         */
        public ProcedureUploadedFile() {
            super();
        }


        /**
         * {@inheritDoc}
         */
        @Override
        public String getUrl() {
            return this.url;
        }


        /**
         * {@inheritDoc}
         */
        @Override
        public Integer getIndex() {
            return this.index;
        }


        /**
         * {@inheritDoc}
         */
        @Override
        public ProcedureUploadedFileMetadata getOriginalMetadata() {
            return this.originalMetadata;
        }


        /**
         * {@inheritDoc}
         */
        @Override
        public File getTemporaryFile() {
            return this.temporaryFile;
        }


        /**
         * {@inheritDoc}
         */
        @Override
        public ProcedureUploadedFileMetadata getTemporaryMetadata() {
            return this.temporaryMetadata;
        }


        /**
         * {@inheritDoc}
         */
        @Override
        public boolean isDeleted() {
            return this.deleted;
        }


        /**
         * Getter for upload.
         *
         * @return the upload
         */
        public MultipartFile getUpload() {
            return upload;
        }

        /**
         * Setter for upload.
         *
         * @param upload the upload to set
         */
        public void setUpload(MultipartFile upload) {
            this.upload = upload;
        }

        /**
         * Setter for url.
         *
         * @param url the url to set
         */
        public void setUrl(String url) {
            this.url = url;
        }

        /**
         * Setter for index.
         *
         * @param index the index to set
         */
        public void setIndex(Integer index) {
            this.index = index;
        }

        /**
         * Setter for originalMetadata.
         *
         * @param originalMetadata the originalMetadata to set
         */
        public void setOriginalMetadata(ProcedureUploadedFileMetadata originalMetadata) {
            this.originalMetadata = originalMetadata;
        }

        /**
         * Setter for temporaryFile.
         *
         * @param temporaryFile the temporaryFile to set
         */
        public void setTemporaryFile(File temporaryFile) {
            this.temporaryFile = temporaryFile;
        }

        /**
         * Setter for temporaryMetadata.
         *
         * @param temporaryMetadata the temporaryMetadata to set
         */
        public void setTemporaryMetadata(ProcedureUploadedFileMetadata temporaryMetadata) {
            this.temporaryMetadata = temporaryMetadata;
        }

        /**
         * Setter for deleted.
         *
         * @param deleted the deleted to set
         */
        public void setDeleted(boolean deleted) {
            this.deleted = deleted;
        }

    }
}
