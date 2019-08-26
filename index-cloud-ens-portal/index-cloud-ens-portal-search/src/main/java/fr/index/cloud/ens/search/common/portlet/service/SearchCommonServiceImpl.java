package fr.index.cloud.ens.search.common.portlet.service;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.osivia.portal.api.context.PortalControllerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import javax.portlet.PortletException;
import java.util.List;
import java.util.Map;

/**
 * Search common service implementation.
 *
 * @author Cédric Krommenhoek
 * @see SearchCommonService
 */
public class SearchCommonServiceImpl implements SearchCommonService {

    /**
     * View resolver.
     */
    @Autowired
    private InternalResourceViewResolver viewResolver;


    /**
     * Constructor.
     */
    public SearchCommonServiceImpl() {
        super();
    }


    @Override
    public String resolveViewPath(PortalControllerContext portalControllerContext, String name) throws PortletException {
        // Path
        String path;

        try {
            View view = this.viewResolver.resolveViewName(name, null);
            JstlView jstlView = (JstlView) view;
            path = jstlView.getUrl();
        } catch (Exception e) {
            throw new PortletException(e);
        }

        return path;
    }


    /**
     * Get selector value.
     *
     * @param selectors  selectors
     * @param selectorId selector identifier
     * @return value, may be null
     */
    protected String getSelectorValue(Map<String, List<String>> selectors, String selectorId) {
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

}
