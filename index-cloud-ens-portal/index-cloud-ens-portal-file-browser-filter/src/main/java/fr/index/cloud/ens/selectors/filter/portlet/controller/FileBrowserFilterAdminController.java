package fr.index.cloud.ens.selectors.filter.portlet.controller;

import fr.index.cloud.ens.selectors.filter.portlet.model.FileBrowserFilterWindowProperties;
import fr.index.cloud.ens.selectors.filter.portlet.service.FileBrowserFilterService;
import org.osivia.portal.api.context.PortalControllerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import javax.portlet.*;

/**
 * File browser filter portlet administration controller.
 *
 * @author Cédric Krommenhoek
 */
@Controller
@RequestMapping("ADMIN")
public class FileBrowserFilterAdminController {

    /**
     * Portlet context.
     */
    @Autowired
    private PortletContext portletContext;
    /**
     * Portlet service.
     */
    @Autowired
    private FileBrowserFilterService service;


    /**
     * Constructor.
     */
    public FileBrowserFilterAdminController() {
        super();
    }


    /**
     * View render mapping.
     *
     * @return view path
     */
    @RenderMapping
    public String view() {
        return "admin";
    }


    /**
     * Save action mapping.
     *
     * @param request          action request
     * @param response         action response
     * @param windowProperties window properties model attribute
     */
    @ActionMapping("save")
    public void save(ActionRequest request, ActionResponse response, @ModelAttribute("windowProperties") FileBrowserFilterWindowProperties windowProperties) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        this.service.setWindowProperties(portalControllerContext, windowProperties);

        response.setPortletMode(PortletMode.VIEW);
        response.setWindowState(WindowState.NORMAL);
    }


    /**
     * Get window properties model attribute.
     *
     * @param request  portlet request
     * @param response portlet response
     * @return window properties
     */
    @ModelAttribute("windowProperties")
    public FileBrowserFilterWindowProperties getWindowProperties(PortletRequest request, PortletResponse response) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        return this.service.getWindowProperties(portalControllerContext);
    }

}
