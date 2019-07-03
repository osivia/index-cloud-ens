package fr.index.cloud.ens.search.options.portlet.controller;

import fr.index.cloud.ens.search.options.portlet.model.SearchOptionsForm;
import fr.index.cloud.ens.search.options.portlet.service.SearchOptionsService;
import net.sf.json.JSONArray;
import org.osivia.portal.api.context.PortalControllerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;

import javax.portlet.*;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Search options portlet controller.
 *
 * @author Cédric Krommenhoek
 */
@Controller
@RequestMapping("VIEW")
public class SearchOptionsController {

    /**
     * Portlet context.
     */
    @Autowired
    private PortletContext portletContext;

    /**
     * Portlet service.
     */
    @Autowired
    private SearchOptionsService service;


    /**
     * View render mapping.
     *
     * @return view path
     */
    @RenderMapping
    public String view() {
        return "view";
    }


    /**
     * Search action mapping.
     *
     * @param request  action request
     * @param response action response
     * @param form     search options form model attribute
     */
    @ActionMapping("search")
    public void search(ActionRequest request, ActionResponse response, @ModelAttribute("form") SearchOptionsForm form) throws PortletException, IOException {
        // Portal Controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        // Search redirection URL
        String url = this.service.getSearchRedirectionUrl(portalControllerContext, form);

        response.sendRedirect(url);
    }


    /**
     * Load levels select2 vocabulary resource mapping.
     *
     * @param request  resource request
     * @param response resource response
     * @param filter   select2 filter request parameter
     */
    @ResourceMapping("loadLevels")
    public void loadLevels(ResourceRequest request, ResourceResponse response, @RequestParam(name = "filter", required = false) String filter) throws PortletException, IOException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(portletContext, request, response);

        // Select2 results
        JSONArray results = this.service.loadLevels(portalControllerContext, filter);

        // Content type
        response.setContentType("application/json");

        // Content
        PrintWriter printWriter = new PrintWriter(response.getPortletOutputStream());
        printWriter.write(results.toString());
        printWriter.close();
    }


    /**
     * Get search options form model attribute.
     *
     * @param request  portlet request
     * @param response portlet response
     * @return form
     */
    @ModelAttribute("form")
    public SearchOptionsForm getForm(PortletRequest request, PortletResponse response) throws PortletException {
        // Portal Controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        return this.service.getForm(portalControllerContext);
    }

}
