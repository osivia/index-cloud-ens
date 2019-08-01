/**
 * 
 */
package fr.index.cloud.ens.directory.person.export.portlet.controller;

import org.osivia.portal.api.context.PortalControllerContext;

/**
 * @author loic
 *
 */
public interface PersonExportService {

	/**
	 * @param portalControllerContext
	 * @param userWorkspacePath 
	 */
	void export(PortalControllerContext portalControllerContext, String userWorkspacePath);

}
