package fr.index.cloud.ens.search.options.portlet.configuration;

import fr.index.cloud.ens.search.common.portlet.configuration.SearchCommonConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

/**
 * Search options portlet configuration.
 *
 * @author Cédric Krommenhoek
 * @see SearchCommonConfiguration
 */
@Configuration
@ComponentScan(basePackages = "fr.index.cloud.ens.search.options.portlet")
public class SearchOptionsConfiguration extends SearchCommonConfiguration {

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
        viewResolver.setPrefix("/WEB-INF/jsp/search-options/");
        viewResolver.setSuffix(".jsp");
        return viewResolver;
    }

}
