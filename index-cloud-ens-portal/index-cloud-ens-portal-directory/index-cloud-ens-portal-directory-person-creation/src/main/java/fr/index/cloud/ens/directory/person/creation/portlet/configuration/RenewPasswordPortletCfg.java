/**
 * 
 */
package fr.index.cloud.ens.directory.person.creation.portlet.configuration;

import org.osivia.directory.v2.service.PersonUpdateService;
import org.osivia.portal.api.directory.v2.DirServiceFactory;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.internationalization.IInternationalizationService;
import org.osivia.portal.api.locator.Locator;
import org.osivia.portal.api.tokens.ITokenService;
import org.osivia.portal.api.urls.IPortalUrlFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import fr.toutatice.portail.cms.nuxeo.api.CMSPortlet;

/**
 * @author Loïc Billon
 *
 */
@Configuration
@ComponentScan(basePackages = "fr.index.cloud.ens.directory.person.creation.portlet")
public class RenewPasswordPortletCfg  extends CMSPortlet {
	
    /** Application context. */
    @Autowired
    private ApplicationContext applicationContext;
	
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
        viewResolver.setPrefix("/WEB-INF/jsp/");
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
        messageSource.setBasename("Resource");
        return messageSource;
    }    
    
    /**
     * Get person service.
     * 
     * @return person service
     */
    @Bean
    public PersonUpdateService getPersonService() {
        return DirServiceFactory.getService(PersonUpdateService.class);
    }


    /**
     * Get internationalization bundle factory.
     * 
     * @return internationalization bundle factory
     */
    @Bean
    public IBundleFactory getBundleFactory() {
        IInternationalizationService internationalizationService = Locator.findMBean(IInternationalizationService.class,
                IInternationalizationService.MBEAN_NAME);
        return internationalizationService.getBundleFactory(this.getClass().getClassLoader(), this.applicationContext);
    }
    
    /**
     * Get portal URL factory.
     *
     * @return portal URL factory
     */
    @Bean
    public IPortalUrlFactory getPortalUrlFactory() {
        return Locator.findMBean(IPortalUrlFactory.class, IPortalUrlFactory.MBEAN_NAME);
    }   
    
    /**
     * Get Token service
     * @return token service
     */
    @Bean
    public ITokenService getTokenService() {
    	return Locator.findMBean(ITokenService.class, ITokenService.MBEAN_NAME);
    }
    
}
