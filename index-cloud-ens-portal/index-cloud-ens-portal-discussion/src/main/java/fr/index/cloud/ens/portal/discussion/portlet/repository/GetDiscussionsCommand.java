package fr.index.cloud.ens.portal.discussion.portlet.repository;

import java.util.Set;

import org.nuxeo.ecm.automation.client.Constants;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import fr.index.cloud.ens.portal.discussion.portlet.model.DiscussionDocument;
import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoQueryFilter;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoQueryFilterContext;

/**
 * Get Discussion Nuxeo command.
 *
 * @author Jean-Sébastien Steux
 * @see INuxeoCommand
 */

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)

public class GetDiscussionsCommand implements INuxeoCommand {



    String user;
    Set<String> webIds;
    
    /**
     * Constructor.
     * 

     */
    public GetDiscussionsCommand( String user, Set<String> webIds) {
       this.user = user;
       this.webIds = webIds;

    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Object execute(Session nuxeoSession) throws Exception {
        
        // Query
        StringBuilder query = new StringBuilder();

        for (String webId: webIds) {
            if( query.length() == 0) 
                query.append(" ( ");
            else
                query.append(" OR ");
            
            query.append("( disc:type = '"+DiscussionDocument.TYPE_USER_COPY+"' AND disc:target = '"+webId+"' )");
        }
        
        if( query.length() > 0)
            query.append(" ) ");            
        

        if( user != null)   {
            if( query.length() > 0)
                query.append(" OR ");            
            
            query.append(" (disc:participants/* = '"+user+"') ");
        }
        
        // Query filter
        NuxeoQueryFilterContext queryFilterContext = new NuxeoQueryFilterContext(NuxeoQueryFilterContext.STATE_LIVE);
        String filteredRequest = NuxeoQueryFilter.addPublicationFilter(queryFilterContext, query.toString());
        
        // Operation request
        OperationRequest request = nuxeoSession.newRequest("Document.QueryES");
        request.set(Constants.HEADER_NX_SCHEMAS, "dublincore, toutatice, discussion");
        request.set("query", filteredRequest);

        return request.execute();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.getClass().getName());

        return builder.toString();
    }

}
