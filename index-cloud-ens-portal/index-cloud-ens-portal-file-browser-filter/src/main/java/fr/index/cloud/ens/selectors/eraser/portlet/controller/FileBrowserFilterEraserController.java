package fr.index.cloud.ens.selectors.eraser.portlet.controller;

import fr.index.cloud.ens.selectors.eraser.portlet.service.FileBrowserFilterEraserService;
import org.osivia.portal.api.context.PortalControllerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import javax.portlet.*;

/**
 * File browser filter eraser portlet controller.
 *
 * @author Cédric Krommenhoek
 */
@Controller
@RequestMapping("VIEW")
public class FileBrowserFilterEraserController {

    /**
     * Portlet context.
     */
    @Autowired
    private PortletContext portletContext;
    /**
     * Portlet service.
     */
    @Autowired
    private FileBrowserFilterEraserService service;


    /**
     * Constructor.
     */
    public FileBrowserFilterEraserController() {
        super();
    }


    /**
     * View render mapping.
     *
     * @param request  render request
     * @param response render response
     * @return view path
     */
    @RenderMapping
    public String view(RenderRequest request, RenderResponse response) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        if (this.service.isEmpty(portalControllerContext)) {
            request.setAttribute("osivia.emptyResponse", "1");
        }

        return "view";
    }


    /**
     * Erase filters action mapping.
     *
     * @param request  action request
     * @param response action response
     */
    @ActionMapping("erase")
    public void erase(ActionRequest request, ActionResponse response) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        this.service.erase(portalControllerContext);
    }

}
