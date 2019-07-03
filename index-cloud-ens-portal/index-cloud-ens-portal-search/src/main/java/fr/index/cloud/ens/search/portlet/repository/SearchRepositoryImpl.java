package fr.index.cloud.ens.search.portlet.repository;

import fr.index.cloud.ens.search.common.portlet.repository.SearchCommonRepositoryImpl;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import org.osivia.portal.api.context.PortalControllerContext;
import org.springframework.stereotype.Repository;

/**
 * Search portlet repository implementation.
 *
 * @author Cédric Krommenhoek
 * @see SearchCommonRepositoryImpl
 * @see SearchRepository
 */
@Repository
public class SearchRepositoryImpl extends SearchCommonRepositoryImpl implements SearchRepository {

    @Override
    public String getNavigationPath(PortalControllerContext portalControllerContext) {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        return nuxeoController.getNavigationPath();
    }

}
