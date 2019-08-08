	/**
 * 
 */
package fr.index.cloud.ens.directory.person.export.portlet.controller;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.RenderRequest;

import org.osivia.directory.v2.service.PersonUpdateService;
import org.osivia.portal.api.Constants;
import org.osivia.portal.api.cache.services.CacheInfo;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.directory.v2.model.Person;
import org.osivia.portal.core.cms.CMSException;
import org.osivia.portal.core.cms.CMSItem;
import org.osivia.portal.core.cms.CMSServiceCtx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import fr.toutatice.portail.cms.nuxeo.api.CMSPortlet;
import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.services.NuxeoCommandContext;

/**
 * @author Loïc Billon
 *
 */
@Controller
@RequestMapping(value = "VIEW")
@SessionAttributes("form")
public class PersonExportPortletController extends CMSPortlet {

    /** Application context. */
    @Autowired
    private ApplicationContext applicationContext;
	
    /** Portlet context. */
    @Autowired
    private PortletContext portletContext;    
    
    /** Portlet config. */
    @Autowired
    private PortletConfig portletConfig;
    
    @Autowired
    private PersonUpdateService personService;
    
    @Autowired
    private PersonExportService service;
    
	
	@RenderMapping
	public String view(RenderRequest request) {
		
		return "view";
	}

	@ActionMapping(name = "exportData")
	public void exportData(ActionRequest request, ActionResponse response) throws CMSException {
		
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);
        
        // Get logger person
        Person person = (Person) request.getAttribute(Constants.ATTR_LOGGED_PERSON_2);
        
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(request, response, this.getPortletContext());
        CMSServiceCtx cmsCtx = nuxeoController.getCMSCtx();
        CMSItem userWorkspace = getCMSService().getUserWorkspace(cmsCtx);
        String navigationPath = userWorkspace.getNavigationPath();
        
        
        service.export(portalControllerContext, navigationPath);
	}
    
    /**
     * Post-construct.
     *
     * @throws PortletException
     */
    @PostConstruct
    public void postConstruct() throws PortletException {
        super.init(this.portletConfig);
    }
    
    
}
