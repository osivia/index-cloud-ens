package fr.index.cloud.ens.search.common.portlet.service;

import fr.index.cloud.ens.search.common.portlet.model.SearchFiltersVocabulary;
import net.sf.json.JSONArray;
import org.osivia.portal.api.context.PortalControllerContext;

import javax.portlet.PortletException;
import java.io.IOException;

/**
 * Search common service interface.
 *
 * @author Cédric Krommenhoek
 */
public interface SearchCommonService {

    /**
     * Mutualized space path.
     */
    String MUTUALIZED_SPACE_PATH = System.getProperty("config.mutualized.path");

    /**
     * Mutualized saved searches category identifier.
     */
    String MUTUALIZED_SAVED_SEARCHES_CATEGORY_ID = "mutualized";

    /**
     * Selectors parameter.
     */
    String SELECTORS_PARAMETER = "selectors";

    /**
     * Keywords selector identifier.
     */
    String KEYWORDS_SELECTOR_ID = "keywords";
    /**
     * Document types selector identifier.
     */
    String DOCUMENT_TYPES_SELECTOR_ID = "documentTypes";
    /**
     * Levels selector identifier.
     */
    String LEVELS_SELECTOR_ID = "levels";
    /**
     * Subjects selector identifier.
     */
    String SUBJECTS_SELECTOR_ID = "subjects";
    /**
     * Location selector identifier.
     */
    String LOCATION_SELECTOR_ID = "location";
    /**
     * Size range selector identifier.
     */
    String SIZE_RANGE_SELECTOR_ID = "size-range";
    /**
     * Size amount selector identifier.
     */
    String SIZE_AMOUNT_SELECTOR_ID = "size-amount";
    /**
     * Size unit selector identifier.
     */
    String SIZE_UNIT_SELECTOR_ID = "size-unit";
    /**
     * Computed size selector identifier.
     */
    String COMPUTED_SIZE_SELECTOR_ID = "size";
    /**
     * Date range selector identifier.
     */
    String DATE_RANGE_SELECTOR_ID = "date-range";
    /**
     * Customized date selector identifier.
     */
    String CUSTOMIZED_DATE_SELECTOR_ID = "customized-date";
    /**
     * Computed date selector identifier.
     */
    String COMPUTED_DATE_SELECTOR_ID = "date";


    /**
     * Resolve view path.
     *
     * @param portalControllerContext portal controller context
     * @param name                    view name
     * @return path
     */
    String resolveViewPath(PortalControllerContext portalControllerContext, String name) throws PortletException;


    /**
     * Load select2 vocabulary.
     *
     * @param portalControllerContext portal controller context
     * @param vocabulary              vocabulary
     * @param filter                  select2 filter
     * @return select2 results JSON array
     */
    JSONArray loadVocabulary(PortalControllerContext portalControllerContext, SearchFiltersVocabulary vocabulary, String filter) throws PortletException, IOException;

}
