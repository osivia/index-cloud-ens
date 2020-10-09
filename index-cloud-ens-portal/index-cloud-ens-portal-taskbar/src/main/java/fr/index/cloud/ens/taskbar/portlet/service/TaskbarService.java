package fr.index.cloud.ens.taskbar.portlet.service;

import fr.index.cloud.ens.taskbar.portlet.model.Taskbar;
import fr.index.cloud.ens.taskbar.portlet.model.TaskbarSearchForm;
import fr.index.cloud.ens.taskbar.portlet.model.TaskbarWindowProperties;
import net.sf.json.JSONArray;
import org.osivia.portal.api.context.PortalControllerContext;

import javax.portlet.PortletException;
import java.io.IOException;
import java.util.List;

/**
 * Taskbar portlet service interface.
 *
 * @author Cédric Krommenhoek
 */
public interface TaskbarService {

    /**
     * Parent document path window property.
     */
    String PATH_WINDOW_PROPERTY = "osivia.taskbar.path";

    /**
     * Selectors parameter.
     */
    String SELECTORS_PARAMETER = "selectors";
    /**
     * Keywords selector identifier.
     */
    String KEYWORDS_SELECTOR_ID = "keywords";
    /**
     * Document types selector identifier.
     */
    String DOCUMENT_TYPES_SELECTOR_ID = "documentTypes";
    /**
     * Levels selector identifier.
     */
    String LEVELS_SELECTOR_ID = "levels";
    /**
     * Subjects selector identifier.
     */
    String SUBJECTS_SELECTOR_ID = "subjects";
    /**
     * Location selector identifier.
     */
    String LOCATION_SELECTOR_ID = "location";


    /**
     * Get window properties.
     *
     * @param portalControllerContext portal controller context
     * @return window properties
     */
    TaskbarWindowProperties getWindowProperties(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Set window properties.
     *
     * @param portalControllerContext portal controller context
     * @param windowProperties        window properties
     */
    void setWindowProperties(PortalControllerContext portalControllerContext, TaskbarWindowProperties windowProperties) throws PortletException;


    /**
     * Get taskbar.
     *
     * @param portalControllerContext portal controller context
     * @return taskbar
     */
    Taskbar getTaskbar(PortalControllerContext portalControllerContext) throws PortletException, IOException;


    /**
     * Get taskbar search form.
     *
     * @param portalControllerContext portal controller context
     * @return search form
     */
    TaskbarSearchForm getTaskbarSearchForm(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Drop.
     *
     * @param portalControllerContext portal controller context
     * @param sourceIds               source identifiers
     * @param targetId                target identifier
     */
    void drop(PortalControllerContext portalControllerContext, List<String> sourceIds, String targetId) throws PortletException;


    /**
     * Reset search filters.
     *
     * @param portalControllerContext portal controller context
     * @param searchForm              search form
     */
    void resetSearchFilters(PortalControllerContext portalControllerContext, TaskbarSearchForm searchForm) throws PortletException;


    /**
     * Search.
     *
     * @param portalControllerContext portal controller context
     * @param searchForm              search form
     */
    void search(PortalControllerContext portalControllerContext, TaskbarSearchForm searchForm) throws PortletException;


    /**
     * Get saved search URL.
     *
     * @param portalControllerContext portal controller context
     * @param id                      saved search identifier
     * @return URL
     */
    String getSavedSearchUrl(PortalControllerContext portalControllerContext, int id) throws PortletException;


    /**
     * Get folder children.
     *
     * @param portalControllerContext portal controller context
     * @param path                    parent folder path
     * @return JSON array
     */
    JSONArray getFolderChildren(PortalControllerContext portalControllerContext, String path) throws PortletException;


    /**
     * Load select2 vocabulary.
     *
     * @param portalControllerContext portal controller context
     * @param vocabularyName          vocabulary name
     * @param filter                  select2 filter
     * @return select2 results JSON array
     */
    JSONArray loadVocabulary(PortalControllerContext portalControllerContext, String vocabularyName, String filter) throws PortletException, IOException;

}
