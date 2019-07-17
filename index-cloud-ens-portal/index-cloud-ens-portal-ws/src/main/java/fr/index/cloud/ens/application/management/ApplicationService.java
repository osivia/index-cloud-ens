package fr.index.cloud.ens.application.management;

import org.dom4j.Element;
import org.osivia.portal.api.context.PortalControllerContext;

import fr.index.cloud.ens.dashboard.DashboardForm;

import javax.portlet.PortletException;
import java.io.IOException;
import java.util.List;

/**
 * Application management service interface.
 *
 * @author JS Steux
 */
public interface ApplicationService {

    /**
     * Get Application form.
     *
     * @param portalControllerContext portal controller context
     * @return dashboard form
     * @throws PortletException
     */
    ApplicationForm getApplicationForm(PortalControllerContext portalControllerContext) throws PortletException;

}
