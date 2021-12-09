package fr.index.cloud.ens.utils.oauth2.redirect;


import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.portlet.PortletSession;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.index.cloud.ens.utils.oauth2.portlet.DriveOperation;

/**
 * Handles the OAuth2 calls
 * 
 * @author Jean-Sébastien
 */
@Controller
public class RedirectController {


    @Autowired
    ServletContext context;


    @PostConstruct
    public void init() {
        MixedApplicationsPortlet.servletContext = context;
    }


    /**
     * Temporary spring authentication
     * 
     */
    public static class OAuth2User extends AbstractAuthenticationToken {

        private static final long serialVersionUID = 1L;

        private String principal;

        public OAuth2User(String name, boolean authenticated) {
            super(null);
            setAuthenticated(authenticated);
            this.principal = name;
        }

        public Object getCredentials() {
            return null;
        }

        public Object getPrincipal() {
            return this.principal;
        }
    }


    @RequestMapping("/redirect")
    public String redirect(HttpServletRequest request) {

        // Get portlet user
       
        OAuth2PortletRedirectBean redirectBean =  (OAuth2PortletRedirectBean) request.getSession().getAttribute("oauth2DirectBean");
        
        // Set as Spring authentication
        if( redirectBean.getUser() != null) {
            Authentication auth = new OAuth2User(redirectBean.getUser(), true);
            SecurityContextHolder.getContext().setAuthentication(auth);
        }


        // call the template
        Object resp = redirectBean.getCall().execute();
        
        // set Response
        redirectBean.setResponse( resp);

        // Prepare the redirection
        request.setAttribute("originUrl", redirectBean.getOriginUrl());
  

        return "return-to-portlet";
    }
}