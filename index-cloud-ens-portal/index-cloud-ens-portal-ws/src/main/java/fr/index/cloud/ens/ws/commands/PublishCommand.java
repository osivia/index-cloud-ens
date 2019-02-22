package fr.index.cloud.ens.ws.commands;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.adapters.DocumentService;
import org.nuxeo.ecm.automation.client.model.DocRef;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.PropertyMap;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;

/**
 * Upload files command.
 *
 * @author Jean-Sébastien Steux
 * @see INuxeoCommand
 */
public class PublishCommand implements INuxeoCommand {

    /** Parent identifier. */
    private final String contentId;

    
    /** meta-datas */
    PropertyMap qualifiers;
    String format;
    String pubId;
    String pubTitle;
    String pubOrganization;


    /**
     * Constructor.
     */
    public PublishCommand(String contentId,  String format, String pubId, String pubTitle, String pubOrganization, PropertyMap qualifiers) {
        super();
        this.contentId = contentId;
        this.format = format;
        this.qualifiers = qualifiers;
        this.pubId = pubId;
        this.pubTitle = pubTitle;
        this.pubOrganization = pubOrganization;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Object execute(Session nuxeoSession) throws Exception {

        DocRef docRef = new DocRef(contentId);
        
        DocumentService documentService = nuxeoSession.getAdapter(DocumentService.class);
        Document doc = documentService.getDocument(docRef);        
        
        //
        String share = doc.getProperties().getString("rshr:linkId") ;
        if( StringUtils.isEmpty(share)) {
            share = ""+ System.currentTimeMillis();
            
            //TODO : Controle d'unicité
            documentService.setProperty(docRef, "rshr:linkId", share);
        }

        if( StringUtils.isNotEmpty(format))
            documentService.setProperty(docRef, "rshr:format", format);            

        documentService.update(docRef, qualifiers);
        
        PropertyMap value = new PropertyMap();
        value.set("pubId", pubId);
        value.set("pubTitle", pubTitle);
        value.set("pubOrganization", pubTitle);

     

        // Operation request
        OperationRequest request = nuxeoSession.newRequest("Document.AddComplexProperty");
        request.setInput(docRef);
        request.set("xpath", "rshr:targets");
        request.set("value", value);
        
        request.execute();        
        
        return share;
    }



    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.getClass().getSimpleName());
        builder.append(" : ");
        builder.append(this.contentId);

        return builder.toString();
    }

}
