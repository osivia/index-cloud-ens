package fr.index.cloud.oauth.tokenStore;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.Documents;
import org.nuxeo.ecm.automation.client.model.PropertyList;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.portal.api.cache.services.CacheInfo;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

import fr.index.cloud.ens.ws.CloudRestController;
import fr.index.cloud.ens.ws.commands.GetUserProfileCommand;
import fr.index.cloud.oauth.commands.GetRefreshTokenCommand;
import fr.index.cloud.oauth.commands.RemoveRefreshTokenCommand;
import fr.index.cloud.oauth.commands.StoreRefreshTokenCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.services.NuxeoCommandContext;


/**
 * Portal store for tokens
 * 
 * access tokens are stored in memory
 * refresh tokens are stored in user profile
 * 
 * @author Jean-Sébastien Steux
 */

public class PortalTokenStore extends InMemoryTokenStore {


    /**
     * getNuxeoController
     * 
     * @return
     */
    private NuxeoController getNuxeoController() {
        NuxeoController nuxeoController = new NuxeoController(CloudRestController.portletContext);
        nuxeoController.setAuthType(NuxeoCommandContext.AUTH_TYPE_SUPERUSER);
        nuxeoController.setCacheType(CacheInfo.CACHE_SCOPE_NONE);
        return nuxeoController;
    }


    @Override
    public void storeRefreshToken(OAuth2RefreshToken refreshToken, OAuth2Authentication authentication) {
        try {
            NuxeoController nuxeoController = getNuxeoController();

            PortalRefreshTokenDatas datas = new PortalRefreshTokenDatas(authentication);
            String jsonData = new ObjectMapper().writeValueAsString(datas);

            String userName = (String) authentication.getPrincipal();
            Document userWorkspace = (Document) nuxeoController.executeNuxeoCommand(new GetUserProfileCommand(userName));

            nuxeoController.executeNuxeoCommand(new StoreRefreshTokenCommand(userWorkspace.getPath(), refreshToken.getValue(), jsonData));
            

        } catch (Exception e) {
            throw new RuntimeException("can't serialize refresh token", e);
        }

    }


    @Override
    public OAuth2Authentication readAuthenticationForRefreshToken(String token) {
        try {

            NuxeoController nuxeoController = getNuxeoController();
            
            Documents docs = (Documents) nuxeoController.executeNuxeoCommand(new GetRefreshTokenCommand(token));
            
            if (docs.size() > 1)
                throw new RuntimeException("more than one token detected");
            
            if (docs.size() == 1) {
                PropertyList tokens = docs.get(0).getProperties().getList("oatk:tokens");
                for (int i = 0; i < tokens.size(); i++) {
                    PropertyMap tokenMap = tokens.getMap(i);
                    String value = tokenMap.getString("value");
                    if (token.equals(value)) {
                        String jsonData = tokenMap.getString("authentication");
                        if (jsonData != null) {
                            PortalRefreshTokenDatas datas = new ObjectMapper().readValue(jsonData, PortalRefreshTokenDatas.class);

                            OAuth2Authentication authAuthentication = datas.getAuthentication();

                            OAuth2Request storedRequest = authAuthentication.getOAuth2Request();
                            Authentication userAuthentication = authAuthentication.getUserAuthentication();

                            OAuth2Authentication authentication = new OAuth2Authentication(storedRequest, userAuthentication);
       
                            return authentication;
                        }
                    }
                }
              }

        } catch (Exception e) {
            throw new RuntimeException("can't serialize refresh token", e);
        }
        return null;
    }


    @Override
    public OAuth2RefreshToken readRefreshToken(String tokenValue) {
        try {

            NuxeoController nuxeoController = getNuxeoController();
            Documents docs = (Documents) nuxeoController.executeNuxeoCommand(new GetRefreshTokenCommand(tokenValue));
            
            if (docs.size() > 1)
                throw new RuntimeException("more then one token detected");
            
            if (docs.size() == 1) {
                PortalRefreshToken refreshToken = new PortalRefreshToken(tokenValue);
                return refreshToken;
            }
        } catch (Exception e) {
            throw new RuntimeException("can't serialize refresh token", e);
        }
        return null;
    }


    @Override
    public void removeRefreshToken(String tokenValue) {
        NuxeoController nuxeoController = getNuxeoController();

        Documents docs = (Documents) nuxeoController.executeNuxeoCommand(new GetRefreshTokenCommand(tokenValue));
        
        if (docs.size() > 1)
            throw new RuntimeException("more than one token detected");
        
        if (docs.size() == 1) {
            Document doc = docs.get(0);
            PropertyList tokens = doc.getProperties().getList("oatk:tokens");
            for (int i = 0; i < tokens.size(); i++) {
                PropertyMap tokenMap = tokens.getMap(i);
                String value = tokenMap.getString("value");
                if (tokenValue.equals(value)) {
                    nuxeoController.executeNuxeoCommand(new RemoveRefreshTokenCommand(doc.getPath(), tokenValue, i));
                }
            }
        }
    }


}
