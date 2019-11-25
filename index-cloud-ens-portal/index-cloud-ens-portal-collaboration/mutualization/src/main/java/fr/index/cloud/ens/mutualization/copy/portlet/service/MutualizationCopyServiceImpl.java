package fr.index.cloud.ens.mutualization.copy.portlet.service;

import fr.index.cloud.ens.mutualization.copy.portlet.model.MutualizationCopyForm;
import fr.index.cloud.ens.mutualization.copy.portlet.repository.MutualizationCopyRepository;
import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.notifications.INotificationsService;
import org.osivia.portal.api.notifications.NotificationsType;
import org.osivia.portal.api.path.IBrowserService;
import org.osivia.portal.api.urls.IPortalUrlFactory;
import org.osivia.portal.api.windows.PortalWindow;
import org.osivia.portal.api.windows.WindowFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.portlet.PortletException;
import javax.portlet.PortletRequest;

/**
 * Mutualization copy portlet service implementation.
 *
 * @author Cédric Krommenhoek
 * @see MutualizationCopyService
 */
@Service
public class MutualizationCopyServiceImpl implements MutualizationCopyService {

    /**
     * Application context.
     */
    @Autowired
    private ApplicationContext applicationContext;

    /**
     * Portlet repository.
     */
    @Autowired
    private MutualizationCopyRepository repository;

    /**
     * Portal URL factory.
     */
    @Autowired
    private IPortalUrlFactory portalUrlFactory;

    /**
     * Browser service.
     */
    @Autowired
    private IBrowserService browserService;

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
     * Constructor.
     */
    public MutualizationCopyServiceImpl() {
        super();
    }


    @Override
    public MutualizationCopyForm getForm(PortalControllerContext portalControllerContext) throws PortletException {
        // Portlet request
        PortletRequest request = portalControllerContext.getRequest();
        // Window
        PortalWindow window = WindowFactory.getWindow(request);

        // User workspace
        Document userWorkspace = this.repository.getUserWorkspace(portalControllerContext);


        // Form
        MutualizationCopyForm form = this.applicationContext.getBean(MutualizationCopyForm.class);

        // Document path
        String documentPath = window.getProperty(DOCUMENT_PATH_WINDOW_PROPERTY);
        form.setDocumentPath(documentPath);

        // User workspace base path
        String basePath;
        if (userWorkspace == null) {
            basePath = null;
        } else {
            basePath = userWorkspace.getPath() + "/documents";
        }
        form.setBasePath(basePath);

        // Default target path
        String targetPath = basePath;

        return form;
    }


    @Override
    public void copy(PortalControllerContext portalControllerContext, MutualizationCopyForm form) throws PortletException {
        // Portlet request
        PortletRequest request = portalControllerContext.getRequest();
        // Internationalization bundle
        Bundle bundle = this.bundleFactory.getBundle(request.getLocale());

        this.repository.copy(portalControllerContext, form);

        // Notification
        String message = bundle.getString("MUTUALIZATION_COPY_MESSAGE_SUCCESS");
        this.notificationsService.addSimpleNotification(portalControllerContext, message, NotificationsType.SUCCESS);
    }


    @Override
    public String getRedirectionUrl(PortalControllerContext portalControllerContext, MutualizationCopyForm form) {
        return this.portalUrlFactory.getCMSUrl(portalControllerContext, null, form.getDocumentPath(), null, null, IPortalUrlFactory.DISPLAYCTX_REFRESH, null, null, null, null);
    }


    @Override
    public String browse(PortalControllerContext portalControllerContext) throws PortletException {
        // Data
        String data;
        try {
            data = this.browserService.browse(portalControllerContext);
        } catch (PortalException e) {
            throw new PortletException(e);
        }

        return data;
    }

}
