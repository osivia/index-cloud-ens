package fr.index.cloud.ens.utils.oauth2.portlet;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.OAuth2RestOperations;

import fr.index.cloud.ens.utils.oauth2.redirect.OAuth2PortletOperation;


/**
 * Sample operation which calls Osivia drive
 * 
 * @author Jean-Sébastien
 */
public class DriveOperation implements OAuth2PortletOperation {

    private OAuth2RestOperations restTemplate;
    
    public DriveOperation(OAuth2RestOperations restTemplate) {
        super();
        this.restTemplate = restTemplate;
    }

    public Object execute() {
        HttpHeaders httpHeaders = new HttpHeaders();
        List<MediaType> mediaTypes = new ArrayList<MediaType>();
        mediaTypes.add(MediaType.APPLICATION_JSON);
        
        httpHeaders.setAccept(mediaTypes);
        HttpEntity<String> httpEntity = new HttpEntity(null, httpHeaders);

       return this.restTemplate.exchange("https://cloud-ens.index-education.local/index-cloud-portal-ens-ws/rest/Drive.content", HttpMethod.GET, httpEntity, String.class);
    }

}
