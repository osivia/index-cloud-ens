package org.osivia.demo.customizer.plugin;

import org.apache.commons.lang.StringUtils;
import org.osivia.portal.api.theming.TemplateAdapter;

/**
 * Demo template adapter.
 *
 * @author Cédric Krommenhoek
 * @see TemplateAdapter
 */
public class DemoTemplateAdapter implements TemplateAdapter {

    /**
     * Constructor.
     */
    public DemoTemplateAdapter() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String adapt(String spacePath, String path, String spaceTemplate, String targetTemplate) {
        // Adapted template
        String template = null;


        // User workspace
        if (StringUtils.startsWith(spacePath, "/default-domain/UserWorkspaces/")) {
            if (StringUtils.equals(targetTemplate, "/default/templates/workspace")) {
                template = "/default/templates/user-workspace";
            }
        }

        return template;
    }

}
