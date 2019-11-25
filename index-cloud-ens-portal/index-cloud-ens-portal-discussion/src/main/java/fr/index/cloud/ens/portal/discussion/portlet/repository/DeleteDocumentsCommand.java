package fr.index.cloud.ens.portal.discussion.portlet.repository;

import java.util.List;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Delete documents Nuxeo command.
 * 
 * @author Cédric Krommenhoek
 * @see DiscussionCommand
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class DeleteDocumentsCommand extends DiscussionCommand {

    /**
     * Constructor.
     * 
     * @param basePath base path
     */
    public DeleteDocumentsCommand(String basePath) {
        super(basePath, null);
    }

    /**
     * Constructor.
     * 
     * @param selectedPaths selected item paths
     */
    public DeleteDocumentsCommand(List<String> selectedPaths) {
        super(null, selectedPaths);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected String getOperationName() {
        return "Services.PurgeDocuments";
    }

}
