/**
 * 
 */
package fr.index.cloud.ens.directory.person.creation.plugin.form;

import java.util.HashMap;
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
 * @author loic
 *
 */
@Component
public class VerifyMailAddressFormFilter implements FormFilter {


    /** Form filter identifier. */
    public static final String IDENTIFIER = "PERSON_VERIFY_MAIL";	
	
    /** Form filter label internationalization key. */
    private static final String LABEL_INTERNATIONALIZATION_KEY = "PERSON_VERIFY_MAIL_LABEL";
    /** Form filter description internationalization key. */
    private static final String DESCRIPTION_INTERNATIONALIZATION_KEY = "PERSON_VERIFY_MAIL_DESCRIPTION";

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
		return LABEL_INTERNATIONALIZATION_KEY;
	}

	/* (non-Javadoc)
	 * @see fr.toutatice.portail.cms.nuxeo.api.forms.FormFilter#getDescriptionKey()
	 */
	@Override
	public String getDescriptionKey() {
		return DESCRIPTION_INTERNATIONALIZATION_KEY;
	}

	/* (non-Javadoc)
	 * @see fr.toutatice.portail.cms.nuxeo.api.forms.FormFilter#getParameters()
	 */
	@Override
	public Map<String, FormFilterParameterType> getParameters() {

        Map<String, FormFilterParameterType> parameters = new HashMap<>();
        return parameters;
	}

	/* (non-Javadoc)
	 * @see fr.toutatice.portail.cms.nuxeo.api.forms.FormFilter#hasChildren()
	 */
	@Override
	public boolean hasChildren() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see fr.toutatice.portail.cms.nuxeo.api.forms.FormFilter#execute(fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterContext, fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterExecutor)
	 */
	@Override
	public void execute(FormFilterContext context, FormFilterExecutor executor)
			throws FormFilterException, PortalException {
		
		service.verifyMail(context, executor);
		
	}

}
