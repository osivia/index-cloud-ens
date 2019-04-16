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
    /** Loaded indicator. */
    private boolean loaded;


    /**
     * Constructor.
     */
    public DashboardForm() {
        super();
    }


    /**
     * Getter for trashedDocuments.
     * 
     * @return the trashedDocuments
     */
    public List<DashboardApplication> getApplications() {
        return applications;
    }

    /**
     * Setter for trashedDocuments.
     * 
     * @param trashedDocuments the trashedDocuments to set
     */
    public void setApplications(List<DashboardApplication> applications) {
        this.applications = applications;
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
