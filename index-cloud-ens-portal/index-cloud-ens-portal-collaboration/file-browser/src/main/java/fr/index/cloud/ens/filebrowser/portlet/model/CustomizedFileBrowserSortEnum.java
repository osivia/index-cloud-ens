package fr.index.cloud.ens.filebrowser.portlet.model;

import fr.index.cloud.ens.filebrowser.commons.portlet.model.AbstractFileBrowserSortField;
import org.apache.commons.lang.StringUtils;

/**
 * File browser customized portlet sort enumeration.
 *
 * @author Cédric Krommenhoek
 * @see AbstractFileBrowserSortField
 */
public enum CustomizedFileBrowserSortEnum implements AbstractFileBrowserSortField {

    /**
     * Relevance sort.
     */
    RELEVANCE("relevance", true),
    /**
     * Title sort.
     */
    TITLE("title"),
    /**
     * Document type sort.
     */
    DOCUMENT_TYPE("document-type", false, true),
    /**
     * Level sort.
     */
    LEVEL("level", false, true),
    /**
     * Subject sort.
     */
    SUBJECT("subject", false, true),
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
     * List mode restriction indicator.
     */
    private final boolean listMode;
    /**
     * Customizable indicator.
     */
    private final boolean customizable;


    /**
     * Constructor.
     *
     * @param id           identifier
     * @param listMode     list mode restriction indicator
     * @param customizable customizable indicator
     */
    CustomizedFileBrowserSortEnum(String id, boolean listMode, boolean customizable) {
        this.id = id;
        this.key = "FILE_BROWSER_SORT_FIELD_" + StringUtils.upperCase(this.name());
        this.listMode = listMode;
        this.customizable = customizable;
    }

    /**
     * Constructor.
     *
     * @param id       identifier
     * @param listMode list mode restriction indicator
     */
    CustomizedFileBrowserSortEnum(String id, boolean listMode) {
        this(id, listMode, false);
    }

    /**
     * Constructor.
     *
     * @param id identifier
     */
    CustomizedFileBrowserSortEnum(String id) {
        this(id, false, false);
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
        return this.listMode;
    }


    @Override
    public boolean isCustomizable() {
        return this.customizable;
    }

}
