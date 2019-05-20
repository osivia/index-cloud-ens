package fr.index.cloud.ens.generator.repository;

import io.codearte.jfairy.Fairy;

import java.io.File;
import java.net.URL;
import java.util.Random;

import org.apache.log4j.Logger;
import org.nuxeo.ecm.automation.client.Constants;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.adapters.DocumentService;
import org.nuxeo.ecm.automation.client.model.Blob;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.FileBlob;
import org.nuxeo.ecm.automation.client.model.PathRef;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.directory.v2.service.PersonUpdateService;
import org.osivia.portal.api.directory.v2.model.Person;
import org.springframework.beans.factory.annotation.Autowired;

import fr.index.cloud.ens.generator.model.Configuration;
import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;

/**
 * Generate command.
 *
 * @author Jean-Sébastien Steux
 * @see INuxeoCommand
 */
public class GenerateCommand implements INuxeoCommand {


    private static Logger LOGGER = Logger.getLogger(GenerateCommand.class);

    /** Generator configuration. */
    private final Configuration configuration;


    private String userPrefix;

    private URL realPath;

    private Fairy fairy;

    private PersonUpdateService personService;


    /**
     * Constructor.
     *
     * @param ldapContext LDAP context
     * @param configuration generator configuration
     * @param exampleFile
     * @param userId userId
     */
    public GenerateCommand(Configuration configuration, PersonUpdateService personService, Fairy fairy, URL exampleFile, String userPrefix) {
        super();
        this.configuration = configuration;
        this.fairy = fairy;
        this.realPath = exampleFile;
        this.userPrefix = userPrefix;
        this.personService = personService;


    }

    private Person createUser(String userId) {


        LOGGER.debug("creating user  " + userId);

        Person owner = personService.getEmptyPerson();
        String uid = userId;
        owner.setUid(uid);
        owner.setMail(uid + "@example.com");

        io.codearte.jfairy.producer.person.Person personGen = fairy.person();
        owner.setSn(personGen.firstName());
        owner.setGivenName(personGen.lastName());
        owner.setDisplayName(personGen.firstName() + " " + personGen.lastName());
        owner.setCn(personGen.lastName() + " " + personGen.firstName());
        if (personGen.isMale()) {
            owner.setTitle("M.");
        } else
            owner.setTitle("Mme.");

        personService.create(owner);
        personService.updatePassword(owner, "tmc");

        return owner;

    }

    public Document getUserProfile(Session automationSession, String userId) throws Exception {

        OperationRequest newRequest = automationSession.newRequest("Services.GetToutaticeUserProfile");
        newRequest.set("username", userId);

        Document refDoc = (Document) newRequest.execute();

        Document doc = (Document) automationSession.newRequest("Document.FetchLiveDocument").setHeader(Constants.HEADER_NX_SCHEMAS, "*").set("value", refDoc)
                .execute();

        return doc;

    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Object execute(Session nuxeoSession) throws Exception {


        for (int iUser = 1; iUser <= configuration.getNbOfUsers(); iUser++) {
            
            String userId = userPrefix + iUser;
            LOGGER.debug("Adding user  " + userId);


            Person createUser = createUser(userId);

            String workspacePath = getUserProfile(nuxeoSession, userId).getPath();
            String path = workspacePath.substring(0, workspacePath.lastIndexOf('/')) + "/documents";


            // LOGGER.debug("Adding user " + createUser.getCn());
            // Document userWorkspace = (Document) nuxeoController.executeNuxeoCommand(new GetUserProfileCommand(createUser.getUid()));
            // String rootPath = userWorkspace.getPath().substring(0, userWorkspace.getPath().lastIndexOf('/')) + "/documents";


            DocumentService documentService = nuxeoSession.getAdapter(DocumentService.class);

            Document docRoot = documentService.getDocument(new PathRef(path));


            int folderId = 0;
            int fileId = 0;

            for (int i = 0; i < configuration.getNbOfRootFolers(); i++) {

                Document folder = createFolder(documentService, userId, docRoot, Integer.toString(folderId));
                folderId = folderId + 1;

                for (int j = 0; j < configuration.getNbOfSubFolers(); j++) {

                    Document subfolder = createFolder(documentService, userId,folder, Integer.toString(folderId));
                    folderId = folderId + 1;

                    for (int k = 0; k < configuration.getNbOfSubItems(); k++) {

                        createFile(documentService, userId,subfolder, Integer.toString(fileId));
                        fileId = fileId + 1;
                    }

                }

                for (int k = 0; k < configuration.getNbOfSubItems(); k++) {

                    createFile(documentService, userId, folder, Integer.toString(fileId));
                    fileId = fileId + 1;

                }


            }

        }

        return null;
    }


    private void createFile(DocumentService documentService, String userId, Document folder, String id) throws Exception {
        PropertyMap properties;

        properties = new PropertyMap();
        properties.set("dc:title", "fichier-tmc-" + userId + "-" + id);
        String user = userId;
        properties.set("dc:creator", user);


        LOGGER.debug("Create file fichier-tmc-" + userId + "-" + id);

        Document file = documentService.createDocument(folder, "File", "fichier-tmc-" + userId + "-" + id, properties);

        // String filepath = Thread.currentThread().getContextClassLoader().getResource(realPath).getPath();
        Blob attachmentBlob = new FileBlob(new File(realPath.getFile()));
        documentService.setBlob(file, attachmentBlob, "file:content");


    }


    private Document createFolder(DocumentService documentService, String userId, Document docRoot, String id) throws Exception {

        LOGGER.info("Create folder dossier-tmc-" + userId + "-" + id);
        PropertyMap properties = new PropertyMap();
        properties.set("dc:title", "dossier-tmc-" + userId + "-" + id);
        String user = userId;
        properties.set("dc:creator", user);
        Document folder = documentService.createDocument(docRoot, "Folder", "dossier-tmc-" + userId + "-" + id, properties);
        return folder;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return "generator-" + userPrefix;
    }


}
