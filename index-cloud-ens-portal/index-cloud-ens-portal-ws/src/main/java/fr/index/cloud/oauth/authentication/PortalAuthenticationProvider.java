package fr.index.cloud.oauth.authentication;

import java.util.ArrayList;
import java.util.List;

import org.osivia.directory.v2.service.PersonUpdateService;
import org.osivia.portal.api.directory.v2.model.Person;
import org.osivia.portal.api.directory.v2.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class PortalAuthenticationProvider implements AuthenticationProvider {

    
    @Autowired
    @Qualifier("personUpdateService")
    private PersonUpdateService personUpdateService;
    
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String name = authentication.getName();
        String password = authentication.getCredentials().toString();
        
//        Person searchPerson = personUpdateService.getEmptyPerson();        
//        searchPerson.setUid(name);
//        List<Person> persons = personUpdateService.findByCriteria(searchPerson);  
//        for (Person aPerson:persons) {
          if(  personUpdateService.verifyPassword(name, password) == false) {
              throw new BadCredentialsException("Bad user/password !");
          }
//        }
        
          List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>(1);
           authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        
        return new PortalAuthentication(name, password,authorities);

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