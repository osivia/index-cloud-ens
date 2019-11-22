package fr.index.cloud.ens.filebrowser.commons.portlet.model;

import org.osivia.services.workspace.filebrowser.portlet.model.FileBrowserSortField;

/**
 * File browser sort field interface.
 */
public interface AbstractFileBrowserSortField extends FileBrowserSortField {

    /**
     * Get customizable field indicator.
     */
    boolean isCustomizable();

}
