package fr.index.cloud.ens.search.filters.portlet.service;

import fr.index.cloud.ens.search.common.portlet.service.SearchCommonService;
import fr.index.cloud.ens.search.filters.portlet.model.SearchFiltersForm;
import net.sf.json.JSONArray;
import org.osivia.portal.api.context.PortalControllerContext;

import javax.portlet.PortletException;
import java.io.IOException;

/**
 * Search filters portlet service interface.
 *
 * @author Cédric Krommenhoek
 * @see SearchCommonService
 */
public interface SearchFiltersService extends SearchCommonService {

    /**
     * Search filters portlet instance.
     */
    String PORTLET_INSTANCE = "index-cloud-ens-search-filters-instance";

    /**
     * Modal indicator window property.
     */
    String MODAL_WINDOW_PROPERTY = "osivia.search.modal";
    /**
     * Navigation path window property.
     */
    String NAVIGATION_PATH_WINDOW_PROPERTY = "osivia.search.navigation-path";
    /**
     * Selectors window property.
     */
    String SELECTORS_WINDOW_PROPERTY = "osivia.search.selectors";

    /**
     * Levels vocabulary name.
     */
    String LEVELS_VOCABULARY = "idx_level";


    /**
     * Get search filters form.
     *
     * @param portalControllerContext portal controller context
     * @return form
     */
    SearchFiltersForm getForm(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Update search location.
     *
     * @param portalControllerContext portal controller context
     * @param form                    search filters form
     */
    void updateLocation(PortalControllerContext portalControllerContext, SearchFiltersForm form) throws PortletException;


    /**
     * Get search redirection URL.
     *
     * @param portalControllerContext portal controller context
     * @param form                    search filters form
     * @return URL
     */
    String getSearchRedirectionUrl(PortalControllerContext portalControllerContext, SearchFiltersForm form) throws PortletException;


    /**
     * Search search.
     *
     * @param portalControllerContext portal controller context
     * @param form                    search options form
     * @return URL
     */
    String saveSearch(PortalControllerContext portalControllerContext, SearchFiltersForm form) throws PortletException;


    /**
     * Load levels select2 vocabulary.
     *
     * @param portalControllerContext portal controller context
     * @param filter                  select2 filter
     * @return select2 results JSON array
     */
    JSONArray loadLevels(PortalControllerContext portalControllerContext, String filter) throws PortletException, IOException;


    /**
     * Get location URL.
     *
     * @param portalControllerContext portal controller context
     * @return URL
     */
    String getLocationUrl(PortalControllerContext portalControllerContext) throws PortletException;

}
