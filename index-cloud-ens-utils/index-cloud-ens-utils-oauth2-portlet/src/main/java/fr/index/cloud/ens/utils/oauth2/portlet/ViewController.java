package fr.index.cloud.ens.utils.oauth2.portlet;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

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

import fr.index.cloud.ens.utils.oauth2.redirect.OAuth2PortletRedirectBean;
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
public class ViewController extends CMSPortlet {


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
        
        // For test only
        try {
            SSLUtils.turnOffSslChecking();
        } catch (Exception e) {
            throw new PortletException(e);
        }
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
    @ExceptionHandler(PortletRedirectionException.class)
    public String exceptionHandler(PortletRequest request, PortletResponse response, PortletRedirectionException exc) throws PortletException {

         PortletSession session = request.getPortletSession(true);

        // Prepare redirection attributes
        session.setAttribute("oauth2DirectBean", exc.getRedirectBean(), PortletSession.APPLICATION_SCOPE);

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
    public String getModel(PortletRequest request, PortletResponse response) throws PortletRedirectionException {
        
        // Get response from oauth2 redirect servlet
        OAuth2PortletRedirectBean redirectBean = (OAuth2PortletRedirectBean) request.getPortletSession(true).getAttribute("oauth2DirectBean", PortletSession.APPLICATION_SCOPE);
        if (redirectBean != null && redirectBean.getResponse() != null) {
            // Remove bean
            request.getPortletSession(true).removeAttribute("oauth2DirectBean", PortletSession.APPLICATION_SCOPE);
        } else {
            // throw exception with informations to redirect
            PortalControllerContext portalControllerContext = new PortalControllerContext(portletContext, request, response);
            OAuth2PortletRedirectBean newRedirectBean = new OAuth2PortletRedirectBean( request.getRemoteUser(), portalUrlFactury.getRefreshPageUrl(portalControllerContext), new DriveOperation(restTemplate));
            throw new PortletRedirectionException(newRedirectBean);
        }

        
        return redirectBean.getResponse().toString();
    }


}
