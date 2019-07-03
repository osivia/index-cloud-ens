package fr.index.cloud.ens.search.portlet.configuration;

import fr.index.cloud.ens.search.common.portlet.configuration.SearchCommonConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

/**
 * Search portlet configuration.
 *
 * @author Cédric Krommenhoek
 * @see SearchCommonConfiguration
 */
@Configuration
@ComponentScan(basePackages = "fr.index.cloud.ens.search.portlet")
public class SearchConfiguration extends SearchCommonConfiguration {

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
        viewResolver.setPrefix("/WEB-INF/jsp/search/");
        viewResolver.setSuffix(".jsp");
        return viewResolver;
    }

}
