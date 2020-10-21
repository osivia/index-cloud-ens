package fr.index.cloud.ens.filebrowser.commons.portlet.service;

import fr.index.cloud.ens.filebrowser.commons.portlet.model.AbstractFileBrowserForm;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.workspace.filebrowser.portlet.service.FileBrowserService;

import javax.portlet.PortletException;

/**
 * File browser portlet service interface.
 *
 * @author Cédric Krommenhoek
 * @see FileBrowserService
 */
public interface AbstractFileBrowserService extends FileBrowserService {

    /**
     * Search filter parameter.
     */
    String SEARCH_FILTER_PARAMETER = "search-filter";


    @Override
    AbstractFileBrowserForm getForm(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Get columns configuration URL.
     *
     * @param portalControllerContext portal controller context.
     * @return URL
     */
    String getColumnsConfigurationUrl(PortalControllerContext portalControllerContext) throws PortletException;

}
