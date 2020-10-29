package fr.index.cloud.ens.search.filters.portlet.repository;

import fr.index.cloud.ens.search.common.portlet.repository.SearchCommonRepositoryImpl;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoDocumentContext;
import org.osivia.portal.api.context.PortalControllerContext;
import org.springframework.stereotype.Repository;

/**
 * Search filters portlet repository implementation.
 *
 * @author Cédric Krommenhoek
 * @see SearchCommonRepositoryImpl
 * @see SearchFiltersRepository
 */
@Repository
public class SearchFiltersRepositoryImpl extends SearchCommonRepositoryImpl implements SearchFiltersRepository {

    /**
     * Constructor.
     */
    public SearchFiltersRepositoryImpl() {
        super();
    }

}
