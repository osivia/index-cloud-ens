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
import org.osivia.portal.core.web.IWebIdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import fr.index.cloud.ens.ext.conversion.ConversionRepository;
import fr.index.cloud.ens.ext.conversion.IConversionService;
import fr.index.cloud.ens.ext.etb.EtablissementService;
import fr.index.cloud.ens.ws.beans.CreateFolderBean;
import fr.index.cloud.ens.ws.beans.CreateUserBean;
import fr.index.cloud.ens.ws.beans.GetSharedUrlBean;
import fr.index.cloud.ens.ws.beans.PublishBean;
import fr.index.cloud.ens.ws.beans.UnpublishBean;
import fr.index.cloud.ens.ws.beans.UploadBean;
import fr.index.cloud.ens.ws.commands.AddPropertiesCommand;
import fr.index.cloud.ens.ws.commands.CreateFolderCommand;
import fr.index.cloud.ens.ws.commands.FetchByPubIdCommand;
import fr.index.cloud.ens.ws.commands.FetchByTitleCommand;
import fr.index.cloud.ens.ws.commands.FolderGetChildrenCommand;
import fr.index.cloud.ens.ws.commands.GetSharedUrlCommand;
import fr.index.cloud.ens.ws.commands.GetUserProfileCommand;
import fr.index.cloud.ens.ws.commands.PublishCommand;
import fr.index.cloud.ens.ws.commands.UnpublishCommand;
import fr.index.cloud.ens.ws.commands.UploadFileCommand;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoException;
import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoDocumentContext;
import fr.toutatice.portail.cms.nuxeo.api.domain.DocumentDTO;
import fr.toutatice.portail.cms.nuxeo.api.services.INuxeoService;
import fr.toutatice.portail.cms.nuxeo.api.services.NuxeoCommandContext;
import fr.toutatice.portail.cms.nuxeo.api.services.dao.DocumentDAO;


/**
 * Services Rest associés au Drive
 * 
 * @author Jean-Sébastien
 */
@RestController
public class DriveRestController {

    
    private static final String PROP_TTC_WEBID = "ttc:webid";
    private static final String PROP_SHARE_LINK = "rshr:linkId";
    private static final String PROP_ENABLE_LINK = "rshr:enabledLink";

    public static final String SHARE_URL_PREFIX = "/s/";
    public static final int ERR_CREATE_USER_MAIL_ALREADYEXIST = 1;

    public final static String DEFAULT_FORMAT = "default";
    public final static String NATIVE_FORMAT = "native";
    public final static String PDF_FORMAT = "pdf";

    public static final String DRIVE_TYPE_FILE = "file";
    public static final String DRIVE_TYPE_FOLDER = "folder";
    public static final String DRIVE_TYPE_ROOT = "root";
    
    
    public static PortletContext portletContext;
    
 
    Map<String, String> levelQualifier = null;



    @Autowired
    @Qualifier("personUpdateService")
    private PersonUpdateService personUpdateService;

    @Autowired
    private ITokenService tokenService;


    @Autowired
    private ErrorMgr errorMgr;

    /**
     * Document DAO.
     */
    @Autowired
    IConversionService conversionService;

    /**
     * Wraps the doc. fetching according to the WS error handling
     * 
     * (Nuxeo Exception are converted into GenericException with the {id} of the content
     * added).
     * 
     * @param ctl
     * @param path
     * @return
     */
    public static Document wrapContentFetching(NuxeoController nuxeoController, String path) throws GenericException {
        Document currentDoc = null;
        try {
            currentDoc = nuxeoController.getDocumentContext(path).getDocument();
        } catch (NuxeoException e) {
            throw new GenericException(e, path);
        }
        return currentDoc;
    }


    /**
     * Get root url
     * 
     * 
     * @param ctl
     * @param path
     * @return
     */

    public static String getUrl(HttpServletRequest request) {
        String url = "https://" + request.getServerName();
        return url;
    }


    public static String getSharedUrl(HttpServletRequest request, String shareLink) {
        return getUrl(request) + SHARE_URL_PREFIX + shareLink;
    }


    /**
     * Get a nuxeoController associated to the current user
     * 
     * @return
     * @throws Exception
     */
    public static NuxeoController getNuxeocontroller(HttpServletRequest request, Principal principal) throws Exception {


        NuxeoController nuxeoController = new NuxeoController(portletContext);
        nuxeoController.setServletRequest(request);
        nuxeoController.setAuthType(NuxeoCommandContext.AUTH_TYPE_USER);
        nuxeoController.setCacheType(CacheInfo.CACHE_SCOPE_NONE);

        if (principal != null) {
            request.setAttribute("osivia.delegation.userName", principal.getName());
        }

        return nuxeoController;

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

    private Map<String, Object> initContent(HttpServletRequest request, Document doc, String type, boolean mainObject) {

        Map<String, Object> contents = new LinkedHashMap<>();

        if (mainObject)
            contents.put("returnCode", ErrorMgr.ERR_OK);

        contents.put("type", type);
        contents.put("id", doc.getProperties().getString(PROP_TTC_WEBID));
        contents.put("title", doc.getTitle());

        if (doc.getProperties().get("common:size") != null) {
            long size = doc.getProperties().getLong("common:size");
            contents.put("fileSize", size);
        }

        if( mainObject) {
            /* link */
            Boolean enableLink = doc.getProperties().getBoolean(PROP_ENABLE_LINK, false);
            if (enableLink) {
                String shareLink = doc.getProperties().getString(PROP_SHARE_LINK);
                if (shareLink != null) {
                    contents.put("shareUrl", getSharedUrl(request, shareLink));
                }
            }

            boolean pdfConvertible = ispdfConvertible(doc);
            contents.put("pdfConvertible", pdfConvertible);
            
            /* format */
            String format = doc.getProperties().getString("rshr:format");
            if( StringUtils.isEmpty(format))    {
                format = DEFAULT_FORMAT;
             }
             contents.put("pubFormat", format);
            
            
            // Digest
            PropertyMap fileContent = doc.getProperties().getMap("file:content");
            if (fileContent != null) {
                contents.put("hash", fileContent.getString("digest"));   
            }
        }
          

        if (doc.getProperties().get("dc:modified") != null) {
            Date lastModifiedDate = doc.getProperties().getDate("dc:modified");
            contents.put("lastModified", lastModifiedDate.getTime());
        }

        return contents;
    }


    /**
     * Checks if is pdf convertible.
     *
     * @param doc the doc
     * @return true, if is pdf convertible
     */

    private boolean ispdfConvertible(Document doc) {
        
        DocumentDTO dto = DocumentDAO.getInstance().toDTO(doc);
        return dto.isPdfConvertible();
     }


    private Map<String, Object> initContent(HttpServletRequest request, Document doc, String type) {
        return initContent(request, doc, type, false);
    }


    @RequestMapping(value = "/Drive.webUrl", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)

    public Map<String, Object> getWebUrl(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "id", required = false) String id, Principal principal) throws Exception {

        Map<String, Object> returnObject = new LinkedHashMap<>();
        returnObject.put("returnCode", ErrorMgr.ERR_OK);

        WSPortalControllerContext ctx = new WSPortalControllerContext(request, response, principal);

        try {
            Map<String, String> tokenAttributes = new ConcurrentHashMap<>();
            tokenAttributes.put("uid", principal.getName());
            String webToken = tokenService.generateToken(tokenAttributes);
            String url = getUrl(request) + "/toutatice-portail-cms-nuxeo/binary?id=" + id + "&webToken=" + webToken + "&viewer=true";
            returnObject.put("url", url);

        } catch (Exception e) {
            returnObject = errorMgr.handleDefaultExceptions(ctx, e);
        }
        return returnObject;
    }


    /**
     * Get content datas for the specified id
     * 
     * @param request
     * @param response
     * @param id
     * @param principal
     * @return
     * @throws Exception
     */

    @RequestMapping(value = "/Drive.content", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)

    public Map<String, Object> getContent(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "id", required = false) String id,
            Principal principal) throws Exception {


        WSPortalControllerContext ctx = new WSPortalControllerContext(request, response, principal);
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
            Document currentDoc = wrapContentFetching(nuxeoController, path);
            PropertyList facets = currentDoc.getFacets();            

            String type;
            if(currentDoc.getPath().equals(rootPath))
                type =  DRIVE_TYPE_ROOT;
            else if (facets.list().contains("Folderish"))
                type =  DRIVE_TYPE_FOLDER;
            else 
                type =  DRIVE_TYPE_FILE;              

            returnObject = initContent(request, currentDoc, type, true);

            // Get hierarchy

            List<Map<String, Object>> hierarchy = new ArrayList<>();
            String hierarchyPath = currentDoc.getPath().substring(0, currentDoc.getPath().lastIndexOf('/'));

            while (hierarchyPath.contains(rootPath)) {
                Document hierarchyDoc = nuxeoController.getDocumentContext(hierarchyPath).getDocument();
                String hierarchyType = hierarchyDoc.getType();
                if (hierarchyPath.equals(rootPath))
                    hierarchyType = DRIVE_TYPE_ROOT;
                else
                    hierarchyType = hierarchyDoc.getType().toLowerCase();
                hierarchy.add(0, initContent(request, hierarchyDoc, hierarchyType));

                // next parent
                hierarchyPath = hierarchyPath.substring(0, hierarchyPath.lastIndexOf('/'));
            }

            returnObject.put("parents", hierarchy);


            List<Map<String, Object>> childrenList = new ArrayList<>();

            if (facets.list().contains("Folderish")) {

                // Add childrens
                Documents children = (Documents) nuxeoController.executeNuxeoCommand(new FolderGetChildrenCommand(currentDoc));

                for (Document doc : children.list()) {
                    childrenList.add(initContent(request, doc, doc.getType().toLowerCase()));
                }

            }
            returnObject.put("childrens", childrenList);

        } catch (Exception e) {
            returnObject = errorMgr.handleDefaultExceptions(ctx, e);
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
    public Map<String, Object> handleFileUpload(@RequestParam(DRIVE_TYPE_FILE) MultipartFile file, @RequestParam("uploadInfos") String fileUpload,
            HttpServletRequest request, HttpServletResponse response, Principal principal) throws Exception {


        WSPortalControllerContext ctx = new WSPortalControllerContext(request, response, principal);

        Map<String, Object> returnObject = new LinkedHashMap<>();
        returnObject.put("returnCode", ErrorMgr.ERR_OK);

        try {

            NuxeoController nuxeoController = getNuxeocontroller(request, principal);

            UploadBean uploadBean = new ObjectMapper().readValue(fileUpload, UploadBean.class);

            // Get parent doc
            Document parentDoc = wrapContentFetching(nuxeoController, IWebIdService.FETCH_PATH_PREFIX + uploadBean.getParentId());
            
            // Get the OAuth2 client ID
            Authentication a = SecurityContextHolder.getContext().getAuthentication();
            String clientId = ((OAuth2Authentication) a).getOAuth2Request().getClientId();


            // Execute import
            INuxeoCommand command = new UploadFileCommand(parentDoc.getId(), file);
            Document doc = (Document) nuxeoController.executeNuxeoCommand(command);
            
            // set qualifiers
            // retrieve doc webId
            doc = wrapContentFetching(nuxeoController, doc.getPath());
            Map<String, String> properties = parseProperties(ctx, doc.getProperties().getString(PROP_TTC_WEBID), clientId, uploadBean.getProperties());
            INuxeoCommand updateCommand = new AddPropertiesCommand(doc, properties);
            nuxeoController.executeNuxeoCommand(updateCommand);            

        } catch (Exception e) {
            returnObject = errorMgr.handleDefaultExceptions(ctx, e);
        }


        return returnObject;
    }


    /**
     * Create a folder
     * 
     * @param file
     * @param parentWebId
     * @param extraField
     * @param request
     * @param response
     * @throws Exception
     */

    @RequestMapping(value = "/Drive.createFolder", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Object> createFolder(@RequestBody CreateFolderBean createFolderBean, HttpServletRequest request, HttpServletResponse response,
            Principal principal) throws Exception {

        WSPortalControllerContext wsCtx = new WSPortalControllerContext(request, response, principal);

        Map<String, Object> returnObject = new LinkedHashMap<>();
        returnObject.put("returnCode", ErrorMgr.ERR_OK);
        try {

            NuxeoController nuxeoController = getNuxeocontroller(request, principal);


            String path = IWebIdService.FETCH_PATH_PREFIX + createFolderBean.getParentId();

            Document parentDoc = wrapContentFetching(nuxeoController, path);


            // Check if already exist
            INuxeoCommand checkCmd = new FetchByTitleCommand(parentDoc, createFolderBean.getFolderName());
            Documents docs = (Documents) nuxeoController.executeNuxeoCommand(checkCmd);

            if (docs.size() > 0) {
                returnObject = errorMgr.getErrorResponse(1, "This folder already exists");
            } else {

                // Execute creation
                INuxeoCommand command = new CreateFolderCommand(parentDoc, createFolderBean.getFolderName());

                @SuppressWarnings("unchecked")
                Document folder = (Document) nuxeoController.executeNuxeoCommand(command);


                returnObject.put("folderId", folder.getProperties().getString(PROP_TTC_WEBID));
            }


        } catch (Exception e) {
            returnObject = errorMgr.handleDefaultExceptions(wsCtx, e);
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

    @RequestMapping(value = "/Drive.getShareUrl", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Object> getSharedUrl(@RequestBody GetSharedUrlBean sharedUrlBean, HttpServletRequest request, HttpServletResponse response,
            Principal principal) throws Exception {

        WSPortalControllerContext wsCtx = new WSPortalControllerContext(request, response, principal);

        Map<String, Object> returnObject = new LinkedHashMap<>();
        returnObject.put("returnCode", ErrorMgr.ERR_OK);
        try {

            NuxeoController nuxeoController = getNuxeocontroller(request, principal);


            String path = IWebIdService.FETCH_PATH_PREFIX + sharedUrlBean.getContentId();
            NuxeoDocumentContext ctx = nuxeoController.getDocumentContext(path);

            Document currentDoc = wrapContentFetching(nuxeoController, path);


            // Check format
            boolean checkFormat = false;
            if (StringUtils.isNotEmpty(sharedUrlBean.getFormat())) {
                if (DEFAULT_FORMAT.equals(sharedUrlBean.getFormat())) {
                    checkFormat = true;
                } else if (NATIVE_FORMAT.equals(sharedUrlBean.getFormat())) {
                    checkFormat = true;
                } else if (PDF_FORMAT.equals(sharedUrlBean.getFormat())) {
                    if (ispdfConvertible(currentDoc))
                        checkFormat = true;
                }
            } else {
                checkFormat = true;
            }

            if (!checkFormat) {
                returnObject = errorMgr.getErrorResponse(1, "Format not supported");
            } else {

                // Execute publish
                INuxeoCommand command = new GetSharedUrlCommand(currentDoc, sharedUrlBean.getFormat());

                @SuppressWarnings("unchecked")
                Map<String, String> returnMap = (Map<String, String>) nuxeoController.executeNuxeoCommand(command);

                // Prepare results
                returnObject.put("shareUrl", getUrl(request) + SHARE_URL_PREFIX + returnMap.get("shareId"));
                returnMap.remove("shareId");


                // Force cache initialisation
                ctx.reload();
            }

        } catch (Exception e) {
            returnObject = errorMgr.handleDefaultExceptions(wsCtx, e);
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

        WSPortalControllerContext wsCtx = new WSPortalControllerContext(request, response, principal);

        Map<String, Object> returnObject = new LinkedHashMap<>();
        returnObject.put("returnCode", ErrorMgr.ERR_OK);
        try {

            NuxeoController nuxeoController = getNuxeocontroller(request, principal);
            
            
            // Get the OAuth2 client ID
            Authentication a = SecurityContextHolder.getContext().getAuthentication();
            String clientId = ((OAuth2Authentication) a).getOAuth2Request().getClientId();
             
            
            Document currentDoc = null;
            
            // Extract share ID
            String url = publishBean.getShareUrl();
            int iName = url.lastIndexOf('/');
            if (iName != -1) {
                currentDoc = nuxeoController.fetchSharedDocument(url.substring(iName + 1), false);
            }



            // Execute publish
            INuxeoCommand command = new PublishCommand(currentDoc, publishBean, clientId);

            @SuppressWarnings("unchecked")
            Map<String, String> returnMap = (Map<String, String>) nuxeoController.executeNuxeoCommand(command);
            
            // set qualifiers
            Map<String, String> properties = parseProperties(wsCtx, currentDoc.getProperties().getString(PROP_TTC_WEBID), clientId, publishBean.getProperties());            
            INuxeoCommand updateCommand = new AddPropertiesCommand(currentDoc, properties);
            nuxeoController.executeNuxeoCommand(updateCommand);

            // Prepare results
            returnObject.put("pubId", returnMap.get("pubId"));

            // Force cache initialisation
            NuxeoDocumentContext ctx = nuxeoController.getDocumentContext(currentDoc.getPath());            
            ctx.reload();

        } catch (Exception e) {
            returnObject = errorMgr.handleDefaultExceptions(wsCtx, e);
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

        WSPortalControllerContext wsCtx = new WSPortalControllerContext(request, response, principal);

        Map<String, Object> returnObject = new LinkedHashMap<>();
        returnObject.put("returnCode", ErrorMgr.ERR_OK);

        try {

            NuxeoController nuxeoController = getNuxeocontroller(request, principal);

            // Find doc by pubId
            Documents docs = (Documents) nuxeoController.executeNuxeoCommand(new FetchByPubIdCommand(unpublishBean.getPubId()));

            if (docs.size() > 0) {

                if (docs.size() > 1)
                    throw new Exception("more than one pubId detected");

                Document currentDoc = docs.get(0);
                NuxeoDocumentContext ctx = nuxeoController.getDocumentContext(currentDoc.getPath());

                // unpublish
                nuxeoController.executeNuxeoCommand(new UnpublishCommand(currentDoc, unpublishBean.getPubId()));

                // Force cache initialisation
                ctx.reload();

            }

            else {
                returnObject = errorMgr.getErrorResponse(1, "Publication with id '" + unpublishBean.getPubId() + "' not found");
            }


        } catch (Exception e) {
            returnObject = errorMgr.handleDefaultExceptions(wsCtx, e);
        }


        return returnObject;
    }


    @RequestMapping(value = "/Admin.createUser", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Object> createUser(@RequestBody CreateUserBean userBean, HttpServletRequest request, HttpServletResponse response, Principal principal)
            throws Exception {


        WSPortalControllerContext wsCtx = new WSPortalControllerContext(request, response, principal);

        Map<String, Object> returnObject = new LinkedHashMap<>();
        returnObject.put("returnCode", ErrorMgr.ERR_OK);

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
            returnObject = errorMgr.handleDefaultExceptions(wsCtx, e);
        }
        return returnObject;
    }


    @RequestMapping(value = "/Drive.error", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Object> error(HttpServletRequest request, HttpServletResponse response, Principal principal,
            @RequestParam(value = "type", required = false) String type) throws Exception {

        WSPortalControllerContext wsCtx = new WSPortalControllerContext(request, response, principal);

        Map<String, Object> returnObject = new LinkedHashMap<>();
        returnObject.put("returnCode", ErrorMgr.ERR_OK);

        try {
            if ("exception".equals(type))
                throw new Exception("this is the exception msg");

            if ("error".equals(type))
                returnObject = errorMgr.getErrorResponse(3, "Erreur de test");

        } catch (Exception e) {
            returnObject = errorMgr.handleDefaultExceptions(wsCtx, e);
        }
        return returnObject;
    }


    private Map<String, String> parseProperties(PortalControllerContext ctx, String docId, String clientId, Map<String, String> requestProperties) {
        // set qualifiers
        Map<String, String> properties = new HashMap<String, String>();
        String standardLevel = convertLevelQualifier(ctx, docId, clientId, requestProperties.get("levelCode"), requestProperties.get("levelName"));
        if (standardLevel != null)
            properties.put("level", standardLevel);
        String standardSubject = convertSubjectQualifier(ctx, docId, clientId, requestProperties.get("subjectCode"), requestProperties.get("subjectName"));
        if (standardSubject != null)               
            properties.put("subject", standardSubject);

        return properties;
    }

    /**
     * Convert the local qualifier to the standard one
     * 
     * @param pronoteQualifier
     * @return the supported qualifier, or null
     */
    private String convertLevelQualifier(PortalControllerContext ctx, String docId, String clientId,String pronoteCode, String pronoteLabel) {
        return conversionService.convert(ctx, docId, clientId, "L", pronoteCode, pronoteLabel);
    }
    
    /**
     * Convert the local qualifier to the standard one
     * 
     * @param pronoteQualifier
     * @return the supported qualifier, or null
     */
    private String convertSubjectQualifier(PortalControllerContext ctx, String docId, String clientId,String pronoteCode, String pronoteLabel) {
        return conversionService.convert(ctx, docId, clientId, "S", pronoteCode, pronoteLabel);
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
