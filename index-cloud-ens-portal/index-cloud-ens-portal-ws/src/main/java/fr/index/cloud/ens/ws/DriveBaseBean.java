package fr.index.cloud.ens.ws;

/**
 * Represents a file
 * 
 * @author Jean-Sébastien
 */
public class DriveBaseBean {

    private final String title;
    private final String id;
    private final String type;    
    

    
    public String getType() {
        return type;
    }


    public String getId() {
        return id;
    }


    public String getTitle() {
        return title;
    }

    public DriveBaseBean(String type, String id, String title) {
        super();
        this.type = type;
        this.title = title;
        this.id = id;
      
    }
}
