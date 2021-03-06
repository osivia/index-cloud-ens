package fr.index.cloud.ens.maintenance.batch;


import org.nuxeo.ecm.automation.client.Constants;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;


/**
 * Get tasks Nuxeo command.
 *
 * @author Jean-Sébastien Steux
 * @see INuxeoCommand
 */
public class GetOpenTasksCommand implements INuxeoCommand {





    public GetOpenTasksCommand() {
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Object execute(Session nuxeoSession) throws Exception {

        OperationRequest request = null;

        // Query
        StringBuilder query = new StringBuilder();

        query.append("SELECT * FROM Document ");


        query.append("WHERE ecm:primaryType = 'TaskDoc' ");
        query.append("AND ecm:currentLifeCycleState = 'opened' ");

        request = nuxeoSession.newRequest("Document.QueryES");
        request.set(Constants.HEADER_NX_SCHEMAS, "dublincore, task");

        request.set("query", query.toString());

        return request.execute();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.getClass().getCanonicalName());
        return builder.toString();
    }

}
