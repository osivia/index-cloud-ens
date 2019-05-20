/**
 * 
 */
package fr.index.cloud.ens.dashboard;

import javax.portlet.*;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;
import org.dom4j.io.HTMLWriter;
import org.osivia.portal.api.context.PortalControllerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;
import org.springframework.web.portlet.context.PortletConfigAware;
import org.springframework.web.portlet.context.PortletContextAware;

import java.io.IOException;
import java.util.Arrays;

/**
 * @author JS Steux
 *
 */
@Controller
@RequestMapping(value = "VIEW")
public class DashboardPortletController {

    /** Portlet service. */
    @Autowired
    private DashboardService service;
    
    /** Portlet context. */
    @Autowired
    private PortletContext portletContext;


	@RenderMapping
	public String view() {
		return "view";
	}


    /**
     * Sort action mapping.
     *
     * @param request  action request
     * @param response action response
     * @param sortId   sort property identifier request parameter
     * @param alt      alternative sort indicator request parameter
     * @param form     dashboard form model attribute
     */
    @ActionMapping("sort")
    public void sort(ActionRequest request, ActionResponse response, @RequestParam("sortId") String sortId, @RequestParam("alt") String alt,
                     @ModelAttribute("dashboardForm") DashboardForm form) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        this.service.sort(portalControllerContext, form, DashboardSort.fromId(sortId), BooleanUtils.toBoolean(alt));
    }


    /**
     * Delete selected items action mapping.
     *
     * @param request  action request
     * @param response action response
     * @param identifiers selection identifiers request parameter
     * @param form     dashboard form model attribute
     */
    @ActionMapping("delete")
    public void delete(ActionRequest request, ActionResponse response, @RequestParam("identifiers") String[] identifiers, @ModelAttribute("dashboardForm") DashboardForm form) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        this.service.delete(portalControllerContext, form, identifiers);
    }


	 /**
     * Get dashboard form model attribute.
     * 
     * @param request portlet request
     * @param response portlet response
     * @return trash form
     * @throws PortletException
     */
    @ModelAttribute("dashboardForm")
    public DashboardForm getDashboardForm(PortletRequest request, PortletResponse response) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(portletContext, request, response);

        return this.service.getDashboardForm(portalControllerContext);
    }


    /**
     * Get toolbar resource mapping.
     *
     * @param request  resource request
     * @param response resource response
     * @param indexes  selected row indexes
     */
    @ResourceMapping("toolbar")
    public void getToolbar(ResourceRequest request, ResourceResponse response, @RequestParam("indexes") String indexes) throws PortletException, IOException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        // Toolbar
        Element toolbar = this.service.getToolbar(portalControllerContext, Arrays.asList(StringUtils.split(StringUtils.trimToEmpty(indexes), ",")));

        // Content type
        response.setContentType("text/html");

        // Content
        HTMLWriter htmlWriter = new HTMLWriter(response.getPortletOutputStream());
        htmlWriter.write(toolbar);
        htmlWriter.close();
    }

}
