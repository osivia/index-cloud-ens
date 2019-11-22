package fr.index.cloud.ens.filebrowser.commons.portlet.service;

import fr.index.cloud.ens.directory.model.preferences.CustomizedUserPreferences;
import fr.index.cloud.ens.directory.service.preferences.CustomizedUserPreferencesService;
import fr.index.cloud.ens.filebrowser.commons.portlet.model.AbstractFileBrowserForm;
import fr.index.cloud.ens.filebrowser.commons.portlet.model.AbstractFileBrowserItem;
import fr.index.cloud.ens.filebrowser.commons.portlet.model.AbstractFileBrowserSortField;
import fr.toutatice.portail.cms.nuxeo.api.domain.DocumentDTO;
import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.workspace.filebrowser.portlet.model.FileBrowserSortField;
import org.osivia.services.workspace.filebrowser.portlet.service.FileBrowserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import javax.portlet.PortletException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * File browser portlet service implementation abstract super-class.
 *
 * @author Cédric Krommenhoek
 * @see FileBrowserServiceImpl
 * @see AbstractFileBrowserService
 */
public abstract class AbstractFileBrowserServiceImpl extends FileBrowserServiceImpl implements AbstractFileBrowserService {

    /**
     * Application context.
     */
    @Autowired
    private ApplicationContext applicationContext;

    /**
     * User preferences service.
     */
    @Autowired
    private CustomizedUserPreferencesService userPreferencesService;


    /**
     * Constructor.
     */
    public AbstractFileBrowserServiceImpl() {
        super();
    }


    @Override
    public AbstractFileBrowserForm getForm(PortalControllerContext portalControllerContext) throws PortletException {
        AbstractFileBrowserForm form = this.applicationContext.getBean(AbstractFileBrowserForm.class);

        if (!form.isInitialized()) {
            this.initializeForm(portalControllerContext, form);

            // User preferences
            CustomizedUserPreferences userPreferences;
            try {
                userPreferences = this.userPreferencesService.getUserPreferences(portalControllerContext);
            } catch (PortalException e) {
                throw new PortletException(e);
            }

            // Customized column
            AbstractFileBrowserSortField customizedColumn = null;
            if (StringUtils.isNotEmpty(userPreferences.getCustomizedColumn())) {
                // Enum values
                List<FileBrowserSortField> values = this.getAllSortFields();

                Iterator<FileBrowserSortField> iterator = values.iterator();
                while ((customizedColumn == null) && iterator.hasNext()) {
                    AbstractFileBrowserSortField value = (AbstractFileBrowserSortField) iterator.next();

                    if (value.isCustomizable() && StringUtils.equals(userPreferences.getCustomizedColumn(), value.getId())) {
                        customizedColumn = value;
                    }
                }
            }
            if (customizedColumn == null) {
                // Default value
                customizedColumn = this.getDefaultCustomizedColumn();
            }
            form.setCustomizedColumn(customizedColumn);

            // Initialized indicator
            form.setInitialized(true);
        }

        return form;
    }


    @Override
    protected AbstractFileBrowserItem createItem(PortalControllerContext portalControllerContext, Document nuxeoDocument) {
        AbstractFileBrowserItem item = (AbstractFileBrowserItem) super.createItem(portalControllerContext, nuxeoDocument);

        // Document DTO
        DocumentDTO documentDto = item.getDocument();

        // Document types
        List<String> documentTypes = this.getPropertyListValues(documentDto, "idxcl:documentTypes");
        item.setDocumentTypes(documentTypes);

        // Levels
        List<String> levels = this.getPropertyListValues(documentDto, "idxcl:levels");
        item.setLevels(levels);

        // Subjects
        List<String> subjects = this.getPropertyListValues(documentDto, "idxcl:subjects");
        item.setSubjects(subjects);

        return item;
    }


    /**
     * Get property list values.
     *
     * @param documentDto document DTO
     * @param name        property name
     * @return values
     */
    private List<String> getPropertyListValues(DocumentDTO documentDto, String name) {
        List<String> values;

        // Property
        Object property = documentDto.getProperties().get(name);

        if (property instanceof List) {
            List<?> list = (List<?>) property;
            values = new ArrayList<>(list.size());

            for (Object object : list) {
                if (object instanceof String) {
                    String value = (String) object;
                    values.add(value);
                }
            }
        } else {
            values = null;
        }

        return values;
    }


    @Override
    public List<AbstractFileBrowserSortField> getCustomizedColumns(PortalControllerContext portalControllerContext) {
        // Customized columns
        List<AbstractFileBrowserSortField> customizedColumns = new ArrayList<>();

        for (FileBrowserSortField value : this.getAllSortFields()) {
            AbstractFileBrowserSortField sortField = (AbstractFileBrowserSortField) value;

            if (sortField.isCustomizable()) {
                customizedColumns.add(sortField);
            }
        }

        return customizedColumns;
    }


    /**
     * Get default customized column.
     *
     * @return column
     */
    protected abstract AbstractFileBrowserSortField getDefaultCustomizedColumn();


    @Override
    public void changeCustomizedColumn(PortalControllerContext portalControllerContext, AbstractFileBrowserForm form, List<FileBrowserSortField> sortFields, String id) throws PortletException {
        List<FileBrowserSortField> values = this.getAllSortFields();

        // Customized column
        AbstractFileBrowserSortField customizedColumn = null;
        Iterator<FileBrowserSortField> iterator = values.iterator();
        while ((customizedColumn == null) && iterator.hasNext()) {
            AbstractFileBrowserSortField value = (AbstractFileBrowserSortField) iterator.next();

            if (StringUtils.equals(id, value.getId())) {
                customizedColumn = value;
            }
        }

        if (customizedColumn == null) {
            // Default result
            customizedColumn = this.getDefaultCustomizedColumn();
        }

        // Update model
        form.setCustomizedColumn(customizedColumn);
        sortFields.clear();
        sortFields.addAll(this.getSortFields(portalControllerContext));

        // User preferences
        CustomizedUserPreferences userPreferences;
        try {
            userPreferences = this.userPreferencesService.getUserPreferences(portalControllerContext);
        } catch (PortalException e) {
            throw new PortletException(e);
        }

        // Update user preferences
        userPreferences.setCustomizedColumn(customizedColumn.getId());
        userPreferences.setUpdated(true);
    }

}
