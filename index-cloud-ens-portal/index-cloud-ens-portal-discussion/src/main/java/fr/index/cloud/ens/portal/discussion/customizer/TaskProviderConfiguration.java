package fr.index.cloud.ens.portal.discussion.customizer;

import org.osivia.directory.v2.service.preferences.UserPreferencesService;
import org.osivia.portal.api.cache.services.ICacheService;
import org.osivia.portal.api.directory.v2.DirServiceFactory;
import org.osivia.portal.api.directory.v2.service.PersonService;
import org.osivia.portal.api.locator.Locator;
import org.osivia.portal.api.status.IStatusService;
import org.osivia.portal.api.tasks.ITasksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;



/**
 * Client detail customizer
 * 
 * @author Jean-Sébastien Steux
 */
@Configuration
@ComponentScan(basePackages = "fr.index.cloud.ens.portal.discussion.customizer,fr.index.cloud.ens.portal.discussion.portlet.repository")
public class TaskProviderConfiguration  {


    
    /**
     * Constructor.
     */
    public TaskProviderConfiguration() {
        super();
    }

    
    /**
     * Get cache service.
     * 
     * @return internationalization service
     */
    @Bean
    public ICacheService getCacheService() {
        return Locator.findMBean(ICacheService.class, ICacheService.MBEAN_NAME);
    }
    
    
    /**
     * Gets the status service.
     *
     * @return the status service
     */
    @Bean
    public IStatusService getStatusService() {
        return Locator.findMBean(IStatusService.class, "osivia:service=StatusServices");
    }

    

    /**
     * Get person service.
     *
     * @return person service
     */
    @Bean
    public PersonService getPersonService() {
        return DirServiceFactory.getService(PersonService.class);
    }

    
    /**
     * Get user preferences service.
     *
     * @return user preferences service
     */
    @Bean
    public UserPreferencesService getUserPreferencesService() {
        return DirServiceFactory.getService(UserPreferencesService.class);
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



}
