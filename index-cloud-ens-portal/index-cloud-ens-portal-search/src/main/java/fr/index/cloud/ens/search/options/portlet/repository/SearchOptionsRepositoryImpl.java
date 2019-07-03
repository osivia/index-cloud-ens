package fr.index.cloud.ens.search.options.portlet.repository;

import fr.index.cloud.ens.search.common.portlet.repository.SearchCommonRepositoryImpl;
import fr.index.cloud.ens.search.options.portlet.repository.command.LoadVocabularyCommand;
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

import javax.portlet.PortletException;
import java.util.concurrent.TimeUnit;

/**
 * Search options portlet repository implementation.
 *
 * @author Cédric Krommenhoek
 * @see SearchCommonRepositoryImpl
 * @see SearchOptionsRepository
 */
@Repository
public class SearchOptionsRepositoryImpl extends SearchCommonRepositoryImpl implements SearchOptionsRepository {

    /**
     * Application context.
     */
    @Autowired
    private ApplicationContext applicationContext;


    @Override
    public NuxeoDocumentContext getDocumentContext(PortalControllerContext portalControllerContext, String path) throws PortletException {
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
