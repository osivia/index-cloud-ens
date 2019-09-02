package fr.index.cloud.ens.search.common.portlet.repository;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.cms.VirtualNavigationUtils;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.taskbar.ITaskbarService;
import org.osivia.portal.api.taskbar.TaskbarTask;
import org.osivia.portal.api.user.UserPreferences;
import org.osivia.portal.api.user.UserSavedSearch;
import org.osivia.portal.core.cms.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.portlet.PortletException;
import java.util.Iterator;
import java.util.List;

/**
 * Search common repository implementation.
 *
 * @author Cédric Krommenhoek
 * @see SearchCommonRepository
 */
public class SearchCommonRepositoryImpl implements SearchCommonRepository {

    /**
     * CMS service locator.
     */
    @Autowired
    private ICMSServiceLocator cmsServiceLocator;

    /**
     * Taskbar service.
     */
    @Autowired
    private ITaskbarService taskbarService;


    /**
     * Constructor.
     */
    public SearchCommonRepositoryImpl() {
        super();
    }


    @Override
    public String getSearchPath(PortalControllerContext portalControllerContext) throws PortletException {
        return this.getTaskPath(portalControllerContext, ITaskbarService.SEARCH_TASK_ID);
    }


    @Override
    public String getSearchFiltersPath(PortalControllerContext portalControllerContext) throws PortletException {
        return this.getTaskPath(portalControllerContext, SEARCH_FILTERS_TASK_ID);
    }


    /**
     * Get task path.
     * @param portalControllerContext portal controller context
     * @param taskId task identifier
     * @return path
     */
    private String getTaskPath(PortalControllerContext portalControllerContext, String taskId) throws PortletException {
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

                if (StringUtils.equals(taskId, stapleId)) {
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
    public UserPreferences getUserPreferences(PortalControllerContext portalControllerContext) throws PortletException {
        // CMS service
        ICMSService cmsService = this.cmsServiceLocator.getCMSService();
        // CMS context
        CMSServiceCtx cmsContext = new CMSServiceCtx();
        cmsContext.setPortalControllerContext(portalControllerContext);

        // User preferences
        UserPreferences userPreferences;
        try {
            userPreferences = cmsService.getUserPreferences(portalControllerContext);
        } catch (PortalException e) {
            throw new PortletException(e);
        }

        return userPreferences;
    }


    @Override
    public List<UserSavedSearch> getSavedSearches(PortalControllerContext portalControllerContext) throws PortletException {
        // User preferences
        UserPreferences userPreferences = this.getUserPreferences(portalControllerContext);

        return userPreferences.getSavedSearches();
    }

}
