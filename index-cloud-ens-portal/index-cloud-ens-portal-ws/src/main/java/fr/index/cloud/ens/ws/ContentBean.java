package fr.index.cloud.ens.ws;

/**
 * Represents a file
 * 
 * @author Jean-Sébastien
 */
public class ContentBean extends DriveBaseBean {

    private final String parentId;

    
    public String getParentId() {
        return parentId;
    }

    public ContentBean(String type, String id, String title, String parentId) {
        super(type, id, title);
        this.parentId = parentId;
    }
}
