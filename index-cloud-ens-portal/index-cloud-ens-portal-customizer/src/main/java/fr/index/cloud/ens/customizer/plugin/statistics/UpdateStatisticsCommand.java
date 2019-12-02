package fr.index.cloud.ens.customizer.plugin.statistics;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.model.DocRef;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.Documents;
import org.nuxeo.ecm.automation.client.model.PathRef;

/**
 * Update statistics Nuxeo command.
 *
 * @author Cédric Krommenhoek
 * @see INuxeoCommand
 */
public class UpdateStatisticsCommand implements INuxeoCommand {

    /**
     * Get live document operation identifier.
     */
    private static final String GET_LIVE_OPERATION_ID = "Proxy.GetSourceDocument";
    /**
     * Update statistics operation identifier.
     */
    private static final String UPDATE_STATISTICS_OPERATION_ID = "Index.UpdateStatistics";


    /**
     * Document path.
     */
    private final String path;


    /**
     * Constructor.
     *
     * @param path document path
     */
    public UpdateStatisticsCommand(String path) {
        super();
        this.path = path;
    }


    @Override
    public Object execute(Session nuxeoSession) throws Exception {
        // Live
        Document live = this.getLive(nuxeoSession);

        if (live != null) {
            // Update statistics
            this.updateStatistics(nuxeoSession, live);
        }

        return null;
    }


    /**
     * Get live document.
     *
     * @param nuxeoSession Nuxeo session
     * @return document
     */
    private Document getLive(Session nuxeoSession) throws Exception {
        OperationRequest request = nuxeoSession.newRequest(GET_LIVE_OPERATION_ID);

        // Proxy reference
        DocRef proxy = new PathRef(this.path);
        request.setInput(proxy);

        // Results
        Documents documents = (Documents) request.execute();

        // Live
        Document live;
        if ((documents == null) || documents.isEmpty()) {
            live = null;
        } else {
            live = documents.get(0);
        }

        return live;
    }


    /**
     * Update statistics.
     *
     * @param nuxeoSession Nuxeo session
     * @param live         live document
     */
    private void updateStatistics(Session nuxeoSession, Document live) throws Exception {
        OperationRequest request = nuxeoSession.newRequest(UPDATE_STATISTICS_OPERATION_ID);
        request.setInput(live);
        request.execute();
    }


    @Override
    public String getId() {
        return null;
    }

}
