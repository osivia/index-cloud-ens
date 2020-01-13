package fr.index.cloud.ens.ws.commands;

import java.util.Date;
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
import org.osivia.portal.api.cms.IDGenerator;

import fr.index.cloud.ens.ws.beans.PublishBean;
import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;

/**
 * Publish command.
 *
 * @author Jean-Sébastien Steux
 * @see INuxeoCommand
 */
public class AddPropertiesCommand implements INuxeoCommand {
    

    

    /** Parent identifier. */
    private final Document doc;

    
    /** meta-datas */
    Map<String, String> qualifiers;
    
  
    /**
     * Constructor.
     */
    public AddPropertiesCommand(Document doc,  Map<String, String> qualifiers) {
        super();
        this.doc = doc;
        this.qualifiers = qualifiers;

    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Object execute(Session nuxeoSession) throws Exception {
        DocumentService documentService = nuxeoSession.getAdapter(DocumentService.class);
         
        PropertyMap properties = new PropertyMap();        
        
         
        PropertyList levels = doc.getProperties().getList( "idxcl:levels");
        if( levels == null)
            levels = new PropertyList();
        
        if( CommandUtils.addToList( levels, qualifiers.get("level")))
            properties.set("idxcl:levels", CommandUtils.convertToString(levels));
        
        PropertyList subjects = doc.getProperties().getList( "idxcl:subjects");
        if( subjects == null)
            subjects = new PropertyList();
        
        if( CommandUtils.addToList( subjects, qualifiers.get("subject")))
            properties.set("idxcl:subjects", CommandUtils.convertToString(subjects));     
        
        PropertyList documentTypes = doc.getProperties().getList( "idxcl:documentTypes");
        if( documentTypes == null)
            documentTypes = new PropertyList();
        
        if( CommandUtils.addToList( documentTypes, qualifiers.get("documentType")))
            properties.set("idxcl:documentTypes", CommandUtils.convertToString(documentTypes));     
  
        
        if( properties.size() > 0)  {
            // Operation request
            OperationRequest request = nuxeoSession.newRequest("Index.UpdateMetadata");
            request.setInput(doc);
            request.set("properties", properties);     
            
             
            request.execute();   
        }
        

        return null;
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
