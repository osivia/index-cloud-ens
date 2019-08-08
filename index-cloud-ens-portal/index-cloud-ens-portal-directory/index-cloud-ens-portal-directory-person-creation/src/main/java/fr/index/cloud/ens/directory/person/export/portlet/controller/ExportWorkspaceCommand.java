/**
 * 
 */
package fr.index.cloud.ens.directory.person.export.portlet.controller;

import java.util.Date;

import org.nuxeo.ecm.automation.client.Session;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;

/**
 * @author loic
 *
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ExportWorkspaceCommand implements INuxeoCommand{

	
	private String userWorkspacePath;

	public ExportWorkspaceCommand(String userWorkspacePath) {
		this.userWorkspacePath = userWorkspacePath;
		
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand#execute(org.nuxeo.ecm.automation.client.Session)
	 */
	@Override
	public Object execute(Session nuxeoSession) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand#getId()
	 */
	@Override
	public String getId() {
		
		return this.getClass().getSimpleName() + "/"+userWorkspacePath+"/"+new Date().getTime();
	}

	
	
}
