/**
 * 
 */
package fr.index.cloud.ens.directory.person.creation.plugin.controller;

import javax.portlet.PortletContext;

import fr.toutatice.portail.cms.nuxeo.api.domain.FragmentType;
import fr.toutatice.portail.cms.nuxeo.api.fragment.FragmentModule;
import fr.toutatice.portail.cms.nuxeo.api.fragment.IFragmentModule;

/**
 * @author Loïc Billon
 *
 */
public class ConfirmationMailFragmentModule extends FragmentModule {

	/**
	 * @param portletContext
	 */
	public ConfirmationMailFragmentModule(PortletContext portletContext) {
		super(portletContext);
	}

	/* (non-Javadoc)
	 * @see fr.toutatice.portail.cms.nuxeo.api.fragment.FragmentModule#getViewJSPName()
	 */
	@Override
	public String getViewJSPName() {
		return "confirmation-mail";
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.portail.cms.nuxeo.api.fragment.FragmentModule#isDisplayedInAdmin()
	 */
	@Override
	public boolean isDisplayedInAdmin() {
		return false;
	}

}
