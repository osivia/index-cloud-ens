package fr.index.cloud.ens.search.filters.portlet.repository;

import fr.index.cloud.ens.search.common.portlet.repository.SearchCommonRepositoryImpl;
import fr.index.cloud.ens.search.filters.portlet.repository.command.LoadVocabularyCommand;
import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoDocumentContext;
import fr.toutatice.portail.cms.nuxeo.api.services.NuxeoCommandContext;
import net.sf.json.JSONArray;
import org.osivia.portal.api.cache.services.CacheInfo;
import org.osivia.portal.api.context.PortalControllerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

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
     * Application context.
     */
    @Autowired
    private ApplicationContext applicationContext;


    /**
     * Constructor.
     */
    public SearchFiltersRepositoryImpl() {
        super();
    }


    @Override
    public String getNavigationPath(PortalControllerContext portalControllerContext) {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        return nuxeoController.getNavigationPath();
    }


    @Override
    public NuxeoDocumentContext getDocumentContext(PortalControllerContext portalControllerContext, String path) {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        return nuxeoController.getDocumentContext(path);
    }


    @Override
    public JSONArray loadVocabulary(PortalControllerContext portalControllerContext, String vocabulary) {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);
        nuxeoController.setCacheTimeOut(TimeUnit.HOURS.toMillis(1));
        nuxeoController.setAuthType(NuxeoCommandContext.AUTH_TYPE_SUPERUSER);
        nuxeoController.setCacheType(CacheInfo.CACHE_SCOPE_PORTLET_CONTEXT);

        // Nuxeo command
        INuxeoCommand command = this.applicationContext.getBean(LoadVocabularyCommand.class, vocabulary);

        return (JSONArray) nuxeoController.executeNuxeoCommand(command);
    }

}
