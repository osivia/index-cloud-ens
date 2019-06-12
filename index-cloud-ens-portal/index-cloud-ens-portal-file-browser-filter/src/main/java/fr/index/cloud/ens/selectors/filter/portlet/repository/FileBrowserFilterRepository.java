package fr.index.cloud.ens.selectors.filter.portlet.repository;

import net.sf.json.JSONArray;
import org.osivia.portal.api.context.PortalControllerContext;

import javax.portlet.PortletException;
import java.io.IOException;

public interface FileBrowserFilterRepository {

    /**
     * Get selection vocabulary label.
     *
     * @param portalControllerContext portal controller context
     * @param vocabulary              vocabulary
     * @param key                     vocabulary key
     * @return label
     * @throws PortletException
     */
    String getSelectionLabel(PortalControllerContext portalControllerContext, String vocabulary, String key) throws PortletException;


    /**
     * Load vocabulary.
     *
     * @param portalControllerContext portal controller context
     * @param vocabulary              vocabulary
     * @return vocabulary JSON array
     */
    JSONArray loadVocabulary(PortalControllerContext portalControllerContext, String vocabulary) throws PortletException, IOException;

}
