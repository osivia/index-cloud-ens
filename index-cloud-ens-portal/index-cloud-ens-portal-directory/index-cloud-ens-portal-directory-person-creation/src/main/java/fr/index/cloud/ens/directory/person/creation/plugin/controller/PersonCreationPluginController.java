/**
 * 
 */
package fr.index.cloud.ens.directory.person.creation.plugin.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.portlet.PortletConfig;
import javax.portlet.PortletException;

import org.osivia.portal.api.customization.CustomizationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

import fr.index.cloud.ens.directory.person.creation.plugin.form.CheckAccountValidityFormFilter;
import fr.index.cloud.ens.directory.person.creation.plugin.form.DecodeUserCreationTokenFilter;
import fr.index.cloud.ens.directory.person.creation.plugin.form.PersonCreationFormFilter;
import fr.index.cloud.ens.directory.person.creation.plugin.form.RenewPasswordFormFilter;
import fr.index.cloud.ens.directory.person.creation.plugin.form.VerifyMailAddressFormFilter;
import fr.toutatice.portail.cms.nuxeo.api.domain.AbstractPluginPortlet;
import fr.toutatice.portail.cms.nuxeo.api.domain.FragmentType;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilter;

/**
 * @author Loïc Billon
 *
 */
@Controller
public class PersonCreationPluginController extends AbstractPluginPortlet {


    /** Plugin name. */
	private static final String PLUGIN_NAME = "cloudens-person-creation.plugin";


    /** Portlet config. */
    @Autowired
    private PortletConfig portletConfig;
    
    /** Application context. */
    @Autowired
    private ApplicationContext applicationContext;
	
	/* (non-Javadoc)
	 * @see fr.toutatice.portail.cms.nuxeo.api.domain.AbstractPluginPortlet#getPluginName()
	 */
	@Override
	protected String getPluginName() {
		return PLUGIN_NAME;
	}



    /**
     * Constructor.
     */
    public PersonCreationPluginController() {
        super();
    }


    /**
     * Post-construct.
     *
     * @throws PortletException
     */
    @PostConstruct
    public void postConstruct() throws PortletException {
        super.init(this.portletConfig);
    }


    /**
     * Pre-destroy.
     */
    @PreDestroy
    public void preDestroy() {
        super.destroy();
    }

	
	
	/* (non-Javadoc)
	 * @see fr.toutatice.portail.cms.nuxeo.api.domain.AbstractPluginPortlet#customizeCMSProperties(org.osivia.portal.api.customization.CustomizationContext)
	 */
	@Override
	protected void customizeCMSProperties(CustomizationContext context) {
		
        // Form filters
        Map<String, FormFilter> formFilters = this.getFormFilters(context);
        formFilters.put(PersonCreationFormFilter.IDENTIFIER, this.applicationContext.getBean(PersonCreationFormFilter.class));
        formFilters.put(VerifyMailAddressFormFilter.IDENTIFIER,  this.applicationContext.getBean(VerifyMailAddressFormFilter.class));
		formFilters.put(DecodeUserCreationTokenFilter.IDENTIFIER, this.applicationContext.getBean(DecodeUserCreationTokenFilter.class));
		
		// TODO : inutilisé, remplacé par la portlet renew-password
		formFilters.put(CheckAccountValidityFormFilter.IDENTIFIER , this.applicationContext.getBean(CheckAccountValidityFormFilter.class));
		
		formFilters.put(RenewPasswordFormFilter.IDENTIFIER , this.applicationContext.getBean(RenewPasswordFormFilter.class));

		// Fragments
		List<FragmentType> fragmentTypes = this.getFragmentTypes(context);
		
		SimpleJspFragmentModule cfm = new SimpleJspFragmentModule(getPortletContext(),"information-mail" );
		fragmentTypes.add(new FragmentType("information-mail", "information mail", cfm));
		SimpleJspFragmentModule cfm2 = new SimpleJspFragmentModule(getPortletContext(),"confirmation-mail" );
		fragmentTypes.add(new FragmentType("confirmation-mail", "Confirmation mail", cfm2));
		SimpleJspFragmentModule cfm3 = new SimpleJspFragmentModule(getPortletContext(),"password-renewed" );
		fragmentTypes.add(new FragmentType("password-renewed", "password-renewed", cfm3));
		
	}

}
