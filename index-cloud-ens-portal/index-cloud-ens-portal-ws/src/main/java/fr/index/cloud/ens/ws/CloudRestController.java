package fr.index.cloud.ens.ws;

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
import org.osivia.portal.api.directory.v2.model.Person;
import org.osivia.portal.api.log.LogContext;
import org.osivia.portal.api.tokens.TokenUtils;
import org.osivia.portal.core.error.ErrorDescriptor;
import org.osivia.portal.core.error.GlobalErrorHandler;
import org.osivia.portal.core.page.PageProperties;
import org.osivia.portal.core.web.IWebIdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
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
import fr.index.cloud.oauth.commands.RemoveRefreshTokenCommand;
import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoException;
import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoDocumentContext;
import fr.toutatice.portail.cms.nuxeo.api.services.NuxeoCommandContext;


@RestController
public class CloudRestController {

    /**
	 * 
	 */
	private static final String TOKEN_PREFIX = "Bearer ";
	private static final String PROP_TTC_WEBID = "ttc:webid";
    private static final String PROP_SHARE_LINK = "rshr:linkId";

    public static PortletContext portletContext;

    Map<String, String> levelQualifier = null;

    public static final int ERR_CREATE_USER_MAIL_ALREADYEXIST = 1;


    @Autowired
    @Qualifier("personUpdateService")
    private PersonUpdateService personUpdateService;


    /** Logger. */
    private static final Log logger = LogFactory.getLog(CloudRestController.class);

    /**
     * Get a nuxeoController associated to the current user
     * 
     * @return
     * @throws Exception
     */
    private NuxeoController getNuxeocontroller(HttpServletRequest request, Principal principal) throws Exception {


        NuxeoController nuxeoController = new NuxeoController(portletContext);
        nuxeoController.setServletRequest(request);
        nuxeoController.setAuthType(NuxeoCommandContext.AUTH_TYPE_USER);
        nuxeoController.setCacheType(CacheInfo.CACHE_SCOPE_NONE);

        request.setAttribute("osivia.delegation.userName", principal.getName());

        return nuxeoController;

    }


    /**
     * Conversion des exceptions en erreur JSON
     * 
     * @param e
     * @return
     */
    private Map<String, Object> handleDefaultExceptions(Exception e, Principal principal) {

        Map<String, Object> response = new LinkedHashMap<>();

        boolean logError = true;
        int returnCode = GenericErrors.ERR_UNKNOWN;

        if (e instanceof NuxeoException) {
            NuxeoException nxe = (NuxeoException) e;

            if (nxe.getErrorCode() == NuxeoException.ERROR_NOTFOUND) {
                response.put("returnCode", GenericErrors.ERR_NOT_FOUND);
                logError = false;
            } else if (nxe.getErrorCode() == NuxeoException.ERROR_FORBIDDEN) {
                response.put("returnCode", GenericErrors.ERR_FORBIDDEN);
                logError = false;
            }
        }

        if (logError) {
            // Token
            // TODO : Add context for Web Service
            // String token = this.logContext.createContext(portalControllerContext, "portal", null);


            // User identifier
            String userId = null;
            if( principal != null)
                userId = principal.getName();

            // Error descriptor
            ErrorDescriptor errorDescriptor = new ErrorDescriptor(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e, null, userId, null);
            // errorDescriptor.setToken(token);

            // Print stack in server.log
            if (errorDescriptor.getException() != null) {
                logger.error("Technical error in web service ", errorDescriptor.getException());
            }


            // Print stack in portal_user_error.log
            GlobalErrorHandler.getInstance().logError(errorDescriptor);


            response.put("returnCode", GenericErrors.ERR_UNKNOWN);
        }

        response.put("returnCode", returnCode);

        return response;

    }


    /**
     * URL to use: /index-cloud-portal-ens-ws/rest/Drive.content
     * 
     * Codes erreurs : NOT_FOUND, FORBIDDEN
     * 
     * @param request
     * @param id
     * @return
     * @throws Exception
     */

    private Map<String, Object> initContent(Document doc, String type, boolean mainObject) {

        Map<String, Object> contents = new LinkedHashMap<>();

        if (mainObject)
            contents.put("returnCode", GenericErrors.ERR_OK);

        contents.put("type", type);
        contents.put("id", doc.getProperties().getString(PROP_TTC_WEBID));
        contents.put("title", doc.getTitle());

        if (doc.getProperties().get("common:size") != null) {
            long size = doc.getProperties().getLong("common:size");
            contents.put("fileSize", size);
        }

        String shareLink = doc.getProperties().getString(PROP_SHARE_LINK);
        if (shareLink != null)
            contents.put("shareLink", shareLink);

        if (doc.getProperties().get("dc:modified") != null) {
            Date lastModifiedDate = doc.getProperties().getDate("dc:modified");
            contents.put("lastModified", lastModifiedDate.getTime());
        }

        return contents;
    }


    private Map<String, Object> initContent(Document doc, String type) {
        return initContent(doc, type, false);
    }


    @RequestMapping(value = "/Drive.webUrl", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)

    public Map<String, Object> getWebUrl(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "type", required = false) String type, @RequestParam(value = "id", required = false) String id,
            Principal principal) throws Exception {
        
        Map<String, Object> returnObject = new LinkedHashMap<>();
        returnObject.put("returnCode", GenericErrors.ERR_OK);
 

        try {
            Map<String, String> tokenAttributes = new ConcurrentHashMap<>();
            tokenAttributes.put("uid", principal.getName());
            String webToken = TokenUtils.generateToken(tokenAttributes);
            String url = "https://" + request.getServerName() + "/toutatice-portail-cms-nuxeo/binary?id="+id+ "&webToken=" + webToken+"&viewer=true";
            returnObject.put("url", url);

        } catch (Exception e) {
            returnObject = handleDefaultExceptions(e, principal);
        }
        return returnObject;
    }


    @RequestMapping(value = "/Drive.content", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)

    public Map<String, Object> getContent(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "id", required = false) String id,
            Principal principal) throws Exception {

        NuxeoController nuxeoController = getNuxeocontroller(request, principal);

        Map<String, Object> returnObject;

        try {
            Document userWorkspace = (Document) nuxeoController.executeNuxeoCommand(new GetUserProfileCommand(principal.getName()));
            String rootPath = userWorkspace.getPath().substring(0, userWorkspace.getPath().lastIndexOf('/')) + "/documents";

            String path = null;
            if (id != null)
                path = IWebIdService.FETCH_PATH_PREFIX + id;
            else
                path = rootPath;

            // Get durrent doc
            Document currentDoc = nuxeoController.getDocumentContext(path).getDocument();

            String type = currentDoc.getPath().equals(rootPath) ? "root" : currentDoc.getType().toLowerCase();

            returnObject = initContent(currentDoc, type, true);

            // Get hierarchy

            List<Map<String, Object>> hierarchy = new ArrayList<>();
            String hierarchyPath = currentDoc.getPath().substring(0, currentDoc.getPath().lastIndexOf('/'));

            while (hierarchyPath.contains(rootPath)) {
                Document hierarchyDoc = nuxeoController.getDocumentContext(hierarchyPath).getDocument();
                String hierarchyType = hierarchyDoc.getType();
                if (hierarchyPath.equals(rootPath))
                    hierarchyType = "root";
                else
                    hierarchyType = hierarchyDoc.getType().toLowerCase();
                hierarchy.add(0, initContent(hierarchyDoc, hierarchyType));

                // next parent
                hierarchyPath = hierarchyPath.substring(0, hierarchyPath.lastIndexOf('/'));
            }

            returnObject.put("parents", hierarchy);


            List<Map<String, Object>> childrenList = new ArrayList<>();
            PropertyList facets = currentDoc.getFacets();
            if (facets.list().contains("Folderish")) {

                // Add childrens
                Documents children = (Documents) nuxeoController.executeNuxeoCommand(new FolderGetChildrenCommand(currentDoc));

                for (Document doc : children.list()) {
                    childrenList.add(initContent(doc, doc.getType().toLowerCase()));
                }

            }
            returnObject.put("childrens", childrenList);

        } catch (Exception e) {
            returnObject = handleDefaultExceptions(e, principal);
        }
        return returnObject;

    }


    /**
     * Upload a file to the current folder
     * 
     * @param file
     * @param parentWebId
     * @param extraField
     * @param request
     * @param response
     * @throws Exception
     */

    @RequestMapping(value = "/Drive.upload", method = RequestMethod.POST, consumes = {"multipart/form-data"})
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Object> handleFileUpload(@RequestParam("file") MultipartFile file, @RequestParam("uploadInfos") String fileUpload,
            HttpServletRequest request, HttpServletResponse response, Principal principal) throws Exception {

        Map<String, Object> returnObject = new LinkedHashMap<>();
        returnObject.put("returnCode", GenericErrors.ERR_OK);

        try {

            NuxeoController nuxeoController = getNuxeocontroller(request, principal);

            UploadBean uploadBean = new ObjectMapper().readValue(fileUpload, UploadBean.class);

            // Get parent doc
            Document parentDoc = nuxeoController.getDocumentContext(IWebIdService.FETCH_PATH_PREFIX + uploadBean.getParentId()).getDocument();

            // set qualifiers
            Map<String, String> properties = parseProperties(uploadBean.getProperties());


            // Execute import
            INuxeoCommand command = new UploadFileCommand(parentDoc.getId(), file, properties);
            nuxeoController.executeNuxeoCommand(command);

        } catch (Exception e) {
            returnObject = handleDefaultExceptions(e, principal);
        }


        return returnObject;
    }


    /**
     * Publish a document to the specified target
     * 
     * @param file
     * @param parentWebId
     * @param extraField
     * @param request
     * @param response
     * @throws Exception
     */

    @RequestMapping(value = "/Drive.publish", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Object> publish(@RequestBody PublishBean publishBean, HttpServletRequest request, HttpServletResponse response, Principal principal)
            throws Exception {
        Map<String, Object> returnObject = new LinkedHashMap<>();
        returnObject.put("returnCode", GenericErrors.ERR_OK);
        try {

            NuxeoController nuxeoController = getNuxeocontroller(request, principal);

            NuxeoDocumentContext ctx = nuxeoController.getDocumentContext(IWebIdService.FETCH_PATH_PREFIX + publishBean.getContentId());

            // Get parent doc
            Document currentDoc = ctx.getDocument();

            // set qualifiers
            Map<String, String> properties = parseProperties(publishBean.getProperties());


            // Execute import
            INuxeoCommand command = new PublishCommand(currentDoc, publishBean.getFormat(), publishBean.getPubId(), publishBean.getPubTitle(),
                    publishBean.getPubOrganization(), properties);
            
            String shareId = (String) nuxeoController.executeNuxeoCommand(command);

            // Force cache initialisation
            ctx.reload();

            returnObject.put("shareId", shareId);

        } catch (Exception e) {
            returnObject = handleDefaultExceptions(e, principal);
        }


        return returnObject;
    }


    /**
     * Publish a document to the specified target
     * 
     * @param file
     * @param parentWebId
     * @param extraField
     * @param request
     * @param response
     * @throws Exception
     */

    @RequestMapping(value = "/Drive.unpublish", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Object> unpublish(@RequestBody UnpublishBean unpublishBean, HttpServletRequest request, HttpServletResponse response,
            Principal principal) throws Exception {


        Map<String, Object> returnObject = new LinkedHashMap<>();
        returnObject.put("returnCode", GenericErrors.ERR_OK);

        try {

            NuxeoController nuxeoController = getNuxeocontroller(request, principal);

            NuxeoDocumentContext ctx = nuxeoController.getDocumentContext(IWebIdService.FETCH_PATH_PREFIX + unpublishBean.getContentId());

            // Get parent doc
            Document currentDoc = ctx.getDocument();

            int indice = -1;

            PropertyList targets = currentDoc.getProperties().getList("rshr:targets");
            for (int i = 0; i < targets.size(); i++) {
                PropertyMap target = targets.getMap(i);
                String pubId = target.getString("pubId");
                if (unpublishBean.getPubId().equals(pubId)) {
                    indice = i;
                    break;
                }
            }

            if (indice != -1) {
                nuxeoController.executeNuxeoCommand(new UnpublishCommand(currentDoc.getId(), indice));
                // Force cache initialisation
                ctx.reload();

            } else {
                returnObject.put("returnCode", 1);
            }


        } catch (Exception e) {
            returnObject = handleDefaultExceptions(e, principal);
        }


        return returnObject;
    }


    @RequestMapping(value = "/Admin.createUser", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Object> createUser(@RequestBody CreateUserBean userBean, HttpServletRequest request, HttpServletResponse response, Principal principal)
            throws Exception {


        Map<String, Object> returnObject = new LinkedHashMap<>();
        returnObject.put("returnCode", GenericErrors.ERR_OK);

        try {

            Person findPerson = personUpdateService.getEmptyPerson();
            findPerson.setMail(userBean.getMail());
            if (personUpdateService.findByCriteria(findPerson).size() > 0) {
                returnObject.put("returnCode", ERR_CREATE_USER_MAIL_ALREADYEXIST);
                return returnObject;
            }

            // Person
            Person person = personUpdateService.getEmptyPerson();
            person.setUid(userBean.getMail());
            person.setCn(userBean.getMail());
            person.setSn(userBean.getFirstName() + " " + userBean.getLastName());
            person.setGivenName(userBean.getFirstName());
            person.setDisplayName(userBean.getFirstName() + " " + userBean.getLastName());
            person.setMail(userBean.getMail());

            personUpdateService.create(person);
            personUpdateService.updatePassword(person, "osivia");


        } catch (Exception e) {
            returnObject = handleDefaultExceptions(e, principal);
        }
        return returnObject;
    }


    private Map<String,String> parseProperties(Map<String, String> requestProperties) {
        // set qualifiers
        Map<String,String> properties = new HashMap<String,String>();
        String standardLevel = convertLevelQualifier(requestProperties.get("level"));
        if (standardLevel != null)
            properties.put("level", standardLevel);
        String subject = requestProperties.get("subject");
        if( subject != null)
            properties.put("subject", subject);        

        return properties;
    }

    /**
     * Convert the local qualifier to the standard one
     * 
     * @param pronoteQualifier
     * @return the supported qualifier, or null
     */
    private String convertLevelQualifier(String pronoteQualifier) {

        if (levelQualifier == null) {
            levelQualifier = new ConcurrentHashMap<>();
            levelQualifier.put("LT", "T");
            levelQualifier.put("L1", "1");
            levelQualifier.put("L2", "2");
            levelQualifier.put("L3", "3");
            levelQualifier.put("L4", "4");
            levelQualifier.put("L5", "5");
            levelQualifier.put("L6", "6");
        }

        return levelQualifier.get(pronoteQualifier);
    }

    /**
     * @return
     */
    @Bean(name = "multipartResolver")
    public CommonsMultipartResolver createMultipartResolver() {
        CommonsMultipartResolver resolver = new CommonsMultipartResolver();
        resolver.setDefaultEncoding("utf-8");
        return resolver;
    }
    
    
    @RequestMapping(value = "/User.signup", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)

    public Map<String, Object> signUp(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	
        Map<String, Object> returnObject = new LinkedHashMap<>();
        returnObject.put("returnCode", GenericErrors.ERR_OK);
 

        try {

        	String message = "";
            String token = request.getHeader("Authorization");
            if( StringUtils.isNotEmpty(token))  {
                if( token.startsWith(TOKEN_PREFIX)) {
                    Algorithm algorithm = Algorithm.HMAC256("??PRONOTESECRET??");
                    JWTVerifier verifier = JWT.require(algorithm)
                        .withIssuer("pronote")
                        .build(); //Reusable verifier instance
                    DecodedJWT jwt = verifier.verify(token.substring(TOKEN_PREFIX.length()));
                    message = jwt.getClaim("firstName").asString() + " " +jwt.getClaim("lastName").asString(); 
                    
         

                    //String webToken = TokenUtils.generateToken(principal.getName());
                    //String url = "https://" + request.getServerName() + "/toutatice-portail-cms-nuxeo/binary?id="+id+ "&webToken=" + webToken+"&viewer=true";
                    //returnObject.put("url", url);        
                    
                    
                }
            }
            
//           throw new Exception("Invalid token");
//        	
        	
        	returnObject.put("url", message);

        } catch (Exception e) {
            returnObject = handleDefaultExceptions(e, null);
        }
        return returnObject;
    }

}
