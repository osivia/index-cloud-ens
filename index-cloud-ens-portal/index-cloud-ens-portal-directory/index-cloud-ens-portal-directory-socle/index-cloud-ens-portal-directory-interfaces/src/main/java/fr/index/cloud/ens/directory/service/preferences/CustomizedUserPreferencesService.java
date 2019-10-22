package fr.index.cloud.ens.directory.service.preferences;

import fr.index.cloud.ens.directory.model.preferences.CustomizedUserPreferences;
import org.osivia.directory.v2.service.preferences.UserPreferencesService;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.context.PortalControllerContext;

/**
 * User preferences service customized interface.
 *
 * @author Cédric Krommenhoek
 * @see UserPreferencesService
 */
public interface CustomizedUserPreferencesService extends UserPreferencesService {

    @Override
    CustomizedUserPreferences getUserPreferences(PortalControllerContext portalControllerContext) throws PortalException;

}
