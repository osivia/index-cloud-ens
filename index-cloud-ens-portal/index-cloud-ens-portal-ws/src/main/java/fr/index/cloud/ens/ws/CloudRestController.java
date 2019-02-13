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
import org.osivia.portal.api.cache.services.CacheInfo;
import org.osivia.portal.core.web.IWebIdService;
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

import fr.index.cloud.ens.ws.beans.PublishBean;
import fr.index.cloud.ens.ws.beans.UploadBean;
import fr.index.cloud.ens.ws.commands.FolderGetChildrenCommand;
import fr.index.cloud.ens.ws.commands.PublishCommand;
import fr.index.cloud.ens.ws.commands.UploadFileCommand;
import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoException;
import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoDocumentContext;
import fr.toutatice.portail.cms.nuxeo.api.services.NuxeoCommandContext;


@RestController
public class CloudRestController {

    private static final String PROP_TTC_WEBID = "ttc:webid";
    private static final String PROP_SHARE_LINK = "rshr:linkId";


    // TODO : HOW TO INJECT FROM SimpleDocumentCreatorController
    public static PortletContext portletContext;
    private static String TOKEN_PREFIX = "Bearer ";
    private static String USERWORKSPACES_DOMAIN = "/default-domain/UserWorkspaces";

    Map<String, String> levelQualifier = null;


    /** Logger. */
    private static final Log logger = LogFactory.getLog(CloudRestController.class);


    /**
     * Get a nuxeoController associated to the current user
     * 
     * @return
     * @throws Exception
     */
    private NuxeoController getNuxeocontroller(HttpServletRequest request, Principal principal) throws Exception {
       
        /*
        String token = request.getHeader("Authorization");
        if (StringUtils.isNotEmpty(token)) {
            if (token.startsWith(TOKEN_PREFIX)) {
                Algorithm algorithm = Algorithm.HMAC256("??PRONOTESECRET??");
                JWTVerifier verifier = JWT.require(algorithm).withIssuer("pronote").build(); // Reusable verifier instance
                DecodedJWT jwt = verifier.verify(token.substring(TOKEN_PREFIX.length()));
                String userId = jwt.getSubject();

                NuxeoController nuxeoController = new NuxeoController(portletContext);
                nuxeoController.setServletRequest(request);
                nuxeoController.setAuthType(NuxeoCommandContext.AUTH_TYPE_USER);
                nuxeoController.setCacheType(CacheInfo.CACHE_SCOPE_NONE);

                request.setAttribute("osivia.delegation.userName", principal.getName());

                return nuxeoController;
            }
        }
        

        throw new Exception("Invalid token");*/
        
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
    private Map<String, Object> handleDefaultExceptions(Exception e) {
        
        Map<String, Object> response = new LinkedHashMap<>();


        if (e instanceof NuxeoException) {
            NuxeoException nxe = (NuxeoException) e;

            if (nxe.getErrorCode() == NuxeoException.ERROR_NOTFOUND)
                response.put("returnCode",GenericErrors.ERR_NOT_FOUND);
            else if (nxe.getErrorCode() == NuxeoException.ERROR_FORBIDDEN)
                response.put("returnCode",GenericErrors.ERR_FORBIDDEN);

        } else {
            // TODO use the log API (look at BinaryServlet)
            e.printStackTrace();
            response.put("returnCode",GenericErrors.ERR_NOT_FOUND);
        }
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
    
    private Map <String, Object> initContent(Document doc, String type, boolean mainObject){
        
        Map<String, Object> contents = new LinkedHashMap<>();

        if( mainObject)
            contents.put("returnCode", 0);       
        
        contents.put("type", type);
        contents.put("id", doc.getProperties().getString(PROP_TTC_WEBID));
        contents.put("title", doc.getTitle());

        if (doc.getProperties().get("common:size") != null)  {
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

    
    private Map <String, Object> initContent(Document doc, String type){
        return  initContent( doc,  type,  false);
    }
    


    @RequestMapping(value = "/Drive.content", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)

    public Map<String,Object> getContent(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "id", required = false) String id, Principal principal)
            throws Exception {

        NuxeoController nuxeoController = getNuxeocontroller(request, principal);

        Map<String,Object> returnObject;
        
        try {
            // TOD get real user workspace
            // see demo project customizer
            String rootPath = USERWORKSPACES_DOMAIN + "/" + request.getAttribute("osivia.delegation.userName") + "/documents";

            String path = null;
            if (id != null)
                path = IWebIdService.FETCH_PATH_PREFIX + id;
            else
                path = rootPath;

            // Get durrent doc
            Document currentDoc = nuxeoController.getDocumentContext(path).getDocument();
            
            boolean isRoot = currentDoc.getPath().equals(rootPath);
            String type = currentDoc.getPath().equals(rootPath) ? "root" :  currentDoc.getType().toLowerCase();
            
            returnObject = initContent(currentDoc, type, true);       
   
            // Get hierarchy

            List<Map<String,Object>> hierarchy = new ArrayList<>();
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
            
   
            List<Map<String,Object>> childrenList = new ArrayList<>();
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
            returnObject = handleDefaultExceptions(e);
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
    public void handleFileUpload(@RequestParam("file") MultipartFile file, @RequestParam("uploadInfos") String fileUpload, HttpServletRequest request,
            HttpServletResponse response, Principal principal) throws Exception {

        NuxeoController nuxeoController = getNuxeocontroller(request, principal);

        UploadBean uploadBean = new ObjectMapper().readValue(fileUpload, UploadBean.class);

        // Get parent doc
        Document parentDoc = nuxeoController.getDocumentContext(IWebIdService.FETCH_PATH_PREFIX + uploadBean.getParentId()).getDocument();

        // set qualifiers
        PropertyMap properties = parseProperties(uploadBean.getProperties());


        // Execute import
        INuxeoCommand command = new UploadFileCommand(parentDoc.getId(), file, properties);
        nuxeoController.executeNuxeoCommand(command);
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

    @RequestMapping(value = "/Drive.publish", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public String publish(@RequestBody PublishBean publishBean, HttpServletRequest request, HttpServletResponse response,  Principal principal) throws Exception {

        NuxeoController nuxeoController = getNuxeocontroller(request, principal);

        NuxeoDocumentContext ctx = nuxeoController.getDocumentContext(IWebIdService.FETCH_PATH_PREFIX + publishBean.getContentId());

        // Get parent doc
        Document currentDoc = ctx.getDocument();

        // set qualifiers
        PropertyMap properties = parseProperties(publishBean.getProperties());


        // Execute import
        INuxeoCommand command = new PublishCommand(currentDoc.getId(), properties);
        String shareLink = (String) nuxeoController.executeNuxeoCommand(command);

        // Force cache initialisation
        ctx.reload();


        return shareLink;
    }


    private PropertyMap parseProperties(Map<String, String> requestProperties) {
        // set qualifiers
        PropertyMap properties = new PropertyMap();
        String standardLevel = convertLevelQualifier(requestProperties.get("level"));
        if (standardLevel != null)
            properties.set("idxcl:level", standardLevel);

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

}
