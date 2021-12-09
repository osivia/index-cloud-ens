package fr.index.cloud.ens.utils.oauth2.portlet.controller;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.portlet.PortletConfig;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osivia.portal.api.Constants;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.urls.IPortalUrlFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.resource.UserRedirectRequiredException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.SessionScope;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.context.PortletConfigAware;
import org.springframework.web.portlet.context.PortletContextAware;

import fr.toutatice.portail.cms.nuxeo.api.CMSPortlet;

/**
 * View generator controller.
 *
 * @author Cédric Krommenhoek
 * @see CMSPortlet
 * @see PortletConfigAware
 * @see PortletContextAware
 */
@Controller
@RequestMapping(value = "VIEW")
public class ViewController extends CMSPortlet  {


    @Autowired
    private PortletConfig portletConfig;


    @Autowired
    private PortletContext portletContext;
    
    @Autowired 
    private OAuth2RestOperations restTemplate;
    
    @Autowired
    private IPortalUrlFactory portalUrlFactury;


    /**
     * Constructor.
     */
    public ViewController() {
        super();
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
     * Pre-destroy.
     */
    @PreDestroy
    public void preDestroy() {
        super.destroy();
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
     * Exception handler.
     *
     * @param request portlet request
     * @param response portlet response
     * @return error path
     * @throws PortletException
     */
    @ExceptionHandler(UserRedirectRequiredException.class)
    public String exceptionHandler(PortletRequest request, PortletResponse response) throws PortletException {
        
        PortalControllerContext portalControllerContext = new PortalControllerContext(portletContext, request, response);
        PortletSession session = request.getPortletSession(true);
        
        // Prepare redirection attributes
        session.setAttribute("oauth2User", request.getRemoteUser(), PortletSession.APPLICATION_SCOPE);
        session.setAttribute("oauth2OriginUrl", portalUrlFactury.getRefreshPageUrl(portalControllerContext), PortletSession.APPLICATION_SCOPE);

        return "goto-redirect";
    }






    /**
     * Get portlet configuration model attribute.
     *
     * @param request portlet request
     * @param response portlet response
     * @return configuration
     * @throws PortletException
     */
    @ModelAttribute(value = "model")
    public String getModel(PortletRequest request, PortletResponse response) throws PortletException {
        // Portal controller context

        Object resp = request.getPortletSession(true).getAttribute("oauth2Response",  PortletSession.APPLICATION_SCOPE);
        if( resp != null)   {
            // Remove response
            request.getPortletSession(true).removeAttribute("oauth2Response",  PortletSession.APPLICATION_SCOPE);
        }   else    {
             // HTTP headers
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> httpEntity = new HttpEntity("hello world", httpHeaders);
    
    
            this.restTemplate.exchange("https://cloud-ens.index-education.local/index-cloud-portal-ens-ws/rest/Drive.content", HttpMethod.GET, httpEntity, String.class);
        }
        
        return resp.toString();
    }




}
