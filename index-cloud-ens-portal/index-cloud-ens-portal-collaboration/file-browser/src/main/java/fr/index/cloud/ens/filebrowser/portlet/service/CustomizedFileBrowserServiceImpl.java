package fr.index.cloud.ens.filebrowser.portlet.service;

import fr.index.cloud.ens.filebrowser.commons.portlet.model.AbstractFileBrowserSortField;
import fr.index.cloud.ens.filebrowser.commons.portlet.service.AbstractFileBrowserServiceImpl;
import fr.index.cloud.ens.filebrowser.portlet.model.CustomizedFileBrowserForm;
import fr.index.cloud.ens.filebrowser.portlet.model.CustomizedFileBrowserItem;
import fr.index.cloud.ens.filebrowser.portlet.model.CustomizedFileBrowserSortEnum;
import fr.toutatice.portail.cms.nuxeo.api.PageSelectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.PropertyList;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.html.DOM4JUtils;
import org.osivia.services.workspace.filebrowser.portlet.model.FileBrowserSortField;
import org.osivia.services.workspace.filebrowser.portlet.model.FileBrowserWindowProperties;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import java.util.*;

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
     * Selector identifiers.
     */
    public static final List<String> SELECTOR_IDENTIFIERS = Arrays.asList(KEYWORDS_SELECTOR_ID, DOCUMENT_TYPES_SELECTOR_ID, LEVELS_SELECTOR_ID, LEVELS_SELECTOR_ID);


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
    protected boolean isListMode(PortalControllerContext portalControllerContext) {
        // Portlet request
        PortletRequest request = portalControllerContext.getRequest();
        // Window properties
        FileBrowserWindowProperties windowProperties = this.getWindowProperties(portalControllerContext);
        // Selectors
        Map<String, List<String>> selectors = PageSelectors.decodeProperties(request.getParameter(SELECTORS_PARAMETER));

        boolean listMode = BooleanUtils.isTrue(windowProperties.getListMode());
        if (!listMode && MapUtils.isNotEmpty(selectors)) {
            Iterator<String> iterator = SELECTOR_IDENTIFIERS.iterator();
            while (!listMode && iterator.hasNext()) {
                String id = iterator.next();
                listMode = CollectionUtils.isNotEmpty(selectors.get(id));
            }
        }
        
        return listMode;
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
        } else if (this.isListMode(portalControllerContext)) {
            field = CustomizedFileBrowserSortEnum.RELEVANCE;
        } else {
            field = CustomizedFileBrowserSortEnum.TITLE;
        }

        return field;
    }


    @Override
    protected void addToolbarItem(Element toolbar, String url, String target, String title, String icon) {
        // Base HTML classes
        String baseHtmlClasses = "btn btn-link btn-link-hover-green text-green-dark btn-sm mr-1";

        // Item
        Element item;
        if (StringUtils.isEmpty(url)) {
            item = DOM4JUtils.generateLinkElement("#", null, null, baseHtmlClasses + " disabled", null, icon);
        } else {
            // Data attributes
            Map<String, String> data = new HashMap<>();

            if ("#osivia-modal".equals(target)) {
                data.put("target", "#osivia-modal");
                data.put("load-url", url);
                data.put("title", title);

                url = "javascript:";
                target = null;
            } else if ("modal".equals(target)) {
                data.put("toggle", "modal");

                target = null;
            }

            item = DOM4JUtils.generateLinkElement(url, target, null, baseHtmlClasses + " no-ajax-link", null, icon);

            // Title
            DOM4JUtils.addAttribute(item, "title", title);

            // Data attributes
            for (Map.Entry<String, String> entry : data.entrySet()) {
                DOM4JUtils.addDataAttribute(item, entry.getKey(), entry.getValue());
            }
        }

        // Text
        Element text = DOM4JUtils.generateElement("span", "d-none d-lg-inline", title);
        item.add(text);

        toolbar.add(item);
    }


    @Override
    protected AbstractFileBrowserSortField getDefaultCustomizedColumn() {
        return CustomizedFileBrowserSortEnum.DOCUMENT_TYPE;
    }

}
