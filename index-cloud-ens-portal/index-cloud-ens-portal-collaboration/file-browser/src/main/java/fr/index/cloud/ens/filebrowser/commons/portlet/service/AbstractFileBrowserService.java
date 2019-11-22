package fr.index.cloud.ens.filebrowser.commons.portlet.service;

import fr.index.cloud.ens.filebrowser.commons.portlet.model.AbstractFileBrowserForm;
import fr.index.cloud.ens.filebrowser.commons.portlet.model.AbstractFileBrowserSortField;
import fr.index.cloud.ens.filebrowser.portlet.model.CustomizedFileBrowserForm;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.workspace.filebrowser.portlet.model.FileBrowserForm;
import org.osivia.services.workspace.filebrowser.portlet.model.FileBrowserSortField;
import org.osivia.services.workspace.filebrowser.portlet.service.FileBrowserService;

import javax.portlet.PortletException;
import java.util.List;

/**
 * File browser portlet service interface.
 *
 * @author Cédric Krommenhoek
 * @see FileBrowserService
 */
public interface AbstractFileBrowserService extends FileBrowserService {

    @Override
    AbstractFileBrowserForm getForm(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Get customized columns.
     *
     * @param portalControllerContext portal controller context
     * @return customized columns
     */
    List<AbstractFileBrowserSortField> getCustomizedColumns(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Change customized column.
     *
     * @param portalControllerContext portal controller context
     * @param form                    form
     * @param sortFields              sort fields
     * @param id                      customized column identifier
     */
    void changeCustomizedColumn(PortalControllerContext portalControllerContext, AbstractFileBrowserForm form, List<FileBrowserSortField> sortFields, String id) throws PortletException;

}
