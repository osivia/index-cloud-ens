package fr.index.cloud.ens.search.saved.portlet.repository;

import fr.index.cloud.ens.search.common.portlet.repository.SearchCommonRepositoryImpl;
import org.osivia.portal.core.cms.ICMSServiceLocator;
import org.springframework.beans.factory.annotation.Autowired;
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
     * CMS service locator.
     */
    @Autowired
    private ICMSServiceLocator cmsServiceLocator;


    /**
     * Constructor.
     */
    public SavedSearchesRepositoryImpl() {
        super();
    }

}
