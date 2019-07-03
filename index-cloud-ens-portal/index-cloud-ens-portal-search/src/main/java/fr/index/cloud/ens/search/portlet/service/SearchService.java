package fr.index.cloud.ens.search.portlet.service;

import fr.index.cloud.ens.search.portlet.model.SearchForm;
import org.osivia.portal.api.context.PortalControllerContext;

import javax.portlet.PortletException;

/**
 * Search portlet service interface.
 *
 * @author Cédric Krommenhoek
 */
public interface SearchService {

    /**
     * Get search form.
     *
     * @param portalControllerContext portal controller context
     * @return form
     */
    SearchForm getForm(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Get search options URL.
     *
     * @param portalControllerContext portal controller context
     * @return URL
     */
    String getOptionsUrl(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Get search redirection URL.
     *
     * @param portalControllerContext portal controller context
     * @param form                    search form
     * @return URL
     */
    String getSearchRedirectionUrl(PortalControllerContext portalControllerContext, SearchForm form) throws PortletException;

}
