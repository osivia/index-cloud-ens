package fr.index.cloud.ens.mutualization.copy.portlet.repository;

import fr.index.cloud.ens.mutualization.copy.portlet.model.MutualizationCopyForm;
import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.context.PortalControllerContext;

import javax.portlet.PortletException;

/**
 * Mutualization copy portlet repository interface.
 *
 * @author Cédric Krommenhoek
 */
public interface MutualizationCopyRepository {

    /**
     * Get user workspace document.
     *
     * @param portalControllerContext portal controller context
     * @return document
     */
    Document getUserWorkspace(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Copy.
     *
     * @param portalControllerContext portal controller context
     * @param form                    form
     */
    void copy(PortalControllerContext portalControllerContext, MutualizationCopyForm form) throws PortletException;

}
