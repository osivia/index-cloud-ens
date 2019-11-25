package fr.index.cloud.ens.mutualization.copy.portlet.model;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Mutualization copy form java-bean.
 *
 * @author Cédric Krommenhoek
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class MutualizationCopyForm {

    /**
     * Document path.
     */
    private String documentPath;
    /**
     * Base path.
     */
    private String basePath;

    /**
     * Target path.
     */
    private String targetPath;


    /**
     * Constructor.
     */
    public MutualizationCopyForm() {
        super();
    }


    public String getDocumentPath() {
        return documentPath;
    }

    public void setDocumentPath(String documentPath) {
        this.documentPath = documentPath;
    }

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public String getTargetPath() {
        return targetPath;
    }

    public void setTargetPath(String targetPath) {
        this.targetPath = targetPath;
    }
}
