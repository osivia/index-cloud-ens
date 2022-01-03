package fr.index.cloud.ens.ws.nuxeo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.portlet.PortletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nuxeo.ecm.automation.client.model.FileBlob;
import org.nuxeo.ecm.automation.client.model.OperationInput;
import org.osivia.portal.api.cache.services.CacheInfo;
import org.osivia.portal.core.cms.CMSBinaryContent;
import org.springframework.http.HttpStatus;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.ResourceUtil;
import fr.toutatice.portail.cms.nuxeo.api.services.NuxeoCommandContext;

@RestController
public class NuxeoDrive {
    
    public static PortletContext portletContext;
    
    private Map<String, DriveUploadedFile> uploadedFiles = new ConcurrentHashMap<String, DriveUploadedFile>();
    
    @RequestMapping(value = "/nuxeo/authentication/token", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public String getAuth( HttpServletRequest request, HttpServletResponse response,
            Principal principal) throws Exception {
        Enumeration headers = request.getHeaderNames();
        Map map = request.getParameterMap();
        return "3c44a584-3893-43b0-9a3f-29e8aed399c4";
    }
    
    @RequestMapping(value = "/nuxeo/api/v1/upload/handlers", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Object> getUploadHandler( HttpServletRequest request, HttpServletResponse response,
            Principal principal) throws Exception {
        return new HashMap<String, Object>();
    }
    
    @RequestMapping(value = "/nuxeo/api/v1/drive/configuration", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public String getConf( HttpServletRequest request, HttpServletResponse response,
            Principal principal) throws Exception {
        Enumeration headers = request.getHeaderNames();
        Map map = request.getParameterMap();        
        return "{\"nxdrive_home\":\""+"/default-domain/UserWorkspaces/a/d/m/admin"+"\"}";
    }

    @RequestMapping(value = "/nuxeo/site/automation", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public String getAutomation( HttpServletRequest request, HttpServletResponse response,
            Principal principal) throws Exception {
        File file = ResourceUtils.getFile("classpath:automation.txt");
        String s = new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())));
        return s;

        }
    
    @RequestMapping(value = "/nuxeo/site/automation/NuxeoDrive.GetTopLevelFolder", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public String getTopLevelFolder( HttpServletRequest request, HttpServletResponse response,
            Principal principal) throws Exception {
        NuxeoController nuxeoController = new NuxeoController(portletContext);
        nuxeoController.setServletRequest(request);
        nuxeoController.setAuthType(NuxeoCommandContext.AUTH_TYPE_SUPERUSER);
        nuxeoController.setCacheType(CacheInfo.CACHE_SCOPE_NONE);
        
        Object res = nuxeoController.executeNuxeoCommand(new GetTopLevelFolderCommand());
        
        FileBlob file = (FileBlob) res;
        
        String s = new String(Files.readAllBytes(Paths.get(file.getFile().getAbsolutePath())));
        return s;
        }
    
    
    ///index-cloud-portal-ens-ws/nuxeo/nxbigfile/default/8a88565e-25f5-4864-8482-1a0635fe63cf/blobholder:0/images.png
    //index-cloud-portal-ens-ws/nuxeo/nxbigfile/default/e49ea6ad-6f31-4bf7-819f-cf6444783d79/blobholder:0/facture%20(copie).docx    
    
    @RequestMapping(value = "/nuxeo/nxbigfile/default/{id}/{blob}/{name}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public void getBigFile(HttpServletRequest request, HttpServletResponse response,
      @PathVariable("id") String id) throws Exception {
        NuxeoController nuxeoController = new NuxeoController(portletContext);
        nuxeoController.setServletRequest(request);
        nuxeoController.setAuthType(NuxeoCommandContext.AUTH_TYPE_SUPERUSER);
        nuxeoController.setCacheType(CacheInfo.CACHE_SCOPE_NONE);
        
        nuxeoController.setStreamingSupport(false);
        CMSBinaryContent content = ResourceUtil.getBlobHolderContent(nuxeoController, id, "0");
        
        ResourceUtil.copy(new FileInputStream(content.getFile()), response.getOutputStream(), 4096);

    }
    
    
    @RequestMapping(value = "/nuxeo/api/v1/upload", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public String startBatch( HttpServletRequest request, HttpServletResponse response, 
            Principal principal) throws Exception {
        NuxeoController nuxeoController = new NuxeoController(portletContext);
        nuxeoController.setServletRequest(request);
        nuxeoController.setAuthType(NuxeoCommandContext.AUTH_TYPE_SUPERUSER);
        nuxeoController.setCacheType(CacheInfo.CACHE_SCOPE_NONE);
        
        return "{\"batchId\": "+System.currentTimeMillis()+"}";
    }
    
    
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public class NoContentException extends RuntimeException {
        private static final long serialVersionUID = 1L;}
    
    @RequestMapping(value = "/nuxeo/api/v1/upload/{batchId}/{idx}")
    @ResponseStatus(HttpStatus.OK)
    public void checkUpload( HttpServletRequest request, HttpServletResponse response, 
            Principal principal, @PathVariable("batchId") String batchId, @PathVariable("idx") String idx) throws Exception {
        NuxeoController nuxeoController = new NuxeoController(portletContext);
        nuxeoController.setServletRequest(request);
        nuxeoController.setAuthType(NuxeoCommandContext.AUTH_TYPE_SUPERUSER);
        nuxeoController.setCacheType(CacheInfo.CACHE_SCOPE_NONE);
        
        if( uploadedFiles.get(batchId+"/"+idx) == null)
            throw new NoContentException();
    }
    
    
    @RequestMapping(value = "/nuxeo/api/v1/upload/{batchId}/{idx}", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED )
    public String upload( HttpServletRequest request, HttpServletResponse response, 
            Principal principal,  @PathVariable("batchId") String batchId, @PathVariable("idx") String idx) throws Exception {
        
        Enumeration headers = request.getHeaderNames();
        
        NuxeoController nuxeoController = new NuxeoController(portletContext);
        nuxeoController.setServletRequest(request);
        nuxeoController.setAuthType(NuxeoCommandContext.AUTH_TYPE_SUPERUSER);
        nuxeoController.setCacheType(CacheInfo.CACHE_SCOPE_NONE);
        

     // Create path components to save the file
        final String name = request.getHeader("X-File-Name");
        final String contentType = request.getHeader("Content-Type");
         

        File file = File.createTempFile("upload", ".upload");
        OutputStream out = new FileOutputStream(file);
        InputStream filecontent = null;
        int total=0;

        try {

            filecontent = request.getInputStream();

            int read = 0;
            final byte[] bytes = new byte[1024];

            while ((read = filecontent.read(bytes)) != -1) {
                out.write(bytes, 0, read);
                total +=read;
            }
            
        } finally {
            if (out != null) {
                
                out.close();
            }
            if (filecontent != null) {
                filecontent.close();
            }
            
        }
        uploadedFiles.put(batchId+"/"+idx, new DriveUploadedFile(name, contentType, file));
        
        return "{\"batchId\": "+batchId+", \"fileIdx\": "+idx+", \"uploadType\": \"normal\", \"uploadedSize\": "+total+"}";
        
    }
    


    @RequestMapping(value = "/nuxeo/api/v1/upload/{batchId}/{idx}/execute/NuxeoDrive.CreateFile", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK )
    public String createFile( HttpServletRequest request, HttpServletResponse response, 
            Principal principal,  @PathVariable("batchId") String batchId, @PathVariable("idx") String idx, @RequestBody String requestBody) throws Exception {
        
        
        Enumeration headers = request.getHeaderNames();
        NuxeoController nuxeoController = new NuxeoController(portletContext);
        nuxeoController.setServletRequest(request);
        nuxeoController.setAuthType(NuxeoCommandContext.AUTH_TYPE_SUPERUSER);
        nuxeoController.setCacheType(CacheInfo.CACHE_SCOPE_NONE);
        
        
        DriveUploadedFile uploadedFile =  uploadedFiles.get(batchId+"/"+idx);
        if( uploadedFile == null)
            throw new NoContentException();
        
        // File name
        String name = uploadedFile.getName();
        if( name == null)
            name = uploadedFile.getFile().getName();
        // File content type
        String contentType;
        if (uploadedFile.getContentType() == null) {
            contentType = null;
        } else {
            contentType = uploadedFile.getContentType();
        }
        
        FileBlob blob = new FileBlob(uploadedFile.getFile(), name, contentType);
        
        GetProxyCommand proxyCommand = new GetProxyCommand("NuxeoDrive.CreateFile", requestBody);
        Map<String,String> parameters = new HashMap<>();
        parameters.put("name", uploadedFile.getName());
        proxyCommand.setParameters(parameters);
        proxyCommand.setOperationInput(blob);
        
        Object res = nuxeoController.executeNuxeoCommand(proxyCommand);
        
        FileBlob file = (FileBlob) res;
        
        String s = new String(Files.readAllBytes(Paths.get(file.getFile().getAbsolutePath())));
        return s;

    }
    
    

    
    /* Proxy calls */
    

    @RequestMapping(value = "/nuxeo/json/cmis", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public String cmis( HttpServletRequest request, HttpServletResponse response,
            Principal principal) throws Exception {
        NuxeoController nuxeoController = new NuxeoController(portletContext);
        nuxeoController.setServletRequest(request);
        nuxeoController.setAuthType(NuxeoCommandContext.AUTH_TYPE_SUPERUSER);
        nuxeoController.setCacheType(CacheInfo.CACHE_SCOPE_NONE);
        
        Object res = nuxeoController.executeNuxeoCommand(new GetProxyCommand("/nuxeo/json/cmis",null));
        
        FileBlob file = (FileBlob) res;
        
        String s = new String(Files.readAllBytes(Paths.get(file.getFile().getAbsolutePath())));
        return s;
        }

    
    
    
    
    @RequestMapping(value = "/nuxeo/site/automation/NuxeoDrive.GetChildren", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public String getChildren( HttpServletRequest request, HttpServletResponse response, @RequestBody String name,
            Principal principal) throws Exception {
        NuxeoController nuxeoController = new NuxeoController(portletContext);
        nuxeoController.setServletRequest(request);
        nuxeoController.setAuthType(NuxeoCommandContext.AUTH_TYPE_SUPERUSER);
        nuxeoController.setCacheType(CacheInfo.CACHE_SCOPE_NONE);
        
        // TODO : parseId from name ou better push name to request body
        //Object res = nuxeoController.executeNuxeoCommand(new GetChildrenCommand("org.nuxeo.drive.service.impl.DefaultTopLevelFolderItemFactory#"));
        Object res = nuxeoController.executeNuxeoCommand(new GetProxyCommand("NuxeoDrive.GetChildren",name));
        
        FileBlob file = (FileBlob) res;
        
        String s = new String(Files.readAllBytes(Paths.get(file.getFile().getAbsolutePath())));
        return s;
        }

    @RequestMapping(value = "/nuxeo/site/automation/Document.fetch", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public String fetch( HttpServletRequest request, HttpServletResponse response, @RequestBody String name,
            Principal principal) throws Exception {
        NuxeoController nuxeoController = new NuxeoController(portletContext);
        nuxeoController.setServletRequest(request);
        nuxeoController.setAuthType(NuxeoCommandContext.AUTH_TYPE_SUPERUSER);
        nuxeoController.setCacheType(CacheInfo.CACHE_SCOPE_NONE);
        
        // TODO : parseId from name ou better push name to request body
        //Object res = nuxeoController.executeNuxeoCommand(new GetChildrenCommand("org.nuxeo.drive.service.impl.DefaultTopLevelFolderItemFactory#"));
        Object res = nuxeoController.executeNuxeoCommand(new GetProxyCommand("Document.fetch",name));
        
        FileBlob file = (FileBlob) res;
        
        String s = new String(Files.readAllBytes(Paths.get(file.getFile().getAbsolutePath())));
        return s;
        }

    @RequestMapping(value = "/nuxeo/site/automation/NuxeoDrive.CreateFolder", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public String createFolder( HttpServletRequest request, HttpServletResponse response, @RequestBody String name,
            Principal principal) throws Exception {
        NuxeoController nuxeoController = new NuxeoController(portletContext);
        nuxeoController.setServletRequest(request);
        nuxeoController.setAuthType(NuxeoCommandContext.AUTH_TYPE_SUPERUSER);
        nuxeoController.setCacheType(CacheInfo.CACHE_SCOPE_NONE);
        
        // TODO : parseId from name ou better push name to request body
        //Object res = nuxeoController.executeNuxeoCommand(new GetChildrenCommand("org.nuxeo.drive.service.impl.DefaultTopLevelFolderItemFactory#"));
        Object res = nuxeoController.executeNuxeoCommand(new GetProxyCommand("NuxeoDrive.CreateFolder",name));
        
        FileBlob file = (FileBlob) res;
        
        String s = new String(Files.readAllBytes(Paths.get(file.getFile().getAbsolutePath())));
        return s;
        }
    
    @RequestMapping(value = "/nuxeo/site/automation/NuxeoDrive.Delete", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public String delete( HttpServletRequest request, HttpServletResponse response, @RequestBody String name,
            Principal principal) throws Exception {
        NuxeoController nuxeoController = new NuxeoController(portletContext);
        nuxeoController.setServletRequest(request);
        nuxeoController.setAuthType(NuxeoCommandContext.AUTH_TYPE_SUPERUSER);
        nuxeoController.setCacheType(CacheInfo.CACHE_SCOPE_NONE);
        
        Object res = nuxeoController.executeNuxeoCommand(new GetProxyCommand("NuxeoDrive.Delete",name));
        
        FileBlob file = (FileBlob) res;
        
        String s = new String(Files.readAllBytes(Paths.get(file.getFile().getAbsolutePath())));
        return s;
        }
    
    @RequestMapping(value = "/nuxeo/site/automation/NuxeoDrive.Rename", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public String rename( HttpServletRequest request, HttpServletResponse response, @RequestBody String name,
            Principal principal) throws Exception {
        NuxeoController nuxeoController = new NuxeoController(portletContext);
        nuxeoController.setServletRequest(request);
        nuxeoController.setAuthType(NuxeoCommandContext.AUTH_TYPE_SUPERUSER);
        nuxeoController.setCacheType(CacheInfo.CACHE_SCOPE_NONE);
        
        Object res = nuxeoController.executeNuxeoCommand(new GetProxyCommand("NuxeoDrive.Rename",name));
        
        FileBlob file = (FileBlob) res;
        
        String s = new String(Files.readAllBytes(Paths.get(file.getFile().getAbsolutePath())));
        return s;
        }    
    
    
    
    @RequestMapping(value = "/nuxeo/site/automation/NuxeoDrive.Move", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public String move( HttpServletRequest request, HttpServletResponse response, @RequestBody String name,
            Principal principal) throws Exception {
        NuxeoController nuxeoController = new NuxeoController(portletContext);
        nuxeoController.setServletRequest(request);
        nuxeoController.setAuthType(NuxeoCommandContext.AUTH_TYPE_SUPERUSER);
        nuxeoController.setCacheType(CacheInfo.CACHE_SCOPE_NONE);
        
        // TODO : parseId from name ou better push name to request body
        //Object res = nuxeoController.executeNuxeoCommand(new GetChildrenCommand("org.nuxeo.drive.service.impl.DefaultTopLevelFolderItemFactory#"));
        Object res = nuxeoController.executeNuxeoCommand(new GetProxyCommand("NuxeoDrive.Move",name));
        
        FileBlob file = (FileBlob) res;
        
        String s = new String(Files.readAllBytes(Paths.get(file.getFile().getAbsolutePath())));
        return s;
        }        
    
    
    @RequestMapping(value = "/nuxeo/site/automation/NuxeoDrive.GetFileSystemItem", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public String getFileSystemItem( HttpServletRequest request, HttpServletResponse response,
            Principal principal, @RequestBody String fileSystemBody) throws Exception {
        NuxeoController nuxeoController = new NuxeoController(portletContext);
        nuxeoController.setServletRequest(request);
        nuxeoController.setAuthType(NuxeoCommandContext.AUTH_TYPE_SUPERUSER);
        nuxeoController.setCacheType(CacheInfo.CACHE_SCOPE_NONE);
        
        //Object res = nuxeoController.executeNuxeoCommand(new GetFileSystemItem("org.nuxeo.drive.service.impl.DefaultTopLevelFolderItemFactory#"));
        
        Object res = nuxeoController.executeNuxeoCommand(new GetProxyCommand("NuxeoDrive.GetFileSystemItem",fileSystemBody));
        
        FileBlob file = (FileBlob) res;
        
        String s = new String(Files.readAllBytes(Paths.get(file.getFile().getAbsolutePath())));
        return s;
        }
    
    @RequestMapping(value = "/nuxeo/site/automation/NuxeoDrive.GetChangeSummary", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public String getChangeSummary( HttpServletRequest request, HttpServletResponse response,
            Principal principal, @RequestBody String fileSystemBody) throws Exception {
        NuxeoController nuxeoController = new NuxeoController(portletContext);
        nuxeoController.setServletRequest(request);
        nuxeoController.setAuthType(NuxeoCommandContext.AUTH_TYPE_SUPERUSER);
        nuxeoController.setCacheType(CacheInfo.CACHE_SCOPE_NONE);
        

        
        Object res = nuxeoController.executeNuxeoCommand(new GetProxyCommand("NuxeoDrive.GetChangeSummary",fileSystemBody));
        
        FileBlob file = (FileBlob) res;
        
        String s = new String(Files.readAllBytes(Paths.get(file.getFile().getAbsolutePath())));
        return s;
        }

}
