package fr.index.cloud.ens.search.portlet.controller;

import fr.index.cloud.ens.search.portlet.model.SearchForm;
import fr.index.cloud.ens.search.portlet.service.SearchService;
import org.osivia.portal.api.context.PortalControllerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import javax.portlet.*;
import java.io.IOException;

/**
 * Search portlet controller.
 *
 * @author Cédric Krommenhoek
 */
@Controller
@RequestMapping("VIEW")
public class SearchController {

    /**
     * Portlet context.
     */
    @Autowired
    private PortletContext portletContext;

    /**
     * Portlet service.
     */
    @Autowired
    private SearchService service;


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
     * @param form     search form model attribute
     */
    @ActionMapping("search")
    public void search(ActionRequest request, ActionResponse response, @ModelAttribute("form") SearchForm form) throws PortletException, IOException {
        // Portal Controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        // Search redirection URL
        String url = this.service.getSearchRedirectionUrl(portalControllerContext, form);

        response.sendRedirect(url);
    }


    /**
     * Get search form model attribute.
     *
     * @param request  portlet request
     * @param response portlet response
     * @return form
     */
    @ModelAttribute("form")
    public SearchForm getForm(PortletRequest request, PortletResponse response) throws PortletException {
        // Portal Controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        return this.service.getForm(portalControllerContext);
    }


    /**
     * Get search options URL.
     *
     * @param request  portlet request
     * @param response portlet response
     * @return URL
     */
    @ModelAttribute("optionsUrl")
    public String getOptionsUrl(PortletRequest request, PortletResponse response) throws PortletException {
        // Portal Controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        return this.service.getOptionsUrl(portalControllerContext);
    }

}
