package fr.index.cloud.ens.ws;

import org.osivia.directory.v2.service.PersonUpdateService;
import org.osivia.portal.api.directory.v2.DirServiceFactory;
import org.osivia.portal.api.directory.v2.service.PersonService;
import org.osivia.portal.api.locator.Locator;
import org.osivia.portal.api.notifications.INotificationsService;
import org.osivia.portal.api.urls.IPortalUrlFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;



/**
 * 
 * Accès aux service du portail
 * 
 * @author JS Steux
 *
 */
@Configuration
public class PortalServices   {



    /**
     * Get notifications service.
     * 
     * @return notification service
     */
    @Bean
    public INotificationsService getNotificationService() {
        return Locator.findMBean(INotificationsService.class, INotificationsService.MBEAN_NAME);
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
     * Get person service.
     *
     * @return person service
     */
    @Bean(name = "personUpdateService")
    public PersonUpdateService getPersonService() {
    	return DirServiceFactory.getService(PersonUpdateService.class);
    }
    


}
