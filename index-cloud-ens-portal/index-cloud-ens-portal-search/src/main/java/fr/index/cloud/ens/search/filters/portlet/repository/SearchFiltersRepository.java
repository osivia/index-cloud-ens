package fr.index.cloud.ens.search.filters.portlet.repository;

import fr.index.cloud.ens.search.common.portlet.repository.SearchCommonRepository;
import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoDocumentContext;
import org.osivia.portal.api.context.PortalControllerContext;

import javax.portlet.PortletException;

/**
 * Search filters portlet repository interface.
 *
 * @author Cédric Krommenhoek
 * @see SearchCommonRepository
 */
public interface SearchFiltersRepository extends SearchCommonRepository {

    /**
     * Get navigation path.
     *
     * @param portalControllerContext portal controller context
     * @return navigation path
     */
    String getNavigationPath(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Get document context.
     *
     * @param portalControllerContext portal controller context
     * @param path                    document path
     * @return document context
     */
    NuxeoDocumentContext getDocumentContext(PortalControllerContext portalControllerContext, String path) throws PortletException;

}
