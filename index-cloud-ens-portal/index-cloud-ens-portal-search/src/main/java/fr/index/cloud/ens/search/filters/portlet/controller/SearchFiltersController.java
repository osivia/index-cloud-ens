package fr.index.cloud.ens.search.filters.portlet.controller;

import fr.index.cloud.ens.search.filters.portlet.model.*;
import fr.index.cloud.ens.search.filters.portlet.model.converter.SearchFiltersDatePropertyEditor;
import fr.index.cloud.ens.search.filters.portlet.service.SearchFiltersService;
import net.sf.json.JSONArray;
import org.osivia.portal.api.context.PortalControllerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.portlet.bind.PortletRequestDataBinder;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;

import javax.portlet.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Search filters portlet controller.
 *
 * @author Cédric Krommenhoek
 */
@Controller
@RequestMapping("VIEW")
@SessionAttributes("form")
public class SearchFiltersController {

    /**
     * Portlet context.
     */
    @Autowired
    private PortletContext portletContext;

    /**
     * Portlet service.
     */
    @Autowired
    private SearchFiltersService service;

    /**
     * Search filters date property editor.
     */
    @Autowired
    private SearchFiltersDatePropertyEditor datePropertyEditor;


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
     * @param request       action request
     * @param response      action response
     * @param form          search filters form model attribute
     * @param sessionStatus session status
     */
    @ActionMapping(name = "submit", params = "search")
    public void search(ActionRequest request, ActionResponse response, @ModelAttribute("form") SearchFiltersForm form, SessionStatus sessionStatus) throws PortletException, IOException {
        // Portal Controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        // Complete session
        sessionStatus.setComplete();

        // Search redirection URL
        String url = this.service.getSearchRedirectionUrl(portalControllerContext, form);

        response.sendRedirect(url);
    }


    /**
     * Update action mapping.
     *
     * @param form search filters form model attribute
     */
    @ActionMapping(name = "submit", params = "update")
    public void update(@ModelAttribute("form") SearchFiltersForm form) {
        // Do nothing: model has been updated
    }


    /**
     * Update location action mapping.
     *
     * @param request  action request
     * @param response action response
     * @param form     search filters form model attribute
     */
    @ActionMapping(name = "submit", params = "update-location")
    public void updateLocation(ActionRequest request, ActionResponse response, @ModelAttribute("form") SearchFiltersForm form) throws PortletException {
        // Portal Controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        this.service.updateLocation(portalControllerContext, form);
    }


    /**
     * Save search action mapping.
     *
     * @param request  action request
     * @param response action response
     * @param form     search filters form model attribute
     */
    @ActionMapping(name = "submit", params = "save-search-popover-callback")
    public void saveSearch(ActionRequest request, ActionResponse response, @ModelAttribute("form") SearchFiltersForm form) throws PortletException, IOException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(portletContext, request, response);

        // Redirection
        String url = this.service.saveSearch(portalControllerContext, form);
        response.sendRedirect(url);
    }


    /**
     * Load levels select2 vocabulary resource mapping.
     *
     * @param request        resource request
     * @param response       resource response
     * @param vocabularyName vocabulary name request parameter
     * @param filter         select2 filter request parameter
     */
    @ResourceMapping("load-vocabulary")
    public void loadLevels(ResourceRequest request, ResourceResponse response, @RequestParam("vocabulary") String vocabularyName, @RequestParam(name = "filter", required = false) String filter) throws PortletException, IOException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(portletContext, request, response);

        // Vocabulary
        SearchFiltersVocabulary vocabulary = SearchFiltersVocabulary.fromVocabularyName(vocabularyName);
        // Select2 results
        JSONArray results = this.service.loadVocabulary(portalControllerContext, vocabulary, filter);

        // Content type
        response.setContentType("application/json");

        // Content
        PrintWriter printWriter = new PrintWriter(response.getPortletOutputStream());
        printWriter.write(results.toString());
        printWriter.close();
    }


    /**
     * Save search popover resource mapping.
     *
     * @param request  resource request
     * @param response resource response
     */
    @ResourceMapping("save-search-popover")
    public void saveSearchPopover(ResourceRequest request, ResourceResponse response) throws PortletException, IOException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(portletContext, request, response);

        // View path
        String path = this.service.resolveViewPath(portalControllerContext, "save-search-popover");

        // Portlet request dispatcher
        PortletRequestDispatcher dispatcher = this.portletContext.getRequestDispatcher(path);
        dispatcher.include(request, response);
    }


    /**
     * Get search filters form model attribute.
     *
     * @param request  portlet request
     * @param response portlet response
     * @return form
     */
    @ModelAttribute("form")
    public SearchFiltersForm getForm(PortletRequest request, PortletResponse response) throws PortletException {
        // Portal Controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        return this.service.getForm(portalControllerContext);
    }


    /**
     * Search filters form init binder.
     *
     * @param binder portlet request data binder
     */
    @InitBinder("form")
    public void formInitBinder(PortletRequestDataBinder binder) {
        binder.registerCustomEditor(Date.class, this.datePropertyEditor);
    }


    /**
     * Get location URL model attribute.
     *
     * @param request  portlet request
     * @param response portlet response
     * @return URL
     */
    @ModelAttribute("locationUrl")
    public String getLocationUrl(PortletRequest request, PortletResponse response) throws PortletException {
        // Portal Controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        return this.service.getLocationUrl(portalControllerContext);
    }


    /**
     * Get size ranges model attribute.
     *
     * @return size ranges
     */
    @ModelAttribute("sizeRanges")
    public List<SearchFiltersSizeRange> getSizeRanges() {
        return Arrays.asList(SearchFiltersSizeRange.values());
    }


    /**
     * Get date ranges model attribute.
     *
     * @return date ranges
     */
    @ModelAttribute("dateRanges")
    public List<SearchFiltersDateRange> getDateRanges() {
        return Arrays.asList(SearchFiltersDateRange.values());
    }


    /**
     * Get size units model attribute.
     *
     * @return size units
     */
    @ModelAttribute("sizeUnits")
    public List<SearchFiltersSizeUnit> getSizeUnits() {
        return Arrays.asList(SearchFiltersSizeUnit.values());
    }

}
