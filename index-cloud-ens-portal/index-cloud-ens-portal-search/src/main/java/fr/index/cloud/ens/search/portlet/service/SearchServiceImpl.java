package fr.index.cloud.ens.search.portlet.service;

import fr.index.cloud.ens.search.common.portlet.service.SearchCommonServiceImpl;
import fr.index.cloud.ens.search.filters.portlet.service.SearchFiltersService;
import fr.index.cloud.ens.search.portlet.model.SearchForm;
import fr.index.cloud.ens.search.portlet.model.SearchView;
import fr.index.cloud.ens.search.portlet.model.SearchWindowProperties;
import fr.index.cloud.ens.search.portlet.repository.SearchRepository;
import fr.toutatice.portail.cms.nuxeo.api.PageSelectors;
import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.PropertyList;
import org.osivia.portal.api.Constants;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.page.PageParametersEncoder;
import org.osivia.portal.api.urls.IPortalUrlFactory;
import org.osivia.portal.api.urls.PortalUrlType;
import org.osivia.portal.api.windows.PortalWindow;
import org.osivia.portal.api.windows.WindowFactory;
import org.osivia.portal.core.page.PageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.portlet.ActionResponse;
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
 * @see SearchCommonServiceImpl
 * @see SearchService
 */
@Service
public class SearchServiceImpl extends SearchCommonServiceImpl implements SearchService {

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


    /**
     * Constructor.
     */
    public SearchServiceImpl() {
        super();
    }


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

        // Portlet request
        PortletRequest request = portalControllerContext.getRequest();

        // Search form
        SearchForm form = this.applicationContext.getBean(SearchForm.class);

        // Search view
        SearchView view = windowProperties.getView();
        form.setView(view);


        // Search folder name
        Document document = this.repository.getDocument(portalControllerContext);
        if (document != null) {
            PropertyList facets = document.getFacets();
            if (facets.list().contains("Folderish")) {
                form.setFolderName(document.getTitle());
            }
        }

        if (SearchView.AUTOSUBMIT.equals(view)) {
            // Selectors
            Map<String, List<String>> selectors = PageSelectors.decodeProperties(request.getParameter(SELECTORS_PARAMETER));

            String keywords = getSelectorValue(selectors, KEYWORDS_SELECTOR_ID);
            form.setValue(keywords);
        }

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
        Map<String, String> properties = new HashMap<>();
        properties.put(SearchFiltersService.MODAL_WINDOW_PROPERTY, String.valueOf(true));

        if (SearchView.BUTTON.equals(currentWindowProperties.getView()) || SearchView.AUTOSUBMIT.equals(currentWindowProperties.getView())) {
            // Selectors
            String selectors = request.getParameter(SELECTORS_PARAMETER);
            if (StringUtils.isNotEmpty(selectors)) {
                properties.put(SearchFiltersService.SELECTORS_WINDOW_PROPERTY, selectors);
            }
        } else if (SearchView.INPUT.equals(currentWindowProperties.getView())) {
            // Navigation path
            String navigationPath = this.repository.getNavigationPath(portalControllerContext);
            if (StringUtils.isNotEmpty(navigationPath)) {
                properties.put(SearchFiltersService.NAVIGATION_PATH_WINDOW_PROPERTY, navigationPath);
            }
        }

        // URL
        String url;
        try {
            url = this.portalUrlFactory.getStartPortletUrl(portalControllerContext, SearchFiltersService.PORTLET_INSTANCE, properties, PortalUrlType.MODAL);
        } catch (PortalException e) {
            throw new PortletException(e);
        }

        return url;
    }


    @Override
    public String getSearchRedirectionUrl(PortalControllerContext portalControllerContext, SearchForm form) throws PortletException {
        // Portlet request
        PortletRequest request = portalControllerContext.getRequest();
        // Action response
        ActionResponse response = (ActionResponse) portalControllerContext.getResponse();

        // Current window properties
        SearchWindowProperties currentWindowProperties = this.getWindowProperties(portalControllerContext);

        // Search path
        String path;
        if (SearchView.INPUT.equals(currentWindowProperties.getView())) {
            path = this.repository.getSearchPath(portalControllerContext);
        } else if (SearchView.BUTTON.equals(currentWindowProperties.getView())) {
            path = this.repository.getSearchFiltersPath(portalControllerContext);
        } else {
            path = null;
        }


        // Search URL
        String url;

        if (SearchView.INPUT.equals(currentWindowProperties.getView()) || SearchView.BUTTON.equals(currentWindowProperties.getView())) {
            if (StringUtils.isEmpty(path)) {
                url = null;
            } else {
                // Selectors
                Map<String, List<String>> selectors;
                if (form == null) {
                    selectors = PageSelectors.decodeProperties(request.getParameter(SELECTORS_PARAMETER));
                } else {
                    selectors = new HashMap<>();

                    // Location
                    String navigationPath = this.repository.getNavigationPath(portalControllerContext);
                    if (StringUtils.isNotEmpty(navigationPath)) {
                        selectors.put("location", Collections.singletonList(navigationPath));
                    }

                    // Search
                    String search = form.getValue();
                    if (StringUtils.isNotEmpty(search)) {
                        selectors.put(KEYWORDS_SELECTOR_ID, Collections.singletonList(search));
                    }
                }

                // Page parameters
                Map<String, String> parameters = new HashMap<>(1);
                parameters.put("selectors", PageParametersEncoder.encodeProperties(selectors));

                // CMS URL
                url = this.portalUrlFactory.getCMSUrl(portalControllerContext, null, path, parameters, null, null, null, null, null, null);
            }
        } else if (SearchView.AUTOSUBMIT.equals(currentWindowProperties.getView())) {
            url = null;

            // Selectors
            String selectorsParameter = request.getParameter(SELECTORS_PARAMETER);
            if (StringUtils.isNotEmpty(selectorsParameter)) {
                response.setRenderParameter("lastSelectors", selectorsParameter);
            }
            Map<String, List<String>> selectors = PageSelectors.decodeProperties(selectorsParameter);

            // Search
            String search;
            if (form == null) {
                search = null;
            } else {
                search = form.getValue();
            }
            if (StringUtils.isNotEmpty(search)) {
                selectors.put(KEYWORDS_SELECTOR_ID, Collections.singletonList(search));
            }

            response.setRenderParameter("selectors", PageSelectors.encodeProperties(selectors));

            // Refresh other portlet model attributes
            PageProperties.getProperties().setRefreshingPage(true);

            request.setAttribute("osivia.ajax.preventRefresh", Constants.PORTLET_VALUE_ACTIVATE);
        } else {
            throw new PortletException("Unknown search view.");
        }

        return url;
    }

}
