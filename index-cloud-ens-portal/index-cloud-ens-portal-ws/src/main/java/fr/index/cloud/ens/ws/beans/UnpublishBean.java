package fr.index.cloud.ens.ws.beans;

import java.util.Map;

/**
 * Drive.publish input informations
 * 
 * @author Jean-Sébastien
 */
public class UnpublishBean {
    private String contentId;
    private String pubId;
  
 
    /**
     * Getter for pubId.
     * @return the pubId
     */
    public String getPubId() {
        return pubId;
    }



    
    /**
     * Setter for pubId.
     * @param pubId the pubId to set
     */
    public void setPubId(String pubId) {
        this.pubId = pubId;
    }



    
  


    Map<String,String> properties;

    
    /**
     * Getter for contentId.
     * @return the contentId
     */
    public String getContentId() {
        return contentId;
    }

    
    /**
     * Setter for contentId.
     * @param contentId the contentId to set
     */
    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

}
