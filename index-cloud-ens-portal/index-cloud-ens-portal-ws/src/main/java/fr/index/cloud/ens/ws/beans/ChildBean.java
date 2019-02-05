package fr.index.cloud.ens.ws.beans;

/**
 * Represents a child (file, folder)
 * 
 * @author Jean-Sébastien
 */
public class ChildBean  {

    private final String title;
    private final String id;
    private final String type;
    
    /**
     * Getter for title.
     * @return the title
     */
    public String getTitle() {
        return title;
    }
    
    /**
     * Getter for id.
     * @return the id
     */
    public String getId() {
        return id;
    }
    
    /**
     * Getter for type.
     * @return the type
     */
    public String getType() {
        return type;
    }

    public ChildBean(String type, String id, String title) {
        super();
        this.title = title;
        this.id = id;
        this.type = type;
    }  


}
