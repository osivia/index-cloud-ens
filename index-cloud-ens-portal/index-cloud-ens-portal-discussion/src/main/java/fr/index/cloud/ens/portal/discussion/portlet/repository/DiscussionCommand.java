package fr.index.cloud.ens.portal.discussion.portlet.repository;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.model.DocRef;
import org.nuxeo.ecm.automation.client.model.DocRefs;
import org.osivia.portal.core.cms.CMSException;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;

/**
 * Trash Nuxeo command abstract super-class.
 * 
 * @author Cédric Krommenhoek
 * @see INuxeoCommand
 */
public abstract class DiscussionCommand implements INuxeoCommand {

    /** Base path. */
    private final String basePath;
    /** Selected item paths. */
    private final List<String> selectedPaths;


    /**
     * Constructor.
     * 
     * @param basePath base path
     * @param selectedPaths selected paths
     */
    protected DiscussionCommand(String basePath, List<String> selectedPaths) {
        super();
        this.basePath = basePath;
        this.selectedPaths = selectedPaths;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Object execute(Session nuxeoSession) throws Exception {
        // Operation request
        OperationRequest request = nuxeoSession.newRequest(this.getOperationName());

        if (StringUtils.isNotEmpty(this.basePath)) {
            // Parent
            DocRef parent = new DocRef(this.basePath);
            request.set("parent", parent);
        } else if (CollectionUtils.isNotEmpty(this.selectedPaths)) {
            // Inputs
            DocRefs references = new DocRefs(this.selectedPaths.size());
            for (String path : this.selectedPaths) {
                DocRef reference = new DocRef(path);
                references.add(reference);
            }
            request.setInput(references);
        } else {
            // Error
            throw new CMSException(CMSException.ERROR_UNAVAILAIBLE);
        }

        return request.execute();
    }


    /**
     * Get Nuxeo operation name.
     * 
     * @return operation name
     */
    protected abstract String getOperationName();


    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return null;
    }

}
