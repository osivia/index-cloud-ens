package fr.index.cloud.ens.filebrowser.portlet.model;

import org.osivia.services.workspace.filebrowser.portlet.model.FileBrowserSortField;

/**
 * File browser customized sort field interface.
 */
public interface CustomizedFileBrowserSortField extends FileBrowserSortField {

    /**
     * Get customizable field indicator.
     */
    boolean isCustomizable();

}
