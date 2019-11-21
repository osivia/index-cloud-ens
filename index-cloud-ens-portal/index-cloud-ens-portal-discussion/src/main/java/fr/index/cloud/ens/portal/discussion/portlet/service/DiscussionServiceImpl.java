package fr.index.cloud.ens.portal.discussion.portlet.service;


import fr.index.cloud.ens.portal.discussion.portlet.model.DiscussionCreation;
import fr.index.cloud.ens.portal.discussion.portlet.model.DiscussionsForm;
import fr.index.cloud.ens.portal.discussion.portlet.model.DiscussionsFormSort;
import fr.index.cloud.ens.portal.discussion.portlet.model.DiscussionDocument;
import fr.index.cloud.ens.portal.discussion.portlet.model.comparator.DiscussionComparator;
import fr.index.cloud.ens.portal.discussion.portlet.repository.DiscussionRepository;
import fr.index.cloud.ens.portal.discussion.util.ApplicationContextProvider;
import fr.toutatice.portail.cms.nuxeo.api.domain.DocumentDTO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.dom4j.Element;
import org.jboss.portal.theme.impl.render.dynamic.DynaRenderOptions;
import org.osivia.portal.api.Constants;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.html.AccessibilityRoles;
import org.osivia.portal.api.html.DOM4JUtils;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.menubar.MenubarGroup;
import org.osivia.portal.api.menubar.MenubarItem;
import org.osivia.portal.api.notifications.INotificationsService;
import org.osivia.portal.api.notifications.NotificationsType;
import org.osivia.portal.api.panels.PanelPlayer;
import org.osivia.portal.api.urls.IPortalUrlFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import javax.portlet.*;
import java.util.*;

/**
 * Discussion portlet service implementation.
 *
 * @author Jean-Sébastien Steux
 * @see DiscussionService
 * @see ApplicationContextAware
 */
@Service
public class DiscussionServiceImpl implements DiscussionService, ApplicationContextAware {

    /**
     * Portlet repository.
     */
    @Autowired
    private DiscussionRepository repository;

    /**
     * Portal URL factory.
     */
    @Autowired
    private IPortalUrlFactory portalUrlFactory;

    /**
     * Internationalization bundle factory.
     */
    @Autowired
    private IBundleFactory bundleFactory;

    /**
     * Notifications service.
     */
    @Autowired
    private INotificationsService notificationsService;


    /**
     * Application context.
     */
    private ApplicationContext applicationContext;


    /**
     * Constructor.
     */
    public DiscussionServiceImpl() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public DiscussionsForm getDiscussionsForm(PortalControllerContext portalControllerContext) throws PortletException {
        // Trash form
        DiscussionsForm form = this.applicationContext.getBean(DiscussionsForm.class);

        if (!form.isLoaded()) {
            // Trashed documents
            List<DiscussionDocument> trashedDocuments = this.repository.getDiscussions(portalControllerContext);
            form.setTrashedDocuments(trashedDocuments);

            // Sort
            if (form.getSort() == null) {
                this.sort(portalControllerContext, form, DiscussionsFormSort.DOCUMENT, false);
            } else {
                this.sort(portalControllerContext, form, form.getSort(), form.isAlt());
            }

            form.setLoaded(true);
        }

        return form;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void sort(PortalControllerContext portalControllerContext, DiscussionsForm form, DiscussionsFormSort sort, boolean alt) {
        if (CollectionUtils.isNotEmpty(form.getTrashedDocuments())) {
            // Comparator
            Comparator<DiscussionDocument> comparator = this.applicationContext.getBean(DiscussionComparator.class, sort, alt);

            form.getTrashedDocuments().sort(comparator);

            // Update model
            form.setSort(sort);
            form.setAlt(alt);
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void emptyTrash(PortalControllerContext portalControllerContext, DiscussionsForm form) throws PortletException {
        // Portlet request
        PortletRequest request = portalControllerContext.getRequest();
        // Internationalization bundle
        Bundle bundle = this.bundleFactory.getBundle(request.getLocale());

        // Service invocation
        List<DiscussionDocument> rejected = this.repository.deleteAll(portalControllerContext);

        // Update model
        this.updateModel(portalControllerContext, form, null, rejected, bundle, "TRASH_EMPTY_TRASH_MESSAGE_");
    }




    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(PortalControllerContext portalControllerContext, DiscussionsForm form, String[] identifiers) throws PortletException {
        // Portlet request
        PortletRequest request = portalControllerContext.getRequest();
        // Internationalization bundle
        Bundle bundle = this.bundleFactory.getBundle(request.getLocale());

        // Selected documents
        List<DiscussionDocument> selection = this.getSelection(form, identifiers);
        // Selected paths
        List<String> selectedPaths = this.getSelectedPaths(selection);

        // Service invocation
        List<DiscussionDocument> rejected = this.repository.delete(portalControllerContext, selectedPaths);

        // Update model
        this.updateModel(portalControllerContext, form, selection, rejected, bundle, "TRASH_DELETE_SELECTION_MESSAGE_");
    }


  

    /**
     * Get selected documents.
     *
     * @param form        trash form
     * @param identifiers selected document identifiers
     * @return selected documents
     */
    private List<DiscussionDocument> getSelection(DiscussionsForm form, String[] identifiers) {
        // Trashed document map
        Map<String, DiscussionDocument> trashedDocumentMap;
        if (CollectionUtils.isEmpty(form.getTrashedDocuments())) {
            trashedDocumentMap = null;
        } else {
            trashedDocumentMap = new HashMap<>(form.getTrashedDocuments().size());
            for (DiscussionDocument trashedDocument : form.getTrashedDocuments()) {
                trashedDocumentMap.put(trashedDocument.getId(), trashedDocument);
            }
        }


        // Selected documents
        List<DiscussionDocument> selection;
        if (ArrayUtils.isEmpty(identifiers) || MapUtils.isEmpty(trashedDocumentMap)) {
            selection = null;
        } else {
            selection = new ArrayList<>(identifiers.length);
            for (String identifier : identifiers) {
                DiscussionDocument trashedDocument = trashedDocumentMap.get(identifier);
                if (trashedDocument != null) {
                    selection.add(trashedDocument);
                }
            }
        }

        return selection;
    }


    /**
     * Get selected document paths.
     *
     * @param selection selected documents
     * @return paths
     */
    private List<String> getSelectedPaths(List<DiscussionDocument> selection) {
        // Selected paths
        List<String> paths = new ArrayList<>(selection.size());
        for (DiscussionDocument document : selection) {
            paths.add(document.getPath());
        }

        return paths;
    }


    /**
     * Update model.
     *
     * @param portalControllerContext portal controller context
     * @param form                    trash form
     * @param selection               selected documents
     * @param rejected                rejected documents
     * @param bundle                  internationalization bundle
     * @param messagePrefix           message prefix
     */
    private void updateModel(PortalControllerContext portalControllerContext, DiscussionsForm form, List<DiscussionDocument> selection, List<DiscussionDocument> rejected,
                             Bundle bundle, String messagePrefix) {

        // Portlet request
        PortletRequest request = portalControllerContext.getRequest();

        // Portlet response
        PortletResponse response = portalControllerContext.getResponse();

        if (selection == null) {
            // Select all documents
            selection = new ArrayList<>(form.getTrashedDocuments());
        }

        if (CollectionUtils.isEmpty(rejected)) {
            // Notification
            String message = bundle.getString(messagePrefix + "SUCCESS");
            this.notificationsService.addSimpleNotification(portalControllerContext, message, NotificationsType.SUCCESS);
        } else {
            // Rejected titles
            List<String> titles = new ArrayList<>(rejected.size());
            for (DiscussionDocument document : rejected) {
                titles.add(document.getTitle());
                selection.remove(document);
            }

            // Notification
            String message = bundle.getString(messagePrefix + "WARNING", StringUtils.join(titles, ", "));
            this.notificationsService.addSimpleNotification(portalControllerContext, message, NotificationsType.WARNING);
        }


        // Refresh space data
        request.setAttribute(Constants.PORTLET_ATTR_UPDATE_SPACE_DATA, Constants.PORTLET_VALUE_ACTIVATE);

        // Update public render parameter for associated portlets refresh
        if (response instanceof ActionResponse) {
            ActionResponse actionResponse = (ActionResponse) response;
            actionResponse.setRenderParameter("dnd-update", String.valueOf(System.currentTimeMillis()));
        }

        // Update model
        form.getTrashedDocuments().removeAll(selection);
    }


    @Override
    public Element getToolbar(PortalControllerContext portalControllerContext, List<String> indexes) throws PortletException {
        // Internationalization bundle
        Bundle bundle = this.bundleFactory.getBundle(portalControllerContext.getRequest().getLocale());

        // Toolbar container
        Element container = DOM4JUtils.generateDivElement(null);

        // Toolbar
        Element toolbar = DOM4JUtils.generateDivElement("btn-toolbar", AccessibilityRoles.TOOLBAR);
        container.add(toolbar);

        if (CollectionUtils.isNotEmpty(indexes)) {
            // Trash form
            DiscussionsForm form = this.getDiscussionsForm(portalControllerContext);

            // Trashed documents
            List<DiscussionDocument> documents = form.getTrashedDocuments();

            // Selection
            List<DiscussionDocument> selection = new ArrayList<>(indexes.size());
            for (String index : indexes) {
                int i = NumberUtils.toInt(index, -1);
                if ((i > -1) && (i < documents.size())) {
                    DiscussionDocument document = documents.get(i);
                    if ((document != null) && StringUtils.isNotEmpty(document.getId())) {
                        selection.add(document);
                    }
                }
            }

            if (indexes.size() == selection.size()) {
                // Selection identifiers
                String[] identifiers = new String[selection.size()];
                for (int i = 0; i < selection.size(); i++) {
                    DiscussionDocument document = selection.get(i);
                    identifiers[i] = document.getId();
                }

                // Delete
                Element deleteButton = this.getDeleteToolbarButton(portalControllerContext, bundle);
                toolbar.add(deleteButton);
                Element deleteModalConfirmation = this.getDeleteModalConfirmation(portalControllerContext, bundle, identifiers);
                container.add(deleteModalConfirmation);
            }
        }

        return container;
    }




    /**
     * Get delete selection toolbar button DOM element.
     *
     * @param portalControllerContext portal controller context
     * @param bundle                  internationalization bundle
     * @return DOM element
     */
    private Element getDeleteToolbarButton(PortalControllerContext portalControllerContext, Bundle bundle) {
        // Portlet response
        PortletResponse response = portalControllerContext.getResponse();

        // Modal identifier
        String id = response.getNamespace() + "-delete";

        // Text
        String text = bundle.getString("TRASH_TOOLBAR_DELETE_SELECTION");
        // Icon
        String icon = "glyphicons glyphicons-basic-bin";

        return this.getToolbarButton(id, text, icon);
    }


    /**
     * Get toolbar button DOM element.
     *
     * @param id    modal identifier
     * @param title button text
     * @param icon  button icon
     * @return DOM element
     */
    private Element getToolbarButton(String id, String title, String icon) {
        // URL
        String url = "#" + id;
        // HTML classes
        String htmlClass = "btn btn-primary btn-sm ml-1 no-ajax-link";

        // Button
        Element button = DOM4JUtils.generateLinkElement(url, null, null, htmlClass, null, icon);
        DOM4JUtils.addDataAttribute(button, "toggle", "modal");

        // Text
        Element text = DOM4JUtils.generateElement("span", "d-none d-xl-inline", title);
        button.add(text);

        return button;
    }


    /**
     * Get delete modal confirmation DOM element.
     *
     * @param portalControllerContext portal controller context
     * @param bundle                  internationalization bundle
     * @param identifiers             selection identifiers
     * @return DOM element
     */
    private Element getDeleteModalConfirmation(PortalControllerContext portalControllerContext, Bundle bundle, String[] identifiers) {
        // Portlet response
        PortletResponse response = portalControllerContext.getResponse();

        // Action
        String action = "delete";
        // Modal identifier
        String id = response.getNamespace() + "-delete";
        // Modal title
        String title = bundle.getString("DISCUSSION_DELETE_SELECTION_MODAL_TITLE");
        // Modal message
        String message = bundle.getString("DISCUSSION_DELETE_SELECTION_MODAL_MESSAGE");

        return this.getModalConfirmation(portalControllerContext, bundle, action, identifiers, id, title, message);
    }


    /**
     * Get modal confirmation DOM element.
     *
     * @param portalControllerContext portal controller context
     * @param bundle                  internationalization bundle
     * @param action                  confirmation action name
     * @param identifiers             selection identifiers
     * @param id                      modal identifier
     * @param title                   modal title
     * @param message                 modal message
     * @return DOM element
     */
    private Element getModalConfirmation(PortalControllerContext portalControllerContext, Bundle bundle, String action, String[] identifiers, String id, String title, String message) {
        // Portlet response
        PortletResponse portletResponse = portalControllerContext.getResponse();
        // MIME response
        MimeResponse mimeResponse;
        if (portletResponse instanceof MimeResponse) {
            mimeResponse = (MimeResponse) portletResponse;
        } else {
            mimeResponse = null;
        }


        // Modal
        Element modal = DOM4JUtils.generateDivElement("modal fade");
        DOM4JUtils.addAttribute(modal, "id", id);

        // Modal dialog
        Element modalDialog = DOM4JUtils.generateDivElement("modal-dialog");
        modal.add(modalDialog);

        // Modal content
        Element modalContent = DOM4JUtils.generateDivElement("modal-content");
        modalDialog.add(modalContent);

        // Modal header
        Element modalHeader = DOM4JUtils.generateDivElement("modal-header");
        modalContent.add(modalHeader);

        // Modal title
        Element modalTitle = DOM4JUtils.generateElement("h5", "modal-title", title);
        modalHeader.add(modalTitle);

        // Modal close button
        Element close = DOM4JUtils.generateElement("button", "close", "×");
        DOM4JUtils.addAttribute(close, "type", "button");
        DOM4JUtils.addDataAttribute(close, "dismiss", "modal");
        modalHeader.add(close);

        // Modal body
        Element modalBody = DOM4JUtils.generateDivElement("modal-body");
        modalContent.add(modalBody);

        // Modal body content
        Element bodyContent = DOM4JUtils.generateElement("p", null, message);
        modalBody.add(bodyContent);

        // Modal footer
        Element modalFooter = DOM4JUtils.generateDivElement("modal-footer");
        modalContent.add(modalFooter);

        // Cancel button
        Element cancel = DOM4JUtils.generateElement("button", "btn btn-secondary", bundle.getString("CANCEL"), null, null);
        DOM4JUtils.addAttribute(cancel, "type", "button");
        DOM4JUtils.addDataAttribute(cancel, "dismiss", "modal");
        modalFooter.add(cancel);

        // Confirmation button
        String url;
        if (mimeResponse == null) {
            url = "#";
        } else {
            // Action URL
            PortletURL actionUrl = mimeResponse.createActionURL();
            actionUrl.setParameter(ActionRequest.ACTION_NAME, action);
            actionUrl.setParameter("identifiers", identifiers);

            url = actionUrl.toString();
        }
        Element confirm = DOM4JUtils.generateLinkElement(url, null, null, "btn btn-warning no-ajax-link", bundle.getString("CONFIRM"), null);
        modalFooter.add(confirm);

        return modal;
    }




    /**
     * {@inheritDoc}
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        ApplicationContextProvider.setApplicationContext(applicationContext);
    }


    @Override
    public void createDiscussion(PortalControllerContext portalControllerContext, DiscussionCreation discution) throws PortletException {
        this.repository.createDiscussion(portalControllerContext, discution);
        
    }

}
