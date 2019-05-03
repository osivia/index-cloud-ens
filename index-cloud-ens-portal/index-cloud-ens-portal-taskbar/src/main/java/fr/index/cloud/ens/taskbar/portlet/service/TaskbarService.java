package fr.index.cloud.ens.taskbar.portlet.service;

import fr.index.cloud.ens.taskbar.portlet.model.Taskbar;
import net.sf.json.JSONArray;
import org.osivia.portal.api.context.PortalControllerContext;

import javax.portlet.PortletException;
import java.util.List;

/**
 * Taskbar portlet service interface.
 *
 * @author Cédric Krommenhoek
 */
public interface TaskbarService {

    /**
     * Get taskbar.
     *
     * @param portalControllerContext portal controller context
     * @return taskbar
     */
    Taskbar getTaskbar(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Drop.
     *
     * @param portalControllerContext portal controller context
     * @param sourceIds               source identifiers
     * @param targetId                target identifier
     */
    void drop(PortalControllerContext portalControllerContext, List<String> sourceIds, String targetId) throws PortletException;


    /**
     * Get folder children.
     *
     * @param portalControllerContext portal controller context
     * @param path                    parent folder path
     * @return JSON array
     */
    JSONArray getFolderChildren(PortalControllerContext portalControllerContext, String path) throws PortletException;

}
