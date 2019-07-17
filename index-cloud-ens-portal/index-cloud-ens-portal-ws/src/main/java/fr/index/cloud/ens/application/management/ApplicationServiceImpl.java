package fr.index.cloud.ens.application.management;

import fr.index.cloud.ens.dashboard.DashboardForm;
import fr.index.cloud.ens.ext.etb.IEtablissementService;
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
 * @see ApplicationService
 * @see ApplicationContextAware
 */
@Service
public class ApplicationServiceImpl implements ApplicationService, ApplicationContextAware {


    @Autowired
    IEtablissementService etablissementService;
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
    public ApplicationServiceImpl() {
        super();
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
    public ApplicationForm getApplicationForm(PortalControllerContext portalControllerContext) throws PortletException {
        // application form
        ApplicationForm form = this.applicationContext.getBean(ApplicationForm.class);
        return form;
    }

}
