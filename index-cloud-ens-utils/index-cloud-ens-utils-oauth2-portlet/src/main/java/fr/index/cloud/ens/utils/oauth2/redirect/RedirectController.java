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

@Controller
public class RedirectController {

    @Autowired
    private OAuth2RestOperations restTemplate;

    @Autowired
    ServletContext context;


    @PostConstruct
    public void init() {
        MultiApplicationContextPortletUtils.servletContext = context;
    }


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
        String user = (String) request.getSession().getAttribute("oauth2User");
        
        // Set as Spring authentication
        Authentication auth = new OAuth2User(user, true);
        SecurityContextHolder.getContext().setAuthentication(auth);


        // HTTP headers
        HttpHeaders httpHeaders = new HttpHeaders();
        List<MediaType> mediaTypes = new ArrayList<MediaType>();
        mediaTypes.add(MediaType.APPLICATION_JSON);
        
        httpHeaders.setAccept(mediaTypes);
        HttpEntity<String> httpEntity = new HttpEntity(null, httpHeaders);


        Object resp = this.restTemplate.exchange("https://cloud-ens.index-education.local/index-cloud-portal-ens-ws/rest/Drive.content", HttpMethod.GET, httpEntity,
                String.class);


        request.setAttribute("originUrl", (String) request.getSession().getAttribute("oauth2OriginUrl"));
        
        request.getSession().setAttribute("oauth2Response", resp);


        return "return-to-portlet";
    }
}