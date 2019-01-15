package org.osivia.demo.generator.portlet.service;

import javax.portlet.PortletException;

import org.osivia.demo.generator.portlet.model.CreationForm;
import org.osivia.portal.api.context.PortalControllerContext;

/**
 * Portlet service interface.
 * 
 * @author Cédric Krommenhoek
 */
public interface SimpleDocumentCreatorService {

    /**
     * Create.
     * 
     * @param portalControllerContext portal controller context
     * @param form creation form
     * @throws PortletException
     */
    void create(PortalControllerContext portalControllerContext, CreationForm form) throws PortletException;


    /**
     * Randomize.
     * 
     * @param portalControllerContext portal controller context
     * @param form creation form
     * @throws PortletException
     */
    void randomize(PortalControllerContext portalControllerContext, CreationForm form) throws PortletException;


    /**
     * Get creation form.
     * 
     * @param portalControllerContext portal controller context.
     * @return creation form
     * @throws PortletException
     */
    CreationForm getCreationForm(PortalControllerContext portalControllerContext) throws PortletException;

}
