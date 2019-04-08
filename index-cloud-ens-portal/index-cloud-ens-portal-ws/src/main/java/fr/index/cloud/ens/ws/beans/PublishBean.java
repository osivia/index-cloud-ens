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
    private String pubOrganization;    
    private String pubGroup;    
    private String pubContext;    
    
    
    /**
     * Getter for pubGroup.
     * @return the pubGroup
     */
    public String getPubGroup() {
        return pubGroup;
    }


    
    /**
     * Setter for pubGroup.
     * @param pubGroup the pubGroup to set
     */
    public void setPubGroup(String pubGroup) {
        this.pubGroup = pubGroup;
    }


    
    /**
     * Getter for pubContext.
     * @return the pubContext
     */
    public String getPubContext() {
        return pubContext;
    }


    
    /**
     * Setter for pubContext.
     * @param pubContext the pubContext to set
     */
    public void setPubContext(String pubContext) {
        this.pubContext = pubContext;
    }


    // Classifiers
    Map<String,String> properties;
    
    
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
