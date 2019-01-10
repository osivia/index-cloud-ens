package org.osivia.demo.initializer.service.commands;

import java.io.File;
import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.adapters.DocumentService;
import org.nuxeo.ecm.automation.client.model.Blob;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.Documents;
import org.nuxeo.ecm.automation.client.model.FileBlob;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;

public class LoadRecordsCommand implements INuxeoCommand {

	private Log logger = LogFactory.getLog(LoadRecordsCommand.class);

	
	@Override
	public Object execute(Session nuxeoSession) throws Exception {

		DocumentService documentService = nuxeoSession.getAdapter(DocumentService.class);

        Documents workspaces = documentService.query("SELECT * FROM Worskapce WHERE webc:url = 'espace-de-test'");
		if(workspaces.size() >= 1 ) {
			
			logger.info("Load records...");
			
			Document workspace = workspaces.get(0);
			
			URL recordsUrl = this.getClass().getResource("/docs/records/export-records.zip");
			File records = new File(recordsUrl.getFile());
			Blob blob = new FileBlob(records);
			
			OperationRequest operationRequest = nuxeoSession.newRequest("FileManager.Import").setInput(blob);
	        operationRequest.setContextProperty("currentDocument", workspace);
	        operationRequest.set("overwite", "true");

	        operationRequest.execute();
	        
		}
        

		
		return null;
	}

	@Override
	public String getId() {
		return this.getClass().getSimpleName();
	}

}
