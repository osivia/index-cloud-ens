package fr.index.cloud.ens.utils.oauth2.redirect;

import java.util.Enumeration;

import javax.portlet.PortletConfig;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.servlet.FilterConfig;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

import org.osivia.portal.api.portlet.AnnotationPortletApplicationContext;
import org.osivia.portal.api.portlet.PortletAppUtils;
import org.springframework.web.context.WebApplicationContext;


import fr.toutatice.portail.cms.nuxeo.api.CMSPortlet;

/**
 * Bootstrap entry point
 * 
 * @author Jean-Sébastien
 */
public class MultiApplicationContextPortletUtils extends CMSPortlet {
    
    
    public static ServletContext servletContext = null;

    @Override
    public void init(PortletConfig config) throws PortletException {
        super.init(config);

        
        //Servlet + portlets cohabitations ....
        PortletAppUtils.refreshServletApplicationContext(servletContext);
  }
    
    
    
    @Override
    public void destroy() {
        //Servlet + portlets cohabitations ...        
        PortletAppUtils.removeServletApplicationContext(servletContext);
     }

    
    
}
