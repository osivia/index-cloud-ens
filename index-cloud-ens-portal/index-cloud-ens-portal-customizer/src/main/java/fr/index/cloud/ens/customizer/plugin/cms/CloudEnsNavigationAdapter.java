package fr.index.cloud.ens.customizer.plugin.cms;

import java.util.ArrayList;
import java.util.List;

import javax.portlet.PortletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osivia.portal.api.cms.EcmDocument;
import org.osivia.portal.api.cms.IVirtualNavigationService;
import org.osivia.portal.api.cms.Symlink;
import org.osivia.portal.api.cms.Symlinks;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.locator.Locator;
import org.osivia.portal.core.cms.CMSException;
import org.osivia.portal.core.cms.CMSItem;
import org.osivia.portal.core.cms.CMSServiceCtx;
import org.osivia.portal.core.cms.ICMSService;
import org.osivia.portal.core.cms.ICMSServiceLocator;

import fr.toutatice.portail.cms.nuxeo.api.domain.INavigationAdapterModule;

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
        // Symlinks
        Symlinks symlinks = new Symlinks();

        // CMS service
        ICMSService cmsService = this.cmsServiceLocator.getCMSService();
        // CMS context
        CMSServiceCtx cmsContext = new CMSServiceCtx();
        cmsContext.setPortalControllerContext(portalControllerContext);

        // User workspace
        CMSItem userWorkspace;
        try {
            userWorkspace = cmsService.getUserWorkspace(cmsContext);
        } catch (CMSException e) {
            userWorkspace = null;
            this.log.error("Unable to get user workspaces.", e);
        }

        if (userWorkspace != null) {
            symlinks = new Symlinks();
            List<Symlink> links = new ArrayList<>();
            symlinks.setLinks(links);
            
            // Virtual staples
            Symlink recentItemsSymlink = this.virtualNavigationService.createSymlink(userWorkspace.getCmsPath(), null, "/default-domain/workspaces/configuration/recent-items",null);
            symlinks.getLinks().add(recentItemsSymlink);
            Symlink searchSymlink = this.virtualNavigationService.createSymlink(userWorkspace.getCmsPath(), null, "/default-domain/workspaces/configuration/search",null);
            symlinks.getLinks().add(searchSymlink);
        }

        symlinks.getPaths().add("/default-domain/workspaces/configuration");

        return symlinks;
    }




    @Override
    public void adaptNavigationItem(PortalControllerContext portalControllerContext, CMSItem navigationItem) {
        if (navigationItem.getType() != null && "Workspace".equals(navigationItem.getType().getName()) ) {
            // Virtual staples are not implemented in partial loading mode
            navigationItem.getProperties().put("partialLoading", "0");
        }
    }

}
