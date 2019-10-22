package fr.index.cloud.ens.directory.model.preferences;

import fr.index.cloud.ens.directory.model.preferences.CustomizedUserPreferences;
import org.osivia.directory.v2.model.preferences.UserPreferencesImpl;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * User preferences customized implementation.
 *
 * @author Cédric Krommenhoek
 * @see UserPreferencesImpl
 * @see CustomizedUserPreferences
 */
@Component
@Primary
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class CustomizedUserPreferencesImpl extends UserPreferencesImpl implements CustomizedUserPreferences {

    /**
     * Customized column identifier.
     */
    private String customizedColumnId;


    /**
     * Constructor.
     *
     * @param documentId document identifier
     */
    public CustomizedUserPreferencesImpl(String documentId) {
        super(documentId);
    }


    @Override
    public String getCustomizedColumn() {
        return this.customizedColumnId;
    }


    @Override
    public void setCustomizedColumn(String id) {
        this.customizedColumnId = id;
    }

}
