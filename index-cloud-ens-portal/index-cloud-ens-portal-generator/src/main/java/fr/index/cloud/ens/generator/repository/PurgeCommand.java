package fr.index.cloud.ens.generator.repository;

import io.codearte.jfairy.Fairy;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;
import org.nuxeo.ecm.automation.client.Constants;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.adapters.DocumentService;
import org.nuxeo.ecm.automation.client.model.Blob;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.Documents;
import org.nuxeo.ecm.automation.client.model.FileBlob;
import org.nuxeo.ecm.automation.client.model.PathRef;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.directory.v2.service.PersonUpdateService;
import org.osivia.portal.api.directory.v2.model.Person;
import org.osivia.portal.core.constants.InternalConstants;
import org.springframework.beans.factory.annotation.Autowired;

import fr.index.cloud.ens.generator.model.Configuration;
import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoQueryFilter;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoQueryFilterContext;

/**
 * Generate command.
 *
 * @author Jean-Sébastien Steux
 * @see INuxeoCommand
 */
public class PurgeCommand implements INuxeoCommand {


    private static Logger LOGGER = Logger.getLogger(PurgeCommand.class);

    /** Generator configuration. */
    private final Configuration configuration;


    private String userPrefix;

    private PersonUpdateService personService;


    /**
     * Constructor.
     *
     * @param ldapContext LDAP context
     * @param configuration generator configuration
     * @param exampleFile
     * @param userId userId
     */
    public PurgeCommand(Configuration configuration, PersonUpdateService personService,  String userPrefix) {
        super();
        this.configuration = configuration;
        this.userPrefix = userPrefix;
        this.personService = personService;


    }

    public Document getUserProfile(Session automationSession, String userId) throws Exception {

        OperationRequest newRequest = automationSession.newRequest("Services.GetToutaticeUserProfile");
        newRequest.set("username", userId);

        Document refDoc = (Document) newRequest.execute();

        Document doc = (Document) automationSession.newRequest("Document.FetchLiveDocument").setHeader(Constants.HEADER_NX_SCHEMAS, "*").set("value", refDoc)
                .execute();

        return doc;

    }
   

        
        
        
        /* Delete publications */
        
        /**
         * {@inheritDoc}
         */
        @Override
        public Object execute(Session nuxeoSession) throws Exception {

            DocumentService documentService = nuxeoSession.getAdapter(DocumentService.class);
            
    
            Person personsToDeleteQuery = personService.getEmptyPerson();
            String uid = userPrefix+"*";
            personsToDeleteQuery.setUid(uid);


            List<Person> personsToDelete = personService.findByCriteria(personsToDeleteQuery);

            while (!personsToDelete.isEmpty()) {

                for (Person p : personsToDelete) {
                    
      
                    LOGGER.warn("deleting user " + p.getUid());
      
                    String profilePath = getUserProfile(nuxeoSession, p.getUid()).getPath();
                    String workspacePath = profilePath.substring(0, profilePath.lastIndexOf('/'));
                    
                    // Operation request
                    OperationRequest request = nuxeoSession.newRequest("Document.Delete");
                    request.setInput(new PathRef(workspacePath));
                    request.execute();
                    
                    
                    personService.delete(p);

                }

                personsToDelete = personService.findByCriteria(personsToDeleteQuery);
            }
        
            
            
            /* Delete publications */
            
            // Nuxeo request
            StringBuilder nuxeoRequest = new StringBuilder();
            nuxeoRequest.append("ecm:path STARTSWITH '/default-domain/communaute' AND dc:title ILIKE 'fichier-tmc%'");

            LOGGER.warn("searching " + nuxeoRequest.toString());


            // Query filter
            NuxeoQueryFilterContext queryFilterContext = new NuxeoQueryFilterContext(NuxeoQueryFilterContext.STATE_DEFAULT,
                    InternalConstants.PORTAL_CMS_REQUEST_FILTERING_POLICY_NO_FILTER);
            String filteredRequest = NuxeoQueryFilter.addPublicationFilter(queryFilterContext, nuxeoRequest.toString());

            // Operation request
            OperationRequest operationRequest = nuxeoSession.newRequest("Document.QueryES");
            operationRequest.setHeader(Constants.HEADER_NX_SCHEMAS, "*");
            operationRequest.set("query", "SELECT * FROM Document WHERE " + filteredRequest);
            operationRequest.set("pageSize", 1000);
            operationRequest.set("currentPageIndex", 0);
            
            
            Documents publications =  (Documents) operationRequest.execute();
            
            for(Document publication:publications) {
                LOGGER.warn("deleting publication " + publication.getTitle());
                documentService.remove(publication);
            }
            
            
            return null;
            
            
        }


       


   

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return "purge-" + userPrefix;
    }


}
