package fr.index.cloud.ens.levels.portlet.service;

import fr.index.cloud.ens.levels.portlet.model.HighestLevels;
import org.osivia.portal.api.context.PortalControllerContext;

import javax.portlet.PortletException;

/**
 * Highest levels portlet service interface.
 *
 * @author Cédric Krommenhoek
 */
public interface HighestLevelsService {

    /**
     * Get highest levels.
     *
     * @param portalControllerContext portal controller context
     * @return highest levels
     */
    HighestLevels getHighestLevels(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Get level search URL.
     *
     * @param portalControllerContext portal controller context
     * @param id                      level identifier
     * @return URL
     */
    String getSearchUrl(PortalControllerContext portalControllerContext, String id) throws PortletException;

}
