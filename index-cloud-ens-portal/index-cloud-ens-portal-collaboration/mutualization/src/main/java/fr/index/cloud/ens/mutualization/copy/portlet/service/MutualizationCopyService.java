package fr.index.cloud.ens.mutualization.copy.portlet.service;

import fr.index.cloud.ens.mutualization.copy.portlet.model.MutualizationCopyForm;
import org.osivia.portal.api.context.PortalControllerContext;

import javax.portlet.PortletException;

/**
 * Mutualization copy portlet service interface.
 *
 * @author Cédric Krommenhoek
 */
public interface MutualizationCopyService {

    /**
     * Document path window property.
     */
    String DOCUMENT_PATH_WINDOW_PROPERTY = "osivia.copy.path";


    /**
     * Get form.
     *
     * @param portalControllerContext portal controller context
     * @return form
     */
    MutualizationCopyForm getForm(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Copy.
     *
     * @param portalControllerContext portal controller context
     * @param form                    form
     */
    void copy(PortalControllerContext portalControllerContext, MutualizationCopyForm form) throws PortletException;


    /**
     * Get redirection URL.
     *
     * @param portalControllerContext portal controller context
     * @param form                    form
     * @return URL
     */
    String getRedirectionUrl(PortalControllerContext portalControllerContext, MutualizationCopyForm form) throws PortletException;


    /**
     * Browse.
     *
     * @param portalControllerContext portal controller context
     * @return JSON data
     */
    String browse(PortalControllerContext portalControllerContext) throws PortletException;

}
