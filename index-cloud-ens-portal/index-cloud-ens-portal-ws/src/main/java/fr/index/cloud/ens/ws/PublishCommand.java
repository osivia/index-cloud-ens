package fr.index.cloud.ens.ws;

import org.apache.commons.lang.StringUtils;
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


    /**
     * Constructor.
     */
    public PublishCommand(String contentId,  PropertyMap qualifiers) {
        super();
        this.contentId = contentId;
        this.qualifiers = qualifiers;
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


        documentService.update(docRef, qualifiers);
        
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
