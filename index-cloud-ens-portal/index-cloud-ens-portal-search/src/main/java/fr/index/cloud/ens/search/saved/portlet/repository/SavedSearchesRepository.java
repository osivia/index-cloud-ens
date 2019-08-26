package fr.index.cloud.ens.search.saved.portlet.repository;

import fr.index.cloud.ens.search.common.portlet.repository.SearchCommonRepository;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.user.UserSavedSearch;

import javax.portlet.PortletException;
import java.util.List;

/**
 * Saved searches portlet repository interface.
 *
 * @author Cédric Krommenhoek
 * @see SearchCommonRepository
 */
public interface SavedSearchesRepository extends SearchCommonRepository {

    /**
     * Get saved searches.
     *
     * @param portalControllerContext portal controller context
     * @return saved searches
     */
    List<UserSavedSearch> getSavedSearches(PortalControllerContext portalControllerContext) throws PortletException;

}
