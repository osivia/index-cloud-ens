package fr.index.cloud.ens.directory.service.preferences;

import fr.index.cloud.ens.directory.model.preferences.CustomizedUserPreferences;
import fr.index.cloud.ens.directory.repository.preferences.UpdateCustomizedUserPreferencesCommand;
import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.directory.v2.model.preferences.UserPreferences;
import org.osivia.directory.v2.service.preferences.UserPreferencesServiceImpl;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.context.PortalControllerContext;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * User preferences service customized implementation.
 *
 * @author Cédric Krommenhoek
 * @see UserPreferencesServiceImpl
 * @see CustomizedUserPreferencesService
 */
@Service
@Primary
public class CustomizedUserPreferencesServiceImpl extends UserPreferencesServiceImpl implements CustomizedUserPreferencesService {

    /**
     * Constructor.
     */
    public CustomizedUserPreferencesServiceImpl() {
        super();
    }


    @Override
    public CustomizedUserPreferences getUserPreferences(PortalControllerContext portalControllerContext) throws PortalException {
        // User preferences
        UserPreferences userPreferences = super.getUserPreferences(portalControllerContext);
        // Customized user preferences
        CustomizedUserPreferences customizedUserPreferences;

        if (userPreferences instanceof CustomizedUserPreferences) {
            customizedUserPreferences = (CustomizedUserPreferences) userPreferences;
        } else {
            customizedUserPreferences = null;
        }

        return customizedUserPreferences;
    }


    @Override
    protected UserPreferences createUserPreferences(Document profile) {
        // User preferences
        UserPreferences userPreferences = super.createUserPreferences(profile);

        if (userPreferences instanceof CustomizedUserPreferences) {
            // Customized user preferences
            CustomizedUserPreferences customizedUserPreferences = (CustomizedUserPreferences) userPreferences;

            // File browser customized column
            String customizedColumn = profile.getString(UpdateCustomizedUserPreferencesCommand.CUSTOMIZED_COLUMN_XPATH);
            customizedUserPreferences.setCustomizedColumn(customizedColumn);
        }

        return userPreferences;
    }

}
