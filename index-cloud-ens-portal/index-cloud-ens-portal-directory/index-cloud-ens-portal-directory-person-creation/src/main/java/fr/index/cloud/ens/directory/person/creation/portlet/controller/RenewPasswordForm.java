/**
 * 
 */
package fr.index.cloud.ens.directory.person.creation.portlet.controller;

import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.stereotype.Component;

/**
 * @author Loïc Billon
 *
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class RenewPasswordForm {

	private String mail;
	
	private boolean sent = false;

	/**
	 * @return the mail
	 */
	public String getMail() {
		return mail;
	}

	/**
	 * @param mail the mail to set
	 */
	public void setMail(String mail) {
		this.mail = mail;
	}

	/**
	 * @return the sent
	 */
	public boolean isSent() {
		return sent;
	}

	/**
	 * @param sent the sent to set
	 */
	public void setSent(boolean sent) {
		this.sent = sent;
	}
	
	
	
	
}
