package fr.index.cloud.ens.ws;

import java.util.ArrayList;
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
import org.osivia.portal.api.cache.services.CacheInfo;
import org.osivia.portal.api.cms.DocumentContext;
import org.osivia.portal.core.web.IWebIdService;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoDocumentContext;
import fr.toutatice.portail.cms.nuxeo.api.services.NuxeoCommandContext;


@RestController
public class CloudRestController {

    // TODO : HOW TO INJECT FROM SimpleDocumentCreatorController
    public static PortletContext portletContext;

    Map<String, String> levelQualifier = null;

    /**
     * Get a nuxeoController associated to the current user
     * 
     * @return
     */
    private NuxeoController getNuxeocontroller(HttpServletRequest request) {

        NuxeoController nuxeoController = new NuxeoController(portletContext);
        nuxeoController.setServletRequest(request);
        nuxeoController.setAuthType(NuxeoCommandContext.AUTH_TYPE_USER);
        nuxeoController.setCacheType(CacheInfo.CACHE_SCOPE_NONE);

        request.setAttribute("osivia.delegation.userName", "admin");

        return nuxeoController;
    }

    /**
     * URL to use: https://cloud-ens.osivia.org/index-cloud-portal-ens-ws/rest/Drive.content
     * 
     * @param request
     * @param id
     * @return
     */

    @CrossOrigin
    @RequestMapping(value = "/Drive.content", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)

    public DriveBaseBean getContent(HttpServletRequest request, @RequestParam(value = "id", required = false) String id) {

        NuxeoController nuxeoController = getNuxeocontroller(request);

        DriveBaseBean result = null;

        // TOD get real user workspace
        // see demo project customizer
        String path = "/default-domain/UserWorkspaces/admin/documents";

        if (id != null)
            path = IWebIdService.FETCH_PATH_PREFIX + id;

        // Get durrent doc
        Document currentDoc = nuxeoController.getDocumentContext(path).getDocument();

        // Get parent
        String parentPath = currentDoc.getPath().substring(0, currentDoc.getPath().lastIndexOf('/'));
        Document parentDoc = nuxeoController.getDocumentContext(parentPath).getDocument();
        String parentId = parentDoc.getProperties().getString("ttc:webid");

        // Get grandparent
        String grandParentPath = parentPath.substring(0, parentPath.lastIndexOf('/'));
        Document grandParentDoc = nuxeoController.getDocumentContext(grandParentPath).getDocument();

        // Facets
        PropertyList facets = currentDoc.getFacets();
        if (!facets.list().contains("Folderish")) {
            result = new FileBean(currentDoc.getProperties().getString("ttc:webid"), currentDoc.getTitle(), parentId, currentDoc.getProperties().getString("rshr:linkId"));
        } else {
            // Add childrens
            Documents children = (Documents) nuxeoController.executeNuxeoCommand(new FolderGetChildrenCommand(currentDoc));

            List<ContentBean> contentChildren = new ArrayList();
            for (Document doc : children.list()) {
                contentChildren.add(new ContentBean(doc.getType().toLowerCase(), doc.getProperties().getString("ttc:webid"), doc.getTitle(),
                        parentDoc.getProperties().getString("ttc:webid")));
            }

            if (grandParentDoc.getType().equals("UserWorkspacesRoot"))
                result = new DriveBean(currentDoc.getProperties().getString("ttc:webid"), currentDoc.getTitle(), contentChildren);
            else
                // Folder
                result = new FolderBean(currentDoc.getProperties().getString("ttc:webid"), currentDoc.getTitle(), contentChildren, parentId);
        }
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
    public void handleFileUpload(@RequestParam("file") MultipartFile file, @RequestParam("uploadInfos") String fileUpload ,HttpServletRequest request,
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
    public String publish(  @RequestBody PublishBean publishBean, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

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
     
    
    
    private PropertyMap parseProperties( Map<String, String> requestProperties) {
        // set qualifiers
        PropertyMap properties = new PropertyMap();
        String standardLevel = convertLevelQualifier(  requestProperties.get("level"));
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
