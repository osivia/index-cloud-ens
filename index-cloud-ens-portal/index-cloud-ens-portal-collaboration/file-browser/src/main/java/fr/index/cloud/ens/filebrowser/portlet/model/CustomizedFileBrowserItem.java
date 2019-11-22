package fr.index.cloud.ens.filebrowser.portlet.model;

import fr.index.cloud.ens.filebrowser.commons.portlet.model.AbstractFileBrowserItem;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * File browser customized item java-bean.
 *
 * @author Cédric Krommenhoek
 * @see AbstractFileBrowserItem
 */
@Component
@Primary
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CustomizedFileBrowserItem extends AbstractFileBrowserItem {

    /**
     * PRONOTE indicator.
     */
    private boolean pronote;
    /**
     * Mutualized indicator.
     */
    private boolean mutualized;


    /**
     * Constructor.
     */
    public CustomizedFileBrowserItem() {
        super();
    }


    public boolean isPronote() {
        return pronote;
    }

    public void setPronote(boolean pronote) {
        this.pronote = pronote;
    }

    public boolean isMutualized() {
        return mutualized;
    }

    public void setMutualized(boolean mutualized) {
        this.mutualized = mutualized;
    }
}
