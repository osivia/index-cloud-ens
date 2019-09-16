package fr.index.cloud.ens.search.common.portlet.service;

import org.osivia.portal.api.context.PortalControllerContext;

import javax.portlet.PortletException;

/**
 * Search common service interface.
 *
 * @author Cédric Krommenhoek
 */
public interface SearchCommonService {

    /**
     * Selectors parameter.
     */
    String SELECTORS_PARAMETER = "selectors";

    /**
     * Level selector identifier.
     */
    String LEVEL_SELECTOR_ID = "level";
    /**
     * Subject selector identifier.
     */
    String SUBJECT_SELECTOR_ID = "subject";
    /**
     * Location selector identifier.
     */
    String LOCATION_SELECTOR_ID = "location";
    /**
     * Keywords selector identifier.
     */
    String KEYWORDS_SELECTOR_ID = "search";
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

}
