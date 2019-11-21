package fr.index.cloud.ens.portal.discussion.portlet.repository;


import fr.index.cloud.ens.portal.discussion.portlet.model.DiscussionCreation;
import fr.index.cloud.ens.portal.discussion.portlet.model.DiscussionDocument;
import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.domain.DocumentDTO;
import fr.toutatice.portail.cms.nuxeo.api.services.NuxeoCommandContext;
import fr.toutatice.portail.cms.nuxeo.api.services.dao.DocumentDAO;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.Documents;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.cache.services.CacheInfo;
import org.osivia.portal.api.cms.EcmDocument;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.tasks.ITasksService;
import org.osivia.portal.core.cms.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Repository;

import javax.portlet.PortletException;
import java.util.ArrayList;
import java.util.List;

/**
 * Discussion repository implementation.
 *
 * @author Jean-Sébastien Steux
 * @see DiscussionRepository
 */
@Repository
public class DiscussionRepositoryImpl implements DiscussionRepository {

    /**
     * Application context.
     */
    @Autowired
    private ApplicationContext applicationContext;

    /**
     * CMS service locator.
     */
    @Autowired
    private ICMSServiceLocator cmsServiceLocator;




    /** Tasks service. */
    @Autowired
    private ITasksService tasksService;

    /**
     * Constructor.
     */
    public DiscussionRepositoryImpl() {
        super();
    }


    /**
     * {@inheritDoc}
     * @throws PortletException 
     */
    @Override
    public List<DiscussionDocument> getDiscussions(PortalControllerContext portalControllerContext) throws PortletException {
         List<EcmDocument> documents;
        try {
            documents = this.tasksService.getTasks(portalControllerContext);
        } catch (PortalException e) {
            throw new PortletException(e);
        }

        // Tasks
        List<DiscussionDocument> discussions = new ArrayList<>(documents.size());

        for (EcmDocument ecmDocument : documents) {
            if (ecmDocument instanceof Document) {
                // Nuxeo document
                Document document = (Document) ecmDocument;

                if (document.getType().equals("Discussion")) {
                    DiscussionDocument discussion = this.applicationContext.getBean(DiscussionDocument.class, document);
                    if (discussion != null) {
                        discussions.add(discussion);
                    }
                }
            }
        }


        return discussions;
    }


    private NuxeoController getNuxeoController(PortalControllerContext portalControllerContext) {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);
        nuxeoController.setAuthType(NuxeoCommandContext.AUTH_TYPE_SUPERUSER);
        nuxeoController.setCacheType(CacheInfo.CACHE_SCOPE_NONE);
        return nuxeoController;
    }


    /**
     * Get trashed document.
     *
     * @param nuxeoController Nuxeo controller
     * @param document Nuxeo document
     * @return trashed document
     */
    private DiscussionDocument getTrashedDocument(NuxeoController nuxeoController, Document document) throws CMSException {
        // CMS service
        ICMSService cmsService = this.cmsServiceLocator.getCMSService();
        // CMS context
        CMSServiceCtx cmsContext = nuxeoController.getCMSCtx();

        // Trashed document
        DiscussionDocument trashedDocument;

        trashedDocument = this.applicationContext.getBean(DiscussionDocument.class, document);


        return trashedDocument;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<DiscussionDocument> deleteAll(PortalControllerContext portalControllerContext) throws PortletException {
        return this.delete(portalControllerContext, null);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<DiscussionDocument> delete(PortalControllerContext portalControllerContext, List<String> selectedPaths) throws PortletException {
        return this.executeTrashCommand(portalControllerContext, DeleteDocumentsCommand.class, selectedPaths);
    }


    /**
     * Execute trash Nuxeo command.
     *
     * @param portalControllerContext portal controller context
     * @param clazz trash command class
     * @param selectedPaths selected item paths, may be null
     * @return rejected documents
     */
    private List<DiscussionDocument> executeTrashCommand(PortalControllerContext portalControllerContext, Class<? extends DiscussionCommand> clazz,
            List<String> selectedPaths) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // Nuxeo command
        INuxeoCommand command;

        if (selectedPaths == null) {
            // Base path
            String basePath = nuxeoController.getBasePath();

            command = this.applicationContext.getBean(clazz, basePath);
        } else if (selectedPaths.isEmpty()) {
            command = null;
        } else {
            command = this.applicationContext.getBean(clazz, selectedPaths);
        }


        // Rejected documents
        List<DiscussionDocument> rejected;

        if (command == null) {
            rejected = null;
        } else {
            Documents documents = (Documents) nuxeoController.executeNuxeoCommand(command);
            rejected = new ArrayList<>(documents.size());
            for (Document document : documents.list()) {
                DiscussionDocument trashedDocument;
                try {
                    trashedDocument = this.getTrashedDocument(nuxeoController, document);
                } catch (CMSException e) {
                    throw new PortletException(e);
                }

                if (trashedDocument != null) {
                    rejected.add(trashedDocument);
                }
            }
        }

        return rejected;
    }


    @Override
    public void createDiscussion(PortalControllerContext portalControllerContext, DiscussionCreation discution) throws PortletException {
        NuxeoController nuxeoController = getNuxeoController(portalControllerContext);

        // Nuxeo command
        INuxeoCommand command = this.applicationContext.getBean(CreateDiscussionCommand.class, discution);
        nuxeoController.executeNuxeoCommand(command);

    }


}
