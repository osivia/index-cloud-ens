package fr.index.cloud.ens.initializer.service.commands;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.adapters.DocumentService;
import org.nuxeo.ecm.automation.client.model.*;

import java.io.File;
import java.net.URL;

/**
 * Create mutualized space Nuxeo command.
 *
 * @author Cédric Krommenhoek
 * @see INuxeoCommand
 */
public class CreateMutualizedSpaceCommand implements INuxeoCommand {

    /**
     * Log.
     */
    private final Log log;


    public CreateMutualizedSpaceCommand() {
        super();

        // Log
        this.log = LogFactory.getLog(this.getClass());
    }


    @Override
    public Object execute(Session nuxeoSession) throws Exception {
        // Document service
        DocumentService documentService = nuxeoSession.getAdapter(DocumentService.class);

        // Domain
        Document domain = documentService.getDocument(new PathRef("/default-domain"));

        // Mutualized space
        URL mutualizedSpaceUrl = this.getClass().getResource("/docs/mutualized-space/export-mutualized-space.zip");
        File mutualizedSpaceFile = new File(mutualizedSpaceUrl.getFile());
        Blob mutualizedSpaceBlob = new FileBlob(mutualizedSpaceFile);

        OperationRequest operationRequest = nuxeoSession.newRequest("FileManager.Import").setInput(mutualizedSpaceBlob);
        operationRequest.setContextProperty("currentDocument", domain);
        operationRequest.set("overwite", String.valueOf(true));
        operationRequest.execute();


        // Mass publication
        Documents documents = documentService.query("SELECT * FROM Document WHERE ecm:path STARTSWITH '/default-domain/communaute' AND ecm:primaryType <> 'PortalSite'");
        for (Document document : documents) {
            this.log.info("Publish document : " + document.getPath());

            operationRequest = nuxeoSession.newRequest("Document.SetOnLineOperation").setInput(document);
            operationRequest.execute();
        }

        return null;
    }


    @Override
    public String getId() {
        return null;
    }

}
