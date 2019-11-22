package fr.index.cloud.ens.mutualization.copy.portlet.configuration;

import fr.toutatice.portail.cms.nuxeo.api.CMSPortlet;
import org.osivia.portal.api.portlet.PortletAppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.portlet.context.PortletConfigAware;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import javax.portlet.PortletConfig;
import javax.portlet.PortletException;

/**
 * Mutualization copy portlet configuration.
 *
 * @author Cédric Krommenhoek
 * @see CMSPortlet
 * @see PortletConfigAware
 */
@Configuration
@ComponentScan(basePackages = "fr.index.cloud.ens.mutualization.copy.portlet")
public class MutualizationCopyConfiguration extends CMSPortlet implements PortletConfigAware {

    /**
     * Application context.
     */
    @Autowired
    private ApplicationContext applicationContext;


    /**
     * Constructor.
     */
    public MutualizationCopyConfiguration() {
        super();
    }


    @Override
    public void setPortletConfig(PortletConfig portletConfig) {
        try {
            super.init(portletConfig);
        } catch (PortletException e) {
            throw new RuntimeException(e);
        }

        // Register application
        PortletAppUtils.registerApplication(portletConfig, this.applicationContext);
    }


    /**
     * Get view resolver.
     *
     * @return view resolver
     */
    @Bean
    public InternalResourceViewResolver getViewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setCache(true);
        viewResolver.setViewClass(JstlView.class);
        viewResolver.setPrefix("/WEB-INF/jsp/mutualization-copy/");
        viewResolver.setSuffix(".jsp");
        return viewResolver;
    }


    /**
     * Get message source.
     *
     * @return message source
     */
    @Bean(name = "messageSource")
    public ResourceBundleMessageSource getMessageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("mutualization-copy");
        return messageSource;
    }

}
