package fr.index.cloud.ens.levels.portlet.repository;

import fr.index.cloud.ens.levels.portlet.repository.command.GetDocumentsCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.VocabularyHelper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.Documents;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.cms.VirtualNavigationUtils;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.taskbar.ITaskbarService;
import org.osivia.portal.api.taskbar.TaskbarTask;
import org.osivia.portal.core.cms.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Repository;

import javax.portlet.PortletException;
import java.util.Iterator;
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
     * CMS service locator.
     */
    @Autowired
    private ICMSServiceLocator cmsServiceLocator;
    /**
     * Taskbar service.
     */
    @Autowired
    private ITaskbarService taskbarService;


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

        // CMS service
        ICMSService cmsService = this.cmsServiceLocator.getCMSService();
        // CMS context
        CMSServiceCtx cmsContext = nuxeoController.getCMSCtx();

        // User workspace
        CMSItem userWorkspace;
        try {
            userWorkspace = cmsService.getUserWorkspace(cmsContext);
        } catch (CMSException e) {
            throw new PortletException(e);
        }

        // Documents
        List<Document> documents;
        if (userWorkspace == null) {
            documents = null;
        } else {
            // Nuxeo command
            GetDocumentsCommand command = this.applicationContext.getBean(GetDocumentsCommand.class, userWorkspace.getCmsPath());

            // Documents
            Documents result = (Documents) nuxeoController.executeNuxeoCommand(command);

            documents = result.list();
        }

        return documents;
    }


    @Override
    public String getLabel(PortalControllerContext portalControllerContext, String id) {
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


    @Override
    public String getSearchPath(PortalControllerContext portalControllerContext) throws PortletException {
        // CMS service
        ICMSService cmsService = this.cmsServiceLocator.getCMSService();
        // CMS context
        CMSServiceCtx cmsContext = new CMSServiceCtx();
        cmsContext.setPortalControllerContext(portalControllerContext);

        // User workspace
        CMSItem userWorkspace;
        try {
            userWorkspace = cmsService.getUserWorkspace(cmsContext);
        } catch (CMSException e) {
            throw new PortletException(e);
        }

        // Navigation tasks
        List<TaskbarTask> navigationTasks;
        if (userWorkspace == null) {
            navigationTasks = null;
        } else {
            try {
                navigationTasks = this.taskbarService.getTasks(portalControllerContext, userWorkspace.getCmsPath());
            } catch (PortalException e) {
                throw new PortletException(e);
            }
        }

        // Search taskbar task
        TaskbarTask searchTask = null;
        if (CollectionUtils.isNotEmpty(navigationTasks)) {
            Iterator<TaskbarTask> iterator = navigationTasks.iterator();

            while ((searchTask == null) && iterator.hasNext()) {
                TaskbarTask task = iterator.next();

                // Task staple identifier
                String stapleId = VirtualNavigationUtils.getStapleId(task.getPath());

                if (StringUtils.equals(ITaskbarService.SEARCH_TASK_ID, stapleId)) {
                    searchTask = task;
                }
            }
        }

        // Search path
        String path;
        if (searchTask == null) {
            path = null;
        } else {
            path = searchTask.getPath();
        }

        return path;
    }

}
