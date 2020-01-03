package fr.index.cloud.ens.portal.discussion.portlet.controller;

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

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.directory.v2.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import fr.index.cloud.ens.portal.discussion.portlet.model.DetailForm;
import fr.index.cloud.ens.portal.discussion.portlet.model.DiscussionsForm;
import fr.index.cloud.ens.portal.discussion.portlet.service.DiscussionService;
import fr.toutatice.portail.cms.nuxeo.api.CMSPortlet;

/**
 * View trash portlet controller.
 *
 * @author Jean-Sébastien Steux
 * @see CMSPortlet
 */
@Controller
@RequestMapping(path="VIEW", params = "view=detail")
public class ViewDetailController extends CMSPortlet {


    /**
     * Portlet config.
     */
    @Autowired
    private PortletConfig portletConfig;

    /**
     * Portlet context.
     */
    @Autowired
    private PortletContext portletContext;

    /**
     * Portlet service.
     */
    @Autowired
    private DiscussionService service;

    /**
     * Person service.
     */
    @Autowired
    public PersonService personService;

    /**
     * Constructor.
     */
    public ViewDetailController() {
        super();
    }


    /**
     * Post-construct.
     */
    @PostConstruct
    public void postConstruct() throws PortletException {
        super.init(this.portletConfig);
    }


    /**
     * View render mapping.
     *
     * @param request render request
     * @param response render response
     * @return view path
     */
    @RenderMapping
    public String view(RenderRequest request, RenderResponse response) throws PortletException {
        return "detail";
    }


    /**
     * Get detail form model attribute.
     *
     * @param request portlet request
     * @param response portlet response
     * @return trash form
     */
    @ModelAttribute("detailForm")
    public DetailForm getDiscussionsForm(PortletRequest request, PortletResponse response, @RequestParam(name = "id", required = false) String id, @RequestParam(name = "participant", required = false) String participant, @RequestParam(name = "anchor", required = false) String anchor) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(portletContext, request, response);

        return this.service.getDetailForm(portalControllerContext, id, participant, anchor);
    }


    /**
     * Submit creation action mapping.
     * 
     * @param request action request
     * @param response action response
     * @param form form model attribute
     * @param result binding result
     * @param sessionStatus session status
     * @throws PortletException
     */
    @ActionMapping("addMessage")
    public void addMessage(ActionRequest request, ActionResponse response, @Validated @ModelAttribute("detailForm") DetailForm form, BindingResult result,
            SessionStatus sessionStatus) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);
        
        this.service.addMessage(portalControllerContext, form);

        if (result.hasErrors()) {
            response.setRenderParameter("view", "detail");
            response.setRenderParameter("id", form.getId());
        } else {
            response.setRenderParameter("view", "detail");
            response.setRenderParameter("id", form.getId());
        }
    }

    
    /**
     * Delete current message
     *
     * @param request action request
     * @param response action response
     * @param form detail form model attribute
     */
    @ActionMapping("deleteMessage")
    public void deleteMessage(ActionRequest request, ActionResponse response, @RequestParam("messageId") String messageId,
            @ModelAttribute("detailForm") DetailForm form, BindingResult result) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        this.service.deleteMessage(portalControllerContext, form, messageId);
        
        if (result.hasErrors()) {
            response.setRenderParameter("view", "detail");
            response.setRenderParameter("id", form.getId());
        } else {

            response.setRenderParameter("view", "detail");
            response.setRenderParameter("id", form.getId());
        }
        
    }

    

}
