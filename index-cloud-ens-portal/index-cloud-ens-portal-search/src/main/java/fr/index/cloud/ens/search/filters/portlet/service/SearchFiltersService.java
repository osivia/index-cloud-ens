package fr.index.cloud.ens.search.filters.portlet.service;

import fr.index.cloud.ens.search.common.portlet.service.SearchCommonService;
import fr.index.cloud.ens.search.filters.portlet.model.SearchFiltersForm;
import fr.index.cloud.ens.search.filters.portlet.model.SearchFiltersVocabulary;
import net.sf.json.JSONArray;
import org.osivia.portal.api.context.PortalControllerContext;

import javax.portlet.PortletException;
import java.io.IOException;
import java.util.Date;

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
     * Load select2 vocabulary.
     *
     * @param portalControllerContext portal controller context
     * @param vocabulary              vocabulary
     * @param filter                  select2 filter
     * @return select2 results JSON array
     */
    JSONArray loadVocabulary(PortalControllerContext portalControllerContext, SearchFiltersVocabulary vocabulary, String filter) throws PortletException, IOException;


    /**
     * Get location URL.
     *
     * @param portalControllerContext portal controller context
     * @return URL
     */
    String getLocationUrl(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Format date.
     *
     * @param date date
     * @return formatted date
     */
    String formatDate(Date date);


    /**
     * Parse date.
     *
     * @param source source
     * @return date
     */
    Date parseDate(String source);

}
