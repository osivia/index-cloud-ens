package fr.index.cloud.ens.statistics.operation;

import org.nuxeo.ecm.automation.core.Constants;
import org.nuxeo.ecm.automation.core.annotations.Context;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * Update document statistics operation.
 *
 * @author Cédric Krommenhoek
 */
@Operation(id = UpdateDocumentStatisticsOperation.ID, category = Constants.CAT_DOCUMENT, label = "Update statistics")
public class UpdateDocumentStatisticsOperation {

    /**
     * Operation identifier.
     */
    public static final String ID = "Index.UpdateStatistics";


    /**
     * Core session.
     */
    @Context
    private CoreSession session;


    /**
     * Constructor.
     */
    public UpdateDocumentStatisticsOperation() {
        super();
    }


    /**
     * Run operation.
     *
     * @param document document
     */
    @OperationMethod
    public void run(DocumentModel document) {
        // Silent run
        UpdateDocumentStatisticsRunner runner = new UpdateDocumentStatisticsRunner(this.session, document);
        runner.silentRun(false);
    }

}
