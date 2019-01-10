package org.osivia.directory.v2;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Directory services customized configuration.
 * 
 * @author Cédric Krommenhoek
 * @see AppConfig
 */
@Configuration
@Primary
@EnableTransactionManagement
@ComponentScan(basePackages = "org.osivia.demo.directory")
public class CustomizedConfiguration extends AppConfig {

    /**
     * Constructor.
     */
    public CustomizedConfiguration() {
        super();
    }

}
