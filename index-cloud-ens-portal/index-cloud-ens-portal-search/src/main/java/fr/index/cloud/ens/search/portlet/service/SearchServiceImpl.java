package fr.index.cloud.ens.search.portlet.service;

import fr.index.cloud.ens.search.options.portlet.service.SearchOptionsService;
import fr.index.cloud.ens.search.portlet.model.SearchForm;
import fr.index.cloud.ens.search.portlet.model.SearchView;
import fr.index.cloud.ens.search.portlet.model.SearchWindowProperties;
import fr.index.cloud.ens.search.portlet.repository.SearchRepository;
import fr.toutatice.portail.cms.nuxeo.api.PageSelectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
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
import javax.portlet.PortletResponse;

import java.util.ArrayList;
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
     * Keywords selector identifier.
     */
    public static final String KEYWORDS_SELECTOR_ID = "search";


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

    /**
     * Get selector value.
     *
     * @param selectors  selectors
     * @param selectorId selector identifier
     * @return value, may be null
     */
    public  String getSelectorValue(Map<String, List<String>> selectors, String selectorId) {
        String value;
        if (MapUtils.isEmpty(selectors)) {
            value = null;
        } else {
            List<String> values = selectors.get(selectorId);
            if (CollectionUtils.isEmpty(values)) {
                value = null;
            } else {
                value = values.get(0);
            }
        }
        return value;
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

        if (SearchView.BUTTON.equals(currentWindowProperties.getView()) || SearchView.AUTOSUBMIT.equals(currentWindowProperties.getView())) {
            // Selectors
            String selectors = request.getParameter(SELECTORS_PARAMETER);
            if (StringUtils.isNotEmpty(selectors)) {
                properties.put(SearchOptionsService.SELECTORS_WINDOW_PROPERTY, selectors);
            }
        } else if (SearchView.INPUT.equals(currentWindowProperties.getView())) {
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

        // Portlet request
        PortletRequest request = portalControllerContext.getRequest();
        
        // Portlet request
        ActionResponse response = (ActionResponse) portalControllerContext.getResponse();        
        
        // Current window properties
        SearchWindowProperties currentWindowProperties = this.getWindowProperties(portalControllerContext);

        // Search URL
        String url;

        if (SearchView.INPUT.equals(currentWindowProperties.getView()) || SearchView.BUTTON.equals(currentWindowProperties.getView())) {
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
                url = this.portalUrlFactory.getCMSUrl(portalControllerContext, null, path, parameters, null, null, null, null, null, null);
            }
        }   else    {
            // AUTOSUBMIT : render parameters
            url = null;
            
            Map<String, List<String>> selectors = PageSelectors.decodeProperties(request.getParameter(SELECTORS_PARAMETER));

            List<String> keywords = selectors.get(KEYWORDS_SELECTOR_ID);
            if (keywords == null) {
                keywords = new ArrayList<String>();
                selectors.put(KEYWORDS_SELECTOR_ID, keywords);
            }

            String search = form.getValue();

            if (StringUtils.isNotBlank(search)) {
                keywords.clear();
                keywords.add(search);
            }
            
            if( request.getParameter("selectors")!= null) {
                response.setRenderParameter("lastSelectors", request.getParameter("selectors"));
            }  
            
            
            response.setRenderParameter("selectors", PageSelectors.encodeProperties(selectors));

            // Refresh other portlet model attributes
            PageProperties.getProperties().setRefreshingPage(true);
            
            request.setAttribute("osivia.ajax.preventRefresh", Constants.PORTLET_VALUE_ACTIVATE);
            
        }


        return url;
    }


}
