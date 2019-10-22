package fr.index.cloud.ens.filebrowser.portlet.service;

import fr.index.cloud.ens.directory.model.preferences.CustomizedUserPreferences;
import fr.index.cloud.ens.directory.service.preferences.CustomizedUserPreferencesService;
import fr.index.cloud.ens.filebrowser.portlet.model.CustomizedFileBrowserForm;
import fr.index.cloud.ens.filebrowser.portlet.model.CustomizedFileBrowserItem;
import fr.index.cloud.ens.filebrowser.portlet.model.CustomizedFileBrowserSortEnum;
import fr.index.cloud.ens.filebrowser.portlet.model.CustomizedFileBrowserSortField;
import fr.toutatice.portail.cms.nuxeo.api.domain.DocumentDTO;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.workspace.filebrowser.portlet.model.FileBrowserItem;
import org.osivia.services.workspace.filebrowser.portlet.model.FileBrowserSortField;
import org.osivia.services.workspace.filebrowser.portlet.repository.FileBrowserRepository;
import org.osivia.services.workspace.filebrowser.portlet.service.FileBrowserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
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
 * @see FileBrowserServiceImpl
 * @see CustomizedFileBrowserService
 */
@Service
@Primary
public class CustomizedFileBrowserServiceImpl extends FileBrowserServiceImpl implements CustomizedFileBrowserService {

    /**
     * Log.
     */
    private final Log log;


    /**
     * Application context.
     */
    @Autowired
    private ApplicationContext applicationContext;

    /**
     * Portlet repository.
     */
    @Autowired
    private FileBrowserRepository repository;

    /**
     * User preferences service.
     */
    @Autowired
    private CustomizedUserPreferencesService userPreferencesService;


    /**
     * Constructor.
     */
    public CustomizedFileBrowserServiceImpl() {
        super();

        // Log
        this.log = LogFactory.getLog(this.getClass());
    }


    @Override
    public CustomizedFileBrowserForm getForm(PortalControllerContext portalControllerContext) throws PortletException {
        CustomizedFileBrowserForm form = this.applicationContext.getBean(CustomizedFileBrowserForm.class);

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
            CustomizedFileBrowserSortField customizedColumn = null;
            if (StringUtils.isNotEmpty(userPreferences.getCustomizedColumn())) {
                // Enum values
                CustomizedFileBrowserSortEnum[] values = CustomizedFileBrowserSortEnum.values();

                int i = 0;
                while ((customizedColumn == null) && (i < values.length)) {
                    CustomizedFileBrowserSortField value = values[i];

                    if (value.isCustomizable() && StringUtils.equals(userPreferences.getCustomizedColumn(), value.getId())) {
                        customizedColumn = value;
                    }

                    i++;
                }
            }
            if (customizedColumn == null) {
                // Default value
                customizedColumn = CustomizedFileBrowserSortEnum.DOCUMENT_TYPE;
            }
            form.setCustomizedColumn(customizedColumn);

            // Initialized indicator
            form.setInitialized(true);
        }

        return form;
    }


    @Override
    protected FileBrowserItem createItem(PortalControllerContext portalControllerContext, Document nuxeoDocument) {
        FileBrowserItem item = super.createItem(portalControllerContext, nuxeoDocument);

        if (item instanceof CustomizedFileBrowserItem) {
            // Customized item
            CustomizedFileBrowserItem customizedItem = (CustomizedFileBrowserItem) item;

            // Document DTO
            DocumentDTO documentDto = customizedItem.getDocument();

            // Document types
            List<String> documentTypes = this.getPropertyListValues(documentDto, "idxcl:documentTypes");
            customizedItem.setDocumentTypes(documentTypes);

            // Levels
            List<String> levels = this.getPropertyListValues(documentDto, "idxcl:levels");
            customizedItem.setLevels(levels);

            // Subjects
            List<String> subjects = this.getPropertyListValues(documentDto, "idxcl:subjects");
            customizedItem.setSubjects(subjects);
        }

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
    protected List<FileBrowserSortField> getAllSortFields() {
        // Enum values
        CustomizedFileBrowserSortEnum[] values = CustomizedFileBrowserSortEnum.values();

        return new ArrayList<>(Arrays.asList(values));
    }


    @Override
    protected FileBrowserSortField getDefaultSortField(boolean listMode) {
        FileBrowserSortField field;
        if (listMode) {
            field = CustomizedFileBrowserSortEnum.RELEVANCE;
        } else {
            field = CustomizedFileBrowserSortEnum.TITLE;
        }
        return field;
    }


    @Override
    public List<FileBrowserSortField> getSortFields(PortalControllerContext portalControllerContext) throws PortletException {
        CustomizedFileBrowserSortEnum[] values = CustomizedFileBrowserSortEnum.values();

        // Form
        CustomizedFileBrowserForm form = this.getForm(portalControllerContext);

        // Filtered sort fields
        List<FileBrowserSortField> filteredFields = new ArrayList<>();
        for (CustomizedFileBrowserSortEnum value : values) {
            if ((form.isListMode() || !value.isListMode()) && (!value.isCustomizable() || value.equals(form.getCustomizedColumn()))) {
                filteredFields.add(value);
            }
        }

        return filteredFields;
    }


    @Override
    public List<CustomizedFileBrowserSortField> getCustomizedColumns(PortalControllerContext portalControllerContext) {
        // Customized columns
        List<CustomizedFileBrowserSortField> customizedColumns = new ArrayList<>();

        for (CustomizedFileBrowserSortEnum value : CustomizedFileBrowserSortEnum.values()) {
            if (value.isCustomizable()) {
                customizedColumns.add(value);
            }
        }

        return customizedColumns;
    }


    @Override
    public void changeCustomizedColumn(PortalControllerContext portalControllerContext, CustomizedFileBrowserForm form, List<FileBrowserSortField> sortFields, String id) throws PortletException {
        CustomizedFileBrowserSortEnum[] values = CustomizedFileBrowserSortEnum.values();

        // Customized column
        CustomizedFileBrowserSortField customizedColumn = null;
        int i = 0;
        while ((customizedColumn == null) && (i < values.length)) {
            CustomizedFileBrowserSortField value = values[i];

            if (StringUtils.equals(id, value.getId())) {
                customizedColumn = value;
            }

            i++;
        }

        if (customizedColumn == null) {
            // Default result
            customizedColumn = CustomizedFileBrowserSortEnum.DOCUMENT_TYPE;
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
