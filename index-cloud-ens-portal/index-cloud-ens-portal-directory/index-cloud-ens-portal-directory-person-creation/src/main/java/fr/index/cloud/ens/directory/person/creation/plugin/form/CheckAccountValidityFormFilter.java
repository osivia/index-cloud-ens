/**
 * 
 */
package fr.index.cloud.ens.directory.person.creation.plugin.form;

import java.util.Map;

import org.osivia.portal.api.PortalException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.index.cloud.ens.directory.person.creation.plugin.service.PersonCreationPluginService;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilter;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterContext;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterException;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterExecutor;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterParameterType;

/**
 * procedure filter : check if an user account is valid
 * @author Loïc Billon
 */
@Component
public class CheckAccountValidityFormFilter implements FormFilter {

	public static final String IDENTIFIER = "CHECK_ACCOUNT_VALIDITY";
	private static final String LABEL_KEY = IDENTIFIER +"_LABEL";
	private static final String DESCRIPTION_KEY = IDENTIFIER +"_DESCRIPTION";

    /** Plugin service. */
    @Autowired
    private PersonCreationPluginService service;
    
	/* (non-Javadoc)
	 * @see fr.toutatice.portail.cms.nuxeo.api.forms.FormFilter#getId()
	 */
	@Override
	public String getId() {
		return IDENTIFIER;
	}

	/* (non-Javadoc)
	 * @see fr.toutatice.portail.cms.nuxeo.api.forms.FormFilter#getLabelKey()
	 */
	@Override
	public String getLabelKey() {
		return LABEL_KEY;
	}

	/* (non-Javadoc)
	 * @see fr.toutatice.portail.cms.nuxeo.api.forms.FormFilter#getDescriptionKey()
	 */
	@Override
	public String getDescriptionKey() {
		return DESCRIPTION_KEY;
	}

	/* (non-Javadoc)
	 * @see fr.toutatice.portail.cms.nuxeo.api.forms.FormFilter#getParameters()
	 */
	@Override
	public Map<String, FormFilterParameterType> getParameters() {
		return null;
	}

	/* (non-Javadoc)
	 * @see fr.toutatice.portail.cms.nuxeo.api.forms.FormFilter#hasChildren()
	 */
	@Override
	public boolean hasChildren() {
		return false;
	}

	/* (non-Javadoc)
	 * @see fr.toutatice.portail.cms.nuxeo.api.forms.FormFilter#execute(fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterContext, fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterExecutor)
	 */
	@Override
	public void execute(FormFilterContext context, FormFilterExecutor executor)
			throws FormFilterException, PortalException {
		
		service.checkAccountValidity(context, executor);
		
	}

}
