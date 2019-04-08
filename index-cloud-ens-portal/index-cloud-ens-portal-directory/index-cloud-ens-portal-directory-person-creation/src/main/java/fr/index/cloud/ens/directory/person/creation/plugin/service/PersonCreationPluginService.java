package fr.index.cloud.ens.directory.person.creation.plugin.service;

import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterContext;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterException;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterExecutor;

/**
 * Person creation plugin service interface.
 * 
 * @author Loïc Billon
 */
public interface PersonCreationPluginService {



    /**
     * Create person in directory and send a mail to check validity
     * 
     * @param context form filter context
     * @param executor form filter executor
     * @throws FormFilterException
     */
    void createPerson(FormFilterContext context, FormFilterExecutor executor) throws FormFilterException;


	/**
	 * Verify e-mail account
	 * 
	 * @param context
	 * @param executor
	 */
	void verifyMail(FormFilterContext context, FormFilterExecutor executor);

}
