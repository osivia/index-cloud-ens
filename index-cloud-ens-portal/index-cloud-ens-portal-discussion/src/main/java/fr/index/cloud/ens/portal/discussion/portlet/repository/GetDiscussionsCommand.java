package fr.index.cloud.ens.portal.discussion.portlet.repository;

import org.nuxeo.ecm.automation.client.Constants;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoQueryFilter;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoQueryFilterContext;
import fr.toutatice.portail.cms.nuxeo.api.forms.IFormsService;

/**
 * Get Discussions
 * 
 * @author Jean-Sébastien Steux
 * @see INuxeoCommand
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class GetDiscussionsCommand implements INuxeoCommand {


    /**
     * Constructor.
     * 
     * @param basePath base path
     */
    public GetDiscussionsCommand() {
        super();

    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Object execute(Session nuxeoSession) throws Exception {
        // Clause
//        StringBuilder clause = new StringBuilder();
//        clause.append("ecm:primaryType = 'ProcedureInstance' ");

//        clause.append("AND pi:procedureModelWebId = '").append(TrashRepository.MODEL_ID).append("' ");
//        if (this.invitationState != null) {
//            clause.append("AND pi:globalVariablesValues.").append(MemberManagementRepository.INVITATION_STATE_PROPERTY).append(" = '")
//                    .append(this.invitationState.toString()).append("' ");
//        }
//        if (this.identifiers != null) {
//            clause.append("AND pi:globalVariablesValues.").append(MemberManagementRepository.PERSON_UID_PROPERTY).append(" IN (");
//            boolean first = true;
//            for (String member : this.identifiers) {
//                if (first) {
//                    first = false;
//                } else {
//                    clause.append(", ");
//                }
//
//                clause.append("'").append(member).append("'");
//            }
//            clause.append(") ORDER BY dc:created DESC");
//        }
        
//        clause.append(" ORDER BY dc:created DESC");
//
//        // Filtered clause
//        String filteredClause = NuxeoQueryFilter.addPublicationFilter(NuxeoQueryFilterContext.CONTEXT_LIVE, clause.toString());
//
//        // Operation request
//        OperationRequest request = nuxeoSession.newRequest("Document.QueryES");
//        request.set(Constants.HEADER_NX_SCHEMAS, "dublincore, procedureInstance");
//        request.set("query", "SELECT * FROM Document WHERE " + filteredClause);
//
//        return request.execute();
       
        
      StringBuilder query = new StringBuilder();
      query.append("SELECT * FROM Document ");
      query.append("WHERE ecm:primaryType = 'TaskDoc' ");
      query.append("AND ecm:currentLifeCycleState = 'opened' ");

//      query.append("AND nt:pi.pi:procedureModelWebId = '").append(TrashRepository.MODEL_ID).append("' ");

      
      // Operation request
      OperationRequest request = nuxeoSession.newRequest("Document.QueryES");
      request.set(Constants.HEADER_NX_SCHEMAS, "dublincore, task");
      request.set("query", query.toString());

      return request.execute();        
      
         
        
        // Query
//        StringBuilder query = new StringBuilder();
//        query.append("SELECT * FROM Document ");
//        query.append("WHERE ecm:primaryType = 'TaskDoc' ");
//        query.append("AND ecm:currentLifeCycleState = 'opened' ");
//        if (this.actors != null) {
//            query.append("AND nt:actors/* IN (").append(this.actors).append(") ");
//        }
//        if ((this.notifiable != null) || (this.directives != null)) {
//            query.append("AND (");
//            if (this.notifiable != null) {
//                query.append("nt:task_variables.notifiable = '").append(this.notifiable).append("'");
//
//                if (this.directives != null) {
//                    query.append(" OR ");
//                }
//            }
//            if (this.directives != null) {
//                query.append("nt:directive IN (").append(this.directives).append(")");
//            }
//            query.append(") ");
//        }
//        if (this.path != null) {
//            query.append("AND ecm:path = '").append(this.path).append("' ");
//        }
//        if (this.uuid != null) {
//            query.append("AND nt:pi.pi:globalVariablesValues.uuid = '").append(this.uuid).append("' ");
//        }
//
//        // Operation request
//        OperationRequest request = nuxeoSession.newRequest("Document.QueryES");
//        request.set(Constants.HEADER_NX_SCHEMAS, "dublincore, task");
//        request.set("query", query.toString());
//
//        return request.execute();        
        

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
