/**
 * 
 */
package fr.index.cloud.ens.directory.person.creation.portlet.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import fr.toutatice.portail.cms.nuxeo.api.CMSPortlet;

/**
 * @author Loïc Billon
 *
 */
@Configuration
@ComponentScan(basePackages = "fr.index.cloud.ens.directory.person.creation.portlet")
public class PersonConfirmCreationPortletCfg  extends CMSPortlet {

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
}
