package fr.index.cloud.ens.filebrowser.commons.portlet.controller;

import fr.index.cloud.ens.filebrowser.commons.portlet.model.AbstractFileBrowserForm;
import fr.index.cloud.ens.filebrowser.commons.portlet.model.AbstractFileBrowserSortField;
import fr.index.cloud.ens.filebrowser.commons.portlet.service.AbstractFileBrowserService;
import fr.index.cloud.ens.filebrowser.portlet.model.CustomizedFileBrowserForm;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.workspace.filebrowser.portlet.controller.FileBrowserController;
import org.osivia.services.workspace.filebrowser.portlet.model.FileBrowserSortField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.bind.annotation.ActionMapping;

import javax.portlet.*;
import java.util.List;

/**
 * File browser controller abstract super-class.
 *
 * @author Cédric Krommenhoek
 * @see FileBrowserController
 */
public abstract class AbstractFileBrowserController extends FileBrowserController {

    /**
     * Portlet context.
     */
    @Autowired
    private PortletContext portletContext;

    /**
     * Portlet service.
     */
    @Autowired
    private AbstractFileBrowserService service;


    /**
     * Constructor.
     */
    public AbstractFileBrowserController() {
        super();
    }


    /**
     * Change customized column action mapping.
     *
     * @param request    action request
     * @param response   action response
     * @param id         customized column identifier request parameter
     * @param form       form model attribute
     * @param sortFields sort fields model attribute
     */
    @ActionMapping("change-column")
    public void changeCustomizedColumn(ActionRequest request, ActionResponse response, @RequestParam("column") String id, @ModelAttribute("form") AbstractFileBrowserForm form, @ModelAttribute("sortFields") List<FileBrowserSortField> sortFields) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        this.service.changeCustomizedColumn(portalControllerContext, form, sortFields, id);
    }


    /**
     * Get customized columns model attribute.
     *
     * @param request  portlet request
     * @param response portlet response
     * @return customized columns
     */
    @ModelAttribute("customizedColumns")
    public List<AbstractFileBrowserSortField> getCustomizedColumns(PortletRequest request, PortletResponse response) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        return this.service.getCustomizedColumns(portalControllerContext);
    }

}
