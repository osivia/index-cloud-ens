package fr.index.cloud.ens.dashboard;

import java.util.List;

import org.osivia.portal.api.portlet.Refreshable;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

/**
 * Dashboard form.
 * 
 * @author JS Steux
 */
@Component
@Scope(WebApplicationContext.SCOPE_SESSION)
@Refreshable
public class DashboardForm {

    /** Trashed documents. */
    private List<DashboardApplication> applications;
    /** Sort property. */
    private DashboardSort sort;
    /** Alternative sort indicator. */
    private boolean alt;
    /** Loaded indicator. */
    private boolean loaded;


    /**
     * Constructor.
     */
    public DashboardForm() {
        super();
    }



    /**
     * Gets the applications.
     *
     * @return the applications
     */
    public List<DashboardApplication> getApplications() {
        return applications;
    }

    /**
     * Sets the applications.
     *
     * @param applications the new applications
     */
    public void setApplications(List<DashboardApplication> applications) {
        this.applications = applications;
    }

    public DashboardSort getSort() {
        return sort;
    }

    public void setSort(DashboardSort sort) {
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
