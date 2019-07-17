package fr.index.cloud.ens.search.portlet.model;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Search form java-bean.
 *
 * @author Cédric Krommenhoek
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SearchForm {

    /**
     * Search view.
     */
    private SearchView view;

    /**
     * Input value.
     */
    private String value;
    
    /**
     * folder name.
     */
    private String folderName;

   
    /**
     * Getter for folderName.
     * @return the folderName
     */
    public String getFolderName() {
        return folderName;
    }
   
    /**
     * Setter for folderName.
     * @param folderName the folderName to set
     */
    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public SearchView getView() {
        return view;
    }

    public void setView(SearchView view) {
        this.view = view;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
