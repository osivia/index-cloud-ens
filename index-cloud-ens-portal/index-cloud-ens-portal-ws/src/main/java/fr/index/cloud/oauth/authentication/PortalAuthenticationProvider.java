package fr.index.cloud.oauth.authentication;

import java.util.ArrayList;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class PortalAuthenticationProvider implements AuthenticationProvider {

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String name = authentication.getName();
        String password = authentication.getCredentials().toString();
        
        return new PortalAuthentication(name, password,new ArrayList());

//        if (shouldAuthenticateAgainstThirdPartySystem()) {
//
//            // use the credentials
//            // and authenticate against the third-party system
//            return new UsernamePasswordAuthenticationToken(name, password, new ArrayList<>());
//        } else {
//            return null;
//        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        if( authentication.equals(UsernamePasswordAuthenticationToken.class))   {
            return true;
        }
        
        return authentication.equals(PortalAuthentication.class);
    }
}