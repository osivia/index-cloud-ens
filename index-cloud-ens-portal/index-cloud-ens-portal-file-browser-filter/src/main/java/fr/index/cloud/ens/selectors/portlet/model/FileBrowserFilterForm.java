package fr.index.cloud.ens.selectors.portlet.model;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * File browser filter form java-bean.
 *
 * @author Cédric Krommenhoek
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class FileBrowserFilterForm {

    /**
     * Selection.
     */
    private String selection;
    /**
     * Selection label.
     */
    private String selectionLabel;
    /**
     * Window properties.
     */
    private FileBrowserFilterWindowProperties windowProperties;


    /**
     * Constructor.
     */
    public FileBrowserFilterForm() {
        super();
    }


    public String getSelection() {
        return selection;
    }

    public void setSelection(String selection) {
        this.selection = selection;
    }

    public String getSelectionLabel() {
        return selectionLabel;
    }

    public void setSelectionLabel(String selectionLabel) {
        this.selectionLabel = selectionLabel;
    }

    public FileBrowserFilterWindowProperties getWindowProperties() {
        return windowProperties;
    }

    public void setWindowProperties(FileBrowserFilterWindowProperties windowProperties) {
        this.windowProperties = windowProperties;
    }

}
