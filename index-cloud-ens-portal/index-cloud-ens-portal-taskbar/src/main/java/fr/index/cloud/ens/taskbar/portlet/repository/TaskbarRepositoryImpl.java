package fr.index.cloud.ens.taskbar.portlet.repository;

import fr.index.cloud.ens.taskbar.portlet.model.FolderTask;
import fr.index.cloud.ens.taskbar.portlet.model.Task;
import fr.index.cloud.ens.taskbar.portlet.model.TaskbarWindowProperties;
import fr.index.cloud.ens.taskbar.portlet.model.comparator.FolderTaskComparator;
import fr.index.cloud.ens.taskbar.portlet.repository.command.MoveDocumentsCommand;
import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.cms.DocumentType;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.taskbar.ITaskbarService;
import org.osivia.portal.api.taskbar.TaskbarTask;
import org.osivia.portal.api.urls.IPortalUrlFactory;
import org.osivia.portal.core.cms.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Repository;

import javax.portlet.PortletException;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Taskbar portlet repository implementation.
 *
 * @author Cédric Krommenhoek
 * @see TaskbarRepository
 */
@Repository
public class TaskbarRepositoryImpl implements TaskbarRepository {

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
     * CMS service locator.
     */
    @Autowired
    private ICMSServiceLocator cmsServiceLocator;

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

    /**
     * {@inheritDoc}
     */
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


    /**
     * {@inheritDoc}
     */
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


    /**
     * {@inheritDoc}
     */
    @Override
    public void moveDocuments(PortalControllerContext portalControllerContext, List<String> sourceIds, String targetId) {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // Nuxeo command
        INuxeoCommand command = this.applicationContext.getBean(MoveDocumentsCommand.class, sourceIds, targetId);
        nuxeoController.executeNuxeoCommand(command);
    }

}
