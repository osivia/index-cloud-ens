package fr.index.cloud.ens.ws.commands;

import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.adapters.DocumentService;
import org.nuxeo.ecm.automation.client.model.DocRef;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.PropertyList;
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
    private final Document doc;

    
    /** meta-datas */
    Map<String, String> qualifiers;
    String format;
    String pubId;
    String pubTitle;
    String pubOrganization;


    /**
     * Constructor.
     */
    public PublishCommand(Document doc,  String format, String pubId, String pubTitle, String pubOrganization, Map<String, String> qualifiers) {
        super();
        this.doc = doc;
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

       
        DocumentService documentService = nuxeoSession.getAdapter(DocumentService.class);
    
        
        PropertyMap properties = new PropertyMap();        
        
        //
        String share = doc.getProperties().getString("rshr:linkId") ;
        if( StringUtils.isEmpty(share)) {
            share = ""+ System.currentTimeMillis();
            
            //TODO : Controle d'unicité
            properties.set( "rshr:linkId", share);
        }

        if( StringUtils.isNotEmpty(format))
            properties.set( "rshr:format", format);   
        
       
        CommandUtils.addToList(doc, properties,  qualifiers.get("level"), "idxcl:levels");        
        CommandUtils.addToList(doc, properties,  qualifiers.get("subject"), "idxcl:subjects");       
        
        documentService.update(doc, properties);
 
        
        PropertyMap value = new PropertyMap();
        value.set("pubId", pubId);
        value.set("pubTitle", pubTitle);
        value.set("pubOrganization", pubTitle);

     

        // Operation request
        OperationRequest request = nuxeoSession.newRequest("Document.AddComplexProperty");
        request.setInput(doc);
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
        builder.append(this.doc.getPath());

        return builder.toString();
    }

}
