package org.osivia.demo.customizer.project;

import org.nuxeo.ecm.automation.client.Constants;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.model.Document;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;

/**
 * Get profile Nuxeo command.
 * 
 * @see INuxeoCommand
 */
public class GetProfileCommand implements INuxeoCommand {

    /** User name. */
    private final String userName;


    /**
     * Constructor.
     * 
     * @param userName user name
     */
    public GetProfileCommand(String userName) {
        super();
        this.userName = userName;


    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Object execute(Session session) throws Exception {
        // User profile
        Document userProfile = this.getUserProfile(session);

        // Full document
        return this.getFullDocument(session, userProfile);
    }


    /**
     * Get user profile.
     * 
     * @param session automation session
     * @return document
     * @throws Exception
     */
    private Document getUserProfile(Session session) throws Exception {
        // Operation request
        OperationRequest request = session.newRequest("Services.GetToutaticeUserProfile");
        request.set("username", this.userName);

        return (Document) request.execute();
    }


    /**
     * Get user profile full document.
     * 
     * @param session automation session
     * @param userProfile user profile
     * @return document
     * @throws Exception
     */
    private Document getFullDocument(Session session, Document userProfile) throws Exception {
        // Operation request
        OperationRequest request = session.newRequest("Document.Fetch");
        request.setHeader(Constants.HEADER_NX_SCHEMAS, "*");
        request.set("value", userProfile.getPath());

        return (Document) request.execute();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.getClass().getSimpleName());
        builder.append("/");
        builder.append(this.userName);
        return builder.toString();
    }

}
