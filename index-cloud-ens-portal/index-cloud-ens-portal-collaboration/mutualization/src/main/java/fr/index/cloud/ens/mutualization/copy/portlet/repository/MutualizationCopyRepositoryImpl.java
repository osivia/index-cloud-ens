package fr.index.cloud.ens.mutualization.copy.portlet.repository;

import fr.index.cloud.ens.mutualization.copy.portlet.model.MutualizationCopyForm;
import fr.index.cloud.ens.mutualization.copy.portlet.repository.command.MutualizationCopyCommand;
import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoDocumentContext;
import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.core.cms.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Repository;

import javax.portlet.PortletException;

/**
 * Mutualization copy portlet repository implementation.
 *
 * @author Cédric Krommenhoek
 * @see MutualizationCopyRepository
 */
@Repository
public class MutualizationCopyRepositoryImpl implements MutualizationCopyRepository {

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
     * Constructor.
     */
    public MutualizationCopyRepositoryImpl() {
        super();
    }


    @Override
    public Document getUserWorkspace(PortalControllerContext portalControllerContext) throws PortletException {
        // CMS service
        ICMSService cmsService = this.cmsServiceLocator.getCMSService();
        // CMS context
        CMSServiceCtx cmsContext = new CMSServiceCtx();
        cmsContext.setPortalControllerContext(portalControllerContext);

        // CMS item
        CMSItem cmsItem;
        try {
            cmsItem = cmsService.getUserWorkspace(cmsContext);
        } catch (CMSException e) {
            throw new PortletException(e);
        }

        // Document
        return (Document) cmsItem.getNativeItem();
    }


    @Override
    public void copy(PortalControllerContext portalControllerContext, MutualizationCopyForm form) {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // Source document
        NuxeoDocumentContext sourceDocumentContext = nuxeoController.getDocumentContext(form.getDocumentPath());
        Document source = sourceDocumentContext.getDocument();

        // Nuxeo command
        INuxeoCommand command = this.applicationContext.getBean(MutualizationCopyCommand.class, source, form.getTargetPath());
        nuxeoController.executeNuxeoCommand(command);
    }

}
