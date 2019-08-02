/**
 * 
 */
package fr.index.cloud.ens.directory.person.creation.plugin.controller;

import java.util.HashMap;
import java.util.Map;

import javax.portlet.PortletContext;

import org.jboss.portal.theme.impl.render.dynamic.DynaRenderOptions;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.portal.api.Constants;
import org.osivia.portal.api.player.Player;

import fr.index.cloud.ens.directory.person.creation.portlet.controller.PersonCreationController;
import fr.index.cloud.ens.directory.person.creation.portlet.controller.PersonCreationForm.CreationStep;
import fr.index.cloud.ens.directory.person.creation.portlet.service.PersonCreationService;
import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoDocumentContext;
import fr.toutatice.portail.cms.nuxeo.api.player.INuxeoPlayerModule;

/**
 * @author Loïc Billon
 *
 */
public class CreateAccountPlayer implements INuxeoPlayerModule {

    /** Forum portlet instance. */
    private static final String PORTLET_INSTANCE = "cloudens-person-creation-portlet-instance";
 

    /**
     * Constructor.
     * 
     * @param portletContext portlet context
     */
    public CreateAccountPlayer(PortletContext portletContext) {
        super();
    }


    /**
     * Get forum player.
     *
     * @param documentContext Nuxeo document context
     * @return forum player
     */
    private Player getPortletPlayer(NuxeoDocumentContext documentContext, String uid) {
        Document document = documentContext.getDocument();

        // Window properties
        Map<String, String> properties = new HashMap<>();
        properties.put(Constants.WINDOW_PROP_URI, document.getPath());
        properties.put("osivia.hideDecorators", "1");
        properties.put("osivia.hideTitle", "1");
        properties.put(DynaRenderOptions.PARTIAL_REFRESH_ENABLED, String.valueOf(true));
        properties.put("osivia.ajaxLink", "1");
        properties.put(PersonCreationController.VIEW_WINDOW_PROPERTY, CreationStep.CONFIRM.name());
        properties.put("uid",uid);
        
        // Player
        Player player = new Player();
        player.setWindowProperties(properties);
        player.setPortletInstance(PORTLET_INSTANCE);

        return player;
    }


  

    /**
     * {@inheritDoc}
     */
    @Override
    public Player getCMSPlayer(NuxeoDocumentContext documentContext) {
        Document doc = documentContext.getDocument();

        if ("TaskDoc".equals(doc.getType())) {
            doc = documentContext.getDenormalizedDocument();
            PropertyMap procMap = doc.getProperties().getMap("nt:pi");
            
            if( procMap!= null && PersonCreationService.MODEL_ID.equals(procMap.get("pi:procedureModelWebId"))) {
            	
            	PropertyMap variables = (PropertyMap) procMap.get("pi:globalVariablesValues");
            	String uid = (String) variables.get("uid");
            	
                return this.getPortletPlayer(documentContext, uid);
            }
        } 
        return null;
    }
}
