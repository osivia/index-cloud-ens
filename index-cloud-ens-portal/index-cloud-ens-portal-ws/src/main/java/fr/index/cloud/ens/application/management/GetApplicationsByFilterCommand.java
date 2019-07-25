package fr.index.cloud.ens.application.management;

import org.nuxeo.ecm.automation.client.Constants;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.model.DocRef;
import org.nuxeo.ecm.automation.client.model.PropertyMap;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoQueryFilter;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoQueryFilterContext;


/**
 * Stores a refresh token
 * 
 * @author Jean-Sébastien
 */
public class GetApplicationsByFilterCommand implements INuxeoCommand {

    private String title;
    
    public GetApplicationsByFilterCommand(String title) {
        super();
        this.title = title;
    }

    @Override
    public Object execute(Session nuxeoSession) throws Exception {
        
        // TODO add webID search
        // TODO add filtering
        
        // Clause
        StringBuilder clause = new StringBuilder();
        clause.append("dc:title ILIKE  '%"+title+"%' ");
        
        
        clause.append("AND ecm:primaryType='OAuth2Application'");        

        // Operation request
        OperationRequest request = nuxeoSession.newRequest("Document.QueryES");
        request.set(Constants.HEADER_NX_SCHEMAS, "*");
        request.set("query", "SELECT * FROM Document WHERE " + clause.toString());

        return request.execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.getClass().getSimpleName());
        builder.append("|");
        builder.append(this.title);
        return builder.toString();
    }
}
