package fr.index.cloud.ens.taskbar.portlet.service;

import fr.index.cloud.ens.taskbar.portlet.model.FolderTask;
import fr.index.cloud.ens.taskbar.portlet.model.ServiceTask;
import fr.index.cloud.ens.taskbar.portlet.model.Task;
import fr.index.cloud.ens.taskbar.portlet.model.Taskbar;
import fr.index.cloud.ens.taskbar.portlet.repository.TaskbarRepository;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoException;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.osivia.portal.api.Constants;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.notifications.INotificationsService;
import org.osivia.portal.api.notifications.NotificationsType;
import org.osivia.portal.api.panels.PanelPlayer;
import org.osivia.portal.api.taskbar.ITaskbarService;
import org.osivia.portal.api.taskbar.TaskbarItem;
import org.osivia.portal.api.taskbar.TaskbarItemType;
import org.osivia.portal.api.taskbar.TaskbarTask;
import org.osivia.portal.api.urls.IPortalUrlFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import java.util.*;

/**
 * Taskbar portlet service implementation.
 *
 * @author Cédric Krommenhoek
 * @see TaskbarService
 */
@Service
public class TaskbarServiceImpl implements TaskbarService {

    /**
     * Application context.
     */
    @Autowired
    private ApplicationContext applicationContext;

    /**
     * Portlet repository.
     */
    @Autowired
    private TaskbarRepository repository;

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
     * Notifications service.
     */
    @Autowired
    private INotificationsService notificationsService;


    /**
     * Constructor.
     */
    public TaskbarServiceImpl() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Taskbar getTaskbar(PortalControllerContext portalControllerContext) throws PortletException {
        // Portlet request
        PortletRequest request = portalControllerContext.getRequest();
        // Internationalization bundle
        Bundle bundle = this.bundleFactory.getBundle(request.getLocale());


        // Navigation tasks
        List<TaskbarTask> navigationTasks = this.repository.getNavigationTasks(portalControllerContext);

        // Active navigation task identifier
        String activeId;
        try {
            activeId = this.taskbarService.getActiveId(portalControllerContext);
        } catch (PortalException e) {
            throw new PortletException(e);
        }

        List<TaskbarTask> folders = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(navigationTasks)) {
            for (TaskbarTask navigationTask : navigationTasks) {
                if (!navigationTask.isDisabled() && !navigationTask.isHidden()) {
                    if (TaskbarItemType.CMS.equals(navigationTask.getType()) && "Folder".equals(navigationTask.getDocumentType())) {
                        folders.add(navigationTask);
                    }
                }
            }
        }


        // Tasks
        List<TaskbarTask> tasks = new ArrayList<>();
        // Home
        TaskbarTask home = this.createHomeTaskbarTask(portalControllerContext);
        tasks.add(home);
        // Folders
        tasks.addAll(folders);
        // Trash
        TaskbarTask trash = this.createTrashTaskbarTask(portalControllerContext);
        if (trash != null) {
            tasks.add(trash);
        }

        // Taskbar
        Taskbar taskbar = this.applicationContext.getBean(Taskbar.class);
        taskbar.setTasks(this.createTasks(portalControllerContext, bundle, activeId, tasks));

        return taskbar;
    }


    private TaskbarTask createHomeTaskbarTask(PortalControllerContext portalControllerContext) throws PortletException {
        // Locale
        Locale locale = portalControllerContext.getRequest().getLocale();
        // Internationalization bundle
        Bundle bundle = this.bundleFactory.getBundle(locale);

        // Title
        String title = bundle.getString("TASKBAR_HOME");
        // Icon
        String icon = "glyphicons glyphicons-basic-home";
        // Path
        String path = this.repository.getBasePath(portalControllerContext);

        return this.taskbarService.getFactory().createTaskbarTask(ITaskbarService.HOME_TASK_ID, title, icon, path, null, false);
    }


    private TaskbarTask createTrashTaskbarTask(PortalControllerContext portalControllerContext) throws PortletException {
        // Locale
        Locale locale = portalControllerContext.getRequest().getLocale();
        // Internationalization bundle
        Bundle bundle = this.bundleFactory.getBundle(locale);

        // Taskbar item
        TaskbarItem item;
        try {
            item = this.taskbarService.getItem(portalControllerContext, "TRASH");
        } catch (PortalException e) {
            throw new PortletException(e);
        }

        // Taskbar task
        TaskbarTask task;
        if (item == null) {
            task = null;
        } else {
            // Title
            String title = bundle.getString("TASKBAR_TRASH");

            task = this.taskbarService.getFactory().createTaskbarTask(item, title, null, false);
        }

        return task;
    }


    /**
     * Create tasks from navigation tasks.
     *
     * @param portalControllerContext portal controller context
     * @param bundle                  internationalization bundle
     * @param activeId                active navigation task identifier
     * @param navigationTasks         navigation tasks
     * @return tasks
     */
    private List<Task> createTasks(PortalControllerContext portalControllerContext, Bundle bundle, String activeId,
                                   List<TaskbarTask> navigationTasks) throws PortletException {
        List<Task> tasks;

        if (CollectionUtils.isEmpty(navigationTasks)) {
            tasks = null;
        } else {
            tasks = new ArrayList<>(navigationTasks.size());

            for (TaskbarTask navigationTask : navigationTasks) {
                Task task = this.createTask(portalControllerContext, bundle, activeId, navigationTask);
                tasks.add(task);
            }
        }

        return tasks;
    }


    /**
     * Create task from navigation task.
     *
     * @param portalControllerContext portal controller context
     * @param bundle                  internationalization bundle
     * @param activeId                active navigation task identifier
     * @param navigationTask          navigation task
     * @return task
     */
    private Task createTask(PortalControllerContext portalControllerContext, Bundle bundle, String activeId, TaskbarTask navigationTask) throws PortletException {
        Class<? extends Task> clazz;
        if ("Folder".equals(navigationTask.getDocumentType())) {
            clazz = FolderTask.class;
        } else {
            clazz = ServiceTask.class;
        }

        Task task = this.applicationContext.getBean(clazz);

        // Icon
        String icon = navigationTask.getIcon();
        task.setIcon(icon);

        // Display name
        String displayName;
        if (StringUtils.isEmpty(navigationTask.getTitle())) {
            displayName = bundle.getString(navigationTask.getKey(), navigationTask.getCustomizedClassLoader());
        } else {
            displayName = navigationTask.getTitle();
        }
        task.setDisplayName(displayName);

        // URL
        String url;
        if (navigationTask.getPlayer() != null) {
            // Start portlet URL
            PanelPlayer player = navigationTask.getPlayer();

            // Window properties
            Map<String, String> properties = new HashMap<>();
            if (player.getProperties() != null) {
                properties.putAll(player.getProperties());
            }
            properties.put(ITaskbarService.TASK_ID_WINDOW_PROPERTY, navigationTask.getId());
            if (StringUtils.isNotEmpty(displayName)) {
                properties.put("osivia.title", displayName);
            }
            properties.put("osivia.back.reset", String.valueOf(true));
            properties.put("osivia.navigation.reset", String.valueOf(true));

            try {
                url = this.portalUrlFactory.getStartPortletUrl(portalControllerContext, player.getInstance(), properties);
            } catch (PortalException e) {
                throw new PortletException(e);
            }
        } else if (StringUtils.isNotEmpty(navigationTask.getPath())) {
            // CMS URL
            String path = navigationTask.getPath();

            url = this.portalUrlFactory.getCMSUrl(portalControllerContext, null, path, null, null, "taskbar", null, null, "1", null);
        } else {
            // Unknown case
            url = "#";
        }
        task.setUrl(url);

        // Active indicator
        boolean active = StringUtils.equals(activeId, navigationTask.getId());
        task.setActive(active);


        if (task instanceof FolderTask) {
            // Folder
            FolderTask folder = (FolderTask) task;

            // Folder path
            folder.setPath(navigationTask.getPath());

            // Folder navigation tree
            this.repository.generateFolderNavigationTree(portalControllerContext, folder);
        }

        return task;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void drop(PortalControllerContext portalControllerContext, List<String> sourceIds, String targetId) throws PortletException {
        // Portlet request
        PortletRequest request = portalControllerContext.getRequest();
        // Action response
        ActionResponse response = (ActionResponse) portalControllerContext.getResponse();

        // Internationalization bundle
        Bundle bundle = this.bundleFactory.getBundle(request.getLocale());

        try {
            this.repository.moveDocuments(portalControllerContext, sourceIds, targetId);

            // Notification
            String message = bundle.getString("TASKBAR_MOVE_SUCCESS_MESSAGE");
            this.notificationsService.addSimpleNotification(portalControllerContext, message, NotificationsType.SUCCESS);
        } catch (NuxeoException e) {
            // Notification
            String message = bundle.getString("TASKBAR_MOVE_WARNING_MESSAGE");
            this.notificationsService.addSimpleNotification(portalControllerContext, message, NotificationsType.WARNING);
        }

        // Refresh navigation
        request.setAttribute(Constants.PORTLET_ATTR_UPDATE_CONTENTS, Constants.PORTLET_VALUE_ACTIVATE);

        // Update public render parameter for associated portlets refresh
        response.setRenderParameter("dnd-update", String.valueOf(System.currentTimeMillis()));
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public JSONArray getFolderChildren(PortalControllerContext portalControllerContext, String path) throws PortletException {
        // Children
        SortedSet<FolderTask> children = this.repository.getFolderChildren(portalControllerContext, path);

        // JSON array
        JSONArray array = new JSONArray();
        if (CollectionUtils.isNotEmpty(children)) {
            for (FolderTask child : children) {
                // JSON object
                JSONObject object = new JSONObject();

                // Display name
                object.put("title", child.getDisplayName());
                // URL
                object.put("href", child.getUrl());
                // Folder indicator
                object.put("folder", child.isFolder());
                // Lazy indicator
                object.put("lazy", child.isLazy());
                // Identifier
                object.put("id", child.getId());
                // Path
                object.put("path", child.getPath());
                // Accepted types
                object.put("acceptedtypes", StringUtils.join(child.getAcceptedTypes(), ","));
                // Extra classes
                object.put("extraClasses", "text-secondary");

                array.add(object);
            }

        }

        return array;
    }

}
