package fr.index.cloud.ens.search.portlet.model;

import org.apache.commons.lang.StringUtils;

/**
 * Search views enumeration.
 *
 * @author Cédric Krommenhoek
 */
public enum SearchView {

    /**
     * Input view.
     */
    INPUT,
    /**
     * Button view.
     */
    BUTTON,
    /**
     * Autosubmit view.
     */
    AUTOSUBMIT;
    


    /** Default view. */
    public static final SearchView DEFAULT = INPUT;


    /**
     * Identifier.
     */
    private final String id;
    /**
     * Internationalization key.
     */
    private final String key;


    /**
     * Constructor.
     */
    SearchView() {
        this.id = StringUtils.lowerCase(this.name());
        this.key = "SEARCH_VIEW_" + StringUtils.upperCase(this.name());
    }


    /**
     * Get search view from identifier.
     *
     * @param id identifier
     * @return view
     */
    public static SearchView fromId(String id) {
        SearchView result = DEFAULT;

        for (SearchView value : SearchView.values()) {
            if (StringUtils.equals(id, value.id)) {
                result = value;
            }
        }

        return result;
    }


    public String getId() {
        return id;
    }

    public String getKey() {
        return key;
    }
}
