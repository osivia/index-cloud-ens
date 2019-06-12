package fr.index.cloud.ens.selectors.eraser.portlet.service;

import org.osivia.portal.api.context.PortalControllerContext;

import javax.portlet.PortletException;

/**
 * File browser filter eraser portlet service interface.
 *
 * @author Cédric Krommenhoek
 */
public interface FileBrowserFilterEraserService {

    /**
     * Check if file browser filter erase is empty.
     *
     * @param portalControllerContext portal controller context
     * @return true if portlet is empty
     */
    boolean isEmpty(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Erase file browser filters.
     *
     * @param portalControllerContext portal controller context
     */
    void erase(PortalControllerContext portalControllerContext) throws PortletException;

}
