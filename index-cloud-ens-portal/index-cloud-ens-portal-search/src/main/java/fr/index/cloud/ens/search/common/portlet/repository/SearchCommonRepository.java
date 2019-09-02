package fr.index.cloud.ens.search.common.portlet.repository;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.user.UserPreferences;
import org.osivia.portal.api.user.UserSavedSearch;

import javax.portlet.PortletException;
import java.util.List;

/**
 * Search common repository interface.
 *
 * @author Cédric Krommenhoek
 */
public interface SearchCommonRepository {

    /** Search filters task identifier. */
    String SEARCH_FILTERS_TASK_ID = "SEARCH_FILTERS";


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
     * Get user preferences.
     *
     * @param portalControllerContext portal controller context
     * @return user preferences
     */
    UserPreferences getUserPreferences(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Get saved searches.
     *
     * @param portalControllerContext portal controller context
     * @return saved searches
     */
    List<UserSavedSearch> getSavedSearches(PortalControllerContext portalControllerContext) throws PortletException;

}
