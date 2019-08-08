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
public class PublishCommand implements INuxeoCommand {
    
    private final static String DEFAULT_FORMAT = "default";
    

    /** Parent identifier. */
    private final Document doc;

    

    /** organization */
    String organization;

    /** publication target */    
    PublishBean publishBean;
    
    /**
     * Constructor.
     */
    public PublishCommand(Document doc,  PublishBean publishBean, String organization) {
        super();
        this.doc = doc;
        this.organization= organization;


        this.publishBean = publishBean;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Object execute(Session nuxeoSession) throws Exception {
        DocumentService documentService = nuxeoSession.getAdapter(DocumentService.class);
         
        PropertyMap properties = new PropertyMap();        
        

        Boolean enabledLink = doc.getProperties().getBoolean("rshr:enabledLink", false) ;
        if( !enabledLink)
            properties.set( "rshr:enabledLink", true);
          
       
        documentService.update(doc, properties);
        

        
        PropertyMap value = new PropertyMap();
        String pubId = IDGenerator.generateId();
        value.set("pubId", pubId);
        

        value.set("pubOrganization", organization);
        value.set("pubGroup", publishBean.getPubGroup());   
        value.set("pubContext",publishBean.getPubContext());
        value.set("pubSchoolYear",publishBean.getPubSchoolYear());
        
        value.set("pubDate", new Date(System.currentTimeMillis()));
        
         // Operation request
        OperationRequest request = nuxeoSession.newRequest("Document.AddComplexProperty");
        request.setInput(doc);
        request.set("xpath", "rshr:targets");
        request.set("value", value);
        
        request.execute();        
        
        // Return 
        Map<String, String> returnMap = new HashMap<>();
        returnMap.put("pubId",pubId);
     
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
