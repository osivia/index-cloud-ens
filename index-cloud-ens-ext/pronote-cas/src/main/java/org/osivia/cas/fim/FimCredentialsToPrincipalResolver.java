package org.osivia.cas.fim;

import java.util.HashMap;
import java.util.Map;

import org.jasig.cas.authentication.Credential;
import org.jasig.cas.authentication.principal.Principal;
import org.jasig.cas.authentication.principal.PrincipalResolver;
import org.jasig.cas.authentication.principal.SimplePrincipal;

public class FimCredentialsToPrincipalResolver implements PrincipalResolver {

	//protected final Logger log = LoggerContextFactory.

	public Principal resolve(Credential credential) {
//		if (log.isDebugEnabled()) {
//			log.debug("Attempting to resolve a principal...");
//		}

		FimCredentials fimCredentials = (FimCredentials) credential;

		String principalId = fimCredentials.getId();

		if (principalId == null) {
			return null;
		} else {

			// Login toujours en minuscule (compatibilité entre comptes manuels et comptes
			// FIM).
			principalId = principalId.toLowerCase();
		}

//		if (log.isDebugEnabled()) {
//			log.debug("Creating SimplePrincipal for [" + principalId + "]");
//		}

		final Map<String, Object> convertedAttributes = new HashMap<String, Object>();

		convertedAttributes.putAll(fimCredentials.getAttributes());
		return new SimplePrincipal(principalId, convertedAttributes);
	}

	public boolean supports(Credential credential) {
		return credential instanceof FimCredentials;
	}

}
