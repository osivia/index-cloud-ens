package fr.index.cloud.ens.filebrowser.portlet.service;

import fr.index.cloud.ens.filebrowser.portlet.model.CustomizedFileBrowserForm;
import fr.index.cloud.ens.filebrowser.portlet.model.CustomizedFileBrowserSortField;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.workspace.filebrowser.portlet.model.FileBrowserSortField;
import org.osivia.services.workspace.filebrowser.portlet.service.FileBrowserService;

import javax.portlet.PortletException;
import java.util.List;

/**
 * File browser customized portlet service interface.
 *
 * @author Cédric Krommenhoek
 * @see FileBrowserService
 */
public interface CustomizedFileBrowserService extends FileBrowserService {

    /**
     * Get customized columns.
     *
     * @param portalControllerContext portal controller context
     * @return customized columns
     */
    List<CustomizedFileBrowserSortField> getCustomizedColumns(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Change customized column.
     *
     * @param portalControllerContext portal controller context
     * @param form                    form
     * @param sortFields              sort fields
     * @param id                      customized column identifier
     */
    void changeCustomizedColumn(PortalControllerContext portalControllerContext, CustomizedFileBrowserForm form, List<FileBrowserSortField> sortFields, String id) throws PortletException;

}
