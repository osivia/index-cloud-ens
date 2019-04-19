package org.osivia.cas.adaptors.ldap;

import java.security.GeneralSecurityException;
import java.text.ParseException;
import java.util.Date;
import java.util.Map;

import javax.security.auth.login.FailedLoginException;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.jasig.cas.authentication.HandlerResult;
import org.jasig.cas.authentication.LdapAuthenticationHandler;
import org.jasig.cas.authentication.PreventedException;
import org.jasig.cas.authentication.UsernamePasswordCredential;
import org.ldaptive.auth.Authenticator;

public class AccountValidityHandler extends LdapAuthenticationHandler {

	public AccountValidityHandler(Authenticator authenticator) {
		super(authenticator);
	}
	
	@Override
	protected HandlerResult authenticateUsernamePasswordInternal(UsernamePasswordCredential credentials)
			throws GeneralSecurityException, PreventedException {
		
        credentials.setUsername(StringUtils.lowerCase(credentials.getUsername()));
        
        HandlerResult result = super.authenticateUsernamePasswordInternal(credentials);
        
        Map<String, Object> attributes = result.getPrincipal().getAttributes();
            	
        Object portalPersonValidity = attributes.get("portalPersonValidity");

    	// if portalPersonExternal = FALSE, portalPersonValidity should be after current day
        if(portalPersonValidity != null) {
        	try {
				String dayOfValidity = portalPersonValidity.toString().substring(0, 8);
				Date validity = DateUtils.parseDate(dayOfValidity, "yyyyMMdd");
				
				if(validity.before(new Date())) {
	        		throw new FailedLoginException("Account has expired");
				}
				
			} catch (ParseException e) {
        		throw new FailedLoginException("Invalid expiration date");

			}
        }

        
        return result;
	}

}
