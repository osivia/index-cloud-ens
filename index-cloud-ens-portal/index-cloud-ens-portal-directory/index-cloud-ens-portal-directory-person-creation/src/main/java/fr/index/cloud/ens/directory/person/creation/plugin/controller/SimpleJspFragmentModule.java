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
public class SimpleJspFragmentModule extends FragmentModule {

	private final String viewJspName;
	
	/**
	 * @param portletContext
	 */
	public SimpleJspFragmentModule(PortletContext portletContext, String jspName) {
		super(portletContext);
		this.viewJspName = jspName;
	}

	/* (non-Javadoc)
	 * @see fr.toutatice.portail.cms.nuxeo.api.fragment.FragmentModule#getViewJSPName()
	 */
	@Override
	public String getViewJSPName() {
		return viewJspName;
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.portail.cms.nuxeo.api.fragment.FragmentModule#isDisplayedInAdmin()
	 */
	@Override
	public boolean isDisplayedInAdmin() {
		return false;
	}
	

}
