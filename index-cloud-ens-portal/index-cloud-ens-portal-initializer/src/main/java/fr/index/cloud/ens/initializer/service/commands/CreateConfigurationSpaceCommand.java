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
 * Create configuration space Nuxeo command.
 *
 * @author Jean-Sébastien Steux
 * @see INuxeoCommand
 */
public class CreateConfigurationSpaceCommand implements INuxeoCommand {

    /**
     * Log.
     */
    private final Log log;


    /**
     * Constructor.
     */
    public CreateConfigurationSpaceCommand() {
        super();

        // Log
        this.log = LogFactory.getLog(CreateConfigurationSpaceCommand.class);
    }


    @Override
    public Object execute(Session nuxeoSession) throws Exception {
        // Document service
        DocumentService documentService = nuxeoSession.getAdapter(DocumentService.class);

        // Domain
        Document domain = documentService.getDocument(new PathRef("/default-domain/workspaces"));

        // Publication space
        URL configurationSpaceUrl = this.getClass().getResource("/docs/configuration-space/export-configuration-space.zip");
        File configurationSpaceFile = new File(configurationSpaceUrl.getFile());
        Blob configurationSpaceBlob = new FileBlob(configurationSpaceFile);

        OperationRequest operationRequest = nuxeoSession.newRequest("FileManager.Import").setInput(configurationSpaceBlob);
        operationRequest.setContextProperty("currentDocument", domain);
        operationRequest.set("overwite", String.valueOf(true));
        operationRequest.execute();



        return null;
    }


    @Override
    public String getId() {
        return this.getClass().getSimpleName();
    }

}
