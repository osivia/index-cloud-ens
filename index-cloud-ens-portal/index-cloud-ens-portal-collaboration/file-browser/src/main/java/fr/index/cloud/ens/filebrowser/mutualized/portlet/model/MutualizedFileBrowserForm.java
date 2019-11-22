package fr.index.cloud.ens.filebrowser.mutualized.portlet.model;

import fr.index.cloud.ens.filebrowser.commons.portlet.model.AbstractFileBrowserForm;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Mutualized file browser form java-bean.
 *
 * @author Cédric Krommenhoek
 * @see AbstractFileBrowserForm
 */
@Component
@Primary
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class MutualizedFileBrowserForm extends AbstractFileBrowserForm {

    /**
     * Constructor.
     */
    public MutualizedFileBrowserForm() {
        super();
    }

}
