package fr.index.cloud.ens.selectors.filter.portlet.repository;

import fr.index.cloud.ens.selectors.filter.portlet.repository.command.LoadVocabularyCommand;
import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.VocabularyHelper;
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
 * File browser filter portlet repository implementation.
 *
 * @author Cédric Krommenhoek
 * @see FileBrowserFilterRepository
 */
@Repository
public class FileBrowserFilterRepositoryImpl implements FileBrowserFilterRepository {

    /**
     * Application context.
     */
    @Autowired
    private ApplicationContext applicationContext;


    /**
     * Constructor.
     */
    public FileBrowserFilterRepositoryImpl() {
        super();
    }


    @Override
    public String getSelectionLabel(PortalControllerContext portalControllerContext, String vocabulary, String key) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = this.getNuxeoController(portalControllerContext);

        return VocabularyHelper.getVocabularyLabel(nuxeoController, vocabulary, key);
    }


    @Override
    public JSONArray loadVocabulary(PortalControllerContext portalControllerContext, String vocabulary) {
        // Nuxeo controller
        NuxeoController nuxeoController = this.getNuxeoController(portalControllerContext);

        // Nuxeo command
        INuxeoCommand command = this.applicationContext.getBean(LoadVocabularyCommand.class, vocabulary);

        return (JSONArray) nuxeoController.executeNuxeoCommand(command);
    }


    /**
     * Get Nuxeo controller.
     *
     * @param portalControllerContext portal controller context
     * @return Nuxeo controller
     */
    private NuxeoController getNuxeoController(PortalControllerContext portalControllerContext) {
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);
        nuxeoController.setCacheTimeOut(TimeUnit.HOURS.toMillis(1));
        nuxeoController.setAuthType(NuxeoCommandContext.AUTH_TYPE_SUPERUSER);
        nuxeoController.setCacheType(CacheInfo.CACHE_SCOPE_PORTLET_CONTEXT);
        return nuxeoController;
    }

}
