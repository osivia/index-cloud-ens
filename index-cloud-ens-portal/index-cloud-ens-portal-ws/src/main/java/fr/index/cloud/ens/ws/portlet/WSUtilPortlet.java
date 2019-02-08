package fr.index.cloud.ens.ws.portlet;

import javax.portlet.PortletConfig;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;

import fr.index.cloud.ens.ws.CloudRestController;
import fr.toutatice.portail.cms.nuxeo.api.CMSPortlet;

public class WSUtilPortlet extends CMSPortlet {

    @Override
    public void init(PortletConfig config) throws PortletException {
        super.init(config);

        // Portlet context
        PortletContext portletContext = getPortletContext();

        CloudRestController.portletContext = portletContext;
    }
}
