package fr.index.cloud.ens.search.filters.portlet.configuration;

import fr.index.cloud.ens.search.common.portlet.configuration.SearchCommonConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

/**
 * Search filters portlet configuration.
 *
 * @author Cédric Krommenhoek
 * @see SearchCommonConfiguration
 */
@Configuration
@ComponentScan(basePackages = {"fr.index.cloud.ens.search.filters.portlet", "fr.index.cloud.ens.search.common.portlet"})
public class SearchFiltersConfiguration extends SearchCommonConfiguration {

    /**
     * Constructor.
     */
    public SearchFiltersConfiguration() {
        super();
    }


    @Override
    protected String getJspFolder() {
        return "search-filters";
    }

}
