package org.osivia.demo.customizer.plugin.cms;

import org.nuxeo.ecm.automation.client.Constants;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoQueryFilter;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoQueryFilterContext;

/**
 * Get records Nuxeo command.
 * 
 * @author Cédric Krommenhoek
 * @see INuxeoCommand
 */
public class GetRecordsCommand implements INuxeoCommand {

    /** Operation request identifier. */
    private static final String OPERATION_ID = "Document.QueryES";
    /** Request clause. */
    private static final String CLAUSE = "ecm:primaryType IN ('RecordFolder', 'Record')";
    /** Request schemas. */
    private static final String SCHEMAS = "dublincore, toutatice, record";


    /**
     * Constructor.
     */
    public GetRecordsCommand() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Object execute(Session nuxeoSession) throws Exception {
        // NXQL filtered request
        String filteredRequest = NuxeoQueryFilter.addPublicationFilter(NuxeoQueryFilterContext.CONTEXT_LIVE, CLAUSE);

        // Operation request
        OperationRequest operationRequest = nuxeoSession.newRequest(OPERATION_ID);
        operationRequest.set("query", "SELECT * FROM Document WHERE " + filteredRequest);
        operationRequest.set(Constants.HEADER_NX_SCHEMAS, SCHEMAS);

        return operationRequest.execute();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return this.getClass().getSimpleName();
    }

}
