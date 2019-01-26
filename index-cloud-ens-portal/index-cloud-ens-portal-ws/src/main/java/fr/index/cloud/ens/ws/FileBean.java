package fr.index.cloud.ens.ws;

/**
 * Represents a file
 * 
 * @author Jean-Sébastien
 */
public class FileBean extends ContentBean {
    private String shareLink;

    
    /**
     * Getter for shareLink.
     * @return the shareLink
     */
    public String getShareLink() {
        return shareLink;
    }

    
    /**
     * Setter for shareLink.
     * @param shareLink the shareLink to set
     */
    public void setShareLink(String shareLink) {
        this.shareLink = shareLink;
    }

    public FileBean(String id, String title, String parentId, String shareLink) {
        super("file", id, title, parentId);
        this.shareLink = shareLink;
    }
}
