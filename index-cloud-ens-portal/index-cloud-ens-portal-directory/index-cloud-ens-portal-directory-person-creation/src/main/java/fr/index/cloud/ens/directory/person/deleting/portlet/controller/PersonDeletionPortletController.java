	/**
 * 
 */
package fr.index.cloud.ens.directory.person.deleting.portlet.controller;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.RenderRequest;

import org.osivia.directory.v2.service.PersonUpdateService;
import org.osivia.portal.api.Constants;
import org.osivia.portal.api.cache.services.CacheInfo;
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
public class PersonDeletionPortletController extends CMSPortlet {

    /** Application context. */
    @Autowired
    private ApplicationContext applicationContext;
	
    /** Portlet config. */
    @Autowired
    private PortletConfig portletConfig;
    
    @Autowired
    private PersonUpdateService personService;
    
	
	@RenderMapping
	public String view(RenderRequest request) {
		
		return "view";
	}

	@ModelAttribute("form")
	public PersonDeletionForm getForm(PortletRequest request) {
		PersonDeletionForm form = applicationContext.getBean(PersonDeletionForm.class);
        Person person = (Person) request.getAttribute(Constants.ATTR_LOGGED_PERSON_2);

        form.setDisplayName(person.getDisplayName());
		return form;
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
    
    /**
     * 
     *
     * @param request  resource request
     * @param response resource response
     * @param password password
     * @throws CMSException 
     */
    @ActionMapping("deleteAccount")
    public void deleteAccount(ActionRequest request, ActionResponse response) throws PortletException, IOException, CMSException {

        // Get logger person
        Person person = (Person) request.getAttribute(Constants.ATTR_LOGGED_PERSON_2);
        
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(request, response, this.getPortletContext());
        CMSServiceCtx cmsCtx = nuxeoController.getCMSCtx();
        CMSItem userWorkspace = getCMSService().getUserWorkspace(cmsCtx);
        String navigationPath = userWorkspace.getNavigationPath();
        
        nuxeoController.setAuthType(NuxeoCommandContext.AUTH_TYPE_SUPERUSER);
        nuxeoController.setCacheType(CacheInfo.CACHE_SCOPE_NONE);
        nuxeoController.setAsynchronousCommand(true);


        // Nuxeo command
        INuxeoCommand command = this.applicationContext.getBean(DeleteWorkspaceCommand.class, navigationPath);
        nuxeoController.executeNuxeoCommand(command);
        
        this.personService.delete(person);

    }
	
}
