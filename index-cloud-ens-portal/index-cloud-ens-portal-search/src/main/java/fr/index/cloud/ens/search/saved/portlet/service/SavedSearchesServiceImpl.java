package fr.index.cloud.ens.search.saved.portlet.service;

import fr.index.cloud.ens.search.common.portlet.service.SearchCommonServiceImpl;
import fr.index.cloud.ens.search.saved.portlet.model.SavedSearchesForm;
import fr.index.cloud.ens.search.saved.portlet.model.comparator.SavedSearchOrderComparator;
import fr.index.cloud.ens.search.saved.portlet.repository.SavedSearchesRepository;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.urls.IPortalUrlFactory;
import org.osivia.portal.api.user.UserSavedSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import java.util.*;

/**
 * Saved searches portlet service implementation.
 *
 * @author Cédric Krommenhoek
 * @see SearchCommonServiceImpl
 * @see SavedSearchesService
 */
@Service
public class SavedSearchesServiceImpl extends SearchCommonServiceImpl implements SavedSearchesService {

    /**
     * Application context.
     */
    @Autowired
    private ApplicationContext applicationContext;

    /**
     * Portlet repository.
     */
    @Autowired
    private SavedSearchesRepository repository;

    /**
     * Saved search order comparator.
     */
    @Autowired
    private SavedSearchOrderComparator savedSearchOrderComparator;

    /**
     * Portal URL factory.
     */
    @Autowired
    private IPortalUrlFactory portalUrlFactory;


    /**
     * Constructor.
     */
    public SavedSearchesServiceImpl() {
        super();
    }


    @Override
    public SavedSearchesForm getForm(PortalControllerContext portalControllerContext) throws PortletException {
        // Form
        SavedSearchesForm form = this.applicationContext.getBean(SavedSearchesForm.class);

        // Saved searches
        List<UserSavedSearch> savedSearches = this.repository.getSavedSearches(portalControllerContext);
        if (CollectionUtils.isNotEmpty(savedSearches)) {
            Collections.sort(savedSearches, this.savedSearchOrderComparator);
        }
        form.setSavedSearches(savedSearches);

        return form;
    }


    @Override
    public String renderView(PortalControllerContext portalControllerContext) throws PortletException {
        // Portlet request
        PortletRequest request = portalControllerContext.getRequest();

        // Form
        SavedSearchesForm form = this.getForm(portalControllerContext);

        // Empty response
        if (CollectionUtils.isEmpty(form.getSavedSearches())) {
            request.setAttribute("osivia.emptyResponse", "1");
        }

        return "view";
    }


    @Override
    public String getSavedSearchUrl(PortalControllerContext portalControllerContext, SavedSearchesForm form, int id) throws PortletException {
        // Saved searches
        List<UserSavedSearch> savedSearches = form.getSavedSearches();

        // Saved search
        UserSavedSearch savedSearch = null;
        if (CollectionUtils.isNotEmpty(savedSearches)) {
            Iterator<UserSavedSearch> iterator = savedSearches.iterator();
            while ((savedSearch == null) && iterator.hasNext()) {
                UserSavedSearch item = iterator.next();
                if (id == item.getId()) {
                    savedSearch = item;
                }
            }
        }

        // Search path
        String path = this.repository.getSearchPath(portalControllerContext);

        // URL
        String url;
        if ((savedSearch == null) || StringUtils.isEmpty(path)) {
            url = null;
        } else {
            // Selectors
            String selectors = savedSearch.getData();

            // Page parameters
            Map<String, String> parameters = new HashMap<>(1);
            if (StringUtils.isNotEmpty(selectors)) {
                parameters.put(SELECTORS_PARAMETER, selectors);
            }

            // CMS URL
            url = this.portalUrlFactory.getCMSUrl(portalControllerContext, null, path, parameters, null, null, null, null,
                    null, null);
        }

        return url;
    }

}
