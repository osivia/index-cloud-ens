package fr.index.cloud.ens.ws.beans;

import java.util.List;

/**
 * Represents a content (file, folder)
 * 
 * @author Jean-Sébastien
 */
public class DriveContentBean extends DriveBaseBean {

    private final List<DriveParentBean> parents;

    
    public List<DriveParentBean> getParents() {
        return parents;
    }

    public DriveContentBean(String type, String id, String title, List<DriveParentBean> parents) {
        super(type, id, title);
        this.parents = parents;
    }
}
