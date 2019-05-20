package fr.index.cloud.ens.generator.repository;

import io.codearte.jfairy.Fairy;

import java.io.File;
import java.net.URL;
import java.util.List;
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
public class PurgeCommand implements INuxeoCommand {


    private static Logger LOGGER = Logger.getLogger(PurgeCommand.class);

    /** Generator configuration. */
    private final Configuration configuration;


    private String userPrefix;

    private PersonUpdateService personService;


    /**
     * Constructor.
     *
     * @param ldapContext LDAP context
     * @param configuration generator configuration
     * @param exampleFile
     * @param userId userId
     */
    public PurgeCommand(Configuration configuration, PersonUpdateService personService,  String userPrefix) {
        super();
        this.configuration = configuration;
       this.userPrefix = userPrefix;
        this.personService = personService;


    }

   

    /**
     * {@inheritDoc}
     */
    @Override
    public Object execute(Session nuxeoSession) throws Exception {



        Person personsToDeleteQuery = personService.getEmptyPerson();
        String uid = userPrefix+"*";
        personsToDeleteQuery.setUid(uid);


        List<Person> personsToDelete = personService.findByCriteria(personsToDeleteQuery);

        while (!personsToDelete.isEmpty()) {

            for (Person p : personsToDelete) {

                LOGGER.warn("deleting " + p.getUid());
                personService.delete(p);

            }

            personsToDelete = personService.findByCriteria(personsToDeleteQuery);
        }

        return null;
    }


   

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return "purge-" + userPrefix;
    }


}
