package fr.index.cloud.ens.directory.model.preferences;

import org.osivia.directory.v2.model.preferences.UserPreferences;

/**
 * User preferences customized interface.
 *
 * @author Cédric Krommenhoek
 * @see UserPreferences
 */
public interface CustomizedUserPreferences extends UserPreferences {

    /**
     * Get file browser customized column identifier.
     *
     * @return identifier
     */
    String getCustomizedColumn();


    /**
     * Set file browser customized column identifier.
     *
     * @param id identifier
     */
    void setCustomizedColumn(String id);

}
