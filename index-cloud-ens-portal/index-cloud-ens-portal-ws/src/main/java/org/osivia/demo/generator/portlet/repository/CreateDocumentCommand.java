package org.osivia.demo.generator.portlet.repository;

import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.adapters.DocumentService;
import org.nuxeo.ecm.automation.client.model.DocRef;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.demo.generator.portlet.model.CreationForm;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;

/**
 * Create document Nuxeo command.
 * 
 * @author Cédric Krommenhoek
 * @see INuxeoCommand
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CreateDocumentCommand implements INuxeoCommand {

    /** Creation form. */
    private final CreationForm form;


    /**
     * Constructor.
     * 
     * @param form creation form
     */
    public CreateDocumentCommand(CreationForm form) {
        super();
        this.form = form;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Object execute(Session nuxeoSession) throws Exception {
        // Document service
        DocumentService documentService = nuxeoSession.getAdapter(DocumentService.class);

        // Parent
        DocRef parent = new DocRef(this.form.getParentPath());

        // Properties
        PropertyMap properties = new PropertyMap();
        properties.set("dc:title", this.form.getTitle());
        properties.set("dc:description", this.form.getDescription());

        return documentService.createDocument(parent, this.form.getType(), null, properties);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return null;
    }

}
