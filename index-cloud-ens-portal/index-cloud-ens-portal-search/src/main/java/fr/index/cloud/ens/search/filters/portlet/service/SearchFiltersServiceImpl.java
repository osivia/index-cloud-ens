package fr.index.cloud.ens.search.filters.portlet.service;

import fr.index.cloud.ens.search.common.portlet.service.SearchCommonServiceImpl;
import fr.index.cloud.ens.search.filters.location.portlet.service.SearchFiltersLocationService;
import fr.index.cloud.ens.search.filters.portlet.model.*;
import fr.index.cloud.ens.search.filters.portlet.repository.SearchFiltersRepository;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoException;
import fr.toutatice.portail.cms.nuxeo.api.PageSelectors;
import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoDocumentContext;
import fr.toutatice.portail.cms.nuxeo.api.domain.DocumentDTO;
import fr.toutatice.portail.cms.nuxeo.api.services.dao.DocumentDAO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.osivia.directory.v2.model.preferences.UserPreferences;
import org.osivia.directory.v2.model.preferences.UserSavedSearch;
import org.osivia.directory.v2.service.preferences.UserPreferencesService;
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
import java.text.DateFormat;
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
        // Selectors
        Map<String, List<String>> selectors = PageSelectors.decodeProperties(request.getParameter(SELECTORS_PARAMETER));
        // Navigation path
        String navigationPath = this.repository.getNavigationPath(portalControllerContext);

        // Form
        SearchFiltersForm form = this.applicationContext.getBean(SearchFiltersForm.class);

        // View
        SearchFiltersView view;
        if (BooleanUtils.toBoolean(window.getProperty(HOME_SETTINGS_WINDOW_PROPERTY))) {
            view = SearchFiltersView.HOME_SETTINGS;
        } else if (StringUtils.startsWith(navigationPath, MUTUALIZED_SPACE_PATH)) {
            view = SearchFiltersView.MUTUALIZED_SPACE;
        } else {
            view = SearchFiltersView.DEFAULT;
        }
        form.setView(view);

        // Keywords
        String keywords = this.getSelectorValue(selectors, KEYWORDS_SELECTOR_ID);
        form.setKeywords(keywords);

        // Levels
        List<String> levels = selectors.get(LEVELS_SELECTOR_ID);
        form.setLevels(levels);

        // Subjects
        List<String> subjects = selectors.get(SUBJECTS_SELECTOR_ID);
        form.setSubjects(subjects);

        // Document types
        List<String> documentTypes = selectors.get(DOCUMENT_TYPES_SELECTOR_ID);
        form.setDocumentTypes(documentTypes);

        // Location
        if (SearchFiltersView.DEFAULT.equals(view)) {
            form.setLocationPath(navigationPath);
            this.updateLocation(portalControllerContext, form);
        }

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
        Float sizeAmount;
        if (StringUtils.isEmpty(sizeAmountSelector)) {
            sizeAmount = null;
        } else {
            sizeAmount = NumberUtils.toFloat(sizeAmountSelector);
        }
        form.setSizeAmount(sizeAmount);

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
        if ((documentDto == null) && StringUtils.isNotEmpty(form.getLocationPath())) {
            // Reset location path
            form.setLocationPath(null);
            this.updateLocation(portalControllerContext, form);
        } else {
            form.setLocation(documentDto);
        }
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

        DocumentDTO documentDto;
        try {
            documentDto = this.documentDao.toDTO(documentContext.getDocument());
        } catch (NuxeoException e) {
            documentDto = null;
        }
        
        return documentDto;
    }


    @Override
    public String getSearchRedirectionUrl(PortalControllerContext portalControllerContext, SearchFiltersForm form) throws PortletException {
        // Redirection path
        String path;
        if (SearchFiltersView.MUTUALIZED_SPACE.equals(form.getView())) {
            path = MUTUALIZED_SPACE_PATH;
        } else if (StringUtils.isEmpty(form.getLocationPath())) {
            path = this.repository.getUserWorkspacePath(portalControllerContext);
        } else {
            path = form.getLocationPath();
        }

        return this.getRedirectionUrl(portalControllerContext, form, path);
    }


    @Override
    public String saveSearch(PortalControllerContext portalControllerContext, SearchFiltersForm form) throws PortletException {
        if (StringUtils.isNotBlank(form.getSavedSearchDisplayName())) {
            // Saved search category identifier
            String categoryId;
            if (SearchFiltersView.MUTUALIZED_SPACE.equals(form.getView())) {
                categoryId = MUTUALIZED_SAVED_SEARCHES_CATEGORY_ID;
            } else {
                categoryId = StringUtils.EMPTY;
            }

            // User preferences
            UserPreferences userPreferences;
            try {
                userPreferences = this.userPreferencesService.getUserPreferences(portalControllerContext);
            } catch (PortalException e) {
                throw new PortletException(e);
            }
            // Saved searches
            List<UserSavedSearch> savedSearches = userPreferences.getSavedSearches(categoryId);
            if (CollectionUtils.isEmpty(savedSearches)) {
                savedSearches = new ArrayList<>(1);
            }

            // Search identifier
            int id;
            try {
                id = this.userPreferencesService.generateUserSavedSearchId(portalControllerContext, userPreferences);
            } catch (PortalException e) {
                throw new PortletException(e);
            }

            // Search data
            String data = this.buildSelectorsParameter(form);

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
            userPreferences.setSavedSearches(categoryId, savedSearches);
            userPreferences.setUpdated(true);
        }

        return this.getSearchRedirectionUrl(portalControllerContext, form);
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
            parameters.put(SELECTORS_PARAMETER, this.buildSelectorsParameter(form));

            // CMS URL
            url = this.portalUrlFactory.getCMSUrl(portalControllerContext, null, path, parameters, null, null, null, null,
                    null, null);
        }

        return url;
    }


    /**
     * Build selectors parameter.
     *
     * @param form                    search filters form
     * @return parameter
     */
    private String buildSelectorsParameter(SearchFiltersForm form) {
        // Selectors
        Map<String, List<String>> selectors = new HashMap<>();

        // Keywords
        String keywords = form.getKeywords();
        if (StringUtils.isNotEmpty(keywords)) {
            selectors.put(KEYWORDS_SELECTOR_ID, Collections.singletonList(keywords));
        }

        // Levels
        List<String> levels = form.getLevels();
        if (CollectionUtils.isNotEmpty(levels)) {
            selectors.put(LEVELS_SELECTOR_ID, levels);
        }

        // Subjects
        List<String> subjects = form.getSubjects();
        if (CollectionUtils.isNotEmpty(subjects)) {
            selectors.put(SUBJECTS_SELECTOR_ID, subjects);
        }

        // Document types
        List<String> documentTypes = form.getDocumentTypes();
        if (CollectionUtils.isNotEmpty(documentTypes)) {
            selectors.put(DOCUMENT_TYPES_SELECTOR_ID, documentTypes);
        }

        // Location
        DocumentDTO location = form.getLocation();
        if (location != null) {
            selectors.put(LOCATION_SELECTOR_ID, Collections.singletonList(location.getPath()));
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
            long computedSize = Math.round(new Double(sizeAmount) * Math.pow(UNIT_FACTOR, sizeUnit.getFactor()));
            selectors.put(COMPUTED_SIZE_SELECTOR_ID, Collections.singletonList(Long.toString(computedSize)));
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
        Date computedDate = this.getComputedDate(form);
        if (computedDate != null) {
            selectors.put(COMPUTED_DATE_SELECTOR_ID, Collections.singletonList(this.formatDate(computedDate)));
        }

        return PageParametersEncoder.encodeProperties(selectors);
    }


    /**
     * Get computed date.
     *
     * @param form                    search filters form
     * @return date
     */
    private Date getComputedDate(SearchFiltersForm form) {
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
