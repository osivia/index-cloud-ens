package fr.index.cloud.ens.selectors.filter.portlet.controller;

import fr.index.cloud.ens.selectors.filter.portlet.model.FileBrowserFilterForm;
import fr.index.cloud.ens.selectors.filter.portlet.service.FileBrowserFilterService;
import net.sf.json.JSONArray;
import org.osivia.portal.api.context.PortalControllerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.bind.PortletRequestDataBinder;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;

import javax.portlet.*;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * File browser filter portlet controller.
 *
 * @author Cédric Krommenhoek
 */
@Controller
@RequestMapping("VIEW")
public class FileBrowserFilterController {

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
    public FileBrowserFilterController() {
        super();
    }


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
     * Select action mapping.
     *
     * @param request  action request
     * @param response action response
     * @param form     file browser filter form model attribute
     */
    @ActionMapping("select")
    public void select(ActionRequest request, ActionResponse response, @ModelAttribute("form") FileBrowserFilterForm form) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        this.service.select(portalControllerContext, form);
    }


    /**
     * Load select2 vocabulary resource mapping.
     *
     * @param request  resource request
     * @param response resource response
     * @param filter   select2 filter request parameter
     */
    @ResourceMapping("load")
    public void loadVocabulary(ResourceRequest request, ResourceResponse response, @RequestParam(name = "filter", required = false) String filter) throws PortletException, IOException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(portletContext, request, response);

        // Select2 results
        JSONArray results = this.service.loadVocabulary(portalControllerContext, filter);

        // Content type
        response.setContentType("application/json");

        // Content
        PrintWriter printWriter = new PrintWriter(response.getPortletOutputStream());
        printWriter.write(results.toString());
        printWriter.close();
    }


    /**
     * Get file browser filter form model attribute.
     *
     * @param request  portlet request
     * @param response portlet response
     * @return form
     */
    @ModelAttribute("form")
    public FileBrowserFilterForm getForm(PortletRequest request, PortletResponse response) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        return this.service.getForm(portalControllerContext);
    }


    /**
     * File browser filter form init binder.
     *
     * @param binder data binder
     */
    @InitBinder("form")
    public void formInitBinder(PortletRequestDataBinder binder) {
        binder.setDisallowedFields("selectionLabel", "windowProperties");
    }

}
