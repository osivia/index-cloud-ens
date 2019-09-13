package fr.index.cloud.ens.search.filters.portlet.model;

import fr.toutatice.portail.cms.nuxeo.api.domain.DocumentDTO;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Search filters form java-bean.
 *
 * @author Cédric Krommenhoek
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SearchFiltersForm {

    /**
     * Location.
     */
    private DocumentDTO location;
    /**
     * Modal indicator.
     */
    private boolean modal;

    /**
     * Level.
     */
    private String level;
    /**
     * Subject.
     */
    private String subject;
    /**
     * Location path.
     */
    private String locationPath;
    /**
     * Keywords.
     */
    private String keywords;

    /**
     * Saved search display name.
     */
    private String savedSearchDisplayName;


    public DocumentDTO getLocation() {
        return location;
    }

    public void setLocation(DocumentDTO location) {
        this.location = location;
    }

    public boolean isModal() {
        return modal;
    }

    public void setModal(boolean modal) {
        this.modal = modal;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getLocationPath() {
        return locationPath;
    }

    public void setLocationPath(String locationPath) {
        this.locationPath = locationPath;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getSavedSearchDisplayName() {
        return savedSearchDisplayName;
    }

    public void setSavedSearchDisplayName(String savedSearchDisplayName) {
        this.savedSearchDisplayName = savedSearchDisplayName;
    }
}
