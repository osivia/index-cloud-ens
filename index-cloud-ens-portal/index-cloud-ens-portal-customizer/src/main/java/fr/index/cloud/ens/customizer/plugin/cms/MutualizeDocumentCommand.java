package fr.index.cloud.ens.customizer.plugin.cms;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.adapters.DocumentService;
import org.nuxeo.ecm.automation.client.model.DocRef;
import org.nuxeo.ecm.automation.client.model.PathRef;

/**
 * Mutualize document Nuxeo command.
 *
 * @author Cédric Krommenhoek
 * @see INuxeoCommand
 */
public class MutualizeDocumentCommand implements INuxeoCommand {

    /**
     * Section path.
     */
    private static final String SECTION_PATH = System.getProperty("config.mutualized.path");


    /**
     * Document path.
     */
    private final String path;


    /**
     * Constructor.
     *
     * @param path document path
     */
    public MutualizeDocumentCommand(String path) {
        this.path = path;
    }


    @Override
    public Object execute(Session nuxeoSession) throws Exception {
        // Document service
        DocumentService documentService = nuxeoSession.getAdapter(DocumentService.class);

        // Document reference
        DocRef document = new PathRef(this.path);
        // Publication section reference
        DocRef section = new PathRef(SECTION_PATH);

        return documentService.publish(document, section);
    }


    @Override
    public String getId() {
        return null;
    }

}
