package fr.index.cloud.ens.taskbar.portlet.repository;

import fr.index.cloud.ens.taskbar.portlet.model.*;
import fr.index.cloud.ens.taskbar.portlet.model.comparator.FolderTaskComparator;
import fr.index.cloud.ens.taskbar.portlet.model.comparator.SavedSearchComparator;
import fr.index.cloud.ens.taskbar.portlet.repository.command.MoveDocumentsCommand;
import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoDocumentContext;
import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoPermissions;
import fr.toutatice.portail.cms.nuxeo.api.services.INuxeoCustomizer;
import fr.toutatice.portail.cms.nuxeo.api.services.INuxeoService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.jboss.portal.theme.impl.render.dynamic.DynaRenderOptions;
import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.directory.v2.model.preferences.UserPreferences;
import org.osivia.directory.v2.model.preferences.UserSavedSearch;
import org.osivia.directory.v2.service.preferences.UserPreferencesService;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.cms.DocumentType;
import org.osivia.portal.api.cms.VirtualNavigationUtils;
import org.osivia.portal.api.context.PortalControllerContext;
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

import javax.portlet.*;
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
     * Saved search comparator.
     */
    @Autowired
    private SavedSearchComparator savedSearchComparator;

    /**
     * User preferences service.
     */
    @Autowired
    private UserPreferencesService userPreferencesService;


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


    @Override
    public Task generateFolderTask(PortalControllerContext portalControllerContext, String basePath, String path, String searchPath) throws PortletException {
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
        FolderTask task = this.getFolderTask(portalControllerContext, currentNavigationPath, searchPath, navigationItem);

        // Children
        this.generateFolderChildren(nuxeoController, basePath, task, searchPath);

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
    private FolderTask getFolderTask(PortalControllerContext portalControllerContext, String currentNavigationPath, String searchPath, CMSItem navigationItem) {
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
        boolean selected = this.isSelected(path, currentNavigationPath, searchPath);
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

        // Search location indicator
        boolean searchLocation = StringUtils.equals(searchPath, path);
        task.setSearchLocation(searchLocation);

        return task;
    }


    /**
     * Generate folder children.
     *
     * @param nuxeoController Nuxeo controller
     * @param basePath        base path
     * @param parent          parent folder
     * @param searchPath      search path, may be null
     */
    private void generateFolderChildren(NuxeoController nuxeoController, String basePath, FolderTask parent, String searchPath) throws PortletException {
        // Children
        SortedSet<FolderTask> children = this.getFolderChildren(nuxeoController, basePath, parent.getPath(), searchPath);
        parent.setChildren(children);

        if (CollectionUtils.isNotEmpty(children)) {
            for (FolderTask child : children) {
                // Children
                if (child.isSelected() || !child.isLazy()) {
                    this.generateFolderChildren(nuxeoController, basePath, child, searchPath);
                }
            }
        }
    }


    @Override
    public SortedSet<FolderTask> getFolderChildren(PortalControllerContext portalControllerContext, String basePath, String path, String searchPath) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        return this.getFolderChildren(nuxeoController, basePath, path, searchPath);
    }


    /**
     * Get folder children.
     *
     * @param nuxeoController Nuxeo controller
     * @param basePath        base path
     * @param folderPath      folder path
     * @param searchPath      search path, may be null
     * @return folder children
     */
    private SortedSet<FolderTask> getFolderChildren(NuxeoController nuxeoController, String basePath, String folderPath, String searchPath) throws PortletException {
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
                FolderTask child = getFolderTask(portalControllerContext, currentNavigationPath, searchPath, navigationItem);

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
     * @param searchPath            search path, may be null
     * @return true if path is a parent of current navigation path
     */
    private boolean isSelected(String path, String currentNavigationPath, String searchPath) {
        return this.startsWith(currentNavigationPath, path) || this.startsWith(searchPath, path);
    }


    /**
     * Check if path starts with prefix.
     *
     * @param testedPath   tested path
     * @param prefixedPath prefixed path
     * @return true if path starts with prefix
     */
    private boolean startsWith(String testedPath, String prefixedPath) {
        boolean result = StringUtils.startsWith(testedPath, prefixedPath);

        // "/parent/child-2/foo" starts with "/parent/child", but it isn't a child
        if (result) {
            String[] splittedTestedPath = StringUtils.split(testedPath, "/");
            String[] splittedPrefixedPath = StringUtils.split(prefixedPath, "/");

            int index = splittedPrefixedPath.length - 1;
            result = StringUtils.equals(splittedTestedPath[index], splittedPrefixedPath[index]);
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
    public List<Task> getSavedSearchesTasks(PortalControllerContext portalControllerContext, String activeSavedSearch) throws PortletException {
        // Portlet response
        PortletResponse portletResponse = portalControllerContext.getResponse();
        // MIME response
        MimeResponse mimeResponse;
        if (portletResponse instanceof MimeResponse) {
            mimeResponse = (MimeResponse) portletResponse;
        } else {
            mimeResponse = null;
        }

        // User preferences
        UserPreferences userPreferences;
        try {
            userPreferences = this.userPreferencesService.getUserPreferences(portalControllerContext);
        } catch (PortalException e) {
            throw new PortletException(e);
        }
        // Saved searches
        List<UserSavedSearch> savedSearches = userPreferences.getSavedSearches();


        // Tasks
        List<Task> tasks;
        if ((mimeResponse == null) || CollectionUtils.isEmpty(savedSearches)) {
            tasks = null;
        } else {
            tasks = new ArrayList<>(savedSearches.size());

            // Sort
            Collections.sort(savedSearches, this.savedSearchComparator);

            for (UserSavedSearch savedSearch : savedSearches) {
                // Action URL
                PortletURL actionUrl = mimeResponse.createActionURL();
                actionUrl.setParameter(ActionRequest.ACTION_NAME, "search");
                actionUrl.setParameter("id", String.valueOf(savedSearch.getId()));

                // Task
                ServiceTask task = this.applicationContext.getBean(ServiceTask.class);
                task.setIcon("glyphicons glyphicons-basic-filter");
                task.setDisplayName(savedSearch.getDisplayName());
                task.setUrl(actionUrl.toString());
                task.setActive(StringUtils.equals(activeSavedSearch, String.valueOf(savedSearch.getId())));

                tasks.add(task);
            }
        }

        return tasks;
    }


    @Override
    public String getSearchPath(PortalControllerContext portalControllerContext) throws PortletException {
        // CMS service
        ICMSService cmsService = this.cmsServiceLocator.getCMSService();
        // CMS context
        CMSServiceCtx cmsContext = new CMSServiceCtx();
        cmsContext.setPortalControllerContext(portalControllerContext);

        // User workspace
        CMSItem userWorkspace;
        try {
            userWorkspace = cmsService.getUserWorkspace(cmsContext);
        } catch (CMSException e) {
            throw new PortletException(e);
        }

        // Navigation tasks
        List<TaskbarTask> navigationTasks;
        if (userWorkspace == null) {
            navigationTasks = null;
        } else {
            try {
                navigationTasks = this.taskbarService.getTasks(portalControllerContext, userWorkspace.getCmsPath());
            } catch (PortalException e) {
                throw new PortletException(e);
            }
        }

        // Search taskbar task
        TaskbarTask searchTask = null;
        if (CollectionUtils.isNotEmpty(navigationTasks)) {
            Iterator<TaskbarTask> iterator = navigationTasks.iterator();

            while ((searchTask == null) && iterator.hasNext()) {
                TaskbarTask task = iterator.next();

                // Task staple identifier
                String stapleId = VirtualNavigationUtils.getStapleId(task.getPath());

                if (StringUtils.equals(ITaskbarService.SEARCH_TASK_ID, stapleId)) {
                    searchTask = task;
                }
            }
        }

        // Search path
        String path;
        if (searchTask == null) {
            path = null;
        } else {
            path = searchTask.getPath();
        }

        return path;
    }


    @Override
    public UserSavedSearch getSavedSearch(PortalControllerContext portalControllerContext, int id) throws PortletException {
        // User preferences
        UserPreferences userPreferences = null;
        try {
            userPreferences = this.userPreferencesService.getUserPreferences(portalControllerContext);
        } catch (PortalException e) {
            throw new PortletException(e);
        }
        // Saved searches
        List<UserSavedSearch> savedSearches = userPreferences.getSavedSearches();

        // Saved search
        UserSavedSearch savedSearch = null;
        if (CollectionUtils.isNotEmpty(savedSearches)) {
            Iterator<UserSavedSearch> iterator = savedSearches.iterator();
            while ((savedSearch == null) && iterator.hasNext()) {
                UserSavedSearch item = iterator.next();
                if (id == item.getId()) {
                    savedSearch = item;
                }
            }
        }

        return savedSearch;
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
