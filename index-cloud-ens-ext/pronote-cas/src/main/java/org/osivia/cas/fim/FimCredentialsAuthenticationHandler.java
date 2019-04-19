package org.osivia.cas.fim;

import java.security.GeneralSecurityException;

import org.jasig.cas.authentication.AbstractAuthenticationHandler;
import org.jasig.cas.authentication.Credential;
import org.jasig.cas.authentication.DefaultHandlerResult;
import org.jasig.cas.authentication.HandlerResult;
import org.jasig.cas.authentication.PreventedException;

public class FimCredentialsAuthenticationHandler  extends AbstractAuthenticationHandler {

	public HandlerResult authenticate(Credential credential) throws GeneralSecurityException, PreventedException {

		final FimCredentials c = (FimCredentials) credential;
				
		return new DefaultHandlerResult(this, c);
	}

	public boolean supports(Credential credential) {
		return credential instanceof FimCredentials;
	}

}
