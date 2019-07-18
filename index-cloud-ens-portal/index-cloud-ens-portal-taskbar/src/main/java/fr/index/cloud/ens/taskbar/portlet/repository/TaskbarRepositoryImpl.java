package fr.index.cloud.ens.taskbar.portlet.repository;

import fr.index.cloud.ens.taskbar.portlet.model.AddDropdownItem;
import fr.index.cloud.ens.taskbar.portlet.model.FolderTask;
import fr.index.cloud.ens.taskbar.portlet.model.Task;
import fr.index.cloud.ens.taskbar.portlet.model.TaskbarWindowProperties;
import fr.index.cloud.ens.taskbar.portlet.model.comparator.FolderTaskComparator;
import fr.index.cloud.ens.taskbar.portlet.repository.command.MoveDocumentsCommand;
import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoDocumentContext;
import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoPermissions;
import fr.toutatice.portail.cms.nuxeo.api.services.INuxeoCustomizer;
import fr.toutatice.portail.cms.nuxeo.api.services.INuxeoService;
import fr.toutatice.portail.cms.nuxeo.api.services.tag.INuxeoTagService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;
import org.jboss.portal.theme.impl.render.dynamic.DynaRenderOptions;
import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.Constants;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.cms.DocumentType;
import org.osivia.portal.api.cms.FileMimeType;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.html.DOM4JUtils;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.taskbar.ITaskbarService;
import org.osivia.portal.api.taskbar.TaskbarTask;
import org.osivia.portal.api.urls.IPortalUrlFactory;
import org.osivia.portal.api.urls.PortalUrlType;
import org.osivia.portal.core.cms.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Repository;

import javax.portlet.PortletException;
import java.io.IOException;
import java.util.*;

/**
 * Taskbar portlet repository implementation.
 *
 * @author Cédric Krommenhoek
 * @see TaskbarRepository
 */
@Repository
public class TaskbarRepositoryImpl implements TaskbarRepository {

    /**
     * Document edition portlet instance.
     */
    private static final String DOCUMENT_EDITION_PORTLET_INSTANCE = "osivia-services-document-edition-instance";
    /**
     * Live document creation portlet instance.
     */
    private static final String LIVE_DOCUMENT_CREATION_PORTLET_INSTANCE = "osivia-services-document-creation-portletInstance";


    /**
     * Application context.
     */
    @Autowired
    private ApplicationContext applicationContext;

    /**
     * Portal URL factory.
     */
    @Autowired
    private IPortalUrlFactory portalUrlFactory;

    /**
     * Taskbar service.
     */
    @Autowired
    private ITaskbarService taskbarService;

    /**
     * Internationalization bundle factory.
     */
    @Autowired
    private IBundleFactory bundleFactory;

    /**
     * CMS service locator.
     */
    @Autowired
    private ICMSServiceLocator cmsServiceLocator;

    /**
     * Nuxeo service.
     */
    @Autowired
    private INuxeoService nuxeoService;

    /**
     * Folder task comparator.
     */
    @Autowired
    private FolderTaskComparator folderTaskComparator;


    /**
     * Constructor.
     */
    public TaskbarRepositoryImpl() {
        super();
    }


    @Override
    public String getBasePath(PortalControllerContext portalControllerContext, TaskbarWindowProperties windowProperties) {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // Path window property, may be null
        String path = windowProperties.getPath();

        // Base path
        String basePath;
        if (StringUtils.isEmpty(path)) {
            basePath = nuxeoController.getBasePath();
        } else {
            basePath = nuxeoController.getComputedPath(path);
        }

        return basePath;
    }

    @Override
    public List<TaskbarTask> getNavigationTasks(PortalControllerContext portalControllerContext, String basePath) throws PortletException {
        // Navigation tasks
        List<TaskbarTask> navigationTasks;
        if (StringUtils.isEmpty(basePath)) {
            navigationTasks = new ArrayList<>(0);
        } else {
            try {
                navigationTasks = this.taskbarService.getTasks(portalControllerContext, basePath);
            } catch (PortalException e) {
                throw new PortletException(e);
            }
        }

        return navigationTasks;
    }


    @Override
    public List<AddDropdownItem> generateAddDropdownItems(PortalControllerContext portalControllerContext) throws PortletException, IOException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // Base path
        String basePath = nuxeoController.getBasePath();

        // Dropdown items
        List<AddDropdownItem> dropdownItems;

        if (StringUtils.isEmpty(basePath)) {
            dropdownItems = null;
        } else {
            // Documents path
            String documentsPath = basePath + "/documents";
            // Navigation path
            String navigationPath = nuxeoController.getNavigationPath();
            // Parent document path
            String parentPath;
            if (StringUtils.startsWith(navigationPath, documentsPath)) {
                parentPath = navigationPath;
            } else {
                parentPath = documentsPath;
            }

            // Document context
            NuxeoDocumentContext documentContext = nuxeoController.getDocumentContext(parentPath);
            // Document permissions
            NuxeoPermissions permissions;
            if (documentContext == null) {
                permissions = null;
            } else {
                permissions = documentContext.getPermissions();
            }

            if ((permissions != null) && permissions.isEditable()) {
                dropdownItems = new ArrayList<>();

                // Internationalization bundle
                Bundle bundle = this.bundleFactory.getBundle(portalControllerContext.getRequest().getLocale());
                // Nuxeo customizer
                INuxeoCustomizer nuxeoCustomizer = this.nuxeoService.getCMSCustomizer();
                // Document types
                Map<String, DocumentType> documentTypes = nuxeoCustomizer.getDocumentTypes();

                for (String type : Arrays.asList("Folder", "File")) {
                    AddDropdownItem dropdownItem = this.generateDocumentTypeDropdownItem(nuxeoController, bundle, documentTypes, parentPath, type);

                    if (dropdownItem != null) {
                        dropdownItems.add(dropdownItem);
                    }
                }

                for (String mimeType : Arrays.asList("application/vnd.openxmlformats-officedocument.wordprocessingml.document", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "application/vnd.openxmlformats-officedocument.presentationml.presentation")) {
                    AddDropdownItem dropdownItem = this.generateLiveDocumentDropdownItem(nuxeoController, bundle, parentPath, mimeType);

                    if (dropdownItem != null) {
                        dropdownItems.add(dropdownItem);
                    }
                }
            } else {
                dropdownItems = null;
            }
        }

        return dropdownItems;
    }


    /**
     * Generate document type dropdown item.
     *
     * @param nuxeoController Nuxeo controller
     * @param bundle          internationalization bundle
     * @param documentTypes   document types
     * @param parentPath      parent document path
     * @param type            added document type
     * @return dropdown item
     */
    private AddDropdownItem generateDocumentTypeDropdownItem(NuxeoController nuxeoController, Bundle bundle, Map<String, DocumentType> documentTypes, String parentPath, String type) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = nuxeoController.getPortalCtx();

        // Dropdown item
        AddDropdownItem dropdownItem;

        // Document type
        DocumentType documentType = documentTypes.get(type);

        if (documentType == null) {
            dropdownItem = null;
        } else {
            // Display name
            String displayName = bundle.getString(StringUtils.upperCase(type), documentType.getCustomizedClassLoader());

            // Window properties
            Map<String, String> properties = new HashMap<>();
            properties.put("osivia.document.edition.base-path", nuxeoController.getBasePath());
            properties.put("osivia.document.edition.parent-path", parentPath);
            properties.put("osivia.document.edition.document-type", type);
            properties.put(DynaRenderOptions.PARTIAL_REFRESH_ENABLED, String.valueOf(true));
            properties.put("osivia.ajaxLink", "1");

            // URL
            String url;
            try {
                url = this.portalUrlFactory.getStartPortletUrl(portalControllerContext, DOCUMENT_EDITION_PORTLET_INSTANCE, properties, PortalUrlType.MODAL);
            } catch (PortalException e) {
                throw new PortletException(e);
            }

            // Modal title
            String modalTitle = bundle.getString("DOCUMENT_CREATION_" + StringUtils.upperCase(type));

            // Task
            dropdownItem = this.applicationContext.getBean(AddDropdownItem.class);
            dropdownItem.setIcon(documentType.getIcon());
            dropdownItem.setDisplayName(displayName);
            dropdownItem.setUrl(url);
            dropdownItem.setModalTitle(modalTitle);
        }

        return dropdownItem;
    }


    /**
     * Generate live document dropdown item.
     *
     * @param nuxeoController Nuxeo controller
     * @param bundle          internationalization bundle
     * @param parentPath      parent document path
     * @param mimeType        MIME type
     * @return dropdown item
     */
    private AddDropdownItem generateLiveDocumentDropdownItem(NuxeoController nuxeoController, Bundle bundle, String parentPath, String mimeType) throws PortletException, IOException {
        // Dropdown item
        AddDropdownItem dropdownItem;

        // Nuxeo customizer
        INuxeoCustomizer nuxeoCustomizer = this.nuxeoService.getCMSCustomizer();
        // Tag service
        INuxeoTagService tagService = this.nuxeoService.getTagService();
        // File MIME type
        FileMimeType fileMimeType = nuxeoCustomizer.getFileMimeType(mimeType);

        if (fileMimeType == null) {
            dropdownItem = null;
        } else {
            // Customized icon
            Element customizedIcon = tagService.getMimeTypeIcon(nuxeoController, mimeType, null);
            // Display name
            String displayName = bundle.getString("TASKBAR_ADD_" + StringUtils.upperCase(fileMimeType.getExtension()));

            // Window properties
            Map<String, String> properties = new HashMap<>();
            properties.put("osivia.services.document.creation.type", mimeType);
            properties.put(Constants.WINDOW_PROP_URI, parentPath);
            properties.put(DynaRenderOptions.PARTIAL_REFRESH_ENABLED, String.valueOf(true));
            properties.put("osivia.ajaxLink", "1");

            // URL
            String url;
            try {
                url = this.portalUrlFactory.getStartPortletUrl(nuxeoController.getPortalCtx(), LIVE_DOCUMENT_CREATION_PORTLET_INSTANCE, properties, PortalUrlType.MODAL);
            } catch (PortalException e) {
                throw new PortletException(e);
            }

            // Task
            dropdownItem = this.applicationContext.getBean(AddDropdownItem.class);
            dropdownItem.setCustomizedIcon(DOM4JUtils.write(customizedIcon));
            dropdownItem.setDisplayName(displayName);
            dropdownItem.setUrl(url);
        }

        return dropdownItem;
    }


    @Override
    public Task generateFolderTask(PortalControllerContext portalControllerContext, String basePath, String path) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // CMS service
        ICMSService cmsService = this.cmsServiceLocator.getCMSService();
        // CMS context
        CMSServiceCtx cmsContext = new CMSServiceCtx();
        cmsContext.setPortalControllerContext(portalControllerContext);

        // Current navigation path
        String currentNavigationPath = nuxeoController.getNavigationPath();


        // Navigation item
        CMSItem navigationItem;
        try {
            navigationItem = cmsService.getPortalNavigationItem(cmsContext, basePath, path);
        } catch (CMSException e) {
            throw new PortletException(e);
        }

        // Folder task
        FolderTask task = this.getFolderTask(portalControllerContext, currentNavigationPath, navigationItem);

        // Children
        this.generateFolderChildren(nuxeoController, basePath, task);

        return task;
    }


    /**
     * Get folder task.
     *
     * @param portalControllerContext portal controller context
     * @param currentNavigationPath   current navigation path
     * @param navigationItem          navigation item
     * @return task
     */
    private FolderTask getFolderTask(PortalControllerContext portalControllerContext, String currentNavigationPath, CMSItem navigationItem) {
        FolderTask task = this.applicationContext.getBean(FolderTask.class);

        // Document type
        DocumentType documentType = navigationItem.getType();
        // Fetched children indicator
        Boolean unfetchedChildren = BooleanUtils.toBooleanObject(navigationItem.getProperties().get("unfetchedChildren"));
        // Nuxeo document
        Document document = (Document) navigationItem.getNativeItem();

        // Identifier
        String id = document.getId();
        task.setId(id);

        // Path
        String path = navigationItem.getCmsPath();
        task.setPath(path);

        // Display name
        String displayName = document.getTitle();
        task.setDisplayName(displayName);

        // URL
        String url = this.portalUrlFactory.getCMSUrl(portalControllerContext, null, path, null, null, "menu", null, null, "1", null);
        task.setUrl(url);

        // Active indicator
        boolean active = StringUtils.equals(currentNavigationPath, path);
        task.setActive(active);

        // Selected indicator
        boolean selected = this.isSelected(path, currentNavigationPath);
        task.setSelected(selected);

        // Folder indicator
        boolean folder = (documentType != null) && documentType.isFolderish();
        task.setFolder(folder);

        // Lazy indicator
        boolean lazy = (documentType != null) && documentType.isBrowsable() && !selected && BooleanUtils.isNotFalse(unfetchedChildren);
        task.setLazy(lazy);

        // Accepted types
        String[] acceptedTypes = this.getAcceptedTypes(navigationItem);
        task.setAcceptedTypes(acceptedTypes);

        return task;
    }


    /**
     * Generate folder children.
     *
     * @param nuxeoController Nuxeo controller
     * @param basePath        base path
     * @param parent          parent folder
     */
    private void generateFolderChildren(NuxeoController nuxeoController, String basePath, FolderTask parent) throws PortletException {
        // Children
        SortedSet<FolderTask> children = this.getFolderChildren(nuxeoController, basePath, parent.getPath());
        parent.setChildren(children);

        if (CollectionUtils.isNotEmpty(children)) {
            for (FolderTask child : children) {
                // Children
                if (child.isSelected() || !child.isLazy()) {
                    this.generateFolderChildren(nuxeoController, basePath, child);
                }
            }
        }
    }


    @Override
    public SortedSet<FolderTask> getFolderChildren(PortalControllerContext portalControllerContext, String basePath, String path) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        return this.getFolderChildren(nuxeoController, basePath, path);
    }


    /**
     * Get folder children.
     *
     * @param nuxeoController Nuxeo controller
     * @param basePath        base path
     * @param folderPath      folder path
     * @return folder children
     */
    private SortedSet<FolderTask> getFolderChildren(NuxeoController nuxeoController, String basePath, String folderPath) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = nuxeoController.getPortalCtx();

        // CMS service
        ICMSService cmsService = this.cmsServiceLocator.getCMSService();
        // CMS context
        CMSServiceCtx cmsContext = new CMSServiceCtx();
        cmsContext.setPortalControllerContext(portalControllerContext);

        // Current navigation path
        String currentNavigationPath = nuxeoController.getNavigationPath();


        // Navigation items
        List<CMSItem> navigationItems;
        try {
            navigationItems = cmsService.getPortalNavigationSubitems(cmsContext, basePath, folderPath);
        } catch (CMSException e) {
            throw new PortletException(e);
        }


        // Children
        SortedSet<FolderTask> children;
        if (CollectionUtils.isEmpty(navigationItems)) {
            children = null;
        } else {
            children = new TreeSet<>(this.folderTaskComparator);

            for (CMSItem navigationItem : navigationItems) {
                FolderTask child = getFolderTask(portalControllerContext, currentNavigationPath, navigationItem);

                children.add(child);
            }
        }

        return children;
    }


    /**
     * Check if path is a parent of current navigation path.
     *
     * @param path                  path
     * @param currentNavigationPath current navigation path
     * @return true if path is a parent of current navigation path
     */
    private boolean isSelected(String path, String currentNavigationPath) {
        boolean result = StringUtils.startsWith(currentNavigationPath, path);

        // "/parent/child-2/foo" starts with "/parent/child", but it isn't a child
        if (result) {
            String[] splittedPath = StringUtils.split(path, "/");
            String[] splittedCurrentNavigationPath = StringUtils.split(currentNavigationPath, "/");

            int index = splittedPath.length - 1;
            result = StringUtils.equals(splittedPath[index], splittedCurrentNavigationPath[index]);
        }

        return result;
    }


    /**
     * Get accepted types.
     *
     * @param item CMS item
     * @return accepted types
     */
    private String[] getAcceptedTypes(CMSItem item) {
        String[] acceptedTypes = null;
        if ((item != null) && (item.getType() != null)) {
            List<String> types = item.getType().getSubtypes();
            if (types != null) {
                acceptedTypes = types.toArray(new String[0]);
            }
        }
        return acceptedTypes;
    }


    @Override
    public void moveDocuments(PortalControllerContext portalControllerContext, List<String> sourceIds, String targetId) {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // Nuxeo command
        INuxeoCommand command = this.applicationContext.getBean(MoveDocumentsCommand.class, sourceIds, targetId);
        nuxeoController.executeNuxeoCommand(command);
    }

}
