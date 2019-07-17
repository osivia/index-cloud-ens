package fr.index.cloud.ens.search.options.portlet.service;

import fr.index.cloud.ens.search.options.portlet.model.SearchOptionsForm;
import fr.index.cloud.ens.search.options.portlet.model.SearchOptionsVocabularyItem;
import fr.index.cloud.ens.search.options.portlet.repository.SearchOptionsRepository;
import fr.toutatice.portail.cms.nuxeo.api.PageSelectors;
import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoDocumentContext;
import fr.toutatice.portail.cms.nuxeo.api.domain.DocumentDTO;
import fr.toutatice.portail.cms.nuxeo.api.services.dao.DocumentDAO;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.CharEncoding;
import org.apache.commons.lang.StringUtils;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.page.PageParametersEncoder;
import org.osivia.portal.api.urls.IPortalUrlFactory;
import org.osivia.portal.api.windows.PortalWindow;
import org.osivia.portal.api.windows.WindowFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.Normalizer;
import java.util.*;

/**
 * Search options portlet service implementation.
 *
 * @author Cédric Krommenhoek
 * @see SearchOptionsService
 */
@Service
public class SearchOptionsServiceImpl implements SearchOptionsService {

    /**
     * Selectors parameter.
     */
    private static final String SELECTORS_PARAMETER = "selectors";

    /**
     * Location selector identifier.
     */
    private static final String LOCATION_SELECTOR_ID = "location";
    /**
     * Level selector identifier.
     */
    private static final String LEVEL_SELECTOR_ID = "level";
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
    private SearchOptionsRepository repository;

    /**
     * Portal URL factory.
     */
    @Autowired
    private IPortalUrlFactory portalUrlFactory;

    /**
     * Internationalization bundle factory.
     */
    @Autowired
    private IBundleFactory bundleFactory;

    /**
     * Document DAO.
     */
    @Autowired
    private DocumentDAO documentDao;


    @Override
    public SearchOptionsForm getForm(PortalControllerContext portalControllerContext) throws PortletException {
        // Window
        PortalWindow window = WindowFactory.getWindow(portalControllerContext.getRequest());

        // Form
        SearchOptionsForm form = this.applicationContext.getBean(SearchOptionsForm.class);

        // Selectors
        Map<String, List<String>> selectors = PageSelectors.decodeProperties(window.getProperty(SELECTORS_WINDOW_PROPERTY));

        // Location
        String navigationPath = window.getProperty(NAVIGATION_PATH_WINDOW_PROPERTY);
        if (StringUtils.isEmpty(navigationPath)) {
            navigationPath = this.getSelectorValue(selectors, LOCATION_SELECTOR_ID);
        }
        if (StringUtils.isNotEmpty(navigationPath)) {
            NuxeoDocumentContext documentContext = this.repository.getDocumentContext(portalControllerContext, navigationPath);
            DocumentDTO documentDto = this.documentDao.toDTO(documentContext.getDocument());
            form.setLocation(documentDto);
        }

        // Level
        String level = this.getSelectorValue(selectors, LEVEL_SELECTOR_ID);
        form.setLevel(level);

        // Keywords
        String keywords = this.getSelectorValue(selectors, KEYWORDS_SELECTOR_ID);
        form.setKeywords(keywords);

        return form;
    }


    /**
     * Get selector value.
     *
     * @param selectors  selectors
     * @param selectorId selector identifier
     * @return value, may be null
     */
    public static String getSelectorValue(Map<String, List<String>> selectors, String selectorId) {
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
    public String getSearchRedirectionUrl(PortalControllerContext portalControllerContext, SearchOptionsForm form) throws PortletException {
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
            DocumentDTO location = form.getLocation();
            if (location != null) {
                selectors.put(LOCATION_SELECTOR_ID, Collections.singletonList(location.getPath()));
            }
            // Level
            String level = form.getLevel();
            if (StringUtils.isNotEmpty(level)) {
                selectors.put(LEVEL_SELECTOR_ID, Collections.singletonList(level));
            }
            // Keywords
            String keywords = form.getKeywords();
            if (StringUtils.isNotEmpty(keywords)) {
                selectors.put(KEYWORDS_SELECTOR_ID, Collections.singletonList(keywords));
            }

            // Page parameters
            Map<String, String> parameters = new HashMap<>(1);
            parameters.put(SELECTORS_PARAMETER, PageParametersEncoder.encodeProperties(selectors));

            // CMS URL
            url = this.portalUrlFactory.getCMSUrl(portalControllerContext, null, path, parameters, null, null, null, null,
                    null, null);
        }

        return url;
    }


    @Override
    public void clearLocation(PortalControllerContext portalControllerContext, SearchOptionsForm form) throws PortletException {
        form.setLocation(null);
    }


    @Override
    public JSONArray loadLevels(PortalControllerContext portalControllerContext, String filter) throws PortletException, IOException {
        // Portlet request
        PortletRequest request = portalControllerContext.getRequest();
        // Internationalization bundle
        Bundle bundle = this.bundleFactory.getBundle(request.getLocale());

        // Vocabulary JSON array
        JSONArray array = this.repository.loadVocabulary(portalControllerContext, LEVELS_VOCABULARY);

        // Select2 results
        JSONArray results;
        if ((array == null) || (array.isEmpty())) {
            results = new JSONArray();
        } else {
            results = this.parseVocabulary(array, filter);

            // All
            JSONObject object = new JSONObject();
            object.put("id", StringUtils.EMPTY);
            object.put("text", bundle.getString("SEARCH_OPTIONS_LEVEL_ALL"));
            object.put("optgroup", false);
            object.put("level", 1);
            results.add(0, object);
        }

        return results;
    }


    /**
     * Parse vocabulary JSON array with filter.
     *
     * @param jsonArray JSON array
     * @param filter    filter, may be null
     * @return results
     */
    private JSONArray parseVocabulary(JSONArray jsonArray, String filter) throws IOException {
        Map<String, SearchOptionsVocabularyItem> items = new HashMap<>(jsonArray.size());
        Set<String> rootItems = new LinkedHashSet<>();

        boolean multilevel = false;

        for (Object object : jsonArray) {
            JSONObject jsonObject = (JSONObject) object;

            String key = jsonObject.getString("key");
            String value = jsonObject.getString("value");
            String parent = null;
            if (jsonObject.containsKey("parent")) {
                parent = jsonObject.getString("parent");
            }
            boolean matches = this.matchesVocabularyItem(value, filter);

            SearchOptionsVocabularyItem item = items.get(key);
            if (item == null) {
                item = this.applicationContext.getBean(SearchOptionsVocabularyItem.class, key);
                items.put(key, item);
            }
            item.setValue(value);
            item.setParent(parent);
            if (matches) {
                item.setMatches(true);
                item.setDisplayed(true);
            }

            if (StringUtils.isEmpty(parent)) {
                rootItems.add(key);
            } else {
                multilevel = true;

                SearchOptionsVocabularyItem parentItem = items.get(parent);
                if (parentItem == null) {
                    parentItem = this.applicationContext.getBean(SearchOptionsVocabularyItem.class, parent);
                    items.put(parent, parentItem);
                }
                parentItem.getChildren().add(key);

                if (item.isDisplayed()) {
                    while (parentItem != null) {
                        parentItem.setDisplayed(true);

                        if (StringUtils.isEmpty(parentItem.getParent())) {
                            parentItem = null;
                        } else {
                            parentItem = items.get(parentItem.getParent());
                        }
                    }
                }
            }
        }


        JSONArray results = new JSONArray();
        this.generateVocabularyChildren(items, results, rootItems, multilevel, 1, null);

        return results;
    }


    /**
     * Check if value matches filter.
     *
     * @param value  vocabulary item value
     * @param filter filter
     * @return true if value matches filter
     */
    private boolean matchesVocabularyItem(String value, String filter) throws UnsupportedEncodingException {
        boolean matches = true;

        if (filter != null) {
            // Decoded value
            String decodedValue = URLDecoder.decode(value, CharEncoding.UTF_8);
            // Diacritical value
            String diacriticalValue = Normalizer.normalize(decodedValue, Normalizer.Form.NFD).replaceAll("\\p{IsM}+", StringUtils.EMPTY);

            // Filter
            String[] splittedFilters = StringUtils.split(filter, "*");
            for (String splittedFilter : splittedFilters) {
                // Diacritical filter
                String diacriticalFilter = Normalizer.normalize(splittedFilter, Normalizer.Form.NFD).replaceAll("\\p{IsM}+", StringUtils.EMPTY);

                if (!StringUtils.containsIgnoreCase(diacriticalValue, diacriticalFilter)) {
                    matches = false;
                    break;
                }
            }
        }

        return matches;
    }


    /**
     * Generate vocabulary children.
     *
     * @param items     vocabulary items
     * @param jsonArray results JSON array
     * @param children  children
     * @param optgroup  options group presentation indicator
     * @param level     depth level
     * @param parentId  parent identifier
     */
    private void generateVocabularyChildren(Map<String, SearchOptionsVocabularyItem> items, JSONArray jsonArray, Set<String> children, boolean optgroup, int level,
                                            String parentId) throws UnsupportedEncodingException {
        for (String child : children) {
            SearchOptionsVocabularyItem item = items.get(child);
            if ((item != null) && item.isDisplayed()) {
                // Identifier
                String id;
                if (StringUtils.isEmpty(parentId)) {
                    id = item.getKey();
                } else {
                    id = parentId + "/" + item.getKey();
                }

                JSONObject object = new JSONObject();
                object.put("id", id);
                object.put("text", URLDecoder.decode(item.getValue(), CharEncoding.UTF_8));
                object.put("optgroup", optgroup);
                object.put("level", level);

                if (!item.isMatches()) {
                    object.put("disabled", true);
                }

                jsonArray.add(object);

                if (!item.getChildren().isEmpty()) {
                    this.generateVocabularyChildren(items, jsonArray, item.getChildren(), false, level + 1, id);
                }
            }
        }
    }

}
