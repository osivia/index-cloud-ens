package fr.index.cloud.ens.search.common.portlet.repository;

import org.osivia.directory.v2.model.preferences.UserPreferences;
import org.osivia.directory.v2.model.preferences.UserSavedSearch;
import org.osivia.portal.api.context.PortalControllerContext;

import javax.portlet.PortletException;
import java.util.List;

/**
 * Search common repository interface.
 *
 * @author Cédric Krommenhoek
 */
public interface SearchCommonRepository {

    /**
     * Search filters task identifier.
     */
    String SEARCH_FILTERS_TASK_ID = "SEARCH_FILTERS";


    /**
     * Get user workspace path.
     *
     * @param portalControllerContext portal controller context
     * @return path
     */
    String getUserWorkspacePath(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Get search path.
     *
     * @param portalControllerContext portal controller context
     * @return path
     */
    String getSearchPath(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Get search filters path.
     *
     * @param portalControllerContext portal controller context
     * @return path
     */
    String getSearchFiltersPath(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Get saved searches.
     *
     * @param portalControllerContext portal controller context
     * @return saved searches
     */
    List<UserSavedSearch> getSavedSearches(PortalControllerContext portalControllerContext) throws PortletException;

}
