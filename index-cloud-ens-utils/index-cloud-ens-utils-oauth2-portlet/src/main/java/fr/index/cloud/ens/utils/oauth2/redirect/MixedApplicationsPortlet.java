package fr.index.cloud.ens.utils.oauth2.redirect;

import java.util.Enumeration;

import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import javax.servlet.ServletContext;

import org.osivia.portal.api.portlet.AnnotationPortletApplicationContext;
import org.springframework.web.context.WebApplicationContext;

import fr.toutatice.portail.cms.nuxeo.api.CMSPortlet;

/**
 * Technical portlet for mixed spring servlet and portlets 
 * 
 * - initialize servletContextAttribute which has been dropped by portlet
 * 
 * @author Jean-Sébastien
 */
public class MixedApplicationsPortlet extends CMSPortlet {
    
    
    public static ServletContext servletContext = null;

    @Override
    public void init(PortletConfig config) throws PortletException {
        super.init(config);

 
        Enumeration<String> attrNames = servletContext.getAttributeNames();
        while (attrNames.hasMoreElements()) {
            String attrName = attrNames.nextElement();
            Object attrValue = servletContext.getAttribute(attrName);
            if (attrValue instanceof WebApplicationContext) {
                if (!(attrValue instanceof AnnotationPortletApplicationContext)) {
                    servletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, attrValue);
                }
            }
        }
  }
    
    
    
    @Override
    public void destroy() {
        //Servlet + portlets cohabitations ...        
        if( servletContext != null)
            servletContext.removeAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
      }

    
    
}
