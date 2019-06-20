package fr.index.cloud.ens.ws.beans;

import java.util.Map;

/**
 * Drive.publish input informations
 * 
 * @author Jean-Sébastien
 */
public class PublishBean {
    
    /** The share url. */
    private String shareUrl;
    
 
    
    /** The pub group. */
    private String pubGroup;    
    
    /** The pub context. */
    private String pubContext;    
    
    /** The pub school yeard. */
    private String pubSchoolYear;        
    



    /**
     * Gets the share url.
     *
     * @return the share url
     */
    public String getShareUrl() {
		return shareUrl;
	}



	/**
	 * Sets the share url.
	 *
	 * @param shareUrl the new share url
	 */
	public void setShareUrl(String shareUrl) {
		this.shareUrl = shareUrl;
	}

    
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

    
    
    /**
     * Getter for pubSchoolYear.
     * @return the pubSchoolYear
     */
    public String getPubSchoolYear() {
        return pubSchoolYear;
    }


    
    /**
     * Setter for pubSchoolYear.
     * @param pubSchoolYear the pubSchoolYear to set
     */
    public void setPubSchoolYear(String pubSchoolYear) {
        this.pubSchoolYear = pubSchoolYear;
    }


    /** The properties. */
    // Classifiers
    Map<String,String> properties;
    
   
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
