package fr.index.cloud.ens.filebrowser.portlet.configuration;

import org.osivia.services.workspace.filebrowser.portlet.configuration.FileBrowserConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

/**
 * File browser customized portlet configuration.
 *
 * @author Cédric Krommenhoek
 * @see FileBrowserConfiguration
 */
@Configuration
@ComponentScan(basePackages = "fr.index.cloud.ens.filebrowser.portlet")
public class CustomizedFileBrowserConfiguration extends FileBrowserConfiguration {

    /**
     * Constructor.
     */
    public CustomizedFileBrowserConfiguration() {
        super();
    }


    @Override
    public ResourceBundleMessageSource getMessageSource() {
        ResourceBundleMessageSource messageSource = super.getMessageSource();
        messageSource.setBasenames("file-browser", "customized-file-browser");
        return messageSource;
    }

}
