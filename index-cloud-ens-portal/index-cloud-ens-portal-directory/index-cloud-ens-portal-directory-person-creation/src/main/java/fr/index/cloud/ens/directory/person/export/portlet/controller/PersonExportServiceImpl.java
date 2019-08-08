/**
 * 
 */
package fr.index.cloud.ens.directory.person.export.portlet.controller;

import org.osivia.portal.api.cache.services.CacheInfo;
import org.osivia.portal.api.context.PortalControllerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.services.NuxeoCommandContext;

/**
 * @author loic
 *
 */
@Service
public class PersonExportServiceImpl implements PersonExportService {

    /** Application context. */
    @Autowired
    private ApplicationContext applicationContext;
	
    
	/* (non-Javadoc)
	 * @see fr.index.cloud.ens.directory.person.export.portlet.controller.PersonExportService#export(org.osivia.portal.api.context.PortalControllerContext, java.lang.String)
	 */
	@Override
	public void export(PortalControllerContext portalControllerContext, String userWorkspacePath) {
		
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);
        
        nuxeoController.setAuthType(NuxeoCommandContext.AUTH_TYPE_SUPERUSER);
        nuxeoController.setCacheType(CacheInfo.CACHE_SCOPE_NONE);
        nuxeoController.setAsynchronousCommand(true);
        
        // Nuxeo command
        INuxeoCommand command = this.applicationContext.getBean(ExportWorkspaceCommand.class, userWorkspacePath);
        nuxeoController.executeNuxeoCommand(command);
		
	}

}
