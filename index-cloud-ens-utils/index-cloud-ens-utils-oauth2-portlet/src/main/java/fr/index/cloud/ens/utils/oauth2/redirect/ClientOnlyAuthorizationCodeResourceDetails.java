package fr.index.cloud.ens.utils.oauth2.redirect;

import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;

public class ClientOnlyAuthorizationCodeResourceDetails extends AuthorizationCodeResourceDetails{
    public boolean isClientOnly() {
        return false;
    }

}
