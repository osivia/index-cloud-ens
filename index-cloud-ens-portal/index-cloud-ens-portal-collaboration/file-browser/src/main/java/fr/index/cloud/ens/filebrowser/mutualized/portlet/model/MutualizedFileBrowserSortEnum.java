package fr.index.cloud.ens.filebrowser.mutualized.portlet.model;

import fr.index.cloud.ens.filebrowser.commons.portlet.model.AbstractFileBrowserSortField;
import org.apache.commons.lang.StringUtils;

/**
 * Mutualized file browser sorts enumeration.
 *
 * @author Cédric Krommenhoek
 * @see MutualizedFileBrowserSortField
 */
public enum MutualizedFileBrowserSortEnum implements MutualizedFileBrowserSortField {

    /**
     * Relevance sort.
     */
    RELEVANCE("relevance", null),
    /**
     * Title sort.
     */
    TITLE("title", "mtz:title"),
    /**
     * Document type sort.
     */
    DOCUMENT_TYPE("document-type", true, "idxcl:documentTypes"),
    /**
     * Level sort.
     */
    LEVEL("level", true, "idxcl:levels"),
    /**
     * Subject sort.
     */
    SUBJECT("subject", true, "idxcl:subjects"),
    /**
     * Last modification sort.
     */
    LAST_MODIFICATION("last-modification", "dc:modified"),
    /**
     * File size sort.
     */
    FILE_SIZE("file-size", "common:size");


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
     * NXQL field.
     */
    private final String field;


    /**
     * Constructor.
     *
     * @param id           identifier
     * @param customizable customizable indicator
     * @param field        NXQL field
     */
    MutualizedFileBrowserSortEnum(String id, boolean customizable, String field) {
        this.id = id;
        this.key = "FILE_BROWSER_SORT_FIELD_" + StringUtils.upperCase(this.name());
        this.customizable = customizable;
        this.field = field;
    }


    /**
     * Constructor.
     *
     * @param id    identifier
     * @param field NXQL field
     */
    MutualizedFileBrowserSortEnum(String id, String field) {
        this(id, false, field);
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


    @Override
    public String getField() {
        return this.field;
    }

}
