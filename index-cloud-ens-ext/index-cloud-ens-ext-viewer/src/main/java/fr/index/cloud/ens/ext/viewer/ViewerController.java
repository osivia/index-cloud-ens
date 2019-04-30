package fr.index.cloud.ens.ext.viewer;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller for retrieving the model for and displaying main page.
 * 
 * https://cloud-ens.index-education.local/cloud-viewer/s/HBRHjCVx9Y6-IUFFQWamnVD
 * 
 * @author JS Steux
 */
@Controller
public class ViewerController {

    private static String PORTAL_INTERNAL_URL = "http://httpd:81";

    private static String VIEWER_WS_URI = "/index-cloud-portal-ens-ws/Viewer.getFileInfos?shareId=";

    public ViewerController() {
        super();
    }

    @RequestMapping(value = "/s/{id}")
    public ModelAndView getViewer(Map<String, Object> model, @PathVariable(value = "id") String shareId) throws Exception {

        RestTemplate restTemplate = new RestTemplate();
         
        Map resource = restTemplate.getForObject(PORTAL_INTERNAL_URL + VIEWER_WS_URI + shareId, Map.class);
         
        String format = (String) resource.get("format");
        String downloadURI = (String) resource.get("downloadURI");
        
        if ("native".equals(format)) {
            String fileType = (String) resource.get("fileType");
            String fileName = (String) resource.get("fileName");

            String docExtension = "";
            int index = fileName.lastIndexOf('.');
            if (index != -1) {
                fileName.substring(index + 1);
            }

            //TODO https mode : comment gérer les certificats
            // En attendant, on utilise une url interne ...
            model.put("downloadURL", PORTAL_INTERNAL_URL + downloadURI);
            model.put("shareId", shareId);   
            model.put("fileType", fileType);
            model.put("fileName", fileName);
            model.put("docExtension", docExtension);

            return new ModelAndView("onlyoffice", model);
        } else {
            model.put("downloadURL", downloadURI);         
            return new ModelAndView("pdf", model);
        }

    }
}
