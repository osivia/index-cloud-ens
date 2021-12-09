package fr.index.cloud.ens.utils.oauth2.redirect;

/**
 * This bean is shared between the portlet and the redirection servlet
 */
public class OAuth2PortletRedirectBean {
    
    private String user;
    private String originUrl;
    private OAuth2PortletOperation call;
    private Object response;
    
    

    public OAuth2PortletRedirectBean(String user, String originUrl, OAuth2PortletOperation call) {
        super();
        this.user = user;
        this.originUrl = originUrl;
        this.call = call;
    }

    
    /**
     * Getter for user.
     * @return the user
     */
    public String getUser() {
        return user;
    }

    
    /**
     * Getter for originUrl.
     * @return the originUrl
     */
    public String getOriginUrl() {
        return originUrl;
    }

    /**
     * Getter for call.
     * @return the call
     */
    public OAuth2PortletOperation getCall() {
        return call;
    }
    
    /**
     * Getter for response.
     * @return the response
     */
    public Object getResponse() {
        return response;
    }


    
    /**
     * Setter for response.
     * @param response the response to set
     */
    public void setResponse(Object response) {
        this.response = response;
    }

}
