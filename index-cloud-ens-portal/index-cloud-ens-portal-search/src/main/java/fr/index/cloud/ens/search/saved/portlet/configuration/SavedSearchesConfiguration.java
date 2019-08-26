package fr.index.cloud.ens.search.saved.portlet.configuration;

import fr.index.cloud.ens.search.common.portlet.configuration.SearchCommonConfiguration;
import fr.toutatice.portail.cms.nuxeo.api.CMSPortlet;
import org.osivia.portal.api.locator.Locator;
import org.osivia.portal.api.portlet.PortletAppUtils;
import org.osivia.portal.api.urls.IPortalUrlFactory;
import org.osivia.portal.core.cms.ICMSServiceLocator;
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
 * Saved searches portlet configuration.
 *
 * @author Cédric Krommenhoek
 * @see SearchCommonConfiguration
 */
@Configuration
@ComponentScan(basePackages = "fr.index.cloud.ens.search.saved.portlet")
public class SavedSearchesConfiguration extends SearchCommonConfiguration {

    /**
     * Constructor.
     */
    public SavedSearchesConfiguration() {
        super();
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
        viewResolver.setPrefix("/WEB-INF/jsp/saved-searches/");
        viewResolver.setSuffix(".jsp");
        return viewResolver;
    }

}
