package fr.index.cloud.ens.selectors.common.portlet.service;

import fr.toutatice.portail.cms.nuxeo.api.PageSelectors;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.core.page.PageProperties;

import javax.portlet.ActionResponse;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import java.util.List;
import java.util.Map;

/**
 * File browser filter common service abstract super-class.
 */
public abstract class AbstractFileBrowserFilterServiceImpl {

    /**
     * Selectors parameter.
     */
    private static final String SELECTORS_PARAMETER = "selectors";


    /**
     * Constructor.
     */
    protected AbstractFileBrowserFilterServiceImpl() {
        super();
    }


    /**
     * Get selectors.
     *
     * @param portalControllerContext portal controller context
     * @return selectors
     */
    protected Map<String, List<String>> getSelectors(PortalControllerContext portalControllerContext) {
        // Portlet request
        PortletRequest request = portalControllerContext.getRequest();

        return PageSelectors.decodeProperties(request.getParameter(SELECTORS_PARAMETER));
    }


    /**
     * Set selectors.
     *
     * @param portalControllerContext portal controller context
     * @param selectors               selectors
     */
    protected void setSelectors(PortalControllerContext portalControllerContext, Map<String, List<String>> selectors) {
        // Portlet response
        PortletResponse response = portalControllerContext.getResponse();
        // Action response
        ActionResponse actionResponse;
        if (response instanceof ActionResponse) {
            actionResponse = (ActionResponse) response;
        } else {
            actionResponse = null;
        }

        if (actionResponse != null) {
            actionResponse.setRenderParameter(SELECTORS_PARAMETER, PageSelectors.encodeProperties(selectors));
        }

        // Refresh other portlet model attributes
        PageProperties.getProperties().setRefreshingPage(true);
    }

}
