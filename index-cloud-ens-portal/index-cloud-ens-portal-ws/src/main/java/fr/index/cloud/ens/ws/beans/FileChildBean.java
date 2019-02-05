package fr.index.cloud.ens.ws.beans;


public class FileChildBean extends ChildBean {
    public long size;
    public long lastModified;
    
    
    /**
     * Getter for lastModified.
     * @return the lastModified
     */
    public long getLastModified() {
        return lastModified;
    }


    
    /**
     * Setter for lastModified.
     * @param lastModified the lastModified to set
     */
    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }


    /**
     * Getter for size.
     * @return the size
     */
    public long getSize() {
        return size;
    }

    
    /**
     * Setter for size.
     * @param size the size to set
     */
    public void setSize(long size) {
        this.size = size;
    }
    
    public FileChildBean(String type, String id, String title, long size, long lastModified) {
        super( type,  id,  title);
        this.size = size;
        this.lastModified = lastModified;
    } 
}
