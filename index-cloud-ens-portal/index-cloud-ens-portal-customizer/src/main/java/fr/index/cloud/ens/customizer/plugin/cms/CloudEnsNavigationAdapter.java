package fr.index.cloud.ens.customizer.plugin.cms;

import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.domain.INavigationAdapterModule;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.portal.core.controller.ControllerContext;
import org.jboss.portal.core.model.portal.Page;
import org.osivia.portal.api.cms.EcmDocument;
import org.osivia.portal.api.cms.IVirtualNavigationService;
import org.osivia.portal.api.cms.Symlink;
import org.osivia.portal.api.cms.Symlinks;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.locator.Locator;
import org.osivia.portal.core.cms.*;
import org.osivia.portal.core.context.ControllerContextAdapter;
import org.osivia.portal.core.portalobjects.PortalObjectUtils;

import javax.portlet.PortletContext;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Customized navigation adapter.
 *
 * @author Cédric Krommenhoek
 * @see INavigationAdapterModule
 */
public class CloudEnsNavigationAdapter implements INavigationAdapterModule {

    /**
     * Portlet context.
     */
    private final PortletContext portletContext;

    /**
     * Log.
     */
    private final Log log;

    /**
     * CMS service locator.
     */
    private final ICMSServiceLocator cmsServiceLocator;
    /**
     * Virtual navigation service.
     */
    private final IVirtualNavigationService virtualNavigationService;


    /**
     * Constructor.
     *
     * @param portletContext portlet context
     */
    public CloudEnsNavigationAdapter(PortletContext portletContext) {
        super();
        this.portletContext = portletContext;

        // Log
        this.log = LogFactory.getLog(this.getClass());

        // CMS service locator
        this.cmsServiceLocator = Locator.findMBean(ICMSServiceLocator.class, ICMSServiceLocator.MBEAN_NAME);
        // Virtual navigation service
        this.virtualNavigationService = Locator.findMBean(IVirtualNavigationService.class, IVirtualNavigationService.MBEAN_NAME);
    }


    @Override
    public String adaptNavigationPath(PortalControllerContext portalControllerContext, EcmDocument document) {
        return null;
    }


    @Override
    public Symlinks getSymlinks(PortalControllerContext portalControllerContext) throws CMSException {
        // Controller context
        ControllerContext controllerContext = ControllerContextAdapter.getControllerContext(portalControllerContext);
        // HTTP servlet request
        HttpServletRequest httpServletRequest = controllerContext.getServerInvocation().getServerContext().getClientRequest();

        // Current page
        Page page = PortalObjectUtils.getPage(controllerContext);
        // Base path
        String basePath = page.getProperty("osivia.cms.basePath");

        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(this.portletContext);


        // Symlinks
        Symlinks symlinks = null;

        if (StringUtils.isNotEmpty(basePath)) {
            // CMS service
            ICMSService cmsService = this.cmsServiceLocator.getCMSService();
            // CMS context
            CMSServiceCtx cmsContext = new CMSServiceCtx();
            cmsContext.setPortalControllerContext(portalControllerContext);

            // User workspace
            CMSItem userWorkspace;
            try {
                List<CMSItem> userWorkspaces = cmsService.getWorkspaces(cmsContext, true, false);
                if ((userWorkspaces != null) && (userWorkspaces.size() == 1)) {
                    userWorkspace = userWorkspaces.get(0);
                } else {
                    userWorkspace = null;
                }
            } catch (CMSException e) {
                userWorkspace = null;
                this.log.error("Unable to get user workspaces.", e.fillInStackTrace());
            }

            if ((userWorkspace != null) && StringUtils.equals(basePath, userWorkspace.getCmsPath())) {
                symlinks = new Symlinks();
                List<Symlink> links = new ArrayList<>();
                symlinks.setLinks(links);

                Symlink recentItemsSymlink = this.virtualNavigationService.createSymlink(basePath, "recent-items", "/default-domain/workspaces/admin/recent-items", "recent-items");
                symlinks.getLinks().add(recentItemsSymlink);
            }
        }


        return symlinks;
    }


    @Override
    public void adaptNavigationItem(PortalControllerContext portalControllerContext, CMSItem navigationItem) {

    }

}
