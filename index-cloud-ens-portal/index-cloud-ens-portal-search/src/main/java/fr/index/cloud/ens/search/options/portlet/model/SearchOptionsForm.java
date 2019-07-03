package fr.index.cloud.ens.search.options.portlet.model;

import fr.toutatice.portail.cms.nuxeo.api.domain.DocumentDTO;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Search options form java-bean.
 *
 * @author Cédric Krommenhoek
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SearchOptionsForm {

    /**
     * Location.
     */
    private DocumentDTO location;

    /**
     * Level.
     */
    private String level;
    /**
     * Keywords.
     */
    private String keywords;


    public DocumentDTO getLocation() {
        return location;
    }

    public void setLocation(DocumentDTO location) {
        this.location = location;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }
}
