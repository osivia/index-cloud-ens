package fr.index.cloud.ens.ws;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.security.PublicKey;
import java.security.interfaces.RSAKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.portlet.PortletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.directory.v2.service.PersonUpdateService;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.cache.services.CacheInfo;
import org.osivia.portal.api.directory.v2.model.Person;
import org.osivia.portal.api.tokens.ITokenService;
import org.osivia.portal.core.web.IWebIdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.interfaces.DecodedJWT;

import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoDocumentContext;
import fr.toutatice.portail.cms.nuxeo.api.liveedit.OnlyofficeLiveEditHelper;
import fr.toutatice.portail.cms.nuxeo.api.services.NuxeoCommandContext;


/**
 * Services rest associés aux ressources accessible par la visionneuse
 * 
 * @author Jean-Sébastien Steux
 */
@RestController
public class ViewerRestController {


    public static PortletContext portletContext;



    @Autowired
    private ErrorMgr errorMgr;

    /** Logger. */
    private static final Log logger = LogFactory.getLog(ViewerRestController.class);


    /**
     * Ce service permet d'accéder aux informations relatives à la visionneuse
     * 
     * @param request
     * @param response
     * @return url de création de compte, code d'erreur éventuel.
     * @throws Exception
     */
    @RequestMapping(value = "/Viewer.getFileInfos", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Object> fileInfos(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "shareId", required = false) String shareId, Principal principal) {

        WSPortalControllerContext wsCtx = new WSPortalControllerContext(request, response, null);

        Map<String, Object> returnObject = new LinkedHashMap<>();
        returnObject.put("returnCode", ErrorMgr.ERR_OK);
       
        try {
        	
            NuxeoController nuxeoController = DriveRestController.getNuxeocontroller(request, principal);
            
            Document doc = nuxeoController.fetchSharedDocument(shareId);

            PropertyMap properties = doc.getProperties();
            PropertyMap fileContent = properties.getMap("file:content");
            String filename = fileContent.getString("name");
         
            String mimeType = fileContent.getString("mime-type");
            String fileType= OnlyofficeLiveEditHelper.getFileType(mimeType).name();       
            returnObject.put("fileType", fileType);               
            returnObject.put("fileName", filename); 
             
            returnObject.put("downloadURI", DriveRestController.SHARE_URL_PREFIX+ shareId); 
            returnObject.put("format", properties.getString("rshr:format"));
        }
        catch(Exception e) {
             returnObject = errorMgr.handleDefaultExceptions(wsCtx, e);
        }
        
        return returnObject;

    }
    

   
}
