package fr.index.cloud.ens.filebrowser.mutualized.portlet.model;

import fr.index.cloud.ens.filebrowser.commons.portlet.model.AbstractFileBrowserSortField;
import org.apache.commons.lang.StringUtils;

/**
 * Mutualized file browser sorts enumeration.
 *
 * @author Cédric Krommenhoek
 * @see AbstractFileBrowserSortField
 */
public enum MutualizedFileBrowserSortEnum implements AbstractFileBrowserSortField {

    /**
     * Relevance sort.
     */
    RELEVANCE("relevance"),
    /**
     * Title sort.
     */
    TITLE("title"),
    /**
     * Document type sort.
     */
    DOCUMENT_TYPE("document-type", true),
    /**
     * Level sort.
     */
    LEVEL("level", true),
    /**
     * Subject sort.
     */
    SUBJECT("subject", true),
    /**
     * Last modification sort.
     */
    LAST_MODIFICATION("last-modification"),
    /**
     * File size sort.
     */
    FILE_SIZE("file-size");


    /**
     * Identifier.
     */
    private final String id;
    /**
     * Internationalization key.
     */
    private final String key;
    /**
     * Customizable indicator.
     */
    private final boolean customizable;


    /**
     * Constructor.
     *
     * @param id           identifier
     * @param customizable customizable indicator
     */
    MutualizedFileBrowserSortEnum(String id, boolean customizable) {
        this.id = id;
        this.key = "FILE_BROWSER_SORT_FIELD_" + StringUtils.upperCase(this.name());
        this.customizable = customizable;
    }


    /**
     * Constructor.
     *
     * @param id identifier
     */
    MutualizedFileBrowserSortEnum(String id) {
        this(id, false);
    }


    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public String getKey() {
        return this.key;
    }

    @Override
    public boolean isListMode() {
        return false;
    }

    @Override
    public boolean isCustomizable() {
        return this.customizable;
    }

}
