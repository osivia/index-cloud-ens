package fr.index.cloud.ens.customizer.plugin.cms;

import fr.toutatice.portail.cms.nuxeo.api.domain.INavigationAdapterModule;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.cms.EcmDocument;
import org.osivia.portal.api.cms.IVirtualNavigationService;
import org.osivia.portal.api.cms.Symlink;
import org.osivia.portal.api.cms.Symlinks;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.internationalization.IInternationalizationService;
import org.osivia.portal.api.locator.Locator;
import org.osivia.portal.core.cms.*;

import javax.portlet.PortletContext;
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
     * Internationalization bundle factory.
     */
    private final IBundleFactory bundleFactory;


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
        // Internationalization bundle factory
        IInternationalizationService internationalizationService = Locator.findMBean(IInternationalizationService.class, IInternationalizationService.MBEAN_NAME);
        this.bundleFactory = internationalizationService.getBundleFactory(this.getClass().getClassLoader());
    }


    @Override
    public String adaptNavigationPath(PortalControllerContext portalControllerContext, EcmDocument document) {
        return null;
    }


    @Override
    public Symlinks getSymlinks(PortalControllerContext portalControllerContext) throws CMSException {
        // Symlinks
        Symlinks symlinks = new Symlinks();

        // User workspace
        CMSItem userWorkspace = this.getUserWorkspace(portalControllerContext);

        if (userWorkspace != null) {
            symlinks = new Symlinks();
            List<Symlink> links = new ArrayList<>();
            symlinks.setLinks(links);

            // Virtual staples
            Symlink recentItemsSymlink = this.virtualNavigationService.createSymlink(userWorkspace.getCmsPath(), null, "/default-domain/workspaces/configuration/recent-items", null);
            symlinks.getLinks().add(recentItemsSymlink);
            Symlink searchSymlink = this.virtualNavigationService.createSymlink(userWorkspace.getCmsPath(), null, "/default-domain/workspaces/configuration/search", null);
            symlinks.getLinks().add(searchSymlink);
        }

        symlinks.getPaths().add("/default-domain/workspaces/configuration");

        return symlinks;
    }


    @Override
    public void adaptNavigationItem(PortalControllerContext portalControllerContext, CMSItem navigationItem) {
        if (navigationItem.getType() != null && "Workspace".equals(navigationItem.getType().getName())) {
            // Virtual staples are not implemented in partial loading mode
            navigationItem.getProperties().put("partialLoading", "0");

            // User workspace
            CMSItem userWorkspace = this.getUserWorkspace(portalControllerContext);
            if ((userWorkspace != null) && StringUtils.equals(navigationItem.getCmsPath(), userWorkspace.getCmsPath())) {
                // Internationalization bundle
                Bundle bundle = this.bundleFactory.getBundle(portalControllerContext.getHttpServletRequest().getLocale());

                // Title
                String title = bundle.getString("TOOLBAR_USER_WORKSPACE");

                // Update navigation item properties
                navigationItem.getProperties().put("displayName", title);
                navigationItem.getProperties().put("title", title);

                // Update navigation item document
                Document document = (Document) navigationItem.getNativeItem();
                document.set("dc:title", title);
            }
        }
    }


    /**
     * Get user workspace.
     *
     * @param portalControllerContext portal controller context
     * @return CMS item, may be null
     */
    private CMSItem getUserWorkspace(PortalControllerContext portalControllerContext) {
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
        return userWorkspace;
    }

}
