/**
 * 
 */
package fr.index.cloud.ens.dashboard;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;

import org.osivia.portal.api.context.PortalControllerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.context.PortletConfigAware;
import org.springframework.web.portlet.context.PortletContextAware;

/**
 * @author JS Steux
 *
 */
@Controller
@RequestMapping(value = "VIEW")
public class DashboardPortletController implements PortletConfigAware, PortletContextAware{

    
    /** Portlet service. */
    @Autowired
    private DashboardService service;
    
    /** Portlet config. */
    private PortletConfig portletConfig;
    /** Portlet context. */
    private PortletContext portletContext;
    
	@RenderMapping
	public String view() {
		return "view";
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
     * Delete selected items action mapping.
     * 
     * @param request action request
     * @param response action response
     * @param form trash form model attribute
     * @throws PortletException
     */
    @ActionMapping(name = "update", params = "delete")
    public void delete(ActionRequest request, ActionResponse response, @ModelAttribute("dashboardForm") DashboardForm form) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        this.service.delete(portalControllerContext, form);
    }
    

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPortletConfig(PortletConfig portletConfig) {
        this.portletConfig = portletConfig;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setPortletContext(PortletContext portletContext) {
        this.portletContext = portletContext;
    }

}
