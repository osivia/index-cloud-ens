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

	/**
	 * Check if mail is associated with a valid user account
	 * not used, remplaced by renew-password portlet
	 * 
	 * @deprecated
	 * @param context
	 * @param executor
	 * @throws FormFilterException 
	 */
	void checkAccountValidity(FormFilterContext context, FormFilterExecutor executor) throws FormFilterException;
	
	/**
	 * Renew the userpassword
	 * 
	 * @param context
	 * @param executor
	 * @throws FormFilterException 
	 */
	void renewPassword(FormFilterContext context, FormFilterExecutor executor) throws FormFilterException;
	
	
}
