package fr.index.cloud.ens.selectors.eraser.portlet.service;

import fr.index.cloud.ens.selectors.common.portlet.service.AbstractFileBrowserFilterServiceImpl;
import org.osivia.portal.api.context.PortalControllerContext;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * File browser filter eraser portlet service implementation.
 *
 * @author Cédric Krommenhoek
 * @see AbstractFileBrowserFilterServiceImpl
 * @see FileBrowserFilterEraserService
 */
@Service
public class FileBrowserFilterEraserServiceImpl extends AbstractFileBrowserFilterServiceImpl implements FileBrowserFilterEraserService {

    /**
     * Constructor.
     */
    public FileBrowserFilterEraserServiceImpl() {
        super();
    }


    @Override
    public boolean isEmpty(PortalControllerContext portalControllerContext) {
        // Selectors
        Map<String, List<String>> selectors = this.getSelectors(portalControllerContext);

        return selectors.isEmpty();
    }


    @Override
    public void erase(PortalControllerContext portalControllerContext) {
        // Selectors
        Map<String, List<String>> selectors = this.getSelectors(portalControllerContext);

        // Clear selectors
        selectors.clear();

        this.setSelectors(portalControllerContext, selectors);
    }

}
