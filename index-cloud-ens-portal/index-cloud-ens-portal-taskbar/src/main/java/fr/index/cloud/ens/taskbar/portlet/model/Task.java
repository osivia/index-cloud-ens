package fr.index.cloud.ens.taskbar.portlet.model;

/**
 * Task java-bean abstract super-class.
 *
 * @author Cédric Krommenhoek
 */
public abstract class Task {

    /**
     * Icon.
     */
    private String icon;
    /**
     * Display name.
     */
    private String displayName;
    /**
     * URL.
     */
    private String url;
    /**
     * Active indicator.
     */
    private boolean active;


    /**
     * Constructor.
     */
    public Task() {
        super();
    }


    /**
     * Check if task is expandable.
     *
     * @return true if task is expandable
     */
    public abstract boolean isExpandable();


    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

}
