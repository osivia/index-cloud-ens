package fr.index.cloud.ens.customizer.attributes;

import fr.toutatice.portail.cms.nuxeo.api.PageSelectors;
import fr.toutatice.portail.cms.nuxeo.api.services.NuxeoConnectionProperties;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.portal.core.controller.ControllerCommand;
import org.jboss.portal.core.controller.ControllerContext;
import org.jboss.portal.core.controller.ControllerException;
import org.jboss.portal.core.model.portal.Page;
import org.jboss.portal.core.model.portal.Portal;
import org.jboss.portal.core.model.portal.PortalObjectPath;
import org.jboss.portal.core.model.portal.command.render.RenderPageCommand;
import org.jboss.portal.core.model.portal.navstate.PageNavigationalState;
import org.jboss.portal.core.navstate.NavigationalStateContext;
import org.jboss.portal.core.theme.PageRendition;
import org.osivia.directory.v2.model.preferences.UserPreferences;
import org.osivia.directory.v2.model.preferences.UserSavedSearch;
import org.osivia.directory.v2.service.preferences.UserPreferencesService;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.directory.v2.DirServiceFactory;
import org.osivia.portal.api.locator.Locator;
import org.osivia.portal.api.theming.IAttributesBundle;
import org.osivia.portal.api.urls.IPortalUrlFactory;
import org.osivia.portal.core.cms.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;
import java.util.*;

/**
 * Customized attributes bundle.
 *
 * @author Cédric Krommenhoek
 * @see IAttributesBundle
 */
public class CustomizedAttributesBundle implements IAttributesBundle {

    /**
     * SSO applications attribute name.
     */
    private static final String APPLICATIONS = "osivia.sso.applications";
    /**
     * My account URL.
     */
    private static final String MY_ACCOUNT_URL = "osivia.my-account.url";
    /**
     * User workspace URL.
     */
    private static final String USER_WORKSPACE_URL = "osivia.userWorkspace.url";
    /**
     * Mutualized space URL.
     */
    private static final String MUTUALIZED_SPACE_URL = "osivia.mutualizedSpace.url";
    /**
     * RGPD URL.
     */
    private static final String RGPD_URL = "osivia.rgpd.url";
    /**
     * Nav items.
     */
    private static final String NAV_ITEMS = "osivia.nav.items";
    /**
     * Index Éducation URL key.
     */
    private static final String INDEX_EDUCATION_URL_KEY = "index-education.url";
    /**
     * Hide first breadcrumb item indicator.
     */
    private static final String HIDE_FIRST_BREADCRUMB_ITEM = "osivia.breadcrumb.hide-first";
    /**
     * Active saved search.
     */
    private static final String ACTIVE_SAVED_SEARCH = "osivia.saved-search.active";


    /**
     * Index Éducation URL value.
     */
    private static final String INDEX_EDUCATION_URL_VALUE = "https://www.index-education.com";

    /**
     * Mutualized space path environment property.
     */
    private static final String MUTUALIZED_SPACE_PATH_PROPERTY = "config.mutualized.path";

    /**
     * Singleton instance.
     */
    private static final IAttributesBundle INSTANCE = new CustomizedAttributesBundle();


    /**
     * Log.
     */
    private final Log log;

    /**
     * Attribute names.
     */
    private final Set<String> names;
    /**
     * SSO applications.
     */
    private final List<String> applications;

    /**
     * Portal URL factory.
     */
    private final IPortalUrlFactory portalUrlFactory;
    /**
     * CMS service locator.
     */
    private final ICMSServiceLocator cmsServiceLocator;
    /**
     * User preferences service.
     */
    private UserPreferencesService userPreferencesService;


    /**
     * Constructor.
     */
    private CustomizedAttributesBundle() {
        super();
        this.log = LogFactory.getLog(this.getClass());

        // Attributes names
        this.names = new HashSet<>();
        this.names.add(APPLICATIONS);
        this.names.add(MY_ACCOUNT_URL);
        this.names.add(USER_WORKSPACE_URL);
        this.names.add(MUTUALIZED_SPACE_URL);
        this.names.add(RGPD_URL);
        this.names.add(NAV_ITEMS);
        this.names.add(INDEX_EDUCATION_URL_KEY);
        this.names.add(HIDE_FIRST_BREADCRUMB_ITEM);
        this.names.add(ACTIVE_SAVED_SEARCH);

        // SSO applications
        this.applications = new ArrayList<>();
        this.applications.add(NuxeoConnectionProperties.getPublicBaseUri().toString().concat("/logout"));
        this.applications.add(System.getProperty("cas.logout"));

        // Portal URL factory
        this.portalUrlFactory = Locator.findMBean(IPortalUrlFactory.class, IPortalUrlFactory.MBEAN_NAME);
        // CMS service locator
        this.cmsServiceLocator = Locator.findMBean(ICMSServiceLocator.class, ICMSServiceLocator.MBEAN_NAME);
        // User preferences service
        this.userPreferencesService = DirServiceFactory.getService(UserPreferencesService.class);
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

        // Current portal
        Portal portal = renderPageCommand.getPortal();
        // Current page
        Page page = renderPageCommand.getPage();
        // Current CMS base path
        String basePath = page.getProperty("osivia.cms.basePath");


        // SSO applications
        attributes.put(APPLICATIONS, this.applications);


        // My account URL
        Page myAccountPage = portal.getChild("mon-compte", Page.class);
        String myAccountUrl;
        if (myAccountPage == null) {
            myAccountUrl = null;
        } else {
            String myAccountPageId = myAccountPage.getId().toString(PortalObjectPath.SAFEST_FORMAT);
            myAccountUrl = this.portalUrlFactory.getViewPageUrl(portalControllerContext, myAccountPageId);
        }
        attributes.put(MY_ACCOUNT_URL, myAccountUrl);


        // User workspace
        CMSItem userWorkspace;
        try {
            userWorkspace = cmsService.getUserWorkspace(cmsContext);
        } catch (CMSException e) {
            userWorkspace = null;
            this.log.error("Unable to get user workspaces.", e.fillInStackTrace());
        }
        // User workspace URL
        String userWorkspaceUrl;
        if ((userWorkspace != null) && StringUtils.isNotEmpty(userWorkspace.getCmsPath())) {
            // User workspace URL
            userWorkspaceUrl = this.portalUrlFactory.getCMSUrl(portalControllerContext, null, userWorkspace.getCmsPath(), null, null, null, null, null,
                    null, null);
        } else {
            userWorkspaceUrl = null;
        }
        attributes.put(USER_WORKSPACE_URL, userWorkspaceUrl);


        // Mutualized space
        String mutualizedSpacePath = System.getProperty(MUTUALIZED_SPACE_PATH_PROPERTY);
        String mutualizedSpaceUrl;
        if (StringUtils.isEmpty(mutualizedSpacePath)) {
            mutualizedSpaceUrl = null;
        } else {
            mutualizedSpaceUrl = this.portalUrlFactory.getCMSUrl(portalControllerContext, null, mutualizedSpacePath, null, null, null, null, null, null, null);
        }
        attributes.put(MUTUALIZED_SPACE_URL, mutualizedSpaceUrl);


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

        // Home
        String homeUrl = controllerContext.getServerInvocation().getServerContext().getPortalContextPath();
        NavItem homeNavItem = new NavItem();
        homeNavItem.setUrl(homeUrl);
        homeNavItem.setIcon("glyphicons glyphicons-basic-home");
        homeNavItem.setKey("TOOLBAR_HOME");
        homeNavItem.setActive(StringUtils.isEmpty(basePath));
        navItems.add(homeNavItem);

        // User workspace nav item
        NavItem userWorkspaceNavItem = new NavItem();
        userWorkspaceNavItem.setUrl(userWorkspaceUrl);
        userWorkspaceNavItem.setIcon("glyphicons glyphicons-basic-user-rounded");
        userWorkspaceNavItem.setKey("TOOLBAR_USER_WORKSPACE");
        userWorkspaceNavItem.setColor("text-cloud-dark");
        userWorkspaceNavItem.setActive((userWorkspace != null) && StringUtils.equals(basePath, userWorkspace.getCmsPath()));
        navItems.add(userWorkspaceNavItem);

        // Mutualized space nav item
        NavItem mutualizedSpaceNavItem = new NavItem();
        mutualizedSpaceNavItem.setUrl(mutualizedSpaceUrl);
        mutualizedSpaceNavItem.setIcon("glyphicons glyphicons-basic-share");
        mutualizedSpaceNavItem.setKey("TOOLBAR_COMMUNITY_WORKSPACE");
        mutualizedSpaceNavItem.setColor("text-mutualized-dark");
        mutualizedSpaceNavItem.setActive(StringUtils.equals(basePath, mutualizedSpacePath));
        navItems.add(mutualizedSpaceNavItem);


        // Index Éducation URL
        attributes.put(INDEX_EDUCATION_URL_KEY, INDEX_EDUCATION_URL_VALUE);


        // Hide first breadcrumb item indicator
        attributes.put(HIDE_FIRST_BREADCRUMB_ITEM, userWorkspaceNavItem.isActive());


        // Active saved search
        NavigationalStateContext nsContext = (NavigationalStateContext) controllerContext.getAttributeResolver(ControllerCommand.NAVIGATIONAL_STATE_SCOPE);
        PageNavigationalState pageState = nsContext.getPageNavigationalState(page.getId().toString());
        Map<String, List<String>> selectors;
        if (pageState == null) {
            selectors = null;
        } else {
            String[] parameter = pageState.getParameter(new QName(XMLConstants.DEFAULT_NS_PREFIX, "selectors"));
            if (ArrayUtils.isEmpty(parameter)) {
                selectors = null;
            } else {
                selectors = PageSelectors.decodeProperties(parameter[0]);
            }
        }
        UserSavedSearch activeSavedSearch = null;
        if (MapUtils.isNotEmpty(selectors)) {
            List<String> values = selectors.get("active-saved-search");
            if (CollectionUtils.isNotEmpty(values)) {
                String value = values.get(0);

                // User preferences
                UserPreferences userPreferences;
                try {
                    userPreferences = this.userPreferencesService.getUserPreferences(portalControllerContext);
                } catch (PortalException e) {
                    this.log.error(e.getMessage(), e.getCause());
                    userPreferences = null;
                }

                // Saved searches
                List<UserSavedSearch> savedSearches;
                if (userPreferences == null) {
                    savedSearches = null;
                } else {
                    savedSearches = userPreferences.getSavedSearches();
                }

                // Active saved search
                if (CollectionUtils.isNotEmpty(savedSearches)) {
                    Iterator<UserSavedSearch> iterator = savedSearches.iterator();
                    while ((activeSavedSearch == null) && iterator.hasNext()) {
                        UserSavedSearch savedSearch = iterator.next();

                        if (StringUtils.equals(value, String.valueOf(savedSearch.getId()))) {
                            activeSavedSearch = savedSearch;
                        }
                    }
                }
            }
        }
        if (activeSavedSearch != null) {
            attributes.put(ACTIVE_SAVED_SEARCH, activeSavedSearch.getDisplayName());
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Set<String> getAttributeNames() {
        return this.names;
    }

}
