package fr.index.cloud.ens.dashboard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import javax.portlet.PortletException;

import org.apache.commons.collections.CollectionUtils;
import org.osivia.portal.api.context.PortalControllerContext;
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

import fr.index.cloud.ens.ext.etb.EtablissementService;
import fr.index.cloud.ens.ext.etb.IEtablissementService;
import fr.index.cloud.oauth.config.OAuth2ServerConfig;
import fr.index.cloud.oauth.tokenStore.AggregateRefreshTokenInfos;
import fr.index.cloud.oauth.tokenStore.IPortalTokenStore;
import fr.index.cloud.oauth.tokenStore.PortalRefreshToken;
import fr.index.cloud.oauth.tokenStore.PortalRefreshTokenAuthenticationDatas;
import fr.index.cloud.oauth.tokenStore.PortalTokenStore;

/**
 * Web service dashboard portlet service implementation.
 * 
 * @author JS Steux
 * @see DashboardService
 * @see ApplicationContextAware
 */
@Service
public class DashboardServiceImpl implements DashboardService, ApplicationContextAware {


    /** Internationalization bundle factory. */
    @Autowired
    private IBundleFactory bundleFactory;

    /** Notifications service. */
    @Autowired
    private INotificationsService notificationsService;


    /** Application context. */
    private ApplicationContext applicationContext;
    
    @Autowired
    private IPortalTokenStore tokenStore;

    @Autowired
    IEtablissementService etablissementService;

    
    @Bean
    public IPortalTokenStore tokenStore() {
        return new PortalTokenStore();
    }


    /**
     * Constructor.
     */
    public DashboardServiceImpl() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public DashboardForm getDashboardForm(PortalControllerContext portalControllerContext) throws PortletException {
        // Dashboard form
        DashboardForm form = this.applicationContext.getBean(DashboardForm.class);

        if (!form.isLoaded()) {
            // Trashed documents
            
            Collection<AggregateRefreshTokenInfos> tokens = tokenStore.findTokensByUserName(portalControllerContext.getRequest().getRemoteUser());
            
            List<DashboardApplication> applications = new ArrayList<>();
            for (AggregateRefreshTokenInfos token : tokens)    {
                
                String clientName = null;
                String clientId = token.getAuthentication().getClientId();
                
                if( token.getAuthentication().getClientId().startsWith(OAuth2ServerConfig.PRONOTE_CLIENT_PREFIX)) {
                    clientName = etablissementService.getEtablissement(clientId.substring(OAuth2ServerConfig.PRONOTE_CLIENT_PREFIX.length())).getNom();
                }
                
                if( clientName == null)
                    clientName = clientId;
                
                applications.add(new DashboardApplication(token, clientName));
                
            }
            
            form.setApplications(applications);

            form.setLoaded(true);
        }

        return form;
    }



    
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(PortalControllerContext portalControllerContext, DashboardForm form) throws PortletException {
        // Internationalization bundle
        Locale locale = portalControllerContext.getRequest().getLocale();
        Bundle bundle = this.bundleFactory.getBundle(locale);

        // Selected documents
        List<DashboardApplication> selection = this.getSelection(form);


        // Service invocation
        for (DashboardApplication application:selection)    {
            tokenStore.removeRefreshToken(new PortalRefreshToken(application.getToken().getValue()));
        }
        

        // Update model
        this.updateModel(portalControllerContext, form, selection, bundle, "DASHBOARD_DELETE_SELECTION_MESSAGE_");
    }


    /**
     * Get selected documents.
     * 
     * @param form trash form
     * @return selected paths
     */
    private List<DashboardApplication> getSelection(DashboardForm form) {
        // Trashed documents
        List<DashboardApplication> applications;
        if (form == null) {
            applications = null;
        } else {
            applications = form.getApplications();
        }

        // Selected documents
        List<DashboardApplication> selection;
        if (CollectionUtils.isEmpty(applications)) {
            selection = new ArrayList<>(0);
        } else {
            selection = new ArrayList<>(applications.size());
            for (DashboardApplication document : applications) {
                if (document.isSelected()) {
                    selection.add(document);
                }
            }
        }

        return selection;
    }


    /**
     * Update model.
     * 
     * @param portalControllerContext portal controller context
     * @param form trash form
     * @param selection selected documents
     * @param rejected rejected documents
     * @param bundle internationalization bundle
     * @param messagePrefix message prefix
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

    /**
     * {@inheritDoc}
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        ApplicationContextProvider.setApplicationContext(applicationContext);
    }

}
