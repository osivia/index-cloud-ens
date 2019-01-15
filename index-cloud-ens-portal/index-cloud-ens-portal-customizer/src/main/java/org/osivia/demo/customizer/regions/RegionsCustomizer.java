package org.osivia.demo.customizer.regions;

import java.util.Arrays;
import java.util.Map;

import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;

import org.osivia.portal.api.customization.CustomizationContext;
import org.osivia.portal.api.customization.CustomizationModuleMetadatas;
import org.osivia.portal.api.customization.ICustomizationModule;
import org.osivia.portal.api.customization.ICustomizationModulesRepository;
import org.osivia.portal.api.theming.IRenderedRegions;

/**
 * Regions customizer.
 *
 * @author Cédric Krommenhoek
 * @see GenericPortlet
 * @see ICustomizationModule
 */
public class RegionsCustomizer extends GenericPortlet implements ICustomizationModule {

    /** Customizer name. */
    private static final String CUSTOMIZER_NAME = "demo.customizer.regions";
    /** Customization modules repository attribute name. */
    private static final String ATTRIBUTE_CUSTOMIZATION_MODULES_REPOSITORY = "CustomizationModulesRepository";

    /** Charte context path. */
    private static final String CHARTE_CONTEXT_PATH = "/demo-charte";
    /** Charte Web context path. */
    private static final String CHARTE_WEB_CONTEXT_PATH = "/demo-charte-web";
    /** Charte extranet context path. */
    private static final String CHARTE_EXTRANET_CONTEXT_PATH = "/demo-charte-extranet";


    /** Customization module metadatas. */
    private final CustomizationModuleMetadatas metadatas;


    /** Customization modules repository. */
    private ICustomizationModulesRepository repository;


    /**
     * Constructor.
     */
    public RegionsCustomizer() {
        super();
        this.metadatas = this.generateMetadatas();
    }


    /**
     * Generate customization module metadatas.
     *
     * @return metadatas
     */
    private CustomizationModuleMetadatas generateMetadatas() {
        CustomizationModuleMetadatas metadatas = new CustomizationModuleMetadatas();
        metadatas.setName(CUSTOMIZER_NAME);
        metadatas.setModule(this);
        metadatas.setCustomizationIDs(Arrays.asList(IRenderedRegions.CUSTOMIZER_ID));
        return metadatas;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void init() throws PortletException {
        super.init();
        this.repository = (ICustomizationModulesRepository) this.getPortletContext().getAttribute(ATTRIBUTE_CUSTOMIZATION_MODULES_REPOSITORY);
        this.repository.register(this.metadatas);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void destroy() {
        super.destroy();
        this.repository.unregister(this.metadatas);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void customize(CustomizationContext customizationContext) {
        Map<String, Object> attributes = customizationContext.getAttributes();
        IRenderedRegions renderedRegion = (IRenderedRegions) attributes.get(IRenderedRegions.CUSTOMIZER_ATTRIBUTE_RENDERED_REGIONS);

        // Context path
        String contextPath = (String) attributes.get(IRenderedRegions.CUSTOMIZER_ATTRIBUTE_THEME_CONTEXT_PATH);

        if (CHARTE_CONTEXT_PATH.equals(contextPath)) {
            // Toolbar
            renderedRegion.customizeRenderedRegion("toolbar", "/regions/toolbar.jsp");
            // Tabs
            renderedRegion.removeRenderedRegion("tabs");
            // Remove footer
            renderedRegion.removeRenderedRegion("footer");
        } 
    }

}
