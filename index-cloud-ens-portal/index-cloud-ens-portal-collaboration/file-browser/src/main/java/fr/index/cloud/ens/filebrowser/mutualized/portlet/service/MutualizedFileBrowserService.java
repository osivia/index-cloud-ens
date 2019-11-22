package fr.index.cloud.ens.filebrowser.mutualized.portlet.service;

import fr.index.cloud.ens.filebrowser.commons.portlet.service.AbstractFileBrowserService;
import fr.index.cloud.ens.filebrowser.mutualized.portlet.model.MutualizedFileBrowserForm;
import fr.index.cloud.ens.filebrowser.portlet.service.CustomizedFileBrowserService;
import org.osivia.portal.api.context.PortalControllerContext;

import javax.portlet.PortletException;

/**
 * Mutualized file browser portlet service interface.
 *
 * @author Cédric Krommenhoek
 */
public interface MutualizedFileBrowserService extends AbstractFileBrowserService {

    /**
     * Portlet instance.
     */
    String PORTLET_INSTANCE = "index-cloud-ens-mutualized-file-browser-instance";


    /**
     * Get form.
     *
     * @param portalControllerContext portal controller context
     * @return form
     */
    MutualizedFileBrowserForm getForm(PortalControllerContext portalControllerContext) throws PortletException;

}
