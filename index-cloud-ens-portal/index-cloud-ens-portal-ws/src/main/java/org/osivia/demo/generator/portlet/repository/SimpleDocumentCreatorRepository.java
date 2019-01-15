package org.osivia.demo.generator.portlet.repository;

import javax.portlet.PortletException;

import org.osivia.demo.generator.portlet.model.CreationForm;
import org.osivia.portal.api.context.PortalControllerContext;

/**
 * Portlet repository interface.
 * 
 * @author Cédric Krommenhoek
 */
public interface SimpleDocumentCreatorRepository {

    /**
     * Create.
     * 
     * @param portalControllerContext portal controller context.
     * @param form creation form
     * @throws PortletException
     */
    void create(PortalControllerContext portalControllerContext, CreationForm form) throws PortletException;

}
