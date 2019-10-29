package fr.index.cloud.ens.initializer.service;

import fr.index.cloud.ens.initializer.service.commands.*;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.services.NuxeoCommandContext;
import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.cache.services.CacheInfo;
import org.osivia.portal.api.context.PortalControllerContext;
import org.springframework.stereotype.Service;

/**
 * Initializer portlet service implementation.
 *
 * @author Loïc Billon
 * @author Cédric Krommenhoek
 * @see InitializerService
 */
@Service
public class InitializerServiceImpl implements InitializerService {

    /**
     * Constructor.
     */
    public InitializerServiceImpl() {
        super();
    }


    @Override
    public void initialize(PortalControllerContext portalControllerContext) {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);
        nuxeoController.setAuthType(NuxeoCommandContext.AUTH_TYPE_SUPERUSER);
        nuxeoController.setCacheType(CacheInfo.CACHE_SCOPE_PORTLET_CONTEXT);
        nuxeoController.setAsynchronousCommand(false);

        // Container
        Document modelsContainer = (Document) nuxeoController.executeNuxeoCommand(new CreateProcedureContainerCommand());

        // Models
        nuxeoController.executeNuxeoCommand(new CreateProcedureModelsCommand(modelsContainer));

        // Publication space
        nuxeoController.executeNuxeoCommand(new CreatePublicationSpaceCommand());

        // Configuration space
        nuxeoController.executeNuxeoCommand(new CreateConfigurationSpaceCommand());

        // Mutualized space
        nuxeoController.executeNuxeoCommand(new CreateMutualizedSpaceCommand());
    }

}
