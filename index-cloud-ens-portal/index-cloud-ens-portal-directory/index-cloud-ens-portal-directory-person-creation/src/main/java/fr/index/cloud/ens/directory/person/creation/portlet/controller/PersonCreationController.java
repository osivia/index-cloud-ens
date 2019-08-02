/**
 * 
 */
package fr.index.cloud.ens.directory.person.creation.portlet.controller;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;
import org.dom4j.io.HTMLWriter;
import org.osivia.portal.api.Constants;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.directory.v2.model.Person;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.locator.Locator;
import org.osivia.portal.api.tokens.ITokenService;
import org.osivia.portal.api.windows.PortalWindow;
import org.osivia.portal.api.windows.WindowFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.portlet.bind.PortletRequestDataBinder;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;

import fr.index.cloud.ens.directory.person.creation.portlet.controller.PersonCreationForm.CreationStep;
import fr.index.cloud.ens.directory.person.creation.portlet.service.PersonCreationService;
import fr.index.cloud.ens.directory.person.renew.portlet.controller.RenewPasswordForm.RenewPasswordStep;
import fr.index.cloud.ens.directory.person.renew.portlet.service.RenewPasswordService;
import fr.toutatice.portail.cms.nuxeo.api.CMSPortlet;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterException;

/**
 * @author Loïc Billon
 *
 */
@Controller
@RequestMapping(value = "VIEW")
@SessionAttributes("form")
public class PersonCreationController extends CMSPortlet {

    /**
     * Search view window property.
     */
    public static String VIEW_WINDOW_PROPERTY = "creation.person.step";
	
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
    private PersonCreationService service;
	
	@RenderMapping
	public String view(RenderRequest request, RenderResponse response) {
		
		// Get logger person
        Person person = (Person) request.getAttribute(Constants.ATTR_LOGGED_PERSON_2);
        if(person != null) {
        	return "view-logged";
        }
        else {
        	
        	CreationStep currentStep = getCurrentStep(request, response);
			return "view-" + currentStep.name().toLowerCase();
        }
		
	}
	
	@ExceptionHandler(PersonCreationInvalidTokenException.class)
	public String viewError() {
		return "view-invalid-token";
	}
	
	/**
	 * @return
	 */
	private CreationStep getCurrentStep(PortletRequest request, PortletResponse response) {
		
        // Window
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);
        PortalWindow window = WindowFactory.getWindow(portalControllerContext.getRequest());
        
        String view = request.getParameter(VIEW_WINDOW_PROPERTY);
        if(StringUtils.isEmpty(view)) {
        	view = window.getProperty(VIEW_WINDOW_PROPERTY);
        }
        CreationStep step;
        if(view != null) {
        	step = CreationStep.valueOf(view);
        }
        else {
        	step = PersonCreationForm.DEFAULT;
        }
    	
        return step;

	}

	@ModelAttribute("form")
	public PersonCreationForm getForm(PortletRequest request, PortletResponse response) throws PersonCreationInvalidTokenException {
		
		PortalControllerContext portalControllerContext = new PortalControllerContext(getPortletContext(), request, response);

		// Search form
		PersonCreationForm form = this.applicationContext.getBean(PersonCreationForm.class);
		
		CreationStep currentStep = getCurrentStep(request, response);
		
		if(currentStep != CreationStep.CONFIRM) {
		
			
			HttpServletRequest servletRequest = portalControllerContext.getHttpServletRequest();
			if(servletRequest != null) {
				String token = servletRequest.getParameter("token");
				
				ITokenService tokenService = Locator.findMBean(ITokenService.class, ITokenService.MBEAN_NAME);
				Map<String, String> validateToken = tokenService.validateToken(token, false);
				
				if(validateToken != null) {
					form.setFirstname(validateToken.get("firstname"));
					form.setLastname(validateToken.get("lastname"));
					form.setMail(validateToken.get("mail"));
				}
				else {
					throw new PersonCreationInvalidTokenException();
	
				}
				
				
			}
		}
		else {
			
			PortalWindow window = WindowFactory.getWindow(portalControllerContext.getRequest());	
			service.proceedRegistration(portalControllerContext, window.getProperty("uid")); 
		}

		return form;
	}
	
	@ActionMapping(name = "submitForm")
	public void submitForm(ActionRequest request, ActionResponse response, @Validated @ModelAttribute("form") PersonCreationForm form, BindingResult result,
			SessionStatus session) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        if (result.hasErrors()) {
        	copyRenderParameters(request, response);
        } else {
        	this.service.proceedInit(portalControllerContext, form);

        	response.setRenderParameter(VIEW_WINDOW_PROPERTY, RenewPasswordStep.SEND.name());
        	
        }
	}

	private void copyRenderParameters(ActionRequest request, ActionResponse response) {
		// Copy render parameters
		Map<String, String[]> parameters = request.getPrivateParameterMap();
		if (MapUtils.isNotEmpty(parameters)) {
		    for (Entry<String, String[]> entry : parameters.entrySet()) {
		        response.setRenderParameter(entry.getKey(), entry.getValue());
		    }
		}
	}
	

    /**
     * Password rules information resource mapping.
     *
     * @param request  resource request
     * @param response resource response
     * @param password password
     */
    @ResourceMapping("password-information")
    public void passwordInformation(ResourceRequest request, ResourceResponse response, @RequestParam(name = "newpassword", required = false) String newpassword) throws PortletException, IOException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        // Password rules information
        Element information = this.service.getPasswordRulesInformation(portalControllerContext, newpassword);

        // Content type
        response.setContentType("text/html");

        // Content
        HTMLWriter htmlWriter = new HTMLWriter(response.getPortletOutputStream());
        htmlWriter.write(information);
        htmlWriter.close();
    }
	
	
    /**
     * Person edition form init binder.
     *
     * @param binder web data binder
     */
    @InitBinder("form")
    public void initBinder(PortletRequest request, PortletResponse response, PortletRequestDataBinder binder) {

    	CreationStep step = getCurrentStep(request, response);

    	if(step.equals(CreationStep.FORM)) {
    		PersonCreationFormValidator bean = applicationContext.getBean(PersonCreationFormValidator.class);
    		binder.addValidators(bean);
    	}

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
