package org.osivia.demo.initializer.service;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.naming.Name;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.demo.initializer.service.commands.CreateExtranetCommand;
import org.osivia.demo.initializer.service.commands.CreateProcedureContainerCommand;
import org.osivia.demo.initializer.service.commands.CreateProcedureModelsCommand;
import org.osivia.demo.initializer.service.commands.LoadRecordsCommand;
import org.osivia.directory.v2.model.PortalGroup;
import org.osivia.directory.v2.service.PersonUpdateService;
import org.osivia.directory.v2.service.PortalGroupService;
import org.osivia.portal.api.cache.services.CacheInfo;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.directory.v2.DirServiceFactory;
import org.osivia.portal.api.directory.v2.model.Person;
import org.osivia.portal.api.directory.v2.service.GroupService;
import org.osivia.portal.api.directory.v2.service.PersonService;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.locator.Locator;
import org.osivia.portal.core.deploiement.IParametresPortailDeploymentManager;
import org.osivia.services.workspace.portlet.model.WorkspaceCreationForm;
import org.osivia.services.workspace.portlet.service.WorkspaceCreationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterContext;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterException;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterExecutor;
import fr.toutatice.portail.cms.nuxeo.api.services.NuxeoCommandContext;
import fr.toutatice.portail.cms.nuxeo.api.workspace.WorkspaceType;

/**
 * 
 * @author Loïc Billon
 *
 */
@Service
public class InitalizerServiceImpl implements InitializerService {

	@Autowired
	private WorkspaceCreationService workspaceCreationService;
	
	
	@Autowired
	private PersonUpdateService personService;
	
    @Autowired
    private PortalGroupService service;
    
    /**
     * Get portalGroup service.
     * 
     * @return portalGroup service
     */
    @Bean
    public PortalGroupService getPortalGroupService() {
        return DirServiceFactory.getService(PortalGroupService.class);
    }
    

	
	
	public void initialize(PortalControllerContext portalControllerContext) throws PortletException {
		
		// Nuxeo controller
        NuxeoController nuxeoController = this.getNuxeoController(portalControllerContext);
        nuxeoController.setAuthType(NuxeoCommandContext.AUTH_TYPE_SUPERUSER);
        nuxeoController.setCacheType(CacheInfo.CACHE_SCOPE_PORTLET_CONTEXT);
        nuxeoController.setAsynchronousCommand(false);
        
        /*
        String cn = "sav";
		PortalGroup portalGroup = this.service.create(cn);
		portalGroup.setDisplayName("Service après-vente");
		portalGroup.setDescription(StringUtils.trimToNull("Tous les techniciens qui interviennent en support"));


        
        // Person
        Person person = personService.getEmptyPerson();
        person.setUid("technicien");
        person.setCn("technicien");
        person.setSn("technicien");
        person.setGivenName("technicien");
        person.setDisplayName("technicien osivia");
        person.setTitle("M.");
        
        personService.create(person);
   		personService.updatePassword(person, "osivia");
		
		
		// All mails switched to demo@osivia.com
        Person searchPerson = personService.getEmptyPerson(); 		
        searchPerson.setUid("*");
        List<Person> persons = personService.findByCriteria(searchPerson);	
        for (Person aPerson:persons) {
            aPerson.setMail("demo+"+aPerson.getUid()+"@osivia.com");
            personService.update(aPerson);
        }
   		
		
		
   		// Add user to group
   		List<Name> listName = new ArrayList<>();
   		listName.add(person.getDn());
		portalGroup.setMembers(listName);
        this.service.update(portalGroup);		
   		*/
   		
   		
   		
        // Containers
        Document modelsContainer = (Document) nuxeoController.executeNuxeoCommand(new CreateProcedureContainerCommand());
        
        // Models
        nuxeoController.executeNuxeoCommand(new CreateProcedureModelsCommand(modelsContainer));
        
        // Extranet
        nuxeoController.executeNuxeoCommand(new CreateExtranetCommand());
        
        // Create first workspace
        String workspaceId = "Espace de test";
    	WorkspaceCreationForm form = new WorkspaceCreationForm();
    	form.setTitle(workspaceId);
    	form.setDescription(workspaceId);
    	form.setType( WorkspaceType.PRIVATE);    	
    	
    	Person owner = personService.getPerson("admin");

    	
    	form.setOwner(owner.getUid());
    	
    	workspaceCreationService.create(portalControllerContext, form);
    	
    	// Load some records
        nuxeoController.executeNuxeoCommand(new LoadRecordsCommand());
        


   


	}
	
	/**
     * Get Nuxeo controller
     *
     * @param portalControllerContext portal controller context
     * @return Nuxeo controller
     */
    private NuxeoController getNuxeoController(PortalControllerContext portalControllerContext) {
        PortletRequest request = portalControllerContext.getRequest();
        PortletResponse response = portalControllerContext.getResponse();
        PortletContext portletContext = portalControllerContext.getPortletCtx();
        return new NuxeoController(request, response, portletContext);
    }

}
