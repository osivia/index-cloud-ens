package fr.index.cloud.ens.customizer.plugin;

import fr.toutatice.portail.cms.nuxeo.api.domain.AbstractPluginPortlet;
import fr.toutatice.portail.cms.nuxeo.api.domain.ListTemplate;
import org.osivia.portal.api.customization.CustomizationContext;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.internationalization.IInternationalizationService;
import org.osivia.portal.api.locator.Locator;

import javax.portlet.PortletException;
import java.util.Map;

/**
 * Demo plugin.
 *
 * @author Cédric Krommenhoek
 * @see AbstractPluginPortlet
 */
public class CloudEnsPlugin extends AbstractPluginPortlet {

    /**
     * Plugin name.
     */
    private static final String PLUGIN_NAME = "cloud-ens.plugin";


    /**
     * Internationalization bundle factory.
     */
    private final IBundleFactory bundleFactory;


    /**
     * Constructor.
     */
    public CloudEnsPlugin() {
        super();

        // Internationalization bundle factory
        IInternationalizationService internationalizationService = Locator.findMBean(IInternationalizationService.class,
                IInternationalizationService.MBEAN_NAME);
        this.bundleFactory = internationalizationService.getBundleFactory(this.getClass().getClassLoader());
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void init() throws PortletException {
        super.init();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public int getOrder() {
        return Integer.MAX_VALUE;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected String getPluginName() {
        return PLUGIN_NAME;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected void customizeCMSProperties(CustomizationContext customizationContext) {
        // List templates
        this.customizeListTemplates(customizationContext);
    }


    /**
     * Customize list templates.
     *
     * @param customizationContext customization context
     */
    private void customizeListTemplates(CustomizationContext customizationContext) {
        // Internationalization bundle
        Bundle bundle = this.bundleFactory.getBundle(customizationContext.getLocale());

        // List templates
        Map<String, ListTemplate> templates = this.getListTemplates(customizationContext);

        // Quick access
        ListTemplate quickAccessList = new ListTemplate("quick-access", bundle.getString("LIST_TEMPLATE_QUICK_ACCESS"), "*");
        templates.put(quickAccessList.getKey(), quickAccessList);
    }

}
