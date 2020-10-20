package fr.index.cloud.ens.filebrowser.commons.portlet.controller;

import fr.index.cloud.ens.filebrowser.commons.portlet.service.AbstractFileBrowserService;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.workspace.filebrowser.portlet.controller.FileBrowserController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;

/**
 * File browser controller abstract super-class.
 *
 * @author Cédric Krommenhoek
 * @see FileBrowserController
 */
public abstract class AbstractFileBrowserController extends FileBrowserController {

    /**
     * Portlet context.
     */
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private PortletContext portletContext;

    /**
     * Portlet service.
     */
    @Autowired
    private AbstractFileBrowserService service;


    /**
     * Constructor.
     */
    public AbstractFileBrowserController() {
        super();
    }


    /**
     * Get columns configuration URL model attribute.
     *
     * @param request  portlet request
     * @param response portlet response
     * @return URL
     */
    @ModelAttribute("columnsConfigurationUrl")
    public String getColumnsConfigurationUrl(PortletRequest request, PortletResponse response) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        return this.service.getColumnsConfigurationUrl(portalControllerContext);
    }

}
