package fr.index.cloud.ens.statistics.operation;

import fr.toutatice.ecm.platform.core.helper.ToutaticeSilentProcessRunnerHelper;
import org.nuxeo.ecm.automation.core.util.DocumentHelper;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.model.Property;

import java.io.IOException;

/**
 * Update document statistics runner.
 *
 * @author Cédric Krommenhoek
 * @see ToutaticeSilentProcessRunnerHelper
 */
public class UpdateDocumentStatisticsRunner extends ToutaticeSilentProcessRunnerHelper {

    /**
     * Views XPath.
     */
    private static final String VIEWS_XPATH = "mtz:views";


    /**
     * Document.
     */
    private final DocumentModel document;


    /**
     * Constructor.
     *
     * @param session  session
     * @param document document
     */
    public UpdateDocumentStatisticsRunner(CoreSession session, DocumentModel document) {
        super(session);
        this.document = document;
    }


    @Override
    public void run() throws ClientException {
        Property property = this.document.getProperty(VIEWS_XPATH);
        Integer views = property.getValue(Integer.class);

        try {
            DocumentHelper.setProperty(this.session, this.document, VIEWS_XPATH, String.valueOf(views + 1));
        } catch (IOException e) {
            throw new ClientException(e);
        }

        this.session.saveDocument(this.document);
    }

}
