package fr.index.cloud.ens.ws.commands;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.adapters.DocumentService;
import org.nuxeo.ecm.automation.client.model.Blob;
import org.nuxeo.ecm.automation.client.model.Blobs;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.nuxeo.ecm.automation.client.model.StreamBlob;
import org.springframework.web.multipart.MultipartFile;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;

/**
 * Upload files command.
 *
 * @author Jean-Sébastien Steux
 * @see INuxeoCommand
 */
public class UploadFileCommand implements INuxeoCommand {

    /** Parent identifier. */
    private final String parentId;
    /** File items. */
    private final MultipartFile file;

    /** overwite */
    private boolean overwite;

    /** Synchronized ES indexation flag. */
    public static final String ES_SYNC_FLAG = "nx_es_sync";
    
    /** meta-datas */
    Map<String,String> qualifiers;


    /**
     * Constructor.
     */
    public UploadFileCommand(String parentId, MultipartFile file, Map<String,String> qualifiers) {
        super();
        this.parentId = parentId;
        this.file = file;
        this.overwite = true;
        this.qualifiers = qualifiers;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Object execute(Session nuxeoSession) throws Exception {
        Blobs blobs = this.getBlobs();

        // Operation request
        OperationRequest operationRequest = nuxeoSession.newRequest("FileManager.Import").setInput(blobs).setHeader(ES_SYNC_FLAG, "true");
        operationRequest.setContextProperty("currentDocument", this.parentId);
        operationRequest.set("overwite", overwite);

        Document doc = (Document) operationRequest.execute();
        
        DocumentService documentService = nuxeoSession.getAdapter(DocumentService.class);
        
        
        PropertyMap properties = new PropertyMap();     
        
        CommandUtils.addToList(doc, properties,  qualifiers.get("level"), "idxcl:levels");        
        CommandUtils.addToList(doc, properties,  qualifiers.get("subject"), "idxcl:subjects");       
        
        documentService.update(doc, properties);
        
        return doc;
    }


    /**
     * Build a blobs list from input files items.
     *
     * @param fileItems
     * @return blobs list
     * @throws IOException
     */
    public Blobs getBlobs() throws IOException {
        Blobs blobs = new Blobs();

        String name = file.getOriginalFilename();
        Blob blob = new StreamBlob(file.getInputStream(), name, file.getContentType());
        blobs.add(blob);


        return blobs;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.getClass().getSimpleName());
        builder.append(" : ");
        builder.append(this.parentId);
        builder.append(" ; ");
        builder.append(this.file);
        return builder.toString();
    }

}
