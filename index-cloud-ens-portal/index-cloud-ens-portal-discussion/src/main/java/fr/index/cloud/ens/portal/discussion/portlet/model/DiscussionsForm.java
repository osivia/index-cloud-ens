package fr.index.cloud.ens.portal.discussion.portlet.model;

import java.util.List;

import org.osivia.portal.api.portlet.Refreshable;
import org.osivia.portal.api.portlet.RequestLifeCycle;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

/**
 * Discussions form java-bean.
 * 
 * @author Cédric Krommenhoek
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Refreshable
@RequestLifeCycle
public class DiscussionsForm {

    /** Trashed documents. */
    private List<DiscussionDocument> trashedDocuments;
    /** Sort property. */
    private DiscussionsFormSort sort;
    /** Alternative sort indicator. */
    private boolean alt;
    /** Loaded indicator. */
    private boolean loaded;


    /**
     * Constructor.
     */
    public DiscussionsForm() {
        super();
    }


    /**
     * Getter for trashedDocuments.
     * 
     * @return the trashedDocuments
     */
    public List<DiscussionDocument> getTrashedDocuments() {
        return trashedDocuments;
    }

    /**
     * Setter for trashedDocuments.
     * 
     * @param trashedDocuments the trashedDocuments to set
     */
    public void setTrashedDocuments(List<DiscussionDocument> trashedDocuments) {
        this.trashedDocuments = trashedDocuments;
    }

    public DiscussionsFormSort getSort() {
        return sort;
    }

    public void setSort(DiscussionsFormSort sort) {
        this.sort = sort;
    }

    public boolean isAlt() {
        return alt;
    }

    public void setAlt(boolean alt) {
        this.alt = alt;
    }

    /**
     * Getter for loaded.
     * 
     * @return the loaded
     */
    public boolean isLoaded() {
        return loaded;
    }

    /**
     * Setter for loaded.
     * 
     * @param loaded the loaded to set
     */
    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }

}
