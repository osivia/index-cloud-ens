package fr.index.cloud.ens.customizer.attributes;

/**
 * Nav item java-bean.
 *
 * @author Cédric Krommenhoek
 */
public class NavItem {

    /**
     * URL.
     */
    private String url;
    /**
     * Icon.
     */
    private String icon;
    /**
     * Internationalization key.
     */
    private String key;
    /**
     * Active indicator.
     */
    private boolean active;


    /**
     * Constructor.
     */
    public NavItem() {
        super();
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

}
