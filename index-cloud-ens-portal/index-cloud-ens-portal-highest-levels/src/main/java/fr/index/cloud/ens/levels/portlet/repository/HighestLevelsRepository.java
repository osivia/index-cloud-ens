package fr.index.cloud.ens.levels.portlet.repository;

import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.context.PortalControllerContext;

import javax.portlet.PortletException;
import java.util.List;

/**
 * Highest levels portlet repository interface.
 *
 * @author Cédric Krommenhoek
 */
public interface HighestLevelsRepository {

    /**
     * Level vocabulary.
     */
    String VOCABULARY = "idx_level";
    /**
     * Levels document property.
     */
    String LEVELS_PROPERTY = "idxcl:levelsTree";


    /**
     * Get documents.
     *
     * @param portalControllerContext portal controller context
     * @return documents
     */
    List<Document> getDocuments(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Get level vocabulary label.
     *
     * @param portalControllerContext portal controller context
     * @param id                      level identifier
     * @return label
     */
    String getLabel(PortalControllerContext portalControllerContext, String id) throws PortletException;


    /**
     * Get search path.
     *
     * @param portalControllerContext portal controller context
     * @return path
     */
    String getSearchPath(PortalControllerContext portalControllerContext) throws PortletException;

}
