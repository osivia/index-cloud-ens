package fr.index.cloud.ens.ws;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
import org.osivia.portal.core.error.GlobalErrorHandler;
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

import fr.index.cloud.ens.ws.beans.BaseBean;
import fr.index.cloud.ens.ws.beans.ChildBean;
import fr.index.cloud.ens.ws.beans.DriveParentBean;
import fr.index.cloud.ens.ws.beans.DriveRootBean;
import fr.index.cloud.ens.ws.beans.FileBean;
import fr.index.cloud.ens.ws.beans.FileChildBean;
import fr.index.cloud.ens.ws.beans.FolderBean;
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
    private NuxeoController getNuxeocontroller(HttpServletRequest request) throws Exception {
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

                request.setAttribute("osivia.delegation.userName", userId);

                return nuxeoController;
            }
        }

        throw new Exception("Invalid token");
    }


    /**
     * Conversion des exceptions en erreur JSON
     * 
     * @param e
     * @return
     */
    private BaseBean handleDefaultExceptions(Exception e) {
        BaseBean returnBean = null;

        if (e instanceof NuxeoException) {
            NuxeoException nxe = (NuxeoException) e;

            if (nxe.getErrorCode() == NuxeoException.ERROR_NOTFOUND)
                returnBean = new BaseBean(GenericErrors.ERR_NOT_FOUND);
            else if (nxe.getErrorCode() == NuxeoException.ERROR_FORBIDDEN)
                returnBean = new BaseBean(GenericErrors.ERR_FORBIDDEN);
        } else {
            // TODO use the log API (look at BinaryServlet)
            e.printStackTrace();
            returnBean = new BaseBean(GenericErrors.ERR_UNKNOWN);
        }

        return returnBean;
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


    @CrossOrigin
    @RequestMapping(value = "/Drive.content1", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)

    public BaseBean getContent1(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "id", required = false) String id)
            throws Exception {

        NuxeoController nuxeoController = getNuxeocontroller(request);

        BaseBean result = null;
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

            // Get hierarchy
            boolean isRoot = true;

            List<DriveParentBean> hierarchy = new ArrayList<>();
            String hierarchyPath = currentDoc.getPath().substring(0, currentDoc.getPath().lastIndexOf('/'));
            

            while (hierarchyPath.contains(rootPath)) {
                isRoot = false;

                Document hierarchyDoc = nuxeoController.getDocumentContext(hierarchyPath).getDocument();
                String type = hierarchyDoc.getType();
                if (hierarchyPath.equals(rootPath))
                    type = DriveRootBean.TYPE;
                hierarchy.add(0, new DriveParentBean(type, hierarchyDoc.getProperties().getString(PROP_TTC_WEBID), hierarchyDoc.getTitle()));

                // next parent
                hierarchyPath = hierarchyPath.substring(0, hierarchyPath.lastIndexOf('/'));
            }


            // Facets
            PropertyList facets = currentDoc.getFacets();
            if (!facets.list().contains("Folderish")) {

                long size = 0;
                if (currentDoc.getProperties().get("common:size") != null)
                    size = currentDoc.getProperties().getLong("common:size");
                
 
                result = new FileBean(currentDoc.getProperties().getString(PROP_TTC_WEBID), currentDoc.getTitle(), hierarchy,
                        currentDoc.getProperties().getString(PROP_SHARE_LINK), size);
            } else {
                // Add childrens
                Documents children = (Documents) nuxeoController.executeNuxeoCommand(new FolderGetChildrenCommand(currentDoc));

                List<ChildBean> contentChildren = new ArrayList<ChildBean>();
                for (Document doc : children.list()) {

                    if (doc.getFacets().list().contains("Folderish")) {
                        contentChildren
                        .add(new ChildBean(doc.getType().toLowerCase(), doc.getProperties().getString(PROP_TTC_WEBID), doc.getTitle()));
                    } else {
                        long size = 0;
                        if (doc.getProperties().get("common:size") != null)
                            size = doc.getProperties().getLong("common:size");
                        
                        Date lastModifiedDate = doc.getProperties().getDate("dc:modified");
                        contentChildren
                                .add(new FileChildBean(doc.getType().toLowerCase(), doc.getProperties().getString(PROP_TTC_WEBID), doc.getTitle(), size, lastModifiedDate.getTime()));
                    }
                }

                if (isRoot)
                    result = new DriveRootBean(currentDoc.getProperties().getString(PROP_TTC_WEBID), currentDoc.getTitle(), contentChildren);
                else
                    // Folder
                    result = new FolderBean(currentDoc.getProperties().getString(PROP_TTC_WEBID), currentDoc.getTitle(), contentChildren, hierarchy);
            }
        } catch (Exception e) {
            result = handleDefaultExceptions(e);
        }
        return result;

    }
    
    
    
    @CrossOrigin
    @RequestMapping(value = "/Drive.content", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)

    public Map<String, Object> getContent(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "id", required = false) String id)
            throws Exception {

  
        Map<String, Object> result = new HashMap<>();
        
        result.put("returnCode", "11111");
     
        return result;

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
    @CrossOrigin
    @RequestMapping(value = "/Drive.upload", method = RequestMethod.POST, consumes = {"multipart/form-data"})
    @ResponseStatus(HttpStatus.OK)
    public void handleFileUpload(@RequestParam("file") MultipartFile file, @RequestParam("uploadInfos") String fileUpload, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        NuxeoController nuxeoController = getNuxeocontroller(request);

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
    @CrossOrigin
    @RequestMapping(value = "/Drive.publish", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public String publish(@RequestBody PublishBean publishBean, HttpServletRequest request, HttpServletResponse response) throws Exception {

        NuxeoController nuxeoController = getNuxeocontroller(request);

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
