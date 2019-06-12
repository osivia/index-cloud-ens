package fr.index.cloud.ens.selectors.filter.portlet.service;

import fr.index.cloud.ens.selectors.common.portlet.service.AbstractFileBrowserFilterServiceImpl;
import fr.index.cloud.ens.selectors.filter.portlet.model.FileBrowserFilterForm;
import fr.index.cloud.ens.selectors.filter.portlet.model.FileBrowserFilterVocabularyItem;
import fr.index.cloud.ens.selectors.filter.portlet.model.FileBrowserFilterWindowProperties;
import fr.index.cloud.ens.selectors.filter.portlet.repository.FileBrowserFilterRepository;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.CharEncoding;
import org.apache.commons.lang.StringUtils;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
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
 * File browser filter portlet service implementation.
 *
 * @author Cédric Krommenhoek
 * @see AbstractFileBrowserFilterServiceImpl
 * @see FileBrowserFilterService
 */
@Service
public class FileBrowserFilterServiceImpl extends AbstractFileBrowserFilterServiceImpl implements FileBrowserFilterService {

    /**
     * Selector clear parameter.
     */
    private static final String SELECTOR_CLEAR_VALUE = "__CLEAR__";


    /**
     * Application context.
     */
    @Autowired
    private ApplicationContext applicationContext;
    /**
     * Portlet repository.
     */
    @Autowired
    private FileBrowserFilterRepository repository;
    /**
     * Internationalization bundle factory.
     */
    @Autowired
    private IBundleFactory bundleFactory;


    /**
     * Constructor.
     */
    public FileBrowserFilterServiceImpl() {
        super();
    }


    @Override
    public FileBrowserFilterWindowProperties getWindowProperties(PortalControllerContext portalControllerContext) {
        // Portlet request
        PortletRequest request = portalControllerContext.getRequest();
        // Window
        PortalWindow window = WindowFactory.getWindow(request);

        // Window properties
        FileBrowserFilterWindowProperties windowProperties = this.applicationContext.getBean(FileBrowserFilterWindowProperties.class);

        // Title
        String title = window.getProperty(TITLE_WINDOW_PROPERTY);
        windowProperties.setTitle(title);

        // Selector identifier
        String selectorId = window.getProperty(SELECTOR_ID_WINDOW_PROPERTY);
        windowProperties.setSelectorId(selectorId);

        // Vocabulary
        String vocabulary = window.getProperty(VOCABULARY_WINDOW_PROPERTY);
        windowProperties.setVocabulary(vocabulary);

        return windowProperties;
    }


    @Override
    public void setWindowProperties(PortalControllerContext portalControllerContext, FileBrowserFilterWindowProperties windowProperties) {
        // Portlet request
        PortletRequest request = portalControllerContext.getRequest();
        // Window
        PortalWindow window = WindowFactory.getWindow(request);

        // Title
        String title = StringUtils.trimToNull(windowProperties.getTitle());
        window.setProperty(TITLE_WINDOW_PROPERTY, title);

        // Selector identifier
        String selectorId = StringUtils.trimToNull(windowProperties.getSelectorId());
        window.setProperty(SELECTOR_ID_WINDOW_PROPERTY, selectorId);

        // Vocabulary
        String vocabulary = StringUtils.trimToNull(windowProperties.getVocabulary());
        window.setProperty(VOCABULARY_WINDOW_PROPERTY, vocabulary);
    }


    @Override
    public FileBrowserFilterForm getForm(PortalControllerContext portalControllerContext) throws PortletException {
        // Window properties
        FileBrowserFilterWindowProperties windowProperties = this.getWindowProperties(portalControllerContext);
        // Selectors
        Map<String, List<String>> selectors = this.getSelectors(portalControllerContext);

        // Selector
        List<String> selector = selectors.get(windowProperties.getSelectorId());

        // Selector value
        String selection;
        if (CollectionUtils.isEmpty(selector)) {
            selection = null;
        } else {
            selection = selector.get(0);
        }

        // Selector label
        String label = this.getSelectionLabel(portalControllerContext, windowProperties.getVocabulary(), selection);

        // File browser filter form
        FileBrowserFilterForm form = this.applicationContext.getBean(FileBrowserFilterForm.class);
        form.setSelection(selection);
        form.setSelectionLabel(label);
        form.setWindowProperties(windowProperties);

        return form;
    }


    @Override
    public void select(PortalControllerContext portalControllerContext, FileBrowserFilterForm form) throws PortletException {
        // Window properties
        FileBrowserFilterWindowProperties windowProperties = form.getWindowProperties();
        // Selectors
        Map<String, List<String>> selectors = this.getSelectors(portalControllerContext);

        // Selector
        List<String> selector = selectors.get(windowProperties.getSelectorId());
        if (selector == null) {
            selector = new ArrayList<>(1);
            selectors.put(windowProperties.getSelectorId(), selector);
        } else {
            selector.clear();
        }

        if (StringUtils.equals(SELECTOR_CLEAR_VALUE, form.getSelection())) {
            // Clear
            form.setSelection(null);
        }

        if (StringUtils.isNotEmpty(form.getSelection())) {
            selector.add(form.getSelection());

            // Update model
            String label = this.getSelectionLabel(portalControllerContext, windowProperties.getVocabulary(), form.getSelection());
            form.setSelectionLabel(label);
        }

        this.setSelectors(portalControllerContext, selectors);
    }


    /**
     * Get selection vocabulary label.
     *
     * @param portalControllerContext portal controller context
     * @param vocabulary              vocabulary
     * @param selection               selection
     * @return label
     */
    private String getSelectionLabel(PortalControllerContext portalControllerContext, String vocabulary, String selection) throws PortletException {
        String label;
        if (StringUtils.isEmpty(selection)) {
            label = null;
        } else {
            String key;
            if (StringUtils.contains(selection, "/")) {
                key = StringUtils.substringAfterLast(selection, "/");
            } else {
                key = selection;
            }
            label = this.repository.getSelectionLabel(portalControllerContext, vocabulary, key);
        }
        return label;
    }


    @Override
    public JSONArray loadVocabulary(PortalControllerContext portalControllerContext, String filter) throws PortletException, IOException {
        // Portlet request
        PortletRequest request = portalControllerContext.getRequest();
        // Internationalization bundle
        Bundle bundle = this.bundleFactory.getBundle(request.getLocale());

        // Windows properties
        FileBrowserFilterWindowProperties windowProperties = this.getWindowProperties(portalControllerContext);

        // Vocabulary JSON array
        JSONArray array = this.repository.loadVocabulary(portalControllerContext, windowProperties.getVocabulary());

        // Select2 results
        JSONArray results;
        if ((array == null) || (array.isEmpty())) {
            results = new JSONArray();
        } else {
            results = this.parseVocabulary(array, filter);

            // Clear
            JSONObject object = new JSONObject();
            object.put("id", SELECTOR_CLEAR_VALUE);
            object.put("text", bundle.getString("FILTER_CLEAR"));
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
        Map<String, FileBrowserFilterVocabularyItem> items = new HashMap<>(jsonArray.size());
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

            FileBrowserFilterVocabularyItem item = items.get(key);
            if (item == null) {
                item = this.applicationContext.getBean(FileBrowserFilterVocabularyItem.class, key);
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

                FileBrowserFilterVocabularyItem parentItem = items.get(parent);
                if (parentItem == null) {
                    parentItem = this.applicationContext.getBean(FileBrowserFilterVocabularyItem.class, parent);
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
    private void generateVocabularyChildren(Map<String, FileBrowserFilterVocabularyItem> items, JSONArray jsonArray, Set<String> children, boolean optgroup, int level,
                                            String parentId) throws UnsupportedEncodingException {
        for (String child : children) {
            FileBrowserFilterVocabularyItem item = items.get(child);
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
