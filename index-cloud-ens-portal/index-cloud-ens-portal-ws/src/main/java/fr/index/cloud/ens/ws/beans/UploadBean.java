package fr.index.cloud.ens.ws.beans;

import java.util.Map;

/**
 * Drive.upload input informations
 * 
 * @author Jean-Sébastien
 */
public class UploadBean {
    private String parentId;
    
    DocumentProperties properties;

     
    /**
     * Getter for parentId.
     * @return the parentId
     */
    public String getParentId() {
        return parentId;
    }


    
    /**
     * Setter for parentId.
     * @param parentId the parentId to set
     */
    public void setParentId(String parentId) {
        this.parentId = parentId;
    }


    /**
     * Getter for properties.
     * @return the properties
     */
    public DocumentProperties getProperties() {
        return properties;
    }

    
    /**
     * Setter for properties.
     * @param properties the properties to set
     */
    public void setProperties(DocumentProperties properties) {
        this.properties = properties;
    }
}
