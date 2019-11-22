package fr.index.cloud.ens.filebrowser.mutualized.portlet.controller;

import fr.index.cloud.ens.filebrowser.commons.portlet.controller.AbstractFileBrowserController;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Mutualized file browser portlet controller.
 *
 * @author Cédric Krommenhoek
 * @see AbstractFileBrowserController
 */
@Controller
@Primary
@RequestMapping("VIEW")
public class MutualizedFileBrowserController extends AbstractFileBrowserController {

    /**
     * Constructor.
     */
    public MutualizedFileBrowserController() {
        super();
    }

}
