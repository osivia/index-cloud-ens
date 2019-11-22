package fr.index.cloud.ens.filebrowser.mutualized.portlet.service;

import fr.index.cloud.ens.filebrowser.commons.portlet.model.AbstractFileBrowserSortField;
import fr.index.cloud.ens.filebrowser.commons.portlet.service.AbstractFileBrowserServiceImpl;
import fr.index.cloud.ens.filebrowser.mutualized.portlet.model.MutualizedFileBrowserForm;
import fr.index.cloud.ens.filebrowser.mutualized.portlet.model.MutualizedFileBrowserItem;
import fr.index.cloud.ens.filebrowser.mutualized.portlet.model.MutualizedFileBrowserSortEnum;
import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.workspace.filebrowser.portlet.model.FileBrowserSortField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import javax.portlet.PortletException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Mutualized file browser portlet service implementation.
 *
 * @author Cédric Krommenhoek
 * @see AbstractFileBrowserServiceImpl
 * @see MutualizedFileBrowserService
 */
@Service
@Primary
public class MutualizedFileBrowserServiceImpl extends AbstractFileBrowserServiceImpl implements MutualizedFileBrowserService {

    /**
     * Application context.
     */
    @Autowired
    private ApplicationContext applicationContext;


    /**
     * Constructor.
     */
    public MutualizedFileBrowserServiceImpl() {
        super();
    }


    @Override
    public MutualizedFileBrowserForm getForm(PortalControllerContext portalControllerContext) throws PortletException {
        return (MutualizedFileBrowserForm) super.getForm(portalControllerContext);
    }


    @Override
    protected MutualizedFileBrowserItem createItem(PortalControllerContext portalControllerContext, Document nuxeoDocument) {
        MutualizedFileBrowserItem item = (MutualizedFileBrowserItem) super.createItem(portalControllerContext, nuxeoDocument);

        // Mutualized title
        String title = nuxeoDocument.getString("mtz:title");
        item.setTitle(title);

        return item;
    }


    @Override
    public List<FileBrowserSortField> getSortFields(PortalControllerContext portalControllerContext) throws PortletException {
        MutualizedFileBrowserSortEnum[] values = MutualizedFileBrowserSortEnum.values();

        // Form
        MutualizedFileBrowserForm form = this.getForm(portalControllerContext);

        // Filtered sort fields
        List<FileBrowserSortField> filteredFields = new ArrayList<>();
        for (MutualizedFileBrowserSortEnum value : values) {
            if (!value.isCustomizable() || value.equals(form.getCustomizedColumn())) {
                filteredFields.add(value);
            }
        }

        return filteredFields;
    }


    @Override
    protected List<FileBrowserSortField> getAllSortFields() {
        // Enum values
        MutualizedFileBrowserSortEnum[] values = MutualizedFileBrowserSortEnum.values();

        return new ArrayList<>(Arrays.asList(values));
    }


    @Override
    protected FileBrowserSortField getDefaultSortField(PortalControllerContext portalControllerContext) {
        return MutualizedFileBrowserSortEnum.RELEVANCE;
    }


    @Override
    protected AbstractFileBrowserSortField getDefaultCustomizedColumn() {
        return MutualizedFileBrowserSortEnum.DOCUMENT_TYPE;
    }

}
