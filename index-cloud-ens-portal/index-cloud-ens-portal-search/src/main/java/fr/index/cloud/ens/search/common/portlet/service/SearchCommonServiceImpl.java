package fr.index.cloud.ens.search.common.portlet.service;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;

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
     * Constructor.
     */
    public SearchCommonServiceImpl() {
        super();
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
