package fr.index.cloud.ens.ws.beans;

import java.util.List;

/**
 * Represents a file
 * 
 * @author Jean-Sébastien
 */
public class FileBean extends DriveContentBean {
    private String shareLink;
    private long size;
    
    
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

    public FileBean(String id, String title, List<DriveParentBean> parentsId, String shareLink, long size) {
        super("file", id, title, parentsId);
        this.shareLink = shareLink;
    }
}
