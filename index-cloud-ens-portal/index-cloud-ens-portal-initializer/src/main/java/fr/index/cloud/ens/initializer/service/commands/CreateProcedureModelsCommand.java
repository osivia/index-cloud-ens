package fr.index.cloud.ens.initializer.service.commands;

import java.io.File;
import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.model.Blob;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.FileBlob;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;

public class CreateProcedureModelsCommand implements INuxeoCommand {

	private Log logger = LogFactory.getLog(CreateProcedureModelsCommand.class);
	
	private Document modelsContainer;
	
	public CreateProcedureModelsCommand(Document modelsContainer) {
		this.modelsContainer = modelsContainer;
	}

	@Override
	public Object execute(Session nuxeoSession) throws Exception {
		
		URL proceduresUrl = this.getClass().getResource("/docs/models/");
		File dir = new File(proceduresUrl.getFile());
		File[] procedures = dir.listFiles();
		for (int i = 0; i < procedures.length; i++) {
			
			
			
			logger.info("Add procedure : " + procedures[i].getName());
			
			try {
				
				Blob blob = new FileBlob(procedures[i]);
				
				OperationRequest operationRequest = nuxeoSession.newRequest("FileManager.Import").setInput(blob);
		        operationRequest.setContextProperty("currentDocument", this.modelsContainer.getId());
		        operationRequest.set("overwite", "true");

		        operationRequest.execute();
			}
			catch(Exception e) {
				logger.error("Error when importing procedure : " + procedures[i].getName());

			}
		}
		
		return null;
		
	}

	@Override
	public String getId() {
		return this.getClass().getSimpleName();
	}

}
