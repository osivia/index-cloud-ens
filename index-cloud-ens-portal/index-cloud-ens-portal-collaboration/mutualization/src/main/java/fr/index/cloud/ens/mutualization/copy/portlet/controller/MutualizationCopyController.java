package fr.index.cloud.ens.mutualization.copy.portlet.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

/**
 * Mutualization copy portlet controller.
 *
 * @author Cédric Krommenhoek
 */
@Controller
@RequestMapping("VIEW")
public class MutualizationCopyController {

    /**
     * Constructor.
     */
    public MutualizationCopyController() {
        super();
    }


    /**
     * View render mapping.
     *
     * @return view path
     */
    @RenderMapping
    public String view() {
        return "view";
    }

}
