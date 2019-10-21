package fr.index.cloud.ens.filebrowser.portlet.model;

import org.osivia.portal.api.portlet.Refreshable;
import org.osivia.services.workspace.filebrowser.portlet.model.FileBrowserForm;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

/**
 * File browser customized form java-bean.
 *
 * @author Cédric Krommenhoek
 */
@Component
@Primary
@Scope(WebApplicationContext.SCOPE_SESSION)
@Refreshable
public class CustomizedFileBrowserForm extends FileBrowserForm {

    /**
     * Customized column.
     */
    private CustomizedFileBrowserSortField customizedColumn;


    /**
     * Constructor.
     */
    public CustomizedFileBrowserForm() {
        super();
    }


    public CustomizedFileBrowserSortField getCustomizedColumn() {
        return customizedColumn;
    }

    public void setCustomizedColumn(CustomizedFileBrowserSortField customizedColumn) {
        this.customizedColumn = customizedColumn;
    }
}
