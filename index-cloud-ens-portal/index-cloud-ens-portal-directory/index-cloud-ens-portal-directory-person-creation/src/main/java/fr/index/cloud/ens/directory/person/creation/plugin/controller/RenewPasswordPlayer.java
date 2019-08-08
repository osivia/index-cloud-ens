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

import fr.index.cloud.ens.directory.person.renew.portlet.controller.RenewPasswordForm.RenewPasswordStep;
import fr.index.cloud.ens.directory.person.renew.portlet.controller.RenewPasswordPortletController;
import fr.index.cloud.ens.directory.person.renew.portlet.service.RenewPasswordService;
import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoDocumentContext;
import fr.toutatice.portail.cms.nuxeo.api.player.INuxeoPlayerModule;

/**
 * @author Loïc Billon
 *
 */
public class RenewPasswordPlayer implements INuxeoPlayerModule {

    /** Forum portlet instance. */
    private static final String PORTLET_INSTANCE = "cloudens-person-renew-pwd-portlet-instance";
 

    /**
     * Constructor.
     * 
     * @param portletContext portlet context
     */
    public RenewPasswordPlayer(PortletContext portletContext) {
        super();
    }


    /**
     * Get forum player.
     *
     * @param documentContext Nuxeo document context
     * @return forum player
     */
    private Player getPortletPlayer(NuxeoDocumentContext documentContext, String mail) {
        Document document = documentContext.getDocument();

        // Window properties
        Map<String, String> properties = new HashMap<>();
        properties.put(Constants.WINDOW_PROP_URI, document.getPath());
        properties.put("osivia.hideDecorators", "1");
        properties.put("osivia.hideTitle", "1");
        properties.put(DynaRenderOptions.PARTIAL_REFRESH_ENABLED, String.valueOf(true));
        properties.put("osivia.ajaxLink", "1");
        properties.put(RenewPasswordPortletController.VIEW_WINDOW_PROPERTY, RenewPasswordStep.PASSWORD.name());
        properties.put("mail",mail);
        
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
            
            if( procMap!= null && RenewPasswordService.MODEL_ID.equals(procMap.get("pi:procedureModelWebId"))) {
            	
            	PropertyMap variables = (PropertyMap) procMap.get("pi:globalVariablesValues");
            	String mail = (String) variables.get("mail");
            	
                return this.getPortletPlayer(documentContext, mail);
            }
        } 
        return null;
    }
}
