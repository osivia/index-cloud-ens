package fr.index.cloud.ens.search.filters.portlet.model;

import fr.toutatice.portail.cms.nuxeo.api.domain.DocumentDTO;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Date;

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
     * Document type.
     */
    private String documentType;
    /**
     * Location path.
     */
    private String locationPath;
    /**
     * Keywords.
     */
    private String keywords;
    /**
     * Size range.
     */
    private SearchFiltersSizeRange sizeRange;
    /**
     * Size amount.
     */
    private Float sizeAmount;
    /**
     * Size unit.
     */
    private SearchFiltersSizeUnit sizeUnit;
    /**
     * Date range.
     */
    private SearchFiltersDateRange dateRange;
    /**
     * Customized date.
     */
    private Date customizedDate;

    /**
     * Saved search display name.
     */
    private String savedSearchDisplayName;


    /**
     * Constructor.
     */
    public SearchFiltersForm() {
        super();
    }


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

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
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

    public SearchFiltersSizeRange getSizeRange() {
        return sizeRange;
    }

    public void setSizeRange(SearchFiltersSizeRange sizeRange) {
        this.sizeRange = sizeRange;
    }

    public Float getSizeAmount() {
        return sizeAmount;
    }

    public void setSizeAmount(Float sizeAmount) {
        this.sizeAmount = sizeAmount;
    }

    public SearchFiltersSizeUnit getSizeUnit() {
        return sizeUnit;
    }

    public void setSizeUnit(SearchFiltersSizeUnit sizeUnit) {
        this.sizeUnit = sizeUnit;
    }

    public SearchFiltersDateRange getDateRange() {
        return dateRange;
    }

    public void setDateRange(SearchFiltersDateRange dateRange) {
        this.dateRange = dateRange;
    }

    public Date getCustomizedDate() {
        return customizedDate;
    }

    public void setCustomizedDate(Date customizedDate) {
        this.customizedDate = customizedDate;
    }

    public String getSavedSearchDisplayName() {
        return savedSearchDisplayName;
    }

    public void setSavedSearchDisplayName(String savedSearchDisplayName) {
        this.savedSearchDisplayName = savedSearchDisplayName;
    }
}
