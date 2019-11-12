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
 * Scan file command.
 *
 * @author Jean-Sébastien Steux
 * @see INuxeoCommand
 */
public class ScanFileCommand implements INuxeoCommand {

    /** Parent identifier. */
    private final Document doc;


    /**
     * Constructor.
     */
    public ScanFileCommand(Document doc) {
        super();
        this.doc = doc;

    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Object execute(Session nuxeoSession) throws Exception {
        // Operation request
        OperationRequest request = nuxeoSession.newRequest("Index.ScanVirus");
        
        request.set("path", doc.getPath());


        int errorCode = 0;
        Blob binaries = (Blob) request.execute();

        if (binaries != null) {

            String response = IOUtils.toString(binaries.getStream(), "UTF-8");

            JSONArray infosContent = JSONArray.fromObject(response);
            Iterator<?> it = infosContent.iterator();
            while (it.hasNext()) {
                JSONObject infos = (JSONObject) it.next();

                errorCode = infos.getInt("error");
            }
        }

        return errorCode;
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
