package fr.index.cloud.oauth.config;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osivia.portal.api.portlet.AnnotationPortletApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import fr.index.cloud.ens.ws.portlet.WSUtilPortlet;
import fr.index.cloud.oauth.tokenStore.PortalAuthorizationCodeStore;

/**
 * Implements web security 
 * - cors
 * - session fixation
 * 
 * @author Jean-Sébastien
 */

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SecurityFilter implements Filter {
    
    /** The logger. */
    protected static Log logger = LogFactory.getLog(SecurityFilter.class);
    
    
    public FilterConfig filterConfig;


    public SecurityFilter() {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) res;
        HttpServletRequest request = (HttpServletRequest) req;
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "x-requested-with, authorization, content-type");

        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            
            boolean continueFilter = true;
            
            // Avoid session-fixation hacks
            if( request.getRequestURI().endsWith("/oauth/token"))    {
                HttpSession session = ((HttpServletRequest) req).getSession(false);
                if( session != null)    {
                    ((HttpServletRequest) req).getSession(false).invalidate();
                }
            }
            
            // /oauth/authorize must be accessed just one time par login
            // if not, the user must reauthenticate for security reason
            if( request.getRequestURI().endsWith("/oauth/authorize"))    {
                HttpSession session = ((HttpServletRequest) req).getSession(false);
                if( session != null)    {
                    if( session.getAttribute("displayAuthorize") != null)  {
                        if( !"true".equals(request.getParameter("user_oauth_approval")))    {
                            ((HttpServletRequest) req).getSession(false).invalidate();
                            if( logger.isDebugEnabled())    {
                                logger.debug("invalidateSession ");
                            }
                            
                        }
                    }                    
                }
            }      
            
            
            if( continueFilter)
                chain.doFilter(req, res);
        }

     }


    @Override
    public void destroy() {
     }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
        WSUtilPortlet.servletContext = filterConfig.getServletContext();
    }
}