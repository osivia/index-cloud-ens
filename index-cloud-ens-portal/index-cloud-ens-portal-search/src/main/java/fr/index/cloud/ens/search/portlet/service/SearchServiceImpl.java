package fr.index.cloud.ens.search.portlet.service;

import fr.index.cloud.ens.search.options.portlet.service.SearchOptionsService;
import fr.index.cloud.ens.search.portlet.model.SearchForm;
import fr.index.cloud.ens.search.portlet.model.SearchView;
import fr.index.cloud.ens.search.portlet.model.SearchWindowProperties;
import fr.index.cloud.ens.search.portlet.repository.SearchRepository;
import org.apache.commons.lang.StringUtils;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.page.PageParametersEncoder;
import org.osivia.portal.api.urls.IPortalUrlFactory;
import org.osivia.portal.api.urls.PortalUrlType;
import org.osivia.portal.api.windows.PortalWindow;
import org.osivia.portal.api.windows.WindowFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Search portlet service implementation.
 *
 * @author Cédric Krommenhoek
 * @see SearchService
 */
@Service
public class SearchServiceImpl implements SearchService {

    /**
     * Selectors parameter.
     */
    private static final String SELECTORS_PARAMETER = "selectors";

    /**
     * Search view prefix.
     */
    private static final String VIEW_PREFIX = "view-";


    /**
     * Application context.
     */
    @Autowired
    private ApplicationContext applicationContext;

    /**
     * Portlet repository.
     */
    @Autowired
    private SearchRepository repository;

    /**
     * Portal URL factory.
     */
    @Autowired
    private IPortalUrlFactory portalUrlFactory;


    @Override
    public SearchWindowProperties getWindowProperties(PortalControllerContext portalControllerContext) {
        // Window
        PortalWindow window = WindowFactory.getWindow(portalControllerContext.getRequest());

        // Window properties
        SearchWindowProperties windowProperties = this.applicationContext.getBean(SearchWindowProperties.class);

        // Search view
        SearchView view = SearchView.fromId(window.getProperty(VIEW_WINDOW_PROPERTY));
        windowProperties.setView(view);

        return windowProperties;
    }


    @Override
    public void setWindowProperties(PortalControllerContext portalControllerContext, SearchWindowProperties windowProperties) {
        // Window
        PortalWindow window = WindowFactory.getWindow(portalControllerContext.getRequest());

        // Search view
        SearchView view = windowProperties.getView();
        if (view == null) {
            view = SearchView.DEFAULT;
        }
        window.setProperty(VIEW_WINDOW_PROPERTY, view.getId());
    }


    @Override
    public SearchForm getForm(PortalControllerContext portalControllerContext) {
        // Window properties
        SearchWindowProperties windowProperties = this.getWindowProperties(portalControllerContext);

        // Search form
        SearchForm form = this.applicationContext.getBean(SearchForm.class);

        // Search view
        SearchView view = windowProperties.getView();
        form.setView(view);

        return form;
    }


    @Override
    public String getViewPath(PortalControllerContext portalControllerContext) throws PortletException {
        // Window properties
        SearchWindowProperties windowProperties = this.getWindowProperties(portalControllerContext);

        return VIEW_PREFIX + windowProperties.getView().getId();
    }


    @Override
    public String getOptionsUrl(PortalControllerContext portalControllerContext) throws PortletException {
        // Portlet request
        PortletRequest request = portalControllerContext.getRequest();

        // Current window properties
        SearchWindowProperties currentWindowProperties = this.getWindowProperties(portalControllerContext);

        // Search options window properties
        Map<String, String> properties = new HashMap<>(1);

        if (SearchView.BUTTON.equals(currentWindowProperties.getView())) {
            // Selectors
            String selectors = request.getParameter(SELECTORS_PARAMETER);
            if (StringUtils.isNotEmpty(selectors)) {
                properties.put(SearchOptionsService.SELECTORS_WINDOW_PROPERTY, selectors);
            }
        } else {
            // Navigation path
            String navigationPath = this.repository.getNavigationPath(portalControllerContext);
            if (StringUtils.isNotEmpty(navigationPath)) {
                properties.put(SearchOptionsService.NAVIGATION_PATH_WINDOW_PROPERTY, navigationPath);
            }
        }

        // URL
        String url;
        try {
            url = this.portalUrlFactory.getStartPortletUrl(portalControllerContext, SearchOptionsService.PORTLET_INSTANCE, properties, PortalUrlType.MODAL);
        } catch (PortalException e) {
            throw new PortletException(e);
        }

        return url;
    }


    @Override
    public String getSearchRedirectionUrl(PortalControllerContext portalControllerContext, SearchForm form) throws PortletException {
        // Search path
        String path = this.repository.getSearchPath(portalControllerContext);

        // Search URL
        String url;
        if (StringUtils.isEmpty(path)) {
            url = null;
        } else {
            // Selectors
            Map<String, List<String>> selectors = new HashMap<>();
            // Location
            String navigationPath = this.repository.getNavigationPath(portalControllerContext);
            if (StringUtils.isNotEmpty(navigationPath)) {
                selectors.put("location", Collections.singletonList(navigationPath));
            }
            // Search
            String search = form.getValue();
            if (StringUtils.isNotEmpty(search)) {
                selectors.put("search", Collections.singletonList(search));
            }

            // Page parameters
            Map<String, String> parameters = new HashMap<>(1);
            parameters.put("selectors", PageParametersEncoder.encodeProperties(selectors));

            // CMS URL
            url = this.portalUrlFactory.getCMSUrl(portalControllerContext, null, path, parameters, null, null, null, null,
                    null, null);
        }

        return url;
    }

}
