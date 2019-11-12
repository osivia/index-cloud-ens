package fr.index.cloud.ens.customizer.project;

import fr.toutatice.portail.cms.nuxeo.api.CMSPortlet;
import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.portal.core.model.portal.Page;
import org.jboss.portal.core.model.portal.Portal;
import org.jboss.portal.core.model.portal.PortalObject;
import org.jboss.portal.core.model.portal.Window;
import org.jboss.portal.theme.impl.render.dynamic.DynaRenderOptions;
import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.directory.v2.model.preferences.UserPreferences;
import org.osivia.directory.v2.service.preferences.UserPreferencesService;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.customization.*;
import org.osivia.portal.api.directory.v2.DirServiceFactory;
import org.osivia.portal.api.directory.v2.model.Person;
import org.osivia.portal.api.directory.v2.service.PersonService;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.internationalization.IInternationalizationService;
import org.osivia.portal.api.locator.Locator;
import org.osivia.portal.api.taskbar.ITaskbarService;
import org.osivia.portal.api.urls.IPortalUrlFactory;
import org.osivia.portal.core.cms.*;
import org.osivia.portal.core.constants.InternalConstants;

import javax.portlet.PortletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Project customizer.
 *
 * @author Cédric Krommenhoek
 * @see CMSPortlet
 * @see ICustomizationModule
 */
public class ProjectCustomizer extends CMSPortlet implements ICustomizationModule {

    /**
     * Customizer name.
     */
    private static final String CUSTOMIZER_NAME = "demo.customizer.project";
    /**
     * Customization modules repository attribute name.
     */
    private static final String ATTRIBUTE_CUSTOMIZATION_MODULES_REPOSITORY = "CustomizationModulesRepository";

    /**
     * First connection indicator window property name.
     */
    private static final String FIRST_CONNECTION_INDICATOR_PROPERTY = "first-connection";

    /**
     * CGU level attribute.
     */
    private static final String CGU_LEVEL_ATTRIBUTE = "osivia.services.cgu.level";
    /**
     * CGU path attribute.
     */
    private static final String CGU_PATH_ATTRIBUTE = "osivia.services.cgu.path";

    /**
     * Marker for set up the platform (data injection)
     */
    private static final String PLATFORM_INITIALIZED = "osivia.platform.initialized";
    private static final String INIT_INDICATOR_PROPERTY = "init-indicator";

    /**
     * Portal URL factory.
     */
    private final IPortalUrlFactory portalUrlFactory;
    /**
     * CMS service locator.
     */
    private final ICMSServiceLocator cmsServiceLocator;
    /**
     * Person service.
     */
    private final PersonService personService;
    /**
     * Internationalization bundle factory.
     */
    private final IBundleFactory bundleFactory;
    /** Taskbar service. */
    private final ITaskbarService taskbarService;
    /** User preferences service. */
    private final UserPreferencesService userPreferencesService;


    /**
     * Customization module metadatas.
     */
    private final CustomizationModuleMetadatas metadatas;
    /**
     * Log.
     */
    private final Log log;
    /**
     * Customization modules repository.
     */
    private ICustomizationModulesRepository repository;


    /**
     * Constructor.
     */
    public ProjectCustomizer() {
        super();

        // Portal URL factory
        this.portalUrlFactory = Locator.findMBean(IPortalUrlFactory.class, IPortalUrlFactory.MBEAN_NAME);
        // CMS service locator
        this.cmsServiceLocator = Locator.findMBean(ICMSServiceLocator.class, ICMSServiceLocator.MBEAN_NAME);
        // Person service
        this.personService = DirServiceFactory.getService(PersonService.class);
        // Internationalization bundle factory
        IInternationalizationService internationalizationService = Locator.findMBean(IInternationalizationService.class,
                IInternationalizationService.MBEAN_NAME);
        this.bundleFactory = internationalizationService.getBundleFactory(this.getClass().getClassLoader());
        // Taskbar service
        this.taskbarService = Locator.findMBean(ITaskbarService.class, ITaskbarService.MBEAN_NAME);
        // User preferences service
        this.userPreferencesService = DirServiceFactory.getService(UserPreferencesService.class);

        // Logs
        this.log = LogFactory.getLog(this.getClass());

        // Customization module metadata
        this.metadatas = new CustomizationModuleMetadatas();
        this.metadatas.setName(CUSTOMIZER_NAME);
        this.metadatas.setModule(this);
        this.metadatas.setCustomizationIDs(Arrays.asList(IProjectCustomizationConfiguration.CUSTOMIZER_ID));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void init() throws PortletException {
        this.repository = (ICustomizationModulesRepository) this.getPortletContext().getAttribute(ATTRIBUTE_CUSTOMIZATION_MODULES_REPOSITORY);
        this.repository.register(this.metadatas);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void destroy() {
        this.repository.unregister(this.metadatas);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void customize(CustomizationContext customizationContext) {
        // Portal controller context
        PortalControllerContext portalControllerContext = customizationContext.getPortalControllerContext();
        // Customization attributes
        Map<String, Object> attributes = customizationContext.getAttributes();
        // Project customization configuration
        IProjectCustomizationConfiguration configuration = (IProjectCustomizationConfiguration) attributes
                .get(IProjectCustomizationConfiguration.CUSTOMIZER_ATTRIBUTE_CONFIGURATION);
        // HTTP servlet request
        HttpServletRequest servletRequest = configuration.getHttpServletRequest();
        // Principal
        Principal principal = servletRequest.getUserPrincipal();
        // Bundle
        Bundle bundle = this.bundleFactory.getBundle(customizationContext.getLocale());

        if (configuration.isBeforeInvocation()) {
            this.preparePlatformRedirection(portalControllerContext, configuration, bundle);

            if ((principal != null)) {
                this.firstConnectionRedirection(portalControllerContext, configuration, principal, bundle);

                if (StringUtils.isNotEmpty(configuration.getCMSPath())) {
                    this.userWorkspaceHomeRedirection(portalControllerContext, configuration);
                    this.cguRedirection(portalControllerContext, configuration, principal, bundle);
                }
            }
        }
    }


    /**
     * Interceptor used to prepare the platform data
     *
     * @param portalControllerContext
     * @param configuration
     * @param bundle
     */
    private void preparePlatformRedirection(PortalControllerContext portalControllerContext, IProjectCustomizationConfiguration configuration, Bundle bundle) {
        // Page
        Page page = configuration.getPage();

        if (page != null && !"admin".equals(page.getPortal().getName())) {
            Portal portal = page.getPortal();

            PortalObject intranet = portal.getParent().getChild("default");

            // Test init flag
            String flag = intranet.getProperty(PLATFORM_INITIALIZED);
            Window window = page.getChild("virtual", Window.class);

            // Prevent loops
            if ((window == null) || !BooleanUtils.toBoolean(window.getDeclaredProperty(INIT_INDICATOR_PROPERTY))) {
                String reqHost = portalControllerContext.getHttpServletRequest().getServerName();

                if (flag == null) {
                    // Set initialization flag
                    intranet.setDeclaredProperty(PLATFORM_INITIALIZED, "1");

                    // HTTP servlet request
                    HttpServletRequest servletRequest = configuration.getHttpServletRequest();
                    // HTTP session
                    HttpSession session = servletRequest.getSession();
                    session.setAttribute("osivia.platform.init.pathToRedirect", configuration.buildRestorableURL());

                    // Page display name
                    String displayName = bundle.getString("PLATFORM_INIT");

                    // Window properties

                    Map<String, String> properties = new HashMap<>();
                    properties.put(InternalConstants.PROP_WINDOW_TITLE, displayName);
                    properties.put("osivia.ajaxLink", "1");
                    properties.put("osivia.hideTitle", "1");

                    if (servletRequest.getParameter("noajax") != null)
                        properties.put("noajax", "1");

                    properties.put(DynaRenderOptions.PARTIAL_REFRESH_ENABLED, String.valueOf(true));
                    properties.put(INIT_INDICATOR_PROPERTY, String.valueOf(true));

                    // Redirection URL
                    String redirectionUrl;
                    try {
                        redirectionUrl = this.portalUrlFactory.getStartPortletInNewPage(portalControllerContext, "platform-init", displayName,
                                "index-cloud-ens-initializer-instance", properties, null);
                    } catch (PortalException e) {
                        throw new RuntimeException(e);
                    }

                    configuration.setRedirectionURL(redirectionUrl);
                }
            }
        }
    }


    /**
     * First connection redirection.
     *
     * @param portalControllerContext portal controller context
     * @param configuration           project customization configuration
     * @param principal               user principal
     * @param bundle                  internationalization bundle
     */
    private void firstConnectionRedirection(PortalControllerContext portalControllerContext, IProjectCustomizationConfiguration configuration,
                                            Principal principal, Bundle bundle) {
        // Person
        Person person = this.personService.getPerson(principal.getName());

        if ((person != null) && StringUtils.isBlank(person.getDisplayName())) {
            // Page
            Page page = configuration.getPage();
            // Window
            Window window;
            if (page == null) {
                window = null;
            } else {
                window = page.getChild("virtual", Window.class);
            }

            // Prevent loop on first connection portlet
            if ((window == null) || !BooleanUtils.toBoolean(window.getDeclaredProperty(FIRST_CONNECTION_INDICATOR_PROPERTY))) {
                // Page display name
                String displayName = bundle.getString("FIRST_CONNECTION_TITLE");

                // Window properties
                Map<String, String> properties = new HashMap<>();
                properties.put(InternalConstants.PROP_WINDOW_TITLE, displayName);
                properties.put("osivia.ajaxLink", "1");
                properties.put(DynaRenderOptions.PARTIAL_REFRESH_ENABLED, String.valueOf(true));
                properties.put(FIRST_CONNECTION_INDICATOR_PROPERTY, String.valueOf(true));
                properties.put("osivia.services.firstConnection.redirectionUrl", StringEscapeUtils.escapeHtml(configuration.buildRestorableURL()));

                // Redirection URL
                String redirectionUrl;
                try {
                    redirectionUrl = this.portalUrlFactory.getStartPortletInNewPage(portalControllerContext, "first-connection", displayName,
                            "osivia-services-first-connection-instance", properties, null);
                } catch (PortalException e) {
                    throw new RuntimeException(e);
                }

                configuration.setRedirectionURL(redirectionUrl);
            }
        }
    }


    /**
     * User workspace home redirection.
     *
     * @param portalControllerContext portal controller context
     * @param configuration           project customizer configuration
     */
    private void userWorkspaceHomeRedirection(PortalControllerContext portalControllerContext, IProjectCustomizationConfiguration configuration) {
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

        if (userWorkspace != null && StringUtils.startsWith(configuration.getCMSPath(), userWorkspace.getCmsPath())) {
            // Active task identifier
            String activeId;
            try {
                activeId = this.taskbarService.getActiveId(portalControllerContext);
            } catch (PortalException e) {
                activeId = null;
                this.log.error("Unable to get active task identifier.", e);
            }

            if (StringUtils.equals(ITaskbarService.HOME_TASK_ID, activeId)) {
                String documentsPath = userWorkspace.getCmsPath() + "/documents";
                String url = this.portalUrlFactory.getCMSUrl(portalControllerContext, null, documentsPath, null, null, null, null, null, null, null);
                configuration.setRedirectionURL(url);
            }
        }
    }


    /**
     * CGU redirection.
     *
     * @param portalControllerContext portal controller context
     * @param configuration           project customization configuration
     * @param principal               user principal
     * @param bundle                  internationalization bundle
     */
    private void cguRedirection(PortalControllerContext portalControllerContext, IProjectCustomizationConfiguration configuration, Principal principal,
                                Bundle bundle) {
        // Page
        Page page = configuration.getPage();

        if (page != null) {
            // Window
            Window window = page.getChild("virtual", Window.class);

            // HTTP servlet request
            HttpServletRequest servletRequest = configuration.getHttpServletRequest();
            // HTTP session
            HttpSession session = servletRequest.getSession();

            // Nuxeo controller
            NuxeoController nuxeoController = new NuxeoController(this.getPortletContext());
            nuxeoController.setServletRequest(servletRequest);

            // CGU path
            String path = page.getProperty(CGU_PATH_ATTRIBUTE);
            // Portal level
            String portalLevel = page.getProperty(CGU_LEVEL_ATTRIBUTE);

            // Is CGU defined ?
            if ((portalLevel == null) || (path == null)) {
                return;
            }

            // CGU already checked (in session) ?
            String checkedLevel = String.valueOf(session.getAttribute(CGU_LEVEL_ATTRIBUTE));
            if (StringUtils.equals(portalLevel, checkedLevel)) {
                return;
            }

            // No CGU request on CGU !!!
            if ((window != null) && StringUtils.isNotEmpty(window.getDeclaredProperty(CGU_PATH_ATTRIBUTE))) {
                return;
            }

            // User preferences
            UserPreferences userPreferences;
            try {
                userPreferences = this.userPreferencesService.getUserPreferences(portalControllerContext);
            } catch (PortalException e) {
                this.log.error(e.getMessage());
                userPreferences = null;
            }

            // User level
            String userLevel;
            if (userPreferences == null) {
                userLevel = null;
            } else {
                userLevel = userPreferences.getTermsOfService();
                session.setAttribute(CGU_LEVEL_ATTRIBUTE, userLevel);
            }

            if (!portalLevel.equals(userLevel)) {
                session.setAttribute("osivia.services.cgu.pathToRedirect", configuration.buildRestorableURL());

                // Window properties
                Map<String, String> properties = new HashMap<String, String>();
                properties.put(CGU_PATH_ATTRIBUTE, path);
                properties.put(CGU_LEVEL_ATTRIBUTE, portalLevel);
                properties.put("osivia.title", bundle.getString("CGU_TITLE"));
                properties.put("osivia.hideTitle", "1");
                // Redirection URL
                String redirectionUrl;
                try {
                    redirectionUrl = this.portalUrlFactory.getStartPortletInNewPage(portalControllerContext, "cgu", bundle.getString("CGU_TITLE_MINI"),
                            "osivia-services-cgu-portailPortletInstance", properties, null);
                } catch (PortalException e) {
                    throw new RuntimeException(e);
                }

                configuration.setRedirectionURL(redirectionUrl);
            }
        }
    }

}
