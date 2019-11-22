package fr.index.cloud.ens.filebrowser.commons.portlet.model;

import org.osivia.services.workspace.filebrowser.portlet.model.FileBrowserForm;

/**
 * File browser form java-bean abstract super-class.
 *
 * @author Cédric Krommenhoek
 */
public abstract class AbstractFileBrowserForm extends FileBrowserForm {

    /**
     * Customized column.
     */
    private AbstractFileBrowserSortField customizedColumn;


    /**
     * Constructor.
     */
    public AbstractFileBrowserForm() {
        super();
    }


    public AbstractFileBrowserSortField getCustomizedColumn() {
        return customizedColumn;
    }

    public void setCustomizedColumn(AbstractFileBrowserSortField customizedColumn) {
        this.customizedColumn = customizedColumn;
    }

}
