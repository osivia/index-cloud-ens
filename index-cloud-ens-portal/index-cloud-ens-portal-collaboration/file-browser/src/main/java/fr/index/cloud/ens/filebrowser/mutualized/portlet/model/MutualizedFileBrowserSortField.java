package fr.index.cloud.ens.filebrowser.mutualized.portlet.model;

import fr.index.cloud.ens.filebrowser.commons.portlet.model.AbstractFileBrowserSortField;

/**
 * Mutualized file browser sort field interface.
 *
 * @author Cédric Krommenhoek
 */
public interface MutualizedFileBrowserSortField extends AbstractFileBrowserSortField {

    /**
     * Get NXQL field.
     *
     * @return field
     */
    String getField();

}
