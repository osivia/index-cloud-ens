package fr.index.cloud.ens.taskbar.portlet.repository;

import fr.index.cloud.ens.taskbar.portlet.model.FolderTask;
import fr.index.cloud.ens.taskbar.portlet.model.Task;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.taskbar.TaskbarTask;

import javax.portlet.PortletException;
import java.util.List;
import java.util.SortedSet;

/**
 * Taskbar portlet repository interface.
 *
 * @author Cédric Krommenhoek
 */
public interface TaskbarRepository {

    /**
     * Get navigation tasks.
     *
     * @param portalControllerContext portal controller context
     * @return tasks
     */
    List<TaskbarTask> getNavigationTasks(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Generate folder tree.
     *
     * @param portalControllerContext portal controller context
     * @param path                    task path
     */
    Task generateFolderTask(PortalControllerContext portalControllerContext, String path) throws PortletException;


    /**
     * Get folder children.
     *
     * @param portalControllerContext portal controller context
     * @param path                    folder path
     * @return folder children
     */
    SortedSet<FolderTask> getFolderChildren(PortalControllerContext portalControllerContext, String path) throws PortletException;


    /**
     * Move documents.
     *
     * @param portalControllerContext portal controller context
     * @param sourceIds               source identifiers
     * @param targetId                target identifier
     */
    void moveDocuments(PortalControllerContext portalControllerContext, List<String> sourceIds, String targetId) throws PortletException;


    /**
     * Get base path.
     *
     * @param portalControllerContext portal controller context
     * @return path
     */
    String getBasePath(PortalControllerContext portalControllerContext) throws PortletException;

}
