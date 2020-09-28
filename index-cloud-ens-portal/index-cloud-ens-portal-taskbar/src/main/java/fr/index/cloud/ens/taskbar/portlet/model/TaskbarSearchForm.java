package fr.index.cloud.ens.taskbar.portlet.model;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Taskbar search form java-bean.
 *
 * @author Cédric Krommenhoek
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TaskbarSearchForm {

    /** Keywords. */
    private String keywords;
    /** Document type. */
    private List<String> documentType;
    /** Level. */
    private List<String> level;
    /** Subject. */
    private List<String> subject;


    /**
     * Constructor.
     */
    public TaskbarSearchForm() {
        super();
    }


    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public List<String> getDocumentType() {
        return documentType;
    }

    public void setDocumentType(List<String> documentType) {
        this.documentType = documentType;
    }

    public List<String> getLevel() {
        return level;
    }

    public void setLevel(List<String> level) {
        this.level = level;
    }

    public List<String> getSubject() {
        return subject;
    }

    public void setSubject(List<String> subject) {
        this.subject = subject;
    }
}
