package fr.index.cloud.ens.ws.beans;

import java.util.Map;

/**
 * Drive.publish input informations
 * 
 * @author Jean-Sébastien
 */
public class PublishBean {
    private String contentId;
    private String format;    
    private String pubId;
    private String pubTitle;  
    private String pubOrganization;    
    
    /**
     * Getter for format.
     * @return the format
     */
    public String getFormat() {
        return format;
    }

    
    /**
     * Setter for format.
     * @param format the format to set
     */
    public void setFormat(String format) {
        this.format = format;
    }
    
    
    /**
     * Getter for pubOrganization.
     * @return the pubOrganization
     */
    public String getPubOrganization() {
        return pubOrganization;
    }

    
    /**
     * Setter for pubOrganization.
     * @param pubOrganization the pubOrganization to set
     */
    public void setPubOrganization(String pubOrganization) {
        this.pubOrganization = pubOrganization;
    }



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



    
    
    /**
     * Getter for pubTitle.
     * @return the pubTitle
     */
    public String getPubTitle() {
        return pubTitle;
    }


    
    /**
     * Setter for pubTitle.
     * @param pubTitle the pubTitle to set
     */
    public void setPubTitle(String pubTitle) {
        this.pubTitle = pubTitle;
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

    
    /**
     * Getter for properties.
     * @return the properties
     */
    public Map<String, String> getProperties() {
        return properties;
    }

    
    /**
     * Setter for properties.
     * @param properties the properties to set
     */
    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }
}
