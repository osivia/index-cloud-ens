package fr.index.cloud.ens.taskbar.portlet.service;

import fr.index.cloud.ens.taskbar.portlet.model.*;
import fr.index.cloud.ens.taskbar.portlet.repository.TaskbarRepository;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoException;
import fr.toutatice.portail.cms.nuxeo.api.PageSelectors;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.CharEncoding;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.osivia.directory.v2.model.preferences.UserSavedSearch;
import org.osivia.portal.api.Constants;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.cms.VirtualNavigationUtils;
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
import org.osivia.portal.api.windows.PortalWindow;
import org.osivia.portal.api.windows.WindowFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.Normalizer;
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


    @Override
    public TaskbarWindowProperties getWindowProperties(PortalControllerContext portalControllerContext) {
        // Window
        PortalWindow window = WindowFactory.getWindow(portalControllerContext.getRequest());

        // Window properties
        TaskbarWindowProperties windowProperties = this.applicationContext.getBean(TaskbarWindowProperties.class);

        // Parent document path
        String path = window.getProperty(PATH_WINDOW_PROPERTY);
        windowProperties.setPath(path);

        return windowProperties;
    }


    @Override
    public void setWindowProperties(PortalControllerContext portalControllerContext, TaskbarWindowProperties windowProperties) {
        // Window
        PortalWindow window = WindowFactory.getWindow(portalControllerContext.getRequest());

        // Parent document path
        String path = StringUtils.trimToNull(windowProperties.getPath());
        window.setProperty(PATH_WINDOW_PROPERTY, path);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Taskbar getTaskbar(PortalControllerContext portalControllerContext) throws PortletException, IOException {
        // Portlet request
        PortletRequest request = portalControllerContext.getRequest();
        // Internationalization bundle
        Bundle bundle = this.bundleFactory.getBundle(request.getLocale());

        // Window properties
        TaskbarWindowProperties windowProperties = this.getWindowProperties(portalControllerContext);

        // Base path
        String basePath = this.repository.getBasePath(portalControllerContext, windowProperties);
        // Navigation tasks
        List<TaskbarTask> navigationTasks = this.repository.getNavigationTasks(portalControllerContext, basePath);

        // Selectors
        Map<String, List<String>> selectors = PageSelectors.decodeProperties(request.getParameter("selectors"));


        // Active navigation task identifier
        String activeId;
        try {
            activeId = this.taskbarService.getActiveId(portalControllerContext);
        } catch (PortalException e) {
            throw new PortletException(e);
        }

        // Active saved search
        String activeSavedSearch;
        List<String> savedSearchValues = selectors.get("active-saved-search");
        if (CollectionUtils.isEmpty(savedSearchValues)) {
            activeSavedSearch = null;
        } else {
            activeSavedSearch = savedSearchValues.get(0);
        }


        // Tasks
        List<Task> tasks = new ArrayList<>();
        // Add
        AddTask add = this.applicationContext.getBean(AddTask.class);
        add.setDropdownItems(this.repository.generateAddDropdownItems(portalControllerContext));
        tasks.add(add);
        // Folders
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
        tasks.addAll(this.createTasks(portalControllerContext, basePath, bundle, activeId, folders));
        // Trash
        TaskbarTask trash = this.createTrashTaskbarTask(portalControllerContext);
        if (trash != null) {
            tasks.add(this.createTask(portalControllerContext, bundle, activeId, trash));
        }
        // Recent items
        TaskbarTask recentItems = this.extractVirtualStaple(portalControllerContext, navigationTasks, "RECENT_ITEMS");
        if (recentItems != null) {
            tasks.add(this.createTask(portalControllerContext, bundle, activeId, recentItems));
        }
        // Search
        SearchTask searchTask = this.applicationContext.getBean(SearchTask.class);
        tasks.add(searchTask);
        // Advanced search
        TaskbarTask advancedSearch = this.extractVirtualStaple(portalControllerContext, navigationTasks, "SEARCH_FILTERS");
        if (advancedSearch != null) {
            searchTask.setAdvancedSearch(this.createTask(portalControllerContext, bundle, activeId, advancedSearch));
        }
        // Filters
        FiltersTask filters = this.applicationContext.getBean(FiltersTask.class);
        tasks.add(filters);
        // Saved searches
        List<Task> savedSearches = this.repository.getSavedSearchesTasks(portalControllerContext, activeSavedSearch);
        filters.setSavedSearches(savedSearches);


        // Taskbar
        Taskbar taskbar = this.applicationContext.getBean(Taskbar.class);
        taskbar.setTasks(tasks);

        return taskbar;
    }


    @Override
    public TaskbarSearchForm getTaskbarSearchForm(PortalControllerContext portalControllerContext) {
        // Portlet request
        PortletRequest request = portalControllerContext.getRequest();
        // Selectors
        Map<String, List<String>> selectors = PageSelectors.decodeProperties(request.getParameter(SELECTORS_PARAMETER));

        // Search form
        TaskbarSearchForm searchForm = this.applicationContext.getBean(TaskbarSearchForm.class);

        // Keywords
        String keywords = StringUtils.join(selectors.get(KEYWORDS_SELECTOR_ID), " ");
        searchForm.setKeywords(StringEscapeUtils.escapeHtml(keywords)); // Don't forget to escape HTML to avoid injection

        // Document types
        List<String> documentTypes = selectors.get(DOCUMENT_TYPES_SELECTOR_ID);
        searchForm.setDocumentTypes(documentTypes);

        // Levels
        List<String> levels = selectors.get(LEVELS_SELECTOR_ID);
        searchForm.setLevels(levels);

        // Subjects
        List<String> subjects = selectors.get(SUBJECTS_SELECTOR_ID);
        searchForm.setSubjects(subjects);

        return searchForm;
    }


    /**
     * Create trash taskbar task.
     *
     * @param portalControllerContext portal controller context
     * @return taskbar task
     */
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
     * Extract task from virtual navigation.
     *
     * @param portalControllerContext the portal controller context
     * @param navigationTasks         the navigation tasks
     * @param targetTaskbarItemId     the target taskbar item id
     * @return the taskbar task
     * @throws PortletException the portlet exception
     */

    private TaskbarTask extractVirtualStaple(PortalControllerContext portalControllerContext, List<TaskbarTask> navigationTasks,
                                             String targetTaskbarItemId) throws PortletException {
        TaskbarTask task = null;

        if (CollectionUtils.isNotEmpty(navigationTasks)) {
            for (TaskbarTask navigationTask : navigationTasks) {
                if (!navigationTask.isDisabled() && !navigationTask.isHidden()) {
                    if (TaskbarItemType.CMS.equals(navigationTask.getType())) {
                        String stapleId = VirtualNavigationUtils.getStapleId(navigationTask.getPath());
                        if (targetTaskbarItemId.equals(stapleId)) {
                            try {
                                // Get Item declaration
                                TaskbarItem item = this.taskbarService.getItem(portalControllerContext, stapleId);
                                if (item != null) {
                                    // Create mixin navigation task (task for path + item for icon)
                                    task = this.taskbarService.getFactory().createTaskbarTask(navigationTask.getId(), navigationTask.getTitle(), item.getIcon(),
                                            navigationTask.getPath(), "Staple", false);
                                }
                                break;
                            } catch (PortalException e) {
                                throw new PortletException(e);
                            }
                        }
                    }
                }
            }
        }

        return task;
    }


    /**
     * Create tasks from navigation tasks.
     *
     * @param portalControllerContext portal controller context
     * @param basePath                base path
     * @param bundle                  internationalization bundle
     * @param activeId                active navigation task identifier
     * @param navigationTasks         navigation tasks
     * @return tasks
     */
    private List<Task> createTasks(PortalControllerContext portalControllerContext, String basePath, Bundle bundle, String activeId,
                                   List<TaskbarTask> navigationTasks) throws PortletException {
        // Search path
        String searchPath = this.getSearchPath(portalControllerContext);

        List<Task> tasks;

        if (CollectionUtils.isEmpty(navigationTasks)) {
            tasks = null;
        } else {
            tasks = new ArrayList<>(navigationTasks.size());

            for (TaskbarTask navigationTask : navigationTasks) {
                Task task;
                if ("Folder".equals(navigationTask.getDocumentType())) {
                    task = this.repository.generateFolderTask(portalControllerContext, basePath, navigationTask.getPath(), searchPath);
                } else {
                    task = this.createTask(portalControllerContext, bundle, activeId, navigationTask);
                }

                if (task != null) {
                    tasks.add(task);
                }
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
        Task task = this.applicationContext.getBean(ServiceTask.class);

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


    @Override
    public void resetSearchFilters(PortalControllerContext portalControllerContext, TaskbarSearchForm searchForm) {
        // Action response
        ActionResponse response = (ActionResponse) portalControllerContext.getResponse();

        // Update model
        searchForm.setKeywords(null);
        searchForm.setDocumentTypes(null);
        searchForm.setLevels(null);
        searchForm.setSubjects(null);

        // Selectors
        response.setRenderParameter(SELECTORS_PARAMETER, StringUtils.EMPTY);
    }


    @Override
    public void search(PortalControllerContext portalControllerContext, TaskbarSearchForm searchForm) {
        // Portlet request
        PortletRequest request = portalControllerContext.getRequest();
        // Action response
        ActionResponse response = (ActionResponse) portalControllerContext.getResponse();

        // Prevent Ajax refresh
        request.setAttribute("osivia.ajax.preventRefresh", true);

        // Selectors
        Map<String, List<String>> selectors = PageSelectors.decodeProperties(request.getParameter(SELECTORS_PARAMETER));

        // Keywords
        if (StringUtils.isBlank(searchForm.getKeywords())) {
            selectors.remove(KEYWORDS_SELECTOR_ID);
        } else {
            selectors.put(KEYWORDS_SELECTOR_ID, Collections.singletonList(searchForm.getKeywords()));
        }

        // Document types
        selectors.put(DOCUMENT_TYPES_SELECTOR_ID, searchForm.getDocumentTypes());

        // Levels
        selectors.put(LEVELS_SELECTOR_ID, searchForm.getLevels());

        // Subjects
        selectors.put(SUBJECTS_SELECTOR_ID, searchForm.getSubjects());

        response.setRenderParameter(SELECTORS_PARAMETER, PageSelectors.encodeProperties(selectors));
    }


    @Override
    public String getSavedSearchUrl(PortalControllerContext portalControllerContext, int id) throws PortletException {
        // Saved search
        UserSavedSearch savedSearch = this.repository.getSavedSearch(portalControllerContext, id);

        // Search path
        String path = this.repository.getSearchPath(portalControllerContext);

        // URL
        String url;
        if ((savedSearch == null) || StringUtils.isEmpty(path)) {
            url = null;
        } else {
            // Selectors data
            String data = savedSearch.getData();

            // Page parameters
            Map<String, String> parameters = new HashMap<>(1);
            if (StringUtils.isNotEmpty(data)) {
                // Decoded selectors
                Map<String, List<String>> selectors = PageSelectors.decodeProperties(data);

                // Add saved search selector value
                selectors.put("active-saved-search", Collections.singletonList(String.valueOf(savedSearch.getId())));

                parameters.put(SELECTORS_PARAMETER, PageSelectors.encodeProperties(selectors));
            }

            // CMS URL
            url = this.portalUrlFactory.getCMSUrl(portalControllerContext, null, path, parameters, null, null, null, null,
                    null, null);
        }

        return url;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public JSONArray getFolderChildren(PortalControllerContext portalControllerContext, String path) throws PortletException {
        // Window properties
        TaskbarWindowProperties windowProperties = this.getWindowProperties(portalControllerContext);

        // Base path
        String basePath = this.repository.getBasePath(portalControllerContext, windowProperties);
        // Search path
        String searchPath = this.getSearchPath(portalControllerContext);

        // Children
        SortedSet<FolderTask> children = this.repository.getFolderChildren(portalControllerContext, basePath, path, searchPath);

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


    /**
     * Get search path.
     *
     * @param portalControllerContext portal controller context
     * @return path, may be null
     */
    private String getSearchPath(PortalControllerContext portalControllerContext) {
        // Portlet request
        PortletRequest request = portalControllerContext.getRequest();
        // Selectors
        Map<String, List<String>> selectors = PageSelectors.decodeProperties(request.getParameter(SELECTORS_PARAMETER));

        // Search path
        String searchPath;
        List<String> locationValues = selectors.get(LOCATION_SELECTOR_ID);
        if (CollectionUtils.isEmpty(locationValues)) {
            searchPath = null;
        } else {
            searchPath = locationValues.get(0);
        }

        return searchPath;
    }


    @Override
    public JSONArray loadVocabulary(PortalControllerContext portalControllerContext, String vocabularyName, String filter) throws PortletException, IOException {
        // Vocabulary JSON array
        JSONArray array = this.repository.loadVocabulary(portalControllerContext, vocabularyName);

        // Select2 results
        JSONArray results;
        if ((array == null) || (array.isEmpty())) {
            results = new JSONArray();
        } else {
            results = this.parseVocabulary(array, filter);
        }

        return results;
    }


    /**
     * Parse vocabulary JSON array with filter.
     *
     * @param jsonArray JSON array
     * @param filter    filter, may be null
     * @return results
     */
    private JSONArray parseVocabulary(JSONArray jsonArray, String filter) throws IOException {
        Map<String, TaskbarSearchVocabularyItem> items = new HashMap<>(jsonArray.size());
        Set<String> rootItems = new LinkedHashSet<>();

        boolean multilevel = false;

        for (Object object : jsonArray) {
            JSONObject jsonObject = (JSONObject) object;

            String key = jsonObject.getString("key");
            String value = jsonObject.getString("value");
            String parent = null;
            if (jsonObject.containsKey("parent")) {
                parent = jsonObject.getString("parent");
            }
            boolean matches = this.matchesVocabularyItem(value, filter);

            TaskbarSearchVocabularyItem item = items.get(key);
            if (item == null) {
                item = this.applicationContext.getBean(TaskbarSearchVocabularyItem.class, key);
                items.put(key, item);
            }
            item.setValue(value);
            item.setParent(parent);
            if (matches) {
                item.setMatches(true);
                item.setDisplayed(true);
            }

            if (StringUtils.isEmpty(parent)) {
                rootItems.add(key);
            } else {
                multilevel = true;

                TaskbarSearchVocabularyItem parentItem = items.get(parent);
                if (parentItem == null) {
                    parentItem = this.applicationContext.getBean(TaskbarSearchVocabularyItem.class, parent);
                    items.put(parent, parentItem);
                }
                parentItem.getChildren().add(key);

                if (item.isDisplayed()) {
                    while (parentItem != null) {
                        parentItem.setDisplayed(true);

                        if (StringUtils.isEmpty(parentItem.getParent())) {
                            parentItem = null;
                        } else {
                            parentItem = items.get(parentItem.getParent());
                        }
                    }
                }
            }
        }


        JSONArray results = new JSONArray();
        this.generateVocabularyChildren(items, results, rootItems, multilevel, 1, null);

        return results;
    }


    /**
     * Check if value matches filter.
     *
     * @param value  vocabulary item value
     * @param filter filter
     * @return true if value matches filter
     */
    private boolean matchesVocabularyItem(String value, String filter) throws UnsupportedEncodingException {
        boolean matches = true;

        if (filter != null) {
            // Decoded value
            String decodedValue = URLDecoder.decode(value, CharEncoding.UTF_8);
            // Diacritical value
            String diacriticalValue = Normalizer.normalize(decodedValue, Normalizer.Form.NFD).replaceAll("\\p{IsM}+", StringUtils.EMPTY);

            // Filter
            String[] splittedFilters = StringUtils.split(filter, "*");
            for (String splittedFilter : splittedFilters) {
                // Diacritical filter
                String diacriticalFilter = Normalizer.normalize(splittedFilter, Normalizer.Form.NFD).replaceAll("\\p{IsM}+", StringUtils.EMPTY);

                if (!StringUtils.containsIgnoreCase(diacriticalValue, diacriticalFilter)) {
                    matches = false;
                    break;
                }
            }
        }

        return matches;
    }


    /**
     * Generate vocabulary children.
     *
     * @param items     vocabulary items
     * @param jsonArray results JSON array
     * @param children  children
     * @param optgroup  options group presentation indicator
     * @param level     depth level
     * @param parentId  parent identifier
     */
    private void generateVocabularyChildren(Map<String, TaskbarSearchVocabularyItem> items, JSONArray jsonArray, Set<String> children, boolean optgroup, int level, String parentId) throws UnsupportedEncodingException {
        for (String child : children) {
            TaskbarSearchVocabularyItem item = items.get(child);
            if ((item != null) && item.isDisplayed()) {
                // Identifier
                String id;
                if (StringUtils.isEmpty(parentId)) {
                    id = item.getKey();
                } else {
                    id = parentId + "/" + item.getKey();
                }

                JSONObject object = new JSONObject();
                object.put("id", id);
                object.put("text", URLDecoder.decode(item.getValue(), CharEncoding.UTF_8));
                object.put("optgroup", optgroup);
                object.put("level", level);

                if (!item.isMatches()) {
                    object.put("disabled", true);
                }

                jsonArray.add(object);

                if (!item.getChildren().isEmpty()) {
                    this.generateVocabularyChildren(items, jsonArray, item.getChildren(), false, level + 1, id);
                }
            }
        }
    }

}
