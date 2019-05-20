package fr.index.cloud.ens.dashboard;

import org.dom4j.Element;
import org.osivia.portal.api.context.PortalControllerContext;

import javax.portlet.PortletException;
import java.io.IOException;
import java.util.List;

/**
 * Dashboard service interface.
 *
 * @author JS Steux
 */
public interface DashboardService {

    /**
     * Get dashboard form.
     *
     * @param portalControllerContext portal controller context
     * @return dashboard form
     * @throws PortletException
     */
    DashboardForm getDashboardForm(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Sort dashboard form.
     *
     * @param portalControllerContext portal controller context
     * @param form                    dashboard form
     * @param sort                    sort property
     * @param alt                     alternative sort indicator
     */
    void sort(PortalControllerContext portalControllerContext, DashboardForm form, DashboardSort sort, boolean alt) throws PortletException;


    /**
     * Delete selected items.
     *
     * @param portalControllerContext portal controller context
     * @param form                    trash form
     * @throws PortletException
     */
    void delete(PortalControllerContext portalControllerContext, DashboardForm form, String[] identifiers) throws PortletException;


    /**
     * Get toolbar DOM element.
     *
     * @param portalControllerContext portal controller context
     * @param indexes                 selected row indexes
     * @return DOM element
     */
    Element getToolbar(PortalControllerContext portalControllerContext, List<String> indexes) throws PortletException, IOException;

}
