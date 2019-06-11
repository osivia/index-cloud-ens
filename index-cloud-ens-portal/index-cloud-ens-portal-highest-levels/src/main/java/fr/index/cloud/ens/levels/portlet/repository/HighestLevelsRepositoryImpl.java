package fr.index.cloud.ens.levels.portlet.repository;

import fr.index.cloud.ens.levels.portlet.repository.command.GetDocumentsCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.VocabularyHelper;
import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.Documents;
import org.osivia.portal.api.context.PortalControllerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Repository;

import javax.portlet.PortletException;
import java.util.List;

/**
 * Highest levels portlet repository implementation.
 *
 * @author Cédric Krommenhoek
 * @see HighestLevelsRepository
 */
@Repository
public class HighestLevelsRepositoryImpl implements HighestLevelsRepository {

    /**
     * Application context.
     */
    @Autowired
    private ApplicationContext applicationContext;


    /**
     * Constructor.
     */
    public HighestLevelsRepositoryImpl() {
        super();
    }


    @Override
    public List<Document> getDocuments(PortalControllerContext portalControllerContext) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // Base path
        String basePath = nuxeoController.getBasePath();

        // Nuxeo command
        GetDocumentsCommand command = this.applicationContext.getBean(GetDocumentsCommand.class, basePath);

        // Documents
        Documents documents = (Documents) nuxeoController.executeNuxeoCommand(command);

        return documents.list();
    }


    @Override
    public String getLabel(PortalControllerContext portalControllerContext, String id) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // Vocabulary key
        String key;
        if (StringUtils.contains(id, "/")) {
            key = StringUtils.substringAfterLast(id, "/");
        } else {
            key = id;
        }

        return VocabularyHelper.getVocabularyLabel(nuxeoController, VOCABULARY, key);
    }

}
