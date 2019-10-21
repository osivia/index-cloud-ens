package fr.index.cloud.ens.filebrowser.portlet.model;

import org.osivia.services.workspace.filebrowser.portlet.model.FileBrowserItem;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * File browser customized item java-bean.
 */
@Component
@Primary
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CustomizedFileBrowserItem extends FileBrowserItem {

    /**
     * Document types.
     */
    private List<String> documentTypes;
    /**
     * Levels.
     */
    private List<String> levels;
    /**
     * Subjects.
     */
    private List<String> subjects;


    /**
     * Constructor.
     */
    public CustomizedFileBrowserItem() {
        super();
    }


    public List<String> getDocumentTypes() {
        return documentTypes;
    }

    public void setDocumentTypes(List<String> documentTypes) {
        this.documentTypes = documentTypes;
    }

    public List<String> getLevels() {
        return levels;
    }

    public void setLevels(List<String> levels) {
        this.levels = levels;
    }

    public List<String> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<String> subjects) {
        this.subjects = subjects;
    }
}
