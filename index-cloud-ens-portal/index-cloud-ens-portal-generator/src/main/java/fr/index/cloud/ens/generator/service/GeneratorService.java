package fr.index.cloud.ens.generator.service;

import javax.portlet.PortletException;

import org.osivia.portal.api.context.PortalControllerContext;

import fr.index.cloud.ens.generator.model.Configuration;

/**
 * Generator service interface.
 *
 * @author Cédric Krommenhoek
 */
public interface GeneratorService {

    /**
     * Get generator configuration.
     *
     * @param portalControllerContext portal controller context
     * @return configuration
     * @throws PortletException
     */
    Configuration getConfiguration(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Save generator configuration.
     *
     * @param portalControllerContext portal controller context
     * @param configuration generator configuration
     * @throws PortletException
     */
    void saveConfiguration(PortalControllerContext portalControllerContext, Configuration configuration) throws PortletException;


    /**
     * Generate data.
     *
     * @param portalControllerContext portal controller context
     * @throws PortletException
     */
    void generate(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Purge data.
     * 
     * @param portalControllerContext portal controller context
     * @throws PortletException
     */
    void purge(PortalControllerContext portalControllerContext) throws PortletException;

}
