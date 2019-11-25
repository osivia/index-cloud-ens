package fr.index.cloud.ens.portal.discussion.portlet.controller;

import fr.index.cloud.ens.portal.discussion.portlet.model.DiscussionCreation;
import fr.index.cloud.ens.portal.discussion.portlet.model.DiscussionsForm;
import fr.index.cloud.ens.portal.discussion.portlet.model.DiscussionsFormSort;
import fr.index.cloud.ens.portal.discussion.portlet.repository.DiscussionRepository;
import fr.index.cloud.ens.portal.discussion.portlet.service.DiscussionService;
import fr.toutatice.portail.cms.nuxeo.api.CMSPortlet;
import fr.toutatice.portail.cms.nuxeo.api.services.NuxeoServiceFactory;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;
import org.dom4j.io.HTMLWriter;
import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.directory.v2.model.Person;
import org.osivia.portal.api.directory.v2.service.PersonService;
import org.osivia.portal.api.urls.Link;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;

import javax.annotation.PostConstruct;
import javax.naming.Name;
import javax.portlet.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * View trash portlet controller.
 *
 * @author Jean-Sébastien Steux
 * @see CMSPortlet
 */
@Controller
@RequestMapping("VIEW")
public class ViewDiscussionsController extends CMSPortlet {


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
    public ViewDiscussionsController() {
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
        return "view";
    }


    /**
     * Sort action mapping.
     *
     * @param request action request
     * @param response action response
     * @param sortId sort property identifier request parameter
     * @param alt alternative sort indicator request parameter
     * @param form trash form model attribute
     */
    @ActionMapping("sort")
    public void sort(ActionRequest request, ActionResponse response, @RequestParam("sortId") String sortId, @RequestParam("alt") String alt,
            @ModelAttribute("trashForm") DiscussionsForm form) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        this.service.sort(portalControllerContext, form, DiscussionsFormSort.fromId(sortId), BooleanUtils.toBoolean(alt));
    }


    /**
     * Empty trash action mapping.
     *
     * @param request action request
     * @param response action response
     * @param form trash form model attribute
     */
    @ActionMapping("delete-all")
    public void emptyTrash(ActionRequest request, ActionResponse response, @ModelAttribute("trashForm") DiscussionsForm form) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        this.service.emptyTrash(portalControllerContext, form);
    }


    /**
     * Delete selected items action mapping.
     *
     * @param request action request
     * @param response action response
     * @param identifiers selection identifiers request parameter
     * @param form trash form model attribute
     */
    @ActionMapping("delete")
    public void delete(ActionRequest request, ActionResponse response, @RequestParam("identifiers") String[] identifiers,
            @ModelAttribute("trashForm") DiscussionsForm form) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        this.service.delete(portalControllerContext, form, identifiers);
    }


    /**
     * Send a mail to all users (test)
     *
     * @param request action request
     * @param response action response
     * @param identifiers selection identifiers request parameter
     * @param form trash form model attribute
     */
    @ActionMapping("new-discussion-users")
    public void newDiscussionPersonsTest(ActionRequest request, ActionResponse response) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);


        Map<String, String> variables = new HashMap<>();

        // Add all persons
        Person criteria = this.personService.getEmptyPerson();
        List<Person> persons = personService.findByCriteria(criteria);

        String target = "";
        for (Person person : persons) {
            target += "/" + person.getUid();
        }

        variables.put("osivia.discussion.target.type", "USERS");
        variables.put("osivia.discussion.target.id", target);
        variables.put("message", "hello !"+ target);
        try {
            variables = NuxeoServiceFactory.getFormsService().start(portalControllerContext, DiscussionRepository.MODEL_ID, variables);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * Send a mail to a group (test)
     *
     * @param request action request
     * @param response action response
     * @param identifiers selection identifiers request parameter
     * @param form trash form model attribute
     */
    @ActionMapping("new-discussion-publication")
    public void newDiscussionPublicationTest(ActionRequest request, ActionResponse response) throws PortletException {
        // Portal controller context
//        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);
//
//
//        Map<String, String> variables = new HashMap<>();
//        variables.put("message", "hello PUB/ID1 !");
//        variables.put("osivia.discussion.target.type", "PUB");
//        variables.put("osivia.discussion.target.id", "ID1");
//
//        try {
//            variables = NuxeoServiceFactory.getFormsService().start(portalControllerContext, TrashRepository.MODEL_ID, variables);
//        } catch (Exception e) {
//            System.out.println(e);
//        }
        
        
        DiscussionCreation discussion = new DiscussionCreation();
        discussion.setMessage("discussion LOCAL_COPY/ID1");
        discussion.setType("LOCAL_COPY");
        discussion.setTarget("ID1");
        
        PortalControllerContext portalControllerContext = new PortalControllerContext(portletContext, request, response);
        this.service.createDiscussion(portalControllerContext, discussion);
    }


    /**
     * Get trash form model attribute.
     *
     * @param request portlet request
     * @param response portlet response
     * @return trash form
     */
    @ModelAttribute("discussionsForm")
    public DiscussionsForm getDiscussionsForm(PortletRequest request, PortletResponse response) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(portletContext, request, response);

        return this.service.getDiscussionsForm(portalControllerContext);
    }


    /**
     * Get toolbar resource mapping.
     *
     * @param request resource request
     * @param response resource response
     * @param indexes selected row indexes
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
