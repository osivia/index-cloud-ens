package org.osivia.demo.customizer.plugin.list;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.commons.collections.CollectionUtils;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.demo.customizer.plugin.DemoUtils;

import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.domain.DocumentDTO;
import fr.toutatice.portail.cms.nuxeo.api.portlet.PortletModule;
import net.sf.json.JSONObject;


/**
 * @author Dorian Licois
 */
public class AccessoriesListTemplateModule extends PortletModule {

    private static final String PROCEDURE_COMMANDE_WEBID = "procedure_commande_produit";

    public AccessoriesListTemplateModule(PortletContext portletContext) {
        super(portletContext);
    }

    @Override
    protected void doView(RenderRequest request, RenderResponse response, PortletContext portletContext) throws PortletException, IOException {
        // Documents
        List<DocumentDTO> documents = (List<DocumentDTO>) request.getAttribute("documents");

        if (CollectionUtils.isNotEmpty(documents)) {

            // Nuxeo controller
            NuxeoController nuxeoController = new NuxeoController(request, response, portletContext);

            for (DocumentDTO documentDTO : documents) {

                Map<String, Object> properties = documentDTO.getProperties();
                PropertyMap docProperties = documentDTO.getDocument().getProperties();
                PropertyMap dataMap = docProperties.getMap(DemoUtils.RECORD_PROPERTY_DATA);

                if (dataMap != null) {
                    // title
                    properties.put("title", dataMap.getString(DemoUtils.RECORD_PROPERTY_TITLE));

                    // visuel
                    PropertyMap visuelMap = dataMap.getMap(DemoUtils.ACCESSORY_PROPERTY_VISUEL);
                    if (visuelMap != null) {
                        properties.put("visuelUrl", DemoUtils.getFileUrl(nuxeoController, docProperties, visuelMap.getString("digest"), documentDTO.getPath()));
                        properties.put("visuelFilename", visuelMap.get("fileName"));
                    }

                    // description
                    properties.put("description", dataMap.get(DemoUtils.ACCESSORY_PROPERTY_DESCRIPTION));

                    // prix
                    properties.put("prixht", dataMap.get(DemoUtils.ACCESSORY_PROPERTY_PRIX));

                    // orderUrl
                    properties.put("orderUrl", getOrderUrl(PROCEDURE_COMMANDE_WEBID, nuxeoController, docProperties.getString("ttc:webid")));
                }
            }
        }
    }

    /**
     * Creates the URL to launch the order procedure
     *
     * @param procedureWebid
     * @param nuxeoController
     * @param webid
     * @return
     */
    private String getOrderUrl(String procedureWebid, NuxeoController nuxeoController, String webid) {
        JSONObject variables = new JSONObject();
        variables.put("accessoire", webid);
        return DemoUtils.getLaunchProcedureUrl(nuxeoController, variables, procedureWebid);
    }

}
