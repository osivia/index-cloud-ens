package fr.index.cloud.ens.ws;

/**
 * Represents a file
 * 
 * @author Jean-Sébastien
 */
public class FileBean extends ContentBean {

    public FileBean(String id, String title, String parentId) {
        super("file", id, title, parentId);
    }
}
