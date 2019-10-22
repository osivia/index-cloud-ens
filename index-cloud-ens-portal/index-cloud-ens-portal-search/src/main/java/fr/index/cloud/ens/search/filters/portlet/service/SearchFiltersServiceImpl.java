package fr.index.cloud.ens.search.filters.portlet.service;

import fr.index.cloud.ens.search.common.portlet.service.SearchCommonServiceImpl;
import fr.index.cloud.ens.search.filters.location.portlet.service.SearchFiltersLocationService;
import fr.index.cloud.ens.search.filters.portlet.model.*;
import fr.index.cloud.ens.search.filters.portlet.repository.SearchFiltersRepository;
import fr.toutatice.portail.cms.nuxeo.api.PageSelectors;
import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoDocumentContext;
import fr.toutatice.portail.cms.nuxeo.api.domain.DocumentDTO;
import fr.toutatice.portail.cms.nuxeo.api.services.dao.DocumentDAO;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.CharEncoding;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.osivia.directory.v2.model.preferences.UserPreferences;
import org.osivia.directory.v2.model.preferences.UserSavedSearch;
import org.osivia.directory.v2.service.preferences.UserPreferencesService;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
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
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Search filters portlet service implementation.
 *
 * @author Cédric Krommenhoek
 * @see SearchCommonServiceImpl
 * @see SearchFiltersService
 */
@Service
public class SearchFiltersServiceImpl extends SearchCommonServiceImpl implements SearchFiltersService {

    /**
     * Unit factor.
     */
    private static final double UNIT_FACTOR = 1024;


    /**
     * Date format.
     */
    private final DateFormat dateFormat;

    /**
     * Application context.
     */
    @Autowired
    private ApplicationContext applicationContext;

    /**
     * Portlet repository.
     */
    @Autowired
    private SearchFiltersRepository repository;

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

    /**
     * User preferences service.
     */
    @Autowired
    private UserPreferencesService userPreferencesService;


    /**
     * Constructor.
     */
    public SearchFiltersServiceImpl() {
        super();

        // Date format
        this.dateFormat = new SimpleDateFormat(DateFormatUtils.ISO_DATE_FORMAT.getPattern());
        this.dateFormat.setTimeZone(DateUtils.UTC_TIME_ZONE);
    }


    @Override
    public SearchFiltersForm getForm(PortalControllerContext portalControllerContext) throws PortletException {
        // Portlet request
        PortletRequest request = portalControllerContext.getRequest();
        // Window
        PortalWindow window = WindowFactory.getWindow(request);

        // Form
        SearchFiltersForm form = this.applicationContext.getBean(SearchFiltersForm.class);

        // Modal indicator
        boolean modal = BooleanUtils.toBoolean(window.getProperty(MODAL_WINDOW_PROPERTY));
        form.setModal(modal);

        // Selectors
        Map<String, List<String>> selectors;
        if (modal) {
            selectors = PageSelectors.decodeProperties(window.getProperty(SELECTORS_WINDOW_PROPERTY));
        } else {
            selectors = PageSelectors.decodeProperties(request.getParameter(SELECTORS_PARAMETER));
        }

        // Level
        String level = this.getSelectorValue(selectors, LEVEL_SELECTOR_ID);
        form.setLevel(level);

        // Subject
        String subject = this.getSelectorValue(selectors, SUBJECT_SELECTOR_ID);
        form.setSubject(subject);

        // Location
        String navigationPath = window.getProperty(NAVIGATION_PATH_WINDOW_PROPERTY);
        if (StringUtils.isEmpty(navigationPath)) {
            navigationPath = this.getSelectorValue(selectors, LOCATION_SELECTOR_ID);
        }
        form.setLocationPath(navigationPath);
        this.updateLocation(portalControllerContext, form);

        // Keywords
        String keywords = this.getSelectorValue(selectors, KEYWORDS_SELECTOR_ID);
        form.setKeywords(keywords);

        // Size range
        String sizeRangeSelector = this.getSelectorValue(selectors, SIZE_RANGE_SELECTOR_ID);
        SearchFiltersSizeRange sizeRange;
        if (StringUtils.isEmpty(sizeRangeSelector)) {
            sizeRange = SearchFiltersSizeRange.DEFAULT;
        } else {
            sizeRange = SearchFiltersSizeRange.valueOf(sizeRangeSelector);
        }
        form.setSizeRange(sizeRange);

        // Size amount
        String sizeAmountSelector = this.getSelectorValue(selectors, SIZE_AMOUNT_SELECTOR_ID);
        Float sizeAmout;
        if (StringUtils.isEmpty(sizeAmountSelector)) {
            sizeAmout = null;
        } else {
            sizeAmout = NumberUtils.toFloat(sizeAmountSelector);
        }
        form.setSizeAmount(sizeAmout);

        // Size unit
        String sizeUnitSelector = this.getSelectorValue(selectors, SIZE_UNIT_SELECTOR_ID);
        SearchFiltersSizeUnit sizeUnit;
        if (StringUtils.isEmpty(sizeUnitSelector)) {
            sizeUnit = SearchFiltersSizeUnit.DEFAULT;
        } else {
            sizeUnit = SearchFiltersSizeUnit.valueOf(sizeUnitSelector);
        }
        form.setSizeUnit(sizeUnit);

        // Date range
        String dateRangeSelector = this.getSelectorValue(selectors, DATE_RANGE_SELECTOR_ID);
        SearchFiltersDateRange dateRange;
        if (StringUtils.isEmpty(dateRangeSelector)) {
            dateRange = SearchFiltersDateRange.DEFAULT;
        } else {
            dateRange = SearchFiltersDateRange.valueOf(dateRangeSelector);
        }
        form.setDateRange(dateRange);

        // Customized date
        String customizedDateSelector = this.getSelectorValue(selectors, CUSTOMIZED_DATE_SELECTOR_ID);
        Date customizedDate = this.parseDate(customizedDateSelector);
        form.setCustomizedDate(customizedDate);

        return form;
    }


    @Override
    public void updateLocation(PortalControllerContext portalControllerContext, SearchFiltersForm form) throws PortletException {
        // String path
        String path = form.getLocationPath();
        if (StringUtils.isEmpty(path)) {
            // User workspace path
            String userWorkspacePath = this.repository.getUserWorkspacePath(portalControllerContext);

            if (StringUtils.isEmpty(userWorkspacePath)) {
                throw new PortletException("Unable to find user workspace path.");
            } else {
                path = userWorkspacePath + "/documents";
            }
        }

        // Document DTO
        DocumentDTO documentDto = this.getDocumentDto(portalControllerContext, path);
        form.setLocation(documentDto);
    }


    /**
     * Get document DTO from path.
     *
     * @param portalControllerContext portal controller context
     * @param path                    document path
     * @return document DTO
     */
    private DocumentDTO getDocumentDto(PortalControllerContext portalControllerContext, String path) throws PortletException {
        // Document context
        NuxeoDocumentContext documentContext = this.repository.getDocumentContext(portalControllerContext, path);

        return this.documentDao.toDTO(documentContext.getDocument());
    }


    @Override
    public String getSearchRedirectionUrl(PortalControllerContext portalControllerContext, SearchFiltersForm form) throws PortletException {
        // Search path
        String path = this.repository.getSearchPath(portalControllerContext);

        return this.getRedirectionUrl(portalControllerContext, form, path);
    }


    @Override
    public String saveSearch(PortalControllerContext portalControllerContext, SearchFiltersForm form) throws PortletException {
        if (StringUtils.isNotBlank(form.getSavedSearchDisplayName())) {
            // User preferences
            UserPreferences userPreferences;
            try {
                userPreferences = this.userPreferencesService.getUserPreferences(portalControllerContext);
            } catch (PortalException e) {
                throw new PortletException(e);
            }
            // Saved searches
            List<UserSavedSearch> savedSearches = userPreferences.getSavedSearches();
            if (CollectionUtils.isEmpty(savedSearches)) {
                savedSearches = new ArrayList<>(1);
            }

            // Search identifier
            int max = 0;
            for (UserSavedSearch savedSearch : savedSearches) {
                max = Math.max(max, savedSearch.getId());
            }
            int id = max + 1;

            // Search data
            String data = this.buildSelectorsParameter(portalControllerContext, form);

            // Saved search
            UserSavedSearch savedSearch;
            try {
                savedSearch = this.userPreferencesService.createUserSavedSearch(portalControllerContext, id);
            } catch (PortalException e) {
                throw new PortletException(e);
            }
            savedSearch.setDisplayName(form.getSavedSearchDisplayName());
            savedSearch.setData(data);
            savedSearches.add(savedSearch);

            // Update user preferences
            userPreferences.setSavedSearches(savedSearches);
            userPreferences.setUpdated(true);
        }

        // Search filters path
        String path = this.repository.getSearchFiltersPath(portalControllerContext);

        return this.getRedirectionUrl(portalControllerContext, form, path);
    }


    /**
     * Get redirection URL.
     *
     * @param portalControllerContext portal controller context
     * @param form                    search filters form
     * @param path                    path
     * @return URL
     */
    private String getRedirectionUrl(PortalControllerContext portalControllerContext, SearchFiltersForm form, String path) {
        // Redirection URL
        String url;

        if (StringUtils.isEmpty(path)) {
            url = null;
        } else {
            // Page parameters
            Map<String, String> parameters = new HashMap<>(1);
            parameters.put(SELECTORS_PARAMETER, this.buildSelectorsParameter(portalControllerContext, form));

            // CMS URL
            url = this.portalUrlFactory.getCMSUrl(portalControllerContext, null, path, parameters, null, null, null, null,
                    null, null);
        }

        return url;
    }


    /**
     * Build selectors parameter.
     *
     * @param portalControllerContext portal controller context
     * @param form                    search filters form
     * @return parameter
     */
    private String buildSelectorsParameter(PortalControllerContext portalControllerContext, SearchFiltersForm form) {
        // Selectors
        Map<String, List<String>> selectors = new HashMap<>();

        // Level
        String level = form.getLevel();
        if (StringUtils.isNotEmpty(level)) {
            selectors.put(LEVEL_SELECTOR_ID, Collections.singletonList(level));
        }

        // Subject
        String subject = form.getSubject();
        if (StringUtils.isNotEmpty(subject)) {
            selectors.put(SUBJECT_SELECTOR_ID, Collections.singletonList(subject));
        }

        // Location
        DocumentDTO location = form.getLocation();
        if (location != null) {
            selectors.put(LOCATION_SELECTOR_ID, Collections.singletonList(location.getPath()));
        }

        // Keywords
        String keywords = form.getKeywords();
        if (StringUtils.isNotEmpty(keywords)) {
            selectors.put(KEYWORDS_SELECTOR_ID, Collections.singletonList(keywords));
        }

        // Size range
        selectors.put(SIZE_RANGE_SELECTOR_ID, Collections.singletonList(form.getSizeRange().name()));

        // Size amount
        Float sizeAmount = form.getSizeAmount();
        if (sizeAmount != null) {
            selectors.put(SIZE_AMOUNT_SELECTOR_ID, Collections.singletonList(sizeAmount.toString()));
        }

        // Size unit
        SearchFiltersSizeUnit sizeUnit = form.getSizeUnit();
        selectors.put(SIZE_UNIT_SELECTOR_ID, Collections.singletonList(sizeUnit.name()));

        // Computed size
        if (sizeAmount != null) {
            Long computedSize = Math.round(new Double(sizeAmount) * Math.pow(UNIT_FACTOR, sizeUnit.getFactor()));
            selectors.put(COMPUTED_SIZE_SELECTOR_ID, Collections.singletonList(computedSize.toString()));
        }

        // Date range
        SearchFiltersDateRange dateRange = form.getDateRange();
        selectors.put(DATE_RANGE_SELECTOR_ID, Collections.singletonList(dateRange.name()));

        // Customized date
        Date customizedDate = form.getCustomizedDate();
        if (SearchFiltersDateRange.CUSTOMIZED.equals(dateRange) && (customizedDate != null)) {
            selectors.put(CUSTOMIZED_DATE_SELECTOR_ID, Collections.singletonList(this.formatDate(customizedDate)));
        }

        // Computed date
        Date computedDate = this.getComputedDate(portalControllerContext, form);
        if (computedDate != null) {
            selectors.put(COMPUTED_DATE_SELECTOR_ID, Collections.singletonList(this.formatDate(computedDate)));
        }

        return PageParametersEncoder.encodeProperties(selectors);
    }


    /**
     * Get computed date.
     *
     * @param portalControllerContext portal controller context
     * @param form                    search filters form
     * @return date
     */
    private Date getComputedDate(PortalControllerContext portalControllerContext, SearchFiltersForm form) {
        // Date range
        SearchFiltersDateRange dateRange = form.getDateRange();

        // Computed date
        Date computedDate;
        if (SearchFiltersDateRange.CUSTOMIZED.equals(dateRange)) {
            computedDate = form.getCustomizedDate();
        } else if (dateRange.getOffset() != null) {
            // Current date
            Date currentDate = DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH);

            computedDate = DateUtils.addDays(currentDate, dateRange.getOffset());
        } else {
            computedDate = null;
        }

        return computedDate;
    }


    @Override
    public JSONArray loadVocabulary(PortalControllerContext portalControllerContext, SearchFiltersVocabulary vocabulary, String filter) throws PortletException, IOException {
        // Portlet request
        PortletRequest request = portalControllerContext.getRequest();
        // Internationalization bundle
        Bundle bundle = this.bundleFactory.getBundle(request.getLocale());

        // Vocabulary JSON array
        JSONArray array;
        if (vocabulary == null) {
            array = null;
        } else {
            array = this.repository.loadVocabulary(portalControllerContext, vocabulary.getVocabularyName());
        }

        // Select2 results
        JSONArray results;
        if ((array == null) || (array.isEmpty())) {
            results = new JSONArray();
        } else {
            results = this.parseVocabulary(array, filter);

            // All
            JSONObject object = new JSONObject();
            object.put("id", StringUtils.EMPTY);
            object.put("text", bundle.getString(vocabulary.getAllKey()));
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
        Map<String, SearchFiltersVocabularyItem> items = new HashMap<>(jsonArray.size());
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

            SearchFiltersVocabularyItem item = items.get(key);
            if (item == null) {
                item = this.applicationContext.getBean(SearchFiltersVocabularyItem.class, key);
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

                SearchFiltersVocabularyItem parentItem = items.get(parent);
                if (parentItem == null) {
                    parentItem = this.applicationContext.getBean(SearchFiltersVocabularyItem.class, parent);
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
    private void generateVocabularyChildren(Map<String, SearchFiltersVocabularyItem> items, JSONArray jsonArray, Set<String> children, boolean optgroup, int level,
                                            String parentId) throws UnsupportedEncodingException {
        for (String child : children) {
            SearchFiltersVocabularyItem item = items.get(child);
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


    @Override
    public String getLocationUrl(PortalControllerContext portalControllerContext) throws PortletException {
        // URL
        String url;
        try {
            url = this.portalUrlFactory.getStartPortletUrl(portalControllerContext, SearchFiltersLocationService.PORTLET_INSTANCE, null, PortalUrlType.MODAL);
        } catch (PortalException e) {
            throw new PortletException(e);
        }

        return url;
    }


    @Override
    public String formatDate(Date date) {
        String result;
        if (date == null) {
            result = null;
        } else {
            result = this.dateFormat.format(date);
        }
        return result;
    }


    @Override
    public Date parseDate(String source) {
        Date date;
        if (StringUtils.isEmpty(source)) {
            date = null;
        } else {
            try {
                date = this.dateFormat.parse(source);
            } catch (ParseException e) {
                date = null;
            }
        }
        return date;
    }

}
