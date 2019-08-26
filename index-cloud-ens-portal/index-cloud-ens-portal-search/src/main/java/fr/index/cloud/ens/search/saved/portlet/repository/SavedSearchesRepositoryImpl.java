package fr.index.cloud.ens.search.saved.portlet.repository;

import fr.index.cloud.ens.search.common.portlet.repository.SearchCommonRepositoryImpl;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.user.UserPreferences;
import org.osivia.portal.api.user.UserSavedSearch;
import org.osivia.portal.core.cms.CMSServiceCtx;
import org.osivia.portal.core.cms.ICMSService;
import org.osivia.portal.core.cms.ICMSServiceLocator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.portlet.PortletException;
import java.util.List;

/**
 * Saved searches portlet repository implementation.
 *
 * @author Cédric Krommenhoek
 * @see SearchCommonRepositoryImpl
 * @see SavedSearchesRepository
 */
@Repository
public class SavedSearchesRepositoryImpl extends SearchCommonRepositoryImpl implements SavedSearchesRepository {

    /**
     * CMS service locator.
     */
    @Autowired
    private ICMSServiceLocator cmsServiceLocator;


    /**
     * Constructor.
     */
    public SavedSearchesRepositoryImpl() {
        super();
    }


    @Override
    public List<UserSavedSearch> getSavedSearches(PortalControllerContext portalControllerContext) throws PortletException {
        // User preferences
        UserPreferences userPreferences = this.getUserPreferences(portalControllerContext);

        return userPreferences.getSavedSearches();
    }


    /**
     * Get user preferences.
     *
     * @param portalControllerContext portal controller context
     * @return user preferences
     */
    private UserPreferences getUserPreferences(PortalControllerContext portalControllerContext) throws PortletException {
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

}
