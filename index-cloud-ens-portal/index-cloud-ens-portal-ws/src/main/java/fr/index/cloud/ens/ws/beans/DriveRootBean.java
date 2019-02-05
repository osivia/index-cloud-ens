package fr.index.cloud.ens.ws.beans;

import java.util.List;

/**
 * Represents the root of the drive
 * 
 * @author Jean-Sébastien
 */
public class DriveRootBean extends DriveBaseBean {

    private final List<ChildBean> childrens;
    public static String TYPE = "root";

    public DriveRootBean(String id, String title, List<ChildBean> childrens) {
        super("root", id, title);
        this.childrens = childrens;        
    }

    public List<ChildBean> getChildrens() {
        return childrens;
    }    
 

}
