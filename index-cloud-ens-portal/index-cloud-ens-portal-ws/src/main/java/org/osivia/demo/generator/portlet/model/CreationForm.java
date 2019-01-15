package org.osivia.demo.generator.portlet.model;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Creation form java-bean.
 * 
 * @author Cédric Krommenhoek
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CreationForm {

    /** Document parent path. */
    private String parentPath;
    /** Document type. */
    private String type;
    /** Document title. */
    private String title;
    /** Document description. */
    private String description;


    /**
     * Constructor.
     */
    public CreationForm() {
        super();
    }


    /**
     * Getter for parentPath.
     * 
     * @return the parentPath
     */
    public String getParentPath() {
        return parentPath;
    }

    /**
     * Setter for parentPath.
     * 
     * @param parentPath the parentPath to set
     */
    public void setParentPath(String parentPath) {
        this.parentPath = parentPath;
    }

    /**
     * Getter for type.
     * 
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * Setter for type.
     * 
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Getter for title.
     * 
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Setter for title.
     * 
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Getter for description.
     * 
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Setter for description.
     * 
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

}
