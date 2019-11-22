package fr.index.cloud.ens.filebrowser.portlet.service;

import fr.index.cloud.ens.filebrowser.commons.portlet.model.AbstractFileBrowserSortField;
import fr.index.cloud.ens.filebrowser.commons.portlet.service.AbstractFileBrowserServiceImpl;
import fr.index.cloud.ens.filebrowser.portlet.model.CustomizedFileBrowserForm;
import fr.index.cloud.ens.filebrowser.portlet.model.CustomizedFileBrowserItem;
import fr.index.cloud.ens.filebrowser.portlet.model.CustomizedFileBrowserSortEnum;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.PropertyList;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.workspace.filebrowser.portlet.model.FileBrowserSortField;
import org.osivia.services.workspace.filebrowser.portlet.model.FileBrowserWindowProperties;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import javax.portlet.PortletException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * File browser customized portlet service implementation.
 *
 * @author Cédric Krommenhoek
 * @see AbstractFileBrowserServiceImpl
 * @see CustomizedFileBrowserService
 */
@Service
@Primary
public class CustomizedFileBrowserServiceImpl extends AbstractFileBrowserServiceImpl implements CustomizedFileBrowserService {


    /**
     * Constructor.
     */
    public CustomizedFileBrowserServiceImpl() {
        super();
    }


    @Override
    public CustomizedFileBrowserForm getForm(PortalControllerContext portalControllerContext) throws PortletException {
        return (CustomizedFileBrowserForm) super.getForm(portalControllerContext);
    }


    @Override
    protected CustomizedFileBrowserItem createItem(PortalControllerContext portalControllerContext, Document nuxeoDocument) {
        CustomizedFileBrowserItem item = (CustomizedFileBrowserItem) super.createItem(portalControllerContext, nuxeoDocument);

        // PRONOTE indicator
        PropertyList targets = nuxeoDocument.getProperties().getList("rshr:targets");
        boolean pronote = (targets != null) && !targets.isEmpty();
        item.setPronote(pronote);

        // Mutualized indicator
        boolean mutualized = BooleanUtils.isTrue(nuxeoDocument.getProperties().getBoolean("mtz:enable"));
        item.setMutualized(mutualized);
        super.createItem(portalControllerContext, nuxeoDocument);

        return item;
    }


    @Override
    public List<FileBrowserSortField> getSortFields(PortalControllerContext portalControllerContext) throws PortletException {
        CustomizedFileBrowserSortEnum[] values = CustomizedFileBrowserSortEnum.values();

        // Form
        CustomizedFileBrowserForm form = this.getForm(portalControllerContext);

        // Default sort field
        FileBrowserSortField defaultSortField = this.getDefaultSortField(portalControllerContext);

        // Filtered sort fields
        List<FileBrowserSortField> filteredFields = new ArrayList<>();
        for (CustomizedFileBrowserSortEnum value : values) {
            if ((form.isListMode() || !value.isListMode()) && (!value.isCustomizable() || value.equals(form.getCustomizedColumn())) && !(StringUtils.equals(CustomizedFileBrowserSortEnum.RELEVANCE.getId(), value.getId()) && ((defaultSortField == null) || !StringUtils.equals(CustomizedFileBrowserSortEnum.RELEVANCE.getId(), defaultSortField.getId())))) {
                filteredFields.add(value);
            }
        }

        return filteredFields;
    }


    @Override
    protected List<FileBrowserSortField> getAllSortFields() {
        // Enum values
        CustomizedFileBrowserSortEnum[] values = CustomizedFileBrowserSortEnum.values();

        return new ArrayList<>(Arrays.asList(values));
    }


    @Override
    protected FileBrowserSortField getDefaultSortField(PortalControllerContext portalControllerContext) {
        // Window properties
        FileBrowserWindowProperties windowProperties = this.getWindowProperties(portalControllerContext);

        // Default sort field
        FileBrowserSortField field;
        if (windowProperties.getDefaultSortField() != null) {
            field = this.getSortField(portalControllerContext, windowProperties.getDefaultSortField(), false);
        } else if (BooleanUtils.isTrue(windowProperties.getListMode())) {
            field = CustomizedFileBrowserSortEnum.RELEVANCE;
        } else {
            field = CustomizedFileBrowserSortEnum.TITLE;
        }

        return field;
    }


    @Override
    protected AbstractFileBrowserSortField getDefaultCustomizedColumn() {
        return CustomizedFileBrowserSortEnum.DOCUMENT_TYPE;
    }

}
