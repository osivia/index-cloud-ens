/**
 *
 */
package fr.index.cloud.ens.directory.person.creation.portlet.service;

import fr.index.cloud.ens.directory.person.creation.portlet.model.PersonCreationForm;
import fr.toutatice.portail.cms.nuxeo.api.forms.IFormsService;
import org.dom4j.Element;
import org.osivia.portal.api.context.PortalControllerContext;
import org.springframework.validation.Errors;

/**
 * @author Loïc Billon
 *
 */
public interface PersonCreationService {


    /** model identifier. */
    public static final String MODEL_ID = IFormsService.FORMS_WEB_ID_PREFIX + "person-creation-pronote";

    /**
     * @param portalControllerContext
     * @param newpassword
     * @return
     */
    Element getPasswordRulesInformation(PortalControllerContext portalControllerContext, String newpassword);

    /**
     * @param errors
     * @param field
     * @param password
     */
    void validatePasswordRules(Errors errors, String field, String password);

    /**
     * @param portalControllerContext
     * @param form
     */
    void proceedInit(PortalControllerContext portalControllerContext, PersonCreationForm form);

    /**
     * @param input
     * @return
     */
    String removeAccents(String input);

    /**
     * @param portalControllerContext
     * @param property
     */
    void proceedRegistration(PortalControllerContext portalControllerContext, String property);

}
