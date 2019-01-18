package fr.index.cloud.ens.ws;

import java.util.List;

/**
 * Represents a drive
 * 
 * @author Jean-Sébastien
 */
public class DriveBean extends DriveBaseBean {

    private final List<ContentBean> childrens;

    public DriveBean(String id, String title, List<ContentBean> childrens) {
        super("root", id, title);
        this.childrens = childrens;        
    }

    public List<ContentBean> getChildrens() {
        return childrens;
    }    
 

}
