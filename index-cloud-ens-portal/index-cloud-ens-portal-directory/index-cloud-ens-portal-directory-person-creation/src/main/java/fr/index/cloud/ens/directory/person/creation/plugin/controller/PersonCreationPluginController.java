/**
 * 
 */
package fr.index.cloud.ens.directory.person.creation.plugin.controller;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.portlet.PortletConfig;
import javax.portlet.PortletException;

import org.osivia.portal.api.customization.CustomizationContext;
import org.osivia.portal.api.player.IPlayerModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

import fr.toutatice.portail.cms.nuxeo.api.domain.AbstractPluginPortlet;

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


    /* (non-Javadoc)
     * @see fr.toutatice.portail.cms.nuxeo.api.domain.AbstractPluginPortlet#getOrder()
     */
    @Override
    public int getOrder() {
        // After procedures
        return DEFAULT_DEPLOYMENT_ORDER + 10;
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

        // Players
        List<IPlayerModule> players = this.getPlayers(context);
        CreateAccountPlayer createPlayer = new CreateAccountPlayer(this.getPortletContext());
        players.add(0, createPlayer);
        RenewPasswordPlayer player = new RenewPasswordPlayer(this.getPortletContext());
		players.add(0, player);
	}

}
