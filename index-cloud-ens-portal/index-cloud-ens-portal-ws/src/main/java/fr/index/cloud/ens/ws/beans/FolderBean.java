package fr.index.cloud.ens.ws.beans;

import java.util.List;

/**
 * Represents a folder
 * 
 * @author Jean-Sébastien
 */
public class FolderBean extends DriveContentBean {

    private final List<ChildBean> childrens;

    public FolderBean(String id, String title, List<ChildBean> childrens, List<DriveParentBean> parentId) {
        super("folder",id, title, parentId);
        this.childrens = childrens;
    }

    public List<ChildBean> getChildrens() {
        return childrens;
    }


}
