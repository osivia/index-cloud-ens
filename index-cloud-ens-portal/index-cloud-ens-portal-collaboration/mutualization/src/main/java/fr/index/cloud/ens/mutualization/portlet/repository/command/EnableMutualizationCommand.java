package fr.index.cloud.ens.mutualization.portlet.repository.command;

import fr.index.cloud.ens.mutualization.portlet.model.MutualizationForm;
import fr.index.cloud.ens.mutualization.portlet.repository.MutualizationRepository;
import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import net.sf.json.JSONArray;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.adapters.DocumentService;
import org.nuxeo.ecm.automation.client.model.DocRef;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.PathRef;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

/**
 * Enable mutualization Nuxeo command.
 *
 * @author Cédric Krommenhoek
 * @see INuxeoCommand
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class EnableMutualizationCommand implements INuxeoCommand {

    /**
     * Form.
     */
    private final MutualizationForm form;
    /**
     * Document path.
     */
    private final String documentPath;
    /**
     * Mutualized space path.
     */
    private final String mutualizedSpacePath;


    /**
     * Constructor.
     *
     * @param form                form
     * @param documentPath        document path
     * @param mutualizedSpacePath mutualized space path
     */
    public EnableMutualizationCommand(MutualizationForm form, String documentPath, String mutualizedSpacePath) {
        super();
        this.form = form;
        this.documentPath = documentPath;
        this.mutualizedSpacePath = mutualizedSpacePath;
    }


    @Override
    public Object execute(Session nuxeoSession) throws Exception {
        // Document service
        DocumentService documentService = nuxeoSession.getAdapter(DocumentService.class);

        // Document reference
        DocRef document = new PathRef(this.documentPath);
        // Publication section reference
        DocRef section = new PathRef(this.mutualizedSpacePath);


        // Updated properties
        PropertyMap properties = new PropertyMap();
        properties.set(MutualizationRepository.ENABLE_PROPERTY, true);
        properties.set(MutualizationRepository.TITLE_PROPERTY, this.form.getTitle());
        properties.set(MutualizationRepository.KEYWORDS_PROPERTY, StringUtils.trimToNull(StringUtils.join(this.form.getKeywords(), ",")));
        properties.set(MutualizationRepository.DOCUMENT_TYPES_PROPERTY, StringUtils.trimToNull(StringUtils.join(this.form.getDocumentTypes(), ",")));
        properties.set(MutualizationRepository.LEVELS_PROPERTY, StringUtils.trimToNull(StringUtils.join(this.form.getLevels(), ",")));
        properties.set(MutualizationRepository.SUBJECTS_PROPERTY, StringUtils.trimToNull(StringUtils.join(this.form.getSubjects(), ",")));

        // Updated document
        Document updatedDocument = documentService.update(document, properties);

        return documentService.publish(updatedDocument, section, true);
    }


    /**
     * Convert list to JSON
     *
     * @param values list values
     * @return JSON
     */
    private String convert(Collection<String> values) {
        String result;

        if (CollectionUtils.isEmpty(values)) {
            result = null;
        } else {
            JSONArray array = new JSONArray();
            for (String value : values) {
                array.add(value);
            }
            result = array.toString();
        }

        return result;
    }


    @Override
    public String getId() {
        return null;
    }

}
