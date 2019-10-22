package fr.index.cloud.ens.search.saved.portlet.repository;

import fr.index.cloud.ens.search.common.portlet.repository.SearchCommonRepositoryImpl;
import org.springframework.stereotype.Repository;

/**
 * Saved searches portlet repository implementation.
 *
 * @author Cédric Krommenhoek
 * @see SearchCommonRepositoryImpl
 * @see SavedSearchesRepository
 */
@Repository
public class SavedSearchesRepositoryImpl extends SearchCommonRepositoryImpl implements SavedSearchesRepository {

    /**
     * Constructor.
     */
    public SavedSearchesRepositoryImpl() {
        super();
    }

}
