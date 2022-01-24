package fr.index.cloud.ens.utils.oauth2.portlet;

import fr.index.cloud.ens.utils.oauth2.redirect.OAuth2PortletRedirectBean;

/**
 * Internal portlet exception to redirect OAuth2
 */
public class PortletRedirectionException extends Exception {
    /**
     * 
     */
    private static final long serialVersionUID = 4913200491812298467L;
    OAuth2PortletRedirectBean redirectBean;

    /**
     * Instantiates a new portlet redirection exception.
     *
     * @param redirectBean the redirect bean
     */
    public PortletRedirectionException(OAuth2PortletRedirectBean redirectBean) {
        super();
        this.redirectBean = redirectBean;
    }

    
    /**
     * Getter for redirectBean.
     * @return the redirectBean
     */
    public OAuth2PortletRedirectBean getRedirectBean() {
        return redirectBean;
    }
}
