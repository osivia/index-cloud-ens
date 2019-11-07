package fr.index.cloud.ens.antivirus;

import org.nuxeo.ecm.automation.client.Constants;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoQueryFilter;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoQueryFilterContext;


/**
 * Retrieve quarantine file
 * 
 * @author Jean-Sébastien
 */
public class GetQuaratineFilesCommand implements INuxeoCommand {


    public GetQuaratineFilesCommand() {
        super();

    }

    @Override
    public Object execute(Session nuxeoSession) throws Exception {


        String clause = "idxvs:quarantineDate is not null";

        // NXQL filtered request
        String filteredRequest = NuxeoQueryFilter.addPublicationFilter(NuxeoQueryFilterContext.CONTEXT_LIVE, clause);


        // Operation request
        OperationRequest request = nuxeoSession.newRequest("Document.QueryES");
        request.set(Constants.HEADER_NX_SCHEMAS, "*");

        request.set("query", "SELECT * FROM Document WHERE " + filteredRequest);
        return request.execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.getClass().getSimpleName());
        return builder.toString();
    }
}
