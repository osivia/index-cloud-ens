package fr.index.cloud.ens.search.saved.portlet.service;

import fr.index.cloud.ens.search.common.portlet.service.SearchCommonService;
import fr.index.cloud.ens.search.saved.portlet.model.SavedSearchesForm;
import org.osivia.portal.api.context.PortalControllerContext;

import javax.portlet.PortletException;

/**
 * Saved searches portlet service interface.
 *
 * @author Cédric Krommenhoek
 * @see SearchCommonService
 */
public interface SavedSearchesService extends SearchCommonService {

    /**
     * Get saved searches form.
     *
     * @param portalControllerContext portal controller context
     * @return form
     */
    SavedSearchesForm getForm(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Render view.
     *
     * @param portalControllerContext portal controller context
     * @return view path
     */
    String renderView(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Get saved search URL.
     *
     * @param portalControllerContext portal controller context
     * @param form                    saved searches form
     * @param id                      saved search identifier
     * @return URL
     */
    String getSavedSearchUrl(PortalControllerContext portalControllerContext, SavedSearchesForm form, int id) throws PortletException;

}
