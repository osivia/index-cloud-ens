/**
 * 
 */
package fr.index.cloud.ens.directory.person.creation.portlet.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

/**
 * @author Loïc Billon
 *
 */
@Controller
@RequestMapping(value = "VIEW")
public class PersonConfirmCreationController {

	@RenderMapping
	public String view() {
		return "view";
	}
}
