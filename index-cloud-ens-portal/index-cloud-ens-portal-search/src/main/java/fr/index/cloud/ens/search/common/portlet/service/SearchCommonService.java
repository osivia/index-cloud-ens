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
     * Keywords selector identifier.
     */
    String KEYWORDS_SELECTOR_ID = "search";


    /**
     * Resolve view path.
     *
     * @param portalControllerContext portal controller context
     * @param name                    view name
     * @return path
     */
    String resolveViewPath(PortalControllerContext portalControllerContext, String name) throws PortletException;

}
