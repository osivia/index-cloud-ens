package fr.index.cloud.ens.taskbar.portlet.repository;

import fr.index.cloud.ens.taskbar.portlet.model.AddDropdownItem;
import fr.index.cloud.ens.taskbar.portlet.model.FolderTask;
import fr.index.cloud.ens.taskbar.portlet.model.Task;
import fr.index.cloud.ens.taskbar.portlet.model.TaskbarWindowProperties;
import org.osivia.directory.v2.model.preferences.UserSavedSearch;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.taskbar.TaskbarTask;

import javax.portlet.PortletException;
import java.io.IOException;
import java.util.List;
import java.util.SortedSet;

/**
 * Taskbar portlet repository interface.
 *
 * @author Cédric Krommenhoek
 */
public interface TaskbarRepository {

    /**
     * Get base path.
     *
     * @param portalControllerContext portal controller context
     * @param windowProperties        window properties
     * @return path
     */
    String getBasePath(PortalControllerContext portalControllerContext, TaskbarWindowProperties windowProperties) throws PortletException;


    /**
     * Get navigation tasks.
     *
     * @param portalControllerContext portal controller context
     * @param basePath                base path
     * @return tasks
     */
    List<TaskbarTask> getNavigationTasks(PortalControllerContext portalControllerContext, String basePath) throws PortletException;


    /**
     * Generate "add" dropdown items.
     *
     * @param portalControllerContext portal controller context
     * @return dropdown items
     */
    List<AddDropdownItem> generateAddDropdownItems(PortalControllerContext portalControllerContext) throws PortletException, IOException;


    /**
     * Generate folder task.
     *
     * @param portalControllerContext portal controller context
     * @param basePath                base path
     * @param path                    task path
     * @param searchPath              search path, may be null
     * @return task
     */
    Task generateFolderTask(PortalControllerContext portalControllerContext, String basePath, String path, String searchPath) throws PortletException;


    /**
     * Get folder children.
     *
     * @param portalControllerContext portal controller context
     * @param basePath                base path
     * @param path                    folder path
     * @param searchPath              search path, may be null
     * @return folder children
     */
    SortedSet<FolderTask> getFolderChildren(PortalControllerContext portalControllerContext, String basePath, String path, String searchPath) throws PortletException;


    /**
     * Get saved searches tasks.
     *
     * @param portalControllerContext portal controller context
     * @return tasks
     */
    List<Task> getSavedSearchesTasks(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Get search path.
     *
     * @param portalControllerContext portal controller context
     * @return path
     */
    String getSearchPath(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Get saved search.
     *
     * @param portalControllerContext portal controller context
     * @param id                      saved search identifier
     * @return saved search
     */
    UserSavedSearch getSavedSearch(PortalControllerContext portalControllerContext, int id) throws PortletException;


    /**
     * Move documents.
     *
     * @param portalControllerContext portal controller context
     * @param sourceIds               source identifiers
     * @param targetId                target identifier
     */
    void moveDocuments(PortalControllerContext portalControllerContext, List<String> sourceIds, String targetId) throws PortletException;

}
