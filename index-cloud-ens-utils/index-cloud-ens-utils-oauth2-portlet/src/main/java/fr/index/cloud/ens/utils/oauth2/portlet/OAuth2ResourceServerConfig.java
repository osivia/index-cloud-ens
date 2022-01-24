package fr.index.cloud.ens.utils.oauth2.portlet;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;



@EnableOAuth2Client
@Configuration
public class OAuth2ResourceServerConfig {


    @Bean
    public OAuth2RestTemplate defaultTemplate(OAuth2ClientContext clientContext) throws KeyManagementException, NoSuchAlgorithmException {
        OAuth2RestTemplate template = new OAuth2RestTemplate(authorizationCodeResource(), clientContext);
        return template;
      }

    
    OAuth2ProtectedResourceDetails authorizationCodeResource() {
        
        AuthorizationCodeResourceDetails resource = new AuthorizationCodeResourceDetails();
        resource.setAccessTokenUri("https://cloud-ens.index-education.local/index-cloud-portal-ens-ws/oauth/token");
        resource.setClientId("PRONOTE-1234");
        resource.setClientSecret("secret1234");
        resource.setUserAuthorizationUri("https://cloud-ens.index-education.local/index-cloud-portal-ens-ws/oauth/authorize");
        resource.setPreEstablishedRedirectUri("https://cloud-ens.index-education.local/index-cloud-portal-ens-utils-oauth2-portlet/redirect");
        resource.setUseCurrentUri(false);
        resource.setScope(Arrays.asList("drive"));

        return resource;
   }


}