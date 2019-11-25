package fr.index.cloud.ens.mutualization.copy.portlet.repository.command;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.adapters.DocumentService;
import org.nuxeo.ecm.automation.client.model.*;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Mutualization copy command.
 *
 * @author Cédric Krommenhoek
 * @see INuxeoCommand
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class MutualizationCopyCommand implements INuxeoCommand {

    /**
     * Source document.
     */
    private final Document source;
    /**
     * Target parent path.
     */
    private final String targetParentPath;


    /**
     * Constructor.
     *
     * @param source           source document
     * @param targetParentPath target parent path
     */
    public MutualizationCopyCommand(Document source, String targetParentPath) {
        super();
        this.source = source;
        this.targetParentPath = targetParentPath;
    }


    @Override
    public Object execute(Session nuxeoSession) throws Exception {
        // Document service
        DocumentService documentService = nuxeoSession.getAdapter(DocumentService.class);

        // Parent document reference
        DocRef parent = new PathRef(this.targetParentPath);

        // Document creation
        DocRef target = documentService.createDocument(parent, this.source.getType(), null, this.getProperties());

        // Copy file BLOB
        this.copyBlob(documentService, target);

        return target;
    }


    /**
     * Get properties.
     *
     * @return properties
     */
    private PropertyMap getProperties() {
        PropertyMap properties = new PropertyMap();

        // Title
        properties.set("dc:title", this.source.getTitle());
        // Source webId
        properties.set("mtz:sourceWebId", this.source.getString("ttc:webid"));
        // Keywords
        properties.set("mtz:keywords", this.getListProperty("mtz:keywords"));
        // Document types
        properties.set("idxcl:documentTypes", this.getListProperty("idxcl:documentTypes"));
        // Levels
        properties.set("idxcl:levels", this.getListProperty("idxcl:levels"));
        // Subjects
        properties.set("idxcl:subjects", this.getListProperty("idxcl:subjects"));

        return properties;
    }


    /**
     * Get list property.
     *
     * @param name property name
     * @return property value
     */
    private String getListProperty(String name) {
        PropertyList list = this.source.getProperties().getList(name);
        List<String> values;
        if ((list == null) || list.isEmpty()) {
            values = null;
        } else {
            values = new ArrayList<>(list.size());
            for (int i = 0; i < list.size(); i++) {
                values.add(list.getString(i));
            }
        }

        return StringUtils.join(values, ",");
    }


    /**
     * Copy file BLOB.
     *
     * @param documentService document service
     * @param target          target document
     */
    private void copyBlob(DocumentService documentService, DocRef target) throws Exception {
        FileBlob blob = documentService.getBlob(this.source);
        documentService.setBlob(target, blob);
    }


    @Override
    public String getId() {
        return null;
    }

}
