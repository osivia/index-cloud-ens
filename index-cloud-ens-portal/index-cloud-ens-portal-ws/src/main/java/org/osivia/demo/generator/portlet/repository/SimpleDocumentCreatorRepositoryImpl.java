package org.osivia.demo.generator.portlet.repository;

import javax.portlet.PortletException;

import org.osivia.demo.generator.portlet.model.CreationForm;
import org.osivia.portal.api.context.PortalControllerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Repository;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;

/**
 * Portlet repository implementation.
 * 
 * @author Cédric Krommenhoek
 * @see SimpleDocumentCreatorRepository
 */
@Repository
public class SimpleDocumentCreatorRepositoryImpl implements SimpleDocumentCreatorRepository {

    /** Application context. */
    @Autowired
    private ApplicationContext applicationContext;


    /**
     * Constructor.
     */
    public SimpleDocumentCreatorRepositoryImpl() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void create(PortalControllerContext portalControllerContext, CreationForm form) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // Nuxeo command
        INuxeoCommand command = this.applicationContext.getBean(CreateDocumentCommand.class, form);
        nuxeoController.executeNuxeoCommand(command);
    }

}
