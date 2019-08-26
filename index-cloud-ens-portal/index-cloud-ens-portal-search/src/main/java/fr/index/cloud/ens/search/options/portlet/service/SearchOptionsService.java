package fr.index.cloud.ens.search.options.portlet.service;

import fr.index.cloud.ens.search.common.portlet.service.SearchCommonService;
import fr.index.cloud.ens.search.options.portlet.model.SearchOptionsForm;
import net.sf.json.JSONArray;
import org.osivia.portal.api.context.PortalControllerContext;

import javax.portlet.PortletException;
import java.io.IOException;

/**
 * Search options portlet service interface.
 *
 * @author Cédric Krommenhoek
 * @see SearchCommonService
 */
public interface SearchOptionsService extends SearchCommonService {

    /**
     * Search options portlet instance.
     */
    String PORTLET_INSTANCE = "index-cloud-ens-search-options-instance";

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
     * Get search options form.
     *
     * @param portalControllerContext portal controller context
     * @return form
     */
    SearchOptionsForm getForm(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Get search redirection URL.
     *
     * @param portalControllerContext portal controller context
     * @param form                    search options form
     * @return URL
     */
    String getSearchRedirectionUrl(PortalControllerContext portalControllerContext, SearchOptionsForm form) throws PortletException;


    /**
     * Clear location.
     *
     * @param portalControllerContext portal controller context
     * @param form                    search options form
     */
    void clearLocation(PortalControllerContext portalControllerContext, SearchOptionsForm form) throws PortletException;


    /**
     * Load levels select2 vocabulary.
     *
     * @param portalControllerContext portal controller context
     * @param filter                  select2 filter
     * @return select2 results JSON array
     */
    JSONArray loadLevels(PortalControllerContext portalControllerContext, String filter) throws PortletException, IOException;

}
