package org.osivia.demo.generator.portlet.controller;

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

import org.osivia.demo.generator.portlet.model.CreationForm;
import org.osivia.demo.generator.portlet.model.validator.CreationFormValidator;
import org.osivia.demo.generator.portlet.service.SimpleDocumentCreatorService;
import org.osivia.portal.api.context.PortalControllerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import fr.index.cloud.ens.ws.CloudRestController;
import fr.toutatice.portail.cms.nuxeo.api.CMSPortlet;

/**
 * Portlet controller.
 * 
 * @author Cédric Krommenhoek
 * @see CMSPortlet
 */
@Controller
@RequestMapping("VIEW")
@SessionAttributes("form")
public class SimpleDocumentCreatorController extends CMSPortlet {

    /** Portlet config. */
    @Autowired
    private PortletConfig portletConfig;

    /** Portlet context. */
    @Autowired
    private PortletContext portletContext;

    /** Portlet service. */
    @Autowired
    private SimpleDocumentCreatorService service;

    /** Creation form validator. */
    @Autowired
    private CloudRestController restControler;

    @Autowired
    private CreationFormValidator formValidator;

    
    /**
     * Constructor.
     */
    public SimpleDocumentCreatorController() {
        super();
    }


    /**
     * Portlet initialization.
     *
     * @throws PortletException
     */
    @PostConstruct
    public void postConstruct() throws PortletException {
        super.init(this.portletConfig);
        
        CloudRestController.portletContext = this.portletContext;
    }


    /**
     * View render mapping.
     *
     * @param request render request
     * @param response render response
     * @return view path
     */
    @RenderMapping
    public String view(RenderRequest request, RenderResponse response) {
        return "view";
    }


    /**
     * Save action mapping.
     * 
     * @param request action request
     * @param response action response
     * @param form creation form model attribute
     * @param result binding result
     * @throws PortletException
     */
    @ActionMapping(name = "save", params = "save")
    public void save(ActionRequest request, ActionResponse response, @Validated @ModelAttribute("form") CreationForm form, BindingResult result)
            throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(portletContext, request, response);

        if (!result.hasErrors()) {
            this.service.create(portalControllerContext, form);
        }
    }


    /**
     * Randomize action mapping.
     * 
     * @param request action request
     * @param response action response
     * @param form creation form model attribute
     * @throws PortletException
     */
    @ActionMapping(name = "save", params = "randomize")
    public void randomize(ActionRequest request, ActionResponse response, @ModelAttribute("form") CreationForm form) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(portletContext, request, response);

        this.service.randomize(portalControllerContext, form);
    }


    /**
     * Get creation form model attribute.
     * 
     * @param request portlet request
     * @param response portlet response
     * @return creation form
     * @throws PortletException
     */
    @ModelAttribute("form")
    public CreationForm getForm(PortletRequest request, PortletResponse response) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(portletContext, request, response);

        return this.service.getCreationForm(portalControllerContext);
    }


    /**
     * Creation form init binder.
     *
     * @param binder web data binder
     */
    @InitBinder("form")
    public void formInitBinder(WebDataBinder binder) {
        binder.addValidators(this.formValidator);
    }

}
