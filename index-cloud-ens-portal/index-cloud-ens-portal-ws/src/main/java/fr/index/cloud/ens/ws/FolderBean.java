package fr.index.cloud.ens.ws;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a folder
 * 
 * @author Jean-Sébastien
 */
public class FolderBean extends ContentBean {

    private final List<ContentBean> childrens;

    public FolderBean(String id, String title, List<ContentBean> childrens, String parentId) {
        super("folder",id, title, parentId);
        this.childrens = childrens;
    }

    public List<ContentBean> getChildrens() {
        return childrens;
    }


}
