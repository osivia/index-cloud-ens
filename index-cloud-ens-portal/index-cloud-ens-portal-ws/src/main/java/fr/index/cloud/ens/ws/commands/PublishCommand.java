package fr.index.cloud.ens.ws.commands;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.codec.binary.Base64;
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
 * Publish command.
 *
 * @author Jean-Sébastien Steux
 * @see INuxeoCommand
 */
public class PublishCommand implements INuxeoCommand {
    
    private final static String NATIVE_FORMAT = "native";
    

    /** Parent identifier. */
    private final Document doc;

    
    /** meta-datas */
    Map<String, String> qualifiers;
    
    /** publication format */    
    String format;


    /** publication target */    
    String pubOrganization;
    String pubGroup;
    String pubContext;
    
    /**
     * Constructor.
     */
    public PublishCommand(Document doc,  String format,  String pubOrganization, String pubGroup, String pubContext, Map<String, String> qualifiers) {
        super();
        this.doc = doc;
        this.format = format;
        this.qualifiers = qualifiers;

        this.pubOrganization = pubOrganization;
        this.pubGroup = pubGroup;       
        this.pubContext = pubContext;      
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Object execute(Session nuxeoSession) throws Exception {
        DocumentService documentService = nuxeoSession.getAdapter(DocumentService.class);
         
        PropertyMap properties = new PropertyMap();        
        
        String shareId = doc.getProperties().getString("rshr:linkId") ;
        if( StringUtils.isEmpty(shareId)) {
            shareId = IDGenerator.generateId();
            properties.set( "rshr:linkId", shareId);
        }
        
        Boolean enabledLink = doc.getProperties().getBoolean("rshr:enabledLink", false) ;
        if( !enabledLink)
            properties.set( "rshr:enabledLink", true);
     
        

        if( StringUtils.isNotEmpty(format) && !NATIVE_FORMAT.equals(format))
            properties.set( "rshr:format", format);   
       
        CommandUtils.addToList(doc, properties,  qualifiers.get("level"), "idxcl:levels");        
        CommandUtils.addToList(doc, properties,  qualifiers.get("subject"), "idxcl:subjects");       
        
        documentService.update(doc, properties);
        
        
        if(NATIVE_FORMAT.equals(format))    {
            documentService.removeProperty(doc, "rshr:format");           
        }
        
        PropertyMap value = new PropertyMap();
        String pubId = IDGenerator.generateId();
        value.set("pubId", pubId);
        value.set("pubOrganization", pubOrganization);
        value.set("pubGroup", pubGroup);   
        value.set("pubContext", pubContext);   
        
        // Operation request
        OperationRequest request = nuxeoSession.newRequest("Document.AddComplexProperty");
        request.setInput(doc);
        request.set("xpath", "rshr:targets");
        request.set("value", value);
        
        request.execute();        
        
        // Return 
        Map<String, String> returnMap = new HashMap<>();
        returnMap.put("pudId",pubId);
        returnMap.put("shareId",shareId);       
        return returnMap;
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
