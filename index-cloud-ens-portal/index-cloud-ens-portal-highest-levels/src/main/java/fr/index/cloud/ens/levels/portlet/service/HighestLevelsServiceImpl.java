package fr.index.cloud.ens.levels.portlet.service;

import fr.index.cloud.ens.levels.portlet.model.HighestLevels;
import fr.index.cloud.ens.levels.portlet.model.HighestLevelsItem;
import fr.index.cloud.ens.levels.portlet.model.comparator.HighestLevelsItemsComparator;
import fr.index.cloud.ens.levels.portlet.repository.HighestLevelsRepository;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.PropertyList;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.page.PageParametersEncoder;
import org.osivia.portal.api.urls.IPortalUrlFactory;
import org.osivia.portal.core.cms.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.portlet.PortletException;
import java.util.*;

/**
 * Highest levels portlet service implementation.
 *
 * @author Cédric Krommenhoek
 * @see HighestLevelsService
 */
@Service
public class HighestLevelsServiceImpl implements HighestLevelsService {

    /**
     * Application context.
     */
    @Autowired
    private ApplicationContext applicationContext;
    /**
     * Portal URL factory.
     */
    @Autowired
    private IPortalUrlFactory portalUrlFactory;
    /**
     * Portlet repository.
     */
    @Autowired
    private HighestLevelsRepository repository;
    /**
     * Highest levels items comparator.
     */
    @Autowired
    private HighestLevelsItemsComparator comparator;


    /**
     * Constructor.
     */
    public HighestLevelsServiceImpl() {
        super();
    }


    @Override
    public HighestLevels getHighestLevels(PortalControllerContext portalControllerContext) throws PortletException {
        // Documents
        List<Document> documents = this.repository.getDocuments(portalControllerContext);

        // Highest levels items
        Set<HighestLevelsItem> items;
        if (CollectionUtils.isEmpty(documents)) {
            items = null;
        } else {
            // Counts
            Map<String, Integer> counts = new HashMap<>();
            for (Document document : documents) {
                PropertyList levels = document.getProperties().getList(HighestLevelsRepository.LEVELS_PROPERTY);

                if ((levels != null) && !levels.isEmpty()) {
                    for (int i = 0; i < levels.size(); i++) {
                        String level = levels.getString(i);
                        Integer count = counts.get(level);
                        if (count == null) {
                            count = 1;
                        } else {
                            count++;
                        }
                        counts.put(level, count);
                    }
                }
            }

            items = new TreeSet<>(this.comparator);

            for (Map.Entry<String, Integer> entry : counts.entrySet()) {
                // Highest level item
                HighestLevelsItem item = this.applicationContext.getBean(HighestLevelsItem.class);
                item.setId(entry.getKey());
                item.setLabel(this.repository.getLabel(portalControllerContext, entry.getKey()));
                item.setCount(entry.getValue());

                items.add(item);
            }
        }


        // Highest levels
        HighestLevels levels = this.applicationContext.getBean(HighestLevels.class);
        levels.setItems(items);

        return levels;
    }


    @Override
    public String getSearchUrl(PortalControllerContext portalControllerContext, String id) throws PortletException {
        // Search path
        String path = this.repository.getSearchPath(portalControllerContext);

        // Search URL
        String url;
        if (StringUtils.isEmpty(path)) {
            url = null;
        } else {
            // Selectors
            Map<String, List<String>> selectors = new HashMap<>();
            selectors.put("level", Collections.singletonList(id));

            // Page parameters
            Map<String, String> parameters = new HashMap<>(1);
            parameters.put("selectors", PageParametersEncoder.encodeProperties(selectors));

            // CMS URL
            url = this.portalUrlFactory.getCMSUrl(portalControllerContext, null, path, parameters, null, null, null, null,
                    null, null);
        }

        return url;
    }

}
