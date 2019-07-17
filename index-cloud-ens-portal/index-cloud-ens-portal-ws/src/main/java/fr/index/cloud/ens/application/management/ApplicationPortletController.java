/**
 * 
 */
package fr.index.cloud.ens.application.management;

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
public class ApplicationPortletController {

    /** Portlet service. */
    @Autowired
    private ApplicationService service;
    
    /** Portlet context. */
    @Autowired
    private PortletContext portletContext;


	@RenderMapping
	public String view() {
		return "view";
	}



    @ActionMapping("sort")
    public void sort(ActionRequest request, ActionResponse response, @RequestParam("sortId") String sortId, @RequestParam("alt") String alt,
                     @ModelAttribute("applicationForm") ApplicationForm form) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

    }


    @ModelAttribute("applicationForm")
    public ApplicationForm getApplicationForm(PortletRequest request, PortletResponse response) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(portletContext, request, response);

        return this.service.getApplicationForm(portalControllerContext);
    }



}
