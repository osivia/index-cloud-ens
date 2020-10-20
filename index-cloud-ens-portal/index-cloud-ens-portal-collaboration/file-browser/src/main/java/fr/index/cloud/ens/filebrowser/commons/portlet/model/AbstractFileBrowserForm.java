package fr.index.cloud.ens.filebrowser.commons.portlet.model;

import org.osivia.services.workspace.filebrowser.portlet.model.FileBrowserForm;

import java.util.List;

/**
 * File browser form java-bean abstract super-class.
 *
 * @author Cédric Krommenhoek
 */
public abstract class AbstractFileBrowserForm extends FileBrowserForm {

    /** Columns. */
    private List<AbstractFileBrowserColumn> columns;


    /**
     * Constructor.
     */
    public AbstractFileBrowserForm() {
        super();
    }


    public List<AbstractFileBrowserColumn> getColumns() {
        return columns;
    }

    public void setColumns(List<AbstractFileBrowserColumn> columns) {
        this.columns = columns;
    }
}
