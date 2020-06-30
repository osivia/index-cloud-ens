package fr.index.cloud.ens.portal.discussion.portlet.model;

import java.util.Date;

public class PublicationUse {
    
    
    /** The title. */
    final String  title;
    
    
    /** The last recopy. */
    Date    lastRecopy;
    
    public PublicationUse(String title) {
        super();
        this.title = title;
    }


    /**
     * Getter for title.
     * @return the title
     */
    public String getTitle() {
        return title;
    }
    

    /**
     * Getter for lastRecopy.
     * @return the lastRecopy
     */
    public Date getLastRecopy() {
        return lastRecopy;
    }
    
    /**
     * Setter for lastRecopy.
     * @param lastRecopy the lastRecopy to set
     */
    public void setLastRecopy(Date lastRecopy) {
        this.lastRecopy = lastRecopy;
    }
}
