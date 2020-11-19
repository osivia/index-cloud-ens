package fr.index.cloud.ens.generator.repository;

import javax.portlet.PortletException;

import org.osivia.portal.api.context.PortalControllerContext;

import fr.index.cloud.ens.generator.model.Configuration;

/**
 * Generator repository interface.
 *
 * @author Cédric Krommenhoek
 */
public interface GeneratorRepository {
    
    /**
     * Enable property.
     */
    String ENABLE_PROPERTY = "mtz:enable";
    /**
     * Title property.
     */
    String TITLE_PROPERTY = "mtz:title";
    /**
     * Keywords property.
     */
    String KEYWORDS_PROPERTY = "idxcl:keywords";
    /**
     * Document types property.
     */
    String DOCUMENT_TYPES_PROPERTY = "idxcl:documentTypes";
    /**
     * Levels property.
     */
    String LEVELS_PROPERTY = "idxcl:levels";
    /**
     * Subjects property.
     */
    String SUBJECTS_PROPERTY = "idxcl:subjects";

    /**
     * Get generator configuration.
     *
     * @param portalControllerContext portal controller context
     * @return configuration
     * @throws PortletException
     */
    Configuration getConfiguration(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Set generator configuration.
     *
     * @param portalControllerContext portal controller context
     * @param configuration generator configuration
     * @throws PortletException
     */
    void setConfiguration(PortalControllerContext portalControllerContext, Configuration configuration) throws PortletException;


    /**
     * Generate.
     *
     * @param portalControllerContext portal controller context
     * @throws PortletException
     */
    void generate(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Purge.
     * 
     * @param portalControllerContext portal controller context
     * @throws PortletException
     */
    void purge(PortalControllerContext portalControllerContext) throws PortletException;

}
