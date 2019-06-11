package fr.index.cloud.ens.levels.portlet.repository.command;

import fr.index.cloud.ens.levels.portlet.repository.HighestLevelsRepository;
import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoQueryFilter;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoQueryFilterContext;
import org.nuxeo.ecm.automation.client.Constants;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.osivia.portal.core.constants.InternalConstants;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class GetDocumentsCommand implements INuxeoCommand {

    /**
     * Operation identifier.
     */
    private static final String OPERATION_ID = "Document.QueryES";
    /**
     * Schemas.
     */
    private static final String SCHEMAS = "dublincore, indexClassifiers";


    /**
     * Base path.
     */
    private final String basePath;


    /**
     * Constructor.
     *
     * @param basePath base path
     */
    public GetDocumentsCommand(String basePath) {
        super();
        this.basePath = basePath;
    }


    @Override
    public Object execute(Session nuxeoSession) throws Exception {
        // Nuxeo request
        StringBuilder nuxeoRequest = new StringBuilder();
        nuxeoRequest.append("ecm:path STARTSWITH '").append(this.basePath).append("' ");
        nuxeoRequest.append("AND ").append(HighestLevelsRepository.LEVELS_PROPERTY).append(" IS NOT NULL ");

        // Query filter
        NuxeoQueryFilterContext queryFilterContext = new NuxeoQueryFilterContext(NuxeoQueryFilterContext.STATE_LIVE,
                InternalConstants.PORTAL_CMS_REQUEST_FILTERING_POLICY_NO_FILTER);
        String filteredRequest = NuxeoQueryFilter.addPublicationFilter(queryFilterContext, nuxeoRequest.toString());

        // Operation request
        OperationRequest operationRequest = nuxeoSession.newRequest(OPERATION_ID);
        operationRequest.setHeader(Constants.HEADER_NX_SCHEMAS, SCHEMAS);
        operationRequest.set("query", "SELECT * FROM Document WHERE " + filteredRequest);

        return operationRequest.execute();
    }


    @Override
    public String getId() {
        return null;
    }

}
