package fr.index.cloud.ens.selectors.portlet.service;

import fr.index.cloud.ens.selectors.portlet.model.FileBrowserFilterForm;
import fr.index.cloud.ens.selectors.portlet.model.FileBrowserFilterWindowProperties;
import net.sf.json.JSONArray;
import org.osivia.portal.api.context.PortalControllerContext;

import javax.portlet.PortletException;
import java.io.IOException;

/**
 * File browser filter portlet service interface.
 *
 * @author Cédric Krommenhoek
 */
public interface FileBrowserFilterService {

    /**
     * Title window property.
     */
    String TITLE_WINDOW_PROPERTY = "osivia.file-browser-filter.title";
    /**
     * Selector identifier window property.
     */
    String SELECTOR_ID_WINDOW_PROPERTY = "osivia.file-browser-filter.selector-id";
    /**
     * Vocabulary window property.
     */
    String VOCABULARY_WINDOW_PROPERTY = "osivia.file-browser-filter.vocabulary";


    /**
     * Get window properties.
     *
     * @param portalControllerContext portal controller context
     * @return window properties
     */
    FileBrowserFilterWindowProperties getWindowProperties(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Set window properties.
     *
     * @param portalControllerContext portal controller context
     * @param windowProperties        window properties
     */
    void setWindowProperties(PortalControllerContext portalControllerContext, FileBrowserFilterWindowProperties windowProperties) throws PortletException;


    /**
     * Get file browser filter form.
     *
     * @param portalControllerContext portal controller context
     * @return form
     */
    FileBrowserFilterForm getForm(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Select.
     *
     * @param portalControllerContext portal controller context
     * @param form                    file browser filter form
     */
    void select(PortalControllerContext portalControllerContext, FileBrowserFilterForm form) throws PortletException;


    /**
     * Load select2 vocabulary.
     *
     * @param portalControllerContext portal controller context
     * @param filter                  select2 filter
     * @return select2 results JSON array
     */
    JSONArray loadVocabulary(PortalControllerContext portalControllerContext, String filter) throws PortletException, IOException;

}
