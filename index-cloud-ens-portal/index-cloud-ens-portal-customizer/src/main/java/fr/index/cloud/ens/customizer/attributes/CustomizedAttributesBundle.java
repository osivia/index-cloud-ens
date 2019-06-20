package fr.index.cloud.ens.customizer.attributes;

import java.util.*;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.portal.core.controller.ControllerContext;
import org.jboss.portal.core.controller.ControllerException;
import org.jboss.portal.core.model.portal.Page;
import org.jboss.portal.core.model.portal.command.render.RenderPageCommand;
import org.jboss.portal.core.theme.PageRendition;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.locator.Locator;
import org.osivia.portal.api.theming.IAttributesBundle;
import org.osivia.portal.api.urls.IPortalUrlFactory;
import org.osivia.portal.core.cms.CMSException;
import org.osivia.portal.core.cms.CMSItem;
import org.osivia.portal.core.cms.CMSServiceCtx;
import org.osivia.portal.core.cms.ICMSService;
import org.osivia.portal.core.cms.ICMSServiceLocator;

import fr.toutatice.portail.cms.nuxeo.api.services.NuxeoConnectionProperties;
import org.osivia.portal.core.portalobjects.PortalObjectUtils;

/**
 * Customized attributes bundle.
 *
 * @author Cédric Krommenhoek
 * @see IAttributesBundle
 */
public class CustomizedAttributesBundle implements IAttributesBundle {

    /** SSO applications attribute name. */
    private static final String APPLICATIONS = "osivia.sso.applications";
    /** User workspace URL. */
    private static final String USER_WORKSPACE_URL = "osivia.userWorkspace.url";
    /** RGPD URL. */
    private static final String RGPD_URL = "osivia.rgpd.url";
    /** Nav items. */
    private static final String NAV_ITEMS = "osivia.nav.items";

    /** Singleton instance. */
    private static final IAttributesBundle INSTANCE = new CustomizedAttributesBundle();


    /** Log. */
    private final Log log;

    /** Attribute names. */
    private final Set<String> names;
    /** SSO applications. */
    private final List<String> applications;

    /** Portal URL factory. */
    private final IPortalUrlFactory portalUrlFactory;
    /** CMS service locator. */
    private final ICMSServiceLocator cmsServiceLocator;


    /**
     * Constructor.
     */
    private CustomizedAttributesBundle() {
        super();
        this.log = LogFactory.getLog(this.getClass());

        // Attributes names
        this.names = new HashSet<String>();
        this.names.add(APPLICATIONS);
        this.names.add(USER_WORKSPACE_URL);
        this.names.add(RGPD_URL);
        this.names.add(NAV_ITEMS);

        // SSO applications
        this.applications = new ArrayList<String>();
        this.applications.add(NuxeoConnectionProperties.getPublicBaseUri().toString().concat("/logout"));
        this.applications.add(System.getProperty("cas.logout"));
        
        // Portal URL factory
        this.portalUrlFactory = Locator.findMBean(IPortalUrlFactory.class, IPortalUrlFactory.MBEAN_NAME);
        // CMS service locator
        this.cmsServiceLocator = Locator.findMBean(ICMSServiceLocator.class, ICMSServiceLocator.MBEAN_NAME);
    }


    /**
     * Get singleton instance.
     *
     * @return instance
     */
    public static IAttributesBundle getInstance() {
        return INSTANCE;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void fill(RenderPageCommand renderPageCommand, PageRendition pageRendition, Map<String, Object> attributes) throws ControllerException {
        // Controller context
        ControllerContext controllerContext = renderPageCommand.getControllerContext();
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(controllerContext);

        // CMS service
        ICMSService cmsService = this.cmsServiceLocator.getCMSService();
        // CMS context
        CMSServiceCtx cmsContext = new CMSServiceCtx();
        cmsContext.setPortalControllerContext(portalControllerContext);

        // Current page
        Page page = renderPageCommand.getPage();
        // Current CMS base path
        String basePath = page.getProperty("osivia.cms.basePath");


        // SSO applications
        attributes.put(APPLICATIONS, this.applications);


        // User workspace
        CMSItem userWorkspace = null;
        try {
            List<CMSItem> userWorkspaces = cmsService.getWorkspaces(cmsContext, true, false);
            if ((userWorkspaces != null) && (userWorkspaces.size() == 1)) {
                userWorkspace = userWorkspaces.get(0);
            }
        } catch (CMSException e) {
            this.log.error("Unable to get user workspaces.", e.fillInStackTrace());
        }
        // User workspace URL
        String userWorkspaceUrl;
        if ((userWorkspace != null) && StringUtils.isNotEmpty(userWorkspace.getCmsPath())) {
            // User workspace URL
            userWorkspaceUrl = this.portalUrlFactory.getCMSUrl(portalControllerContext, null, userWorkspace.getCmsPath(), null, null, null, null, null,
                    null, null);
            attributes.put(USER_WORKSPACE_URL, userWorkspaceUrl);
        } else {
            userWorkspaceUrl = null;
        }


        // RGPD URL
        String rgpdUrl;
        try {
            Map<String, String> properties = new HashMap<>(0);
            rgpdUrl = this.portalUrlFactory.getStartPageUrl(portalControllerContext, "rgpd", "/default/templates/rgpd", properties, properties);
        } catch (PortalException e) {
            rgpdUrl = null;
            this.log.error("Unable to compute RGPD URL.", e.fillInStackTrace());
        }
        if (StringUtils.isNotEmpty(rgpdUrl)) {
            attributes.put(RGPD_URL, rgpdUrl);
        }


        // Nav items
        List<NavItem> navItems = new ArrayList<>();
        attributes.put(NAV_ITEMS, navItems);

        // User workspace nav item
        NavItem userWorkspaceNavItem = new NavItem();
        userWorkspaceNavItem.setUrl(StringUtils.defaultIfEmpty(userWorkspaceUrl, "#"));
        userWorkspaceNavItem.setIcon("glyphicons glyphicons-basic-user-rounded");
        userWorkspaceNavItem.setKey("TOOLBAR_USER_WORKSPACE");
        userWorkspaceNavItem.setActive((userWorkspace != null) && StringUtils.equals(basePath, userWorkspace.getCmsPath()));
        navItems.add(userWorkspaceNavItem);

        // Community nav item
        NavItem communityNavItem = new NavItem();
        communityNavItem.setUrl("#"); // FIXME
        communityNavItem.setIcon("glyphicons glyphicons-basic-share");
        communityNavItem.setKey("TOOLBAR_COMMUNITY_WORKSPACE");
        communityNavItem.setActive(false); // FIXME
        navItems.add(communityNavItem);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Set<String> getAttributeNames() {
        return this.names;
    }

}
