package fr.index.cloud.ens.search.common.portlet.repository;

import org.osivia.portal.api.context.PortalControllerContext;

import javax.portlet.PortletException;

/**
 * Search common repository interface.
 *
 * @author Cédric Krommenhoek
 */
public interface SearchCommonRepository {

    /**
     * Get search path.
     *
     * @param portalControllerContext portal controller context
     * @return path
     */
    String getSearchPath(PortalControllerContext portalControllerContext) throws PortletException;

}
