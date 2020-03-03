package fr.index.cloud.oauth.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.index.cloud.oauth.tokenStore.PortalAuthorizationCodeStore;

/**
 * the Accept : application/json must be specified in order to reply correct JSON format
 * 
 * It's due to oAuth2 librairies  ...
 * for exemple, the DefaultOAuth2ExceptionRenderer.class won't reply an applicative code
 */
public class CheckJSONCompatibilityHeader extends HttpServletRequestWrapper {

    /** The logger. */
    protected static Log logger = LogFactory.getLog(CheckJSONCompatibilityHeader.class);
    

    public CheckJSONCompatibilityHeader(HttpServletRequest request) {
        super(request);
    }
    
    /**
     * The default behavior of this method is to return getHeaders(String name)
     * on the wrapped request object.
     */
    public Enumeration getHeaders(String name) {
        boolean mustTransform = false;        
        if ("Accept".equals(name)) {

            Enumeration e = super.getHeaders(name);


            if( e != null && e.hasMoreElements()) {
                String value = (String) e.nextElement();
                if( "*/*".equals(value) && !e.hasMoreElements())    {
                    mustTransform = true;
                }
             }  else    {
                 mustTransform = true;
             }
         }
        
        
        if( mustTransform) {
            if( logger.isDebugEnabled())    {
                logger.debug("add application/json to header");
            }
            
            List<String> values = new ArrayList<String>();
            values.add("application/json");
            return Collections.enumeration(values);
        }          

        return super.getHeaders(name);
    } 

    /**
     * The default behavior of this method is to return getHeader(String name)
     * on the wrapped request object.
     */
    public String getHeader(String name) {
        String value = super.getHeader(name);
        if ("Accept".equals(name)) {
            if (value == null || ("*/*".equals(value)))
                value = "application/json";
        }
        return value;
    }


    /**
     * The default behavior of this method is to return getHeaderNames()
     * on the wrapped request object.
     */

    public Enumeration getHeaderNames() {
        List<String> names = Collections.list(super.getHeaderNames());
        if (!names.contains("Accept"))
            names.add("Accept");
        return Collections.enumeration(names);
    }


}