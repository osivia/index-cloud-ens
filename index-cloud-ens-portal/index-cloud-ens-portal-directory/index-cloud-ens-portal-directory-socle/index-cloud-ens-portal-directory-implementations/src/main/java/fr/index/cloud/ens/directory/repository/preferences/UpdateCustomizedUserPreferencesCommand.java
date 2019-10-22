package fr.index.cloud.ens.directory.repository.preferences;

import fr.index.cloud.ens.directory.model.preferences.CustomizedUserPreferences;
import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.directory.v2.repository.preferences.UpdateUserPreferencesCommand;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Update customized user preferences Nuxeo command.
 *
 * @author Cédric Krommenhoek
 * @see UpdateUserPreferencesCommand
 */
@Component
@Primary
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class UpdateCustomizedUserPreferencesCommand extends UpdateUserPreferencesCommand {

    /**
     * Customized column XPath.
     */
    public static final String CUSTOMIZED_COLUMN_XPATH = "idxup:customizedColumn";


    // User preferences
    private final CustomizedUserPreferences preferences;


    /**
     * Constructor.
     *
     * @param preferences user preferences
     */
    public UpdateCustomizedUserPreferencesCommand(CustomizedUserPreferences preferences) {
        super(preferences);
        this.preferences = preferences;
    }


    @Override
    protected PropertyMap getProperties() {
        PropertyMap properties = super.getProperties();

        // File browser customized column
        String customizedColumn = StringUtils.trimToNull(this.preferences.getCustomizedColumn());
        properties.set(CUSTOMIZED_COLUMN_XPATH, customizedColumn);

        return properties;
    }

}
