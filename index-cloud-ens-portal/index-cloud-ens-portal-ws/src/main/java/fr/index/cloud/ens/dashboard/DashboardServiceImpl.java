package fr.index.cloud.ens.dashboard;

import fr.index.cloud.ens.application.api.Application;
import fr.index.cloud.ens.application.api.IApplicationService;
import fr.index.cloud.oauth.config.OAuth2ServerConfig;
import fr.index.cloud.oauth.tokenStore.AggregateRefreshTokenInfos;
import fr.index.cloud.oauth.tokenStore.IPortalTokenStore;
import fr.index.cloud.oauth.tokenStore.PortalRefreshToken;
import fr.index.cloud.oauth.tokenStore.PortalTokenStore;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.dom4j.Element;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.html.AccessibilityRoles;
import org.osivia.portal.api.html.DOM4JUtils;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.notifications.INotificationsService;
import org.osivia.portal.api.notifications.NotificationsType;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import javax.portlet.*;
import java.util.*;

/**
 * Web service dashboard portlet service implementation.
 *
 * @author JS Steux
 * @see DashboardService
 * @see ApplicationContextAware
 */
@Service
public class DashboardServiceImpl implements DashboardService, ApplicationContextAware {


    @Autowired
    IApplicationService applicationService;
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
    @Autowired
    private IPortalTokenStore tokenStore;


    /**
     * Constructor.
     */
    public DashboardServiceImpl() {
        super();
    }

    @Bean
    public IPortalTokenStore tokenStore() {
        return new PortalTokenStore();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DashboardForm getDashboardForm(PortalControllerContext portalControllerContext) throws PortletException {
        // Dashboard form
        DashboardForm form = this.applicationContext.getBean(DashboardForm.class);

        if (!form.isLoaded()) {
            Collection<AggregateRefreshTokenInfos> tokens = tokenStore.findTokensByUserName(portalControllerContext.getRequest().getRemoteUser());

            List<DashboardApplication> applications = new ArrayList<>();
            for (AggregateRefreshTokenInfos token : tokens) {
                String clientName = null;
                String clientId = token.getAuthentication().getClientId();

                Application oAuth2Application =  applicationService.getApplicationByClientID(clientId);
                if( oAuth2Application != null)
                    clientName = oAuth2Application.getTitle();
 
                if (clientName == null) {
                    clientName = clientId;
                }


                // Application
                DashboardApplication application = this.applicationContext.getBean(DashboardApplication.class);
                application.setToken(token);
                application.setClientId(clientId);
                application.setClientName(clientName);

                applications.add(application);
            }

            form.setApplications(applications);

            // Sort
            if (form.getSort() == null) {
                this.sort(portalControllerContext, form, DashboardSort.APPLICATION, false);
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
    public void sort(PortalControllerContext portalControllerContext, DashboardForm form, DashboardSort sort, boolean alt) {
        if (CollectionUtils.isNotEmpty(form.getApplications())) {
            // Comparator
            Comparator<DashboardApplication> comparator = this.applicationContext.getBean(DashboardApplicationComparator.class, sort, alt);

            Collections.sort(form.getApplications(), comparator);

            // Update model
            form.setSort(sort);
            form.setAlt(alt);
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(PortalControllerContext portalControllerContext, DashboardForm form, String[] identifiers) throws PortletException {
        // Internationalization bundle
        Locale locale = portalControllerContext.getRequest().getLocale();
        Bundle bundle = this.bundleFactory.getBundle(locale);

        // Selected documents
        List<DashboardApplication> selection = this.getSelection(form, identifiers);


        // Service invocation
        for (DashboardApplication application : selection) {
            tokenStore.removeRefreshToken(new PortalRefreshToken(application.getToken().getValue()));
        }


        // Update model
        this.updateModel(portalControllerContext, form, selection, bundle, "DASHBOARD_DELETE_SELECTION_MESSAGE_");
    }


    /**
     * Get selected documents.
     *
     * @param form dashboard form
     * @return selected paths
     */
    private List<DashboardApplication> getSelection(DashboardForm form, String[] identifiers) {
        // Application map
        Map<String, List<DashboardApplication>> applicationMap;
        if (CollectionUtils.isEmpty(form.getApplications())) {
            applicationMap = null;
        } else {
            applicationMap = new HashMap<>(form.getApplications().size());
            for (DashboardApplication application : form.getApplications()) {
                
                List<DashboardApplication> applications = applicationMap.get(application.getClientId());
                if( applications == null)   {
                    applications = new ArrayList<>();
                    applicationMap.put(application.getClientId(), applications);
                }
                
                applications.add( application);
            }
        }


        // Selected documents
        List<DashboardApplication> selection;
        if (ArrayUtils.isEmpty(identifiers) || MapUtils.isEmpty(applicationMap)) {
            selection = null;
        } else {
            selection = new ArrayList<>(identifiers.length);
            for (String identifier : identifiers) {
                List<DashboardApplication> applications = applicationMap.get(identifier);
                if (applications != null) {
                    for( DashboardApplication application:applications) {
                        selection.add(application);
                    }
                }
            }
        }

        return selection;
    }


    /**
     * Update model.
     *
     * @param portalControllerContext portal controller context
     * @param form                    dashboard form
     * @param selection               selected documents
     * @param bundle                  internationalization bundle
     * @param messagePrefix           message prefix
     */
    private void updateModel(PortalControllerContext portalControllerContext, DashboardForm form, List<DashboardApplication> selection, Bundle bundle,
                             String messagePrefix) {
        if (selection == null) {
            // Select all documents
            selection = new ArrayList<>(form.getApplications());
        }


        // Notification
        String message = bundle.getString(messagePrefix + "SUCCESS");
        this.notificationsService.addSimpleNotification(portalControllerContext, message, NotificationsType.SUCCESS);


        // Update model
        form.getApplications().removeAll(selection);
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
            // Dashboard form
            DashboardForm form = this.getDashboardForm(portalControllerContext);

            // Applications
            List<DashboardApplication> applications = form.getApplications();

            // Selection
            List<DashboardApplication> selection = new ArrayList<>(indexes.size());
            for (String index : indexes) {
                int i = NumberUtils.toInt(index, -1);
                if ((i > -1) && (i < applications.size())) {
                    DashboardApplication application = applications.get(i);
                    if (StringUtils.isNotEmpty(application.getClientId())){
                        selection.add(application);
                    }
                }
            }

            if (indexes.size() == selection.size()) {
                // Selection identifiers
                String[] identifiers = new String[selection.size()];
                for (int i = 0; i < selection.size(); i++) {
                    DashboardApplication application = selection.get(i);
                    identifiers[i] = application.getClientId();
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
        String text = bundle.getString("DASHBOARD_TOOLBAR_DELETE_SELECTION");
        // Icon
        String icon = "glyphicons glyphicons-basic-bin";

        return this.getToolbarButton(id, text, icon);
    }


    /**
     * Get toolbar button DOM element.
     *
     * @param id   modal identifier
     * @param text button text
     * @param icon button icon
     * @return DOM element
     */
    private Element getToolbarButton(String id, String text, String icon) {
        // URL
        String url = "#" + id;
        // HTML classes
        String htmlClass = "btn btn-primary btn-sm ml-1 no-ajax-link";

        // Button
        Element button = DOM4JUtils.generateLinkElement(url, null, null, htmlClass, text, icon);
        DOM4JUtils.addDataAttribute(button, "toggle", "modal");

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
        String title = bundle.getString("DASHBOARD_DELETE_SELECTION_MODAL_TITLE");
        // Modal message
        String message = bundle.getString("DASHBOARD_DELETE_SELECTION_MODAL_MESSAGE");

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

        // Confirmation button
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

        // Cancel button
        Element cancel = DOM4JUtils.generateElement("button", "btn btn-secondary", bundle.getString("CANCEL"), null, null);
        DOM4JUtils.addAttribute(cancel, "type", "button");
        DOM4JUtils.addDataAttribute(cancel, "dismiss", "modal");
        modalFooter.add(cancel);

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

}
