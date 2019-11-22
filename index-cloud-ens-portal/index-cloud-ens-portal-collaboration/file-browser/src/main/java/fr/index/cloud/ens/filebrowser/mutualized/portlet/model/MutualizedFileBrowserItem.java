package fr.index.cloud.ens.filebrowser.mutualized.portlet.model;

import fr.index.cloud.ens.filebrowser.commons.portlet.model.AbstractFileBrowserItem;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Mutualized file browser item java-bean.
 *
 * @author Cédric Krommenhoek
 * @see AbstractFileBrowserItem
 */
@Component
@Primary
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class MutualizedFileBrowserItem extends AbstractFileBrowserItem {

    /**
     * Constructor.
     */
    public MutualizedFileBrowserItem() {
        super();
    }

}
