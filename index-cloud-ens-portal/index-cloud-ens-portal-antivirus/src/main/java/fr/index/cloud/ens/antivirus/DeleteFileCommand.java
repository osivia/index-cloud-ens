package fr.index.cloud.ens.antivirus;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.model.Blob;
import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.core.cms.CMSPublicationInfos;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Delete file command.
 *
 * @author Jean-Sébastien Steux
 * @see INuxeoCommand
 */
public class DeleteFileCommand implements INuxeoCommand {

    /** Parent identifier. */
    private final Document doc;


    /**
     * Constructor.
     */
    public DeleteFileCommand(Document doc) {
        super();
        this.doc = doc;

    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Object execute(Session nuxeoSession) throws Exception {
        OperationRequest operationUpdateRequest = nuxeoSession.newRequest("Document.Delete").setInput(doc);
        return operationUpdateRequest.execute();
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
