package fr.index.cloud.ens.levels.portlet.controller;

import fr.index.cloud.ens.levels.portlet.model.HighestLevels;
import fr.index.cloud.ens.levels.portlet.service.HighestLevelsService;
import org.apache.commons.collections.CollectionUtils;
import org.osivia.portal.api.context.PortalControllerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import javax.portlet.*;
import java.io.IOException;

/**
 * Highest levels portlet controller.
 *
 * @author Cédric Krommenhoek
 */
@Controller
@RequestMapping("VIEW")
public class HighestLevelsController {

    /**
     * Portlet context.
     */
    @Autowired
    private PortletContext portletContext;
    /**
     * Portlet service.
     */
    @Autowired
    private HighestLevelsService service;


    /**
     * Constructor.
     */
    public HighestLevelsController() {
        super();
    }


    /**
     * View render mapping.
     *
     * @param request       render request
     * @param response      render response
     * @param highestLevels highest levels model attribute
     * @return view path
     */
    @RenderMapping
    public String view(RenderRequest request, RenderResponse response, @ModelAttribute("highestLevels") HighestLevels highestLevels) {
        if (CollectionUtils.isEmpty(highestLevels.getItems())) {
            request.setAttribute("osivia.emptyResponse", "1");
        }

        return "view";
    }


    /**
     * Search level action mapping.
     *
     * @param request  action request
     * @param response action response
     * @param id       level identifier request parameter
     */
    @ActionMapping("search")
    public void search(ActionRequest request, ActionResponse response, @RequestParam("id") String id) throws PortletException, IOException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        // Level search URL
        String url = this.service.getSearchUrl(portalControllerContext, id);

        // Redirection
        response.sendRedirect(url);
    }


    /**
     * Get highest levels model attribute.
     *
     * @param request  portlet request
     * @param response portlet response
     * @return highest levels
     */
    @ModelAttribute("highestLevels")
    public HighestLevels getHighestLevels(PortletRequest request, PortletResponse response) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        return this.service.getHighestLevels(portalControllerContext);
    }

}
