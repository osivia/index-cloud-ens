package fr.index.cloud.ens.dashboard;

import javax.portlet.PortletException;


import org.osivia.portal.api.context.PortalControllerContext;


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
     * Delete selected items.
     * 
     * @param portalControllerContext portal controller context
     * @param form trash form
     * @throws PortletException
     */
    void delete(PortalControllerContext portalControllerContext, DashboardForm form) throws PortletException;


 }
