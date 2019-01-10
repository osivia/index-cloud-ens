package org.osivia.demo.initializer.service;

import javax.portlet.PortletException;

import org.osivia.portal.api.context.PortalControllerContext;

/**
 * 
 * @author Loïc Billon
 *
 */
public interface InitializerService {

	public void initialize(PortalControllerContext portalControllerContext) throws PortletException;

}
