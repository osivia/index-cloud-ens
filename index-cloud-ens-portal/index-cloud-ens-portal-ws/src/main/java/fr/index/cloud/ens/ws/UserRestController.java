package fr.index.cloud.ens.ws;

import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
import org.codehaus.jackson.map.ObjectMapper;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.Documents;
import org.nuxeo.ecm.automation.client.model.PropertyList;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.directory.v2.service.PersonUpdateService;
import org.osivia.portal.api.cache.services.CacheInfo;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.directory.v2.model.Person;
import org.osivia.portal.api.tokens.ITokenService;
import org.osivia.portal.core.error.ErrorDescriptor;
import org.osivia.portal.core.error.GlobalErrorHandler;
import org.osivia.portal.core.web.IWebIdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import fr.index.cloud.ens.ws.beans.CreateUserBean;
import fr.index.cloud.ens.ws.beans.PublishBean;
import fr.index.cloud.ens.ws.beans.UnpublishBean;
import fr.index.cloud.ens.ws.beans.UploadBean;
import fr.index.cloud.ens.ws.commands.FolderGetChildrenCommand;
import fr.index.cloud.ens.ws.commands.GetUserProfileCommand;
import fr.index.cloud.ens.ws.commands.PublishCommand;
import fr.index.cloud.ens.ws.commands.UnpublishCommand;
import fr.index.cloud.ens.ws.commands.UploadFileCommand;
import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoException;
import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoDocumentContext;
import fr.toutatice.portail.cms.nuxeo.api.services.NuxeoCommandContext;


@RestController
public class UserRestController {

    /**
	 * 
	 */
	private static final String TOKEN_PREFIX = "Bearer ";

    public static PortletContext portletContext;


    @Autowired
    @Qualifier("personUpdateService")
    private PersonUpdateService personUpdateService;
    
    @Autowired
    private ITokenService tokenService;


    /** Logger. */
    private static final Log logger = LogFactory.getLog(UserRestController.class);

  
    /**
     * La création du compte est effectuée depuis un lien ‘Créer un compte’ de PRONOTE.
     * Authentification du jeton JWT + création du portalToken avec les informations transmises dans le contenu du jeton.
     * 
     * @param request
     * @param response
     * @return url de création de compte, code d'erreur éventuel.
     * @throws Exception
     */
    @RequestMapping(value = "/User.signup", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Object> signUp(HttpServletRequest request, HttpServletResponse response) {
    	
        Map<String, Object> returnObject = new LinkedHashMap<>();

    	String url = "";
        String token = request.getHeader("Authorization");
        if( StringUtils.isNotEmpty(token))  {
            if( token.startsWith(TOKEN_PREFIX)) {
            	
            	String issuer = System.getProperty("pronote.issuer");
            	String pronoteSecret = System.getProperty("pronote.secret");
            	DecodedJWT jwt = null;

				try {
					Algorithm algorithm = Algorithm.HMAC256(pronoteSecret);
					
                    JWTVerifier verifier = JWT.require(algorithm)
                            .withIssuer(issuer)
                            .build(); //Reusable verifier instance
                    jwt = verifier.verify(token.substring(TOKEN_PREFIX.length()));
					
				} catch (IllegalArgumentException | UnsupportedEncodingException e) {

					returnObject.put("returnCode", GenericErrors.ERR_FORBIDDEN);
		            logger.error("Erreur d'authentification pronote / cloud",e);
				}
				
				String webToken = null;
				if(jwt != null) {
                    String firstName = jwt.getClaim("firstName").asString(); //attributes.put("firstName", );
                    String lastName = jwt.getClaim("lastName").asString(); //attributes.put("lastName", );
                    String mail = jwt.getClaim("mail").asString(); //attributes.put("mail", );
                    
                    if(StringUtils.isBlank(firstName) || StringUtils.isBlank(lastName) || StringUtils.isBlank(mail)) {
                    	
						returnObject.put("returnCode", GenericErrors.ERR_FORBIDDEN);
			            logger.error("Mauvais paramètres.");
                    }
                    else {
                    	
	                    Map<String, String> attributes = new ConcurrentHashMap<String, String>();
                    	attributes.put("firstname", firstName);
                    	attributes.put("lastname", lastName);
                    	attributes.put("mail", mail);
                    	
                    	Person searchedPerson = personUpdateService.getEmptyPerson();
                    	searchedPerson.setMail(mail);
                    	List<Person> search = personUpdateService.findByCriteria(searchedPerson);
                    	
                    	
                    	if(search.size() > 0 && search.get(0).getLastConnection() != null) {
                    		
							returnObject.put("returnCode", GenericErrors.ERR_FORBIDDEN);
				            logger.error("Un compte actif existe déjà pour cette personne.");
                    	}
                    	else {

                    		webToken = tokenService.generateToken(attributes);
                    	}
                    }
				}

				
				if(webToken != null) {
					
					final PortalControllerContext pcc = new PortalControllerContext(portletContext);
					try {
						
						NuxeoController nuxeoController = new NuxeoController(portletContext);
				        nuxeoController.setServletRequest(request);
				        nuxeoController.setAuthType(NuxeoCommandContext.AUTH_TYPE_SUPERUSER);
				        nuxeoController.setCacheType(CacheInfo.CACHE_SCOPE_NONE);
			            NuxeoDocumentContext ctx = nuxeoController.getDocumentContext(IWebIdService.FETCH_PATH_PREFIX + "procedure_person-creation");

			            // Get parent doc
			            Document userCreationProcedure = ctx.getDocument();
						
//							CMSServiceCtx cmsCtx = new CMSServiceCtx();
//							cmsCtx.set
						//String personCreationPath = cmsServiceLocator.getCMSService().adaptWebPathToCms(cmsCtx , );
			            
			            
			            // TODO portalUrlFactory ne marche pas en dehors d'un contexte portlet.
			            
						//url = urlFactory.getPermaLink(pcc, null, null, userCreationProcedure.getPath(), IPortalUrlFactory.PERM_LINK_TYPE_TASK);
						
			            url = "/portal/cms" +  userCreationProcedure.getPath();
			            
						
					} catch (Exception e) {
						
						returnObject.put("returnCode", GenericErrors.ERR_UNKNOWN);
			            logger.error("Impossible de produire l'url d'inscription.",e);
					} 
					
					if(url != null) {
	                	String publicHost = System.getProperty("osivia.tasks.host");
						url  = publicHost + url + "?token="+ webToken;
	                    returnObject.put("url", url);
	                    returnObject.put("returnCode", GenericErrors.ERR_OK);

					}
				}
            }
        }

        return returnObject;
    }

}
