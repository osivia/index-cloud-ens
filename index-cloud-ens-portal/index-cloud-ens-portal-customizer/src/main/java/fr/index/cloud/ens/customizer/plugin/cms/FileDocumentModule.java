package fr.index.cloud.ens.customizer.plugin.cms;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.portlet.PortletModule;
import org.osivia.portal.api.Constants;
import org.osivia.portal.api.windows.PortalWindow;
import org.osivia.portal.api.windows.WindowFactory;
import org.osivia.portal.core.page.PageProperties;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletContext;

/**
 * File document module.
 *
 * @author Cédric Krommenhoek
 * @see PortletModule
 */
public class FileDocumentModule extends PortletModule {

    /**
     * Portlet context.
     */
    private final PortletContext portletContext;


    /**
     * Constructor.
     *
     * @param portletContext portlet context
     */
    public FileDocumentModule(PortletContext portletContext) {
        super(portletContext);
        this.portletContext = portletContext;
    }


    @Override
    protected void processAction(ActionRequest request, ActionResponse response, PortletContext portletContext) {
        // Action name
        String action = request.getParameter(ActionRequest.ACTION_NAME);

        if ("mutualize".equals(action)) {
            // Nuxeo controller
            NuxeoController nuxeoController = new NuxeoController(request, response, this.portletContext);
            // Current window
            PortalWindow window = WindowFactory.getWindow(request);

            // Document path
            String path = nuxeoController.getComputedPath(window.getProperty(Constants.WINDOW_PROP_URI));

            // Nuxeo command
            INuxeoCommand command = new MutualizeDocumentCommand(path);
            nuxeoController.executeNuxeoCommand(command);

            // Refresh
            PageProperties.getProperties().setRefreshingPage(true);
        }
    }

}
