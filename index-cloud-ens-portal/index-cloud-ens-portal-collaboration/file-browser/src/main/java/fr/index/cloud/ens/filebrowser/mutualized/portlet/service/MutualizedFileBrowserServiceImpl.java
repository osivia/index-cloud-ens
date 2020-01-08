package fr.index.cloud.ens.filebrowser.mutualized.portlet.service;

import fr.index.cloud.ens.filebrowser.commons.portlet.model.AbstractFileBrowserSortField;
import fr.index.cloud.ens.filebrowser.commons.portlet.service.AbstractFileBrowserServiceImpl;
import fr.index.cloud.ens.filebrowser.mutualized.portlet.model.MutualizedFileBrowserForm;
import fr.index.cloud.ens.filebrowser.mutualized.portlet.model.MutualizedFileBrowserItem;
import fr.index.cloud.ens.filebrowser.mutualized.portlet.model.MutualizedFileBrowserSortEnum;
import fr.index.cloud.ens.filebrowser.mutualized.portlet.model.MutualizedFileBrowserWindowProperties;
import fr.index.cloud.ens.filebrowser.mutualized.portlet.repository.MutualizedFileBrowserRepository;
import fr.toutatice.portail.cms.nuxeo.api.domain.DocumentDTO;
import fr.toutatice.portail.cms.nuxeo.api.services.dao.DocumentDAO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.jboss.portal.common.invocation.Scope;
import org.jboss.portal.core.controller.ControllerContext;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.PaginableDocuments;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.windows.PortalWindow;
import org.osivia.portal.api.windows.WindowFactory;
import org.osivia.portal.core.context.ControllerContextAdapter;
import org.osivia.services.workspace.filebrowser.portlet.model.*;
import org.osivia.services.workspace.filebrowser.portlet.model.comparator.FileBrowserItemComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import javax.portlet.*;
import java.io.IOException;
import java.util.*;

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
     * View resolver.
     */
    @Autowired
    private InternalResourceViewResolver viewResolver;

    /**
     * Portlet repository.
     */
    @Autowired
    private MutualizedFileBrowserRepository repository;

    /**
     * Document DAO.
     */
    @Autowired
    private DocumentDAO documentDao;


    /**
     * Constructor.
     */
    public MutualizedFileBrowserServiceImpl() {
        super();
    }


    @Override
    public MutualizedFileBrowserWindowProperties getWindowProperties(PortalControllerContext portalControllerContext) {
        // Portlet request
        PortletRequest request = portalControllerContext.getRequest();
        // Window
        PortalWindow window = WindowFactory.getWindow(request);

        // Window properties
        MutualizedFileBrowserWindowProperties windowProperties = (MutualizedFileBrowserWindowProperties) super.getWindowProperties(portalControllerContext);

        // Page size
        int pageSize = NumberUtils.toInt(window.getProperty(PAGE_SIZE_WINDOW_PROPERTY), PAGE_SIZE_DEFAULT_VALUE);
        windowProperties.setPageSize(pageSize);

        return windowProperties;
    }


    @Override
    public void setWindowProperties(PortalControllerContext portalControllerContext, FileBrowserWindowProperties windowProperties) {
        // Portlet request
        PortletRequest request = portalControllerContext.getRequest();
        // Window
        PortalWindow window = WindowFactory.getWindow(request);

        // Window properties
        MutualizedFileBrowserWindowProperties mutualizedWindowProperties = (MutualizedFileBrowserWindowProperties) windowProperties;

        super.setWindowProperties(portalControllerContext, mutualizedWindowProperties);

        // Page size
        Integer pageSize = mutualizedWindowProperties.getPageSize();
        if (pageSize == null) {
            pageSize = PAGE_SIZE_DEFAULT_VALUE;
        }
        window.setProperty(PAGE_SIZE_WINDOW_PROPERTY, pageSize.toString());
    }


    @Override
    public MutualizedFileBrowserForm getForm(PortalControllerContext portalControllerContext) throws PortletException {
        return (MutualizedFileBrowserForm) super.getForm(portalControllerContext);
    }


    @Override
    protected void initializeForm(PortalControllerContext portalControllerContext, FileBrowserForm form) throws PortletException {
        // Window properties
        MutualizedFileBrowserWindowProperties windowProperties = this.getWindowProperties(portalControllerContext);

        // Mutualized file browser form
        MutualizedFileBrowserForm mutualizedForm = (MutualizedFileBrowserForm) form;

        super.initializeForm(portalControllerContext, mutualizedForm);

        this.initializePagination(portalControllerContext, windowProperties, mutualizedForm);
    }


    /**
     * Initialize pagination.
     *
     * @param portalControllerContext portal controller context
     * @param windowProperties        window properties
     * @param form                    form
     */
    private void initializePagination(PortalControllerContext portalControllerContext, MutualizedFileBrowserWindowProperties windowProperties, MutualizedFileBrowserForm form) throws PortletException {
        // Portlet request
        PortletRequest request = portalControllerContext.getRequest();

        // Previous page index
        int previousPageIndex = NumberUtils.toInt(request.getParameter("page"), 0);
        // Page size
        int pageSize = (previousPageIndex + 1) * windowProperties.getPageSize();

        // Paginable documents
        PaginableDocuments documents = this.repository.getPaginableDocuments(portalControllerContext, windowProperties, form.getPath(), pageSize, 0, form.getCriteria());

        // Items
        List<FileBrowserItem> items = this.createItems(portalControllerContext, documents);
        form.setItems(items);

        // Total
        int total = documents.getTotalSize();
        form.setTotal(total);

        // Next page index
        int nextPageIndex;
        if (documents.getTotalSize() > documents.getPageSize()) {
            nextPageIndex = previousPageIndex + 1;
        } else {
            nextPageIndex = 0;
        }
        form.setNextPageIndex(nextPageIndex);
    }


    /**
     * Create items.
     *
     * @param portalControllerContext portal controller context
     * @param documents               documents
     * @return items
     */
    private List<FileBrowserItem> createItems(PortalControllerContext portalControllerContext, PaginableDocuments documents) throws PortletException {
        List<FileBrowserItem> items;
        if (documents.isEmpty()) {
            items = null;
        } else {
            // User subscriptions
            Set<String> userSubscriptions = this.repository.getUserSubscriptions(portalControllerContext);

            items = new ArrayList<>(documents.getPageSize());

            for (int i = 0; i < documents.getPageSize(); i++) {
                // Document
                Document document = documents.get(i);

                FileBrowserItem item = this.createItem(portalControllerContext, document);

                // Subscription
                boolean subscription = userSubscriptions.contains(document.getId());
                item.setSubscription(subscription);

                // Native order
                item.setNativeOrder(i);

                // Parent document
                Document parent = this.repository.getParentDocument(portalControllerContext, document);
                if (parent != null) {
                    DocumentDTO parentDto = this.documentDao.toDTO(parent);
                    item.setParentDocument(parentDto);
                }

                items.add(item);
            }
        }
        return items;
    }


    @Override
    protected MutualizedFileBrowserItem createItem(PortalControllerContext portalControllerContext, Document nuxeoDocument) {
        MutualizedFileBrowserItem item = (MutualizedFileBrowserItem) super.createItem(portalControllerContext, nuxeoDocument);

        // Mutualized title
        String title = nuxeoDocument.getString("mtz:title");
        item.setTitle(title);

        // Publication date
        Date date = nuxeoDocument.getDate("dc:issued");
        if (date != null) {
            item.setLastModification(date);
        }

        return item;
    }


    @Override
    public void sortItems(PortalControllerContext portalControllerContext, FileBrowserForm form, FileBrowserSortField field, boolean alt) throws PortletException {
        // Window properties
        MutualizedFileBrowserWindowProperties windowProperties = this.getWindowProperties(portalControllerContext);

        // Mutualized form
        MutualizedFileBrowserForm mutualizedForm = (MutualizedFileBrowserForm) form;

        // Sort criteria
        FileBrowserSortCriteria criteria = this.applicationContext.getBean(FileBrowserSortCriteria.class);
        criteria.setField(field);
        criteria.setAlt(alt);
        mutualizedForm.setCriteria(criteria);

        this.initializePagination(portalControllerContext, windowProperties, mutualizedForm);
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


    @Override
    public void savePosition(PortalControllerContext portalControllerContext) throws PortletException {
        // Portlet request
        PortletRequest request = portalControllerContext.getRequest();

        // Prevent Ajax refresh
        request.setAttribute("osivia.ajax.preventRefresh", true);


        // TODO render parameters

    }


    @Override
    public void loadPage(PortalControllerContext portalControllerContext, int pageIndex) throws PortletException, IOException {
        // Portlet request
        PortletRequest request = portalControllerContext.getRequest();
        // Portlet response
        PortletResponse response = portalControllerContext.getResponse();
        // Portlet context
        PortletContext portletContext = portalControllerContext.getPortletCtx();

        // Window properties
        MutualizedFileBrowserWindowProperties windowProperties = this.getWindowProperties(portalControllerContext);
        // Form
        MutualizedFileBrowserForm form = this.getForm(portalControllerContext);

        // Paginable documents
        PaginableDocuments documents = this.repository.getPaginableDocuments(portalControllerContext, windowProperties, form.getPath(), windowProperties.getPageSize(), pageIndex, form.getCriteria());

        // Items
        List<FileBrowserItem> items = this.createItems(portalControllerContext, documents);
        request.setAttribute("items", items);

        // Next page index
        int nextPageIndex;
        if (documents.getTotalSize() > ((pageIndex + 1) * documents.getPageSize())) {
            nextPageIndex = pageIndex + 1;
        } else {
            nextPageIndex = 0;
        }
        request.setAttribute("nextPageIndex", nextPageIndex);

        // JSP path
        String jspPath;
        try {
            View view = this.viewResolver.resolveViewName("page", null);
            JstlView jstlView = (JstlView) view;
            jspPath = jstlView.getUrl();
        } catch (Exception e) {
            throw new PortletException(e);
        }

        // Request dispatcher
        PortletRequestDispatcher dispatcher = portletContext.getRequestDispatcher(jspPath);
        dispatcher.include(request, response);
    }

}
