package fr.index.cloud.ens.ws;

import org.osivia.portal.core.web.IWebIdService;

public class GenericException extends Exception {

    private static final long serialVersionUID = -7558437064829642698L;
    
    Throwable e;
    String contentId;

    public GenericException(Throwable e, String path) {
        super();
        this.e = e;
        
        if( path.startsWith(IWebIdService.FETCH_PATH_PREFIX))    {
            this.contentId = path.substring(IWebIdService.FETCH_PATH_PREFIX.length());
        }
         
    }

    /**
     * Getter for contentId.
     * 
     * @return the contentId
     */
    public String getContentId() {
        return contentId;
    }

    /**
     * Setter for contentId.
     * 
     * @param contentId the contentId to set
     */
    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    /**
     * Getter for e.
     * 
     * @return the e
     */
    public Throwable getE() {
        return e;
    }


}
