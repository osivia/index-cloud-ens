package fr.index.cloud.ens.ws.commands;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.OperationRequest;
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
public class UnpublishCommand implements INuxeoCommand {

    /** Parent identifier. */
    private final String contentId;
    private final int indice;


    /**
     * Constructor.
     */
    public UnpublishCommand(String contentId, int indice) {
        super();
        this.contentId = contentId;
        this.indice = indice;

    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Object execute(Session nuxeoSession) throws Exception {

        DocRef docRef = new DocRef(contentId);
      
        
        // Operation request
        OperationRequest request = nuxeoSession.newRequest("Document.RemoveProperty");
        request.setInput(docRef);
        request.set("xpath", "rshr:targets/"+indice);
       
        request.execute();

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
        builder.append(this.contentId);

        return builder.toString();
    }

}
