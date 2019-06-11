package fr.index.cloud.ens.selectors.portlet.model;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * File browser filter window properties java-bean.
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class FileBrowserFilterWindowProperties {

    /**
     * Title.
     */
    private String title;
    /**
     * Selector identifier.
     */
    private String selectorId;
    /**
     * Vocabulary.
     */
    private String vocabulary;


    /**
     * Constructor.
     */
    public FileBrowserFilterWindowProperties() {
        super();
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSelectorId() {
        return selectorId;
    }

    public void setSelectorId(String selectorId) {
        this.selectorId = selectorId;
    }

    public String getVocabulary() {
        return vocabulary;
    }

    public void setVocabulary(String vocabulary) {
        this.vocabulary = vocabulary;
    }

}
