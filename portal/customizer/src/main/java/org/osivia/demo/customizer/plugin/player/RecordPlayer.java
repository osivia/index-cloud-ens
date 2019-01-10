package org.osivia.demo.customizer.plugin.player;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.jboss.portal.core.controller.ControllerContext;
import org.jboss.portal.core.model.portal.Portal;
import org.jboss.portal.theme.impl.render.dynamic.DynaRenderOptions;
import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.Constants;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.player.Player;
import org.osivia.portal.core.constants.InternalConstants;
import org.osivia.portal.core.context.ControllerContextAdapter;
import org.osivia.portal.core.portalobjects.PortalObjectUtils;

import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoDocumentContext;
import fr.toutatice.portail.cms.nuxeo.api.player.INuxeoPlayerModule;

/**
 * Record player.
 * 
 * @author Cédric Krommenhoek
 * @see INuxeoPlayerModule
 */
public class RecordPlayer implements INuxeoPlayerModule {

    /** View document portlet instance. */
    private static final String VIEW_DOCUMENT_PORTLET_INSTANCE = "toutatice-portail-cms-nuxeo-viewDocumentPortletInstance";


    /**
     * Constructor.
     */
    public RecordPlayer() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Player getCMSPlayer(NuxeoDocumentContext documentContext) {
        Player player;

        if ((documentContext == null) || (documentContext.getDocument() == null)) {
            player = null;
        } else {
            // Nuxeo document
            Document document = documentContext.getDocument();

            if (StringUtils.equals("Record", document.getType()) && StringUtils.equals("record_news", document.getString("rcd:procedureModelWebId"))) {
                // Portal controller context
                PortalControllerContext portalControllerContext = documentContext.getPortalControllerContext();
                // Controller context
                ControllerContext controllerContext = ControllerContextAdapter.getControllerContext(portalControllerContext);
                // Current portal
                Portal portal = PortalObjectUtils.getPortal(controllerContext);

                if (PortalObjectUtils.isSpaceSite(portal)) {
                    player = new Player();

                    // Instance
                    player.setPortletInstance(VIEW_DOCUMENT_PORTLET_INSTANCE);

                    // Window properties
                    Map<String, String> properties = new HashMap<>();
                    properties.put(Constants.WINDOW_PROP_URI, documentContext.getCmsPath());
                    properties.put(Constants.WINDOW_PROP_VERSION, documentContext.getDocumentState().toString());
                    properties.put(DynaRenderOptions.PARTIAL_REFRESH_ENABLED, String.valueOf(true));
                    properties.put("osivia.ajaxLink", "1");
                    properties.put(InternalConstants.METADATA_WINDOW_PROPERTY, "1");
                    player.setWindowProperties(properties);
                } else {
                    player = null;
                }
            } else {
                player = null;
            }
        }

        return player;
    }

}
