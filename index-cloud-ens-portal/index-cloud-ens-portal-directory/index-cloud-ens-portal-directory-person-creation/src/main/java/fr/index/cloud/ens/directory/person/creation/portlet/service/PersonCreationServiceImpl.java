/**
 *
 */
package fr.index.cloud.ens.directory.person.creation.portlet.service;

import fr.index.cloud.ens.directory.person.creation.portlet.model.PersonCreationForm;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterException;
import fr.toutatice.portail.cms.nuxeo.api.forms.IFormsService;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Element;
import org.osivia.directory.v2.service.PersonUpdateService;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.directory.v2.model.Person;
import org.osivia.portal.api.html.DOM4JUtils;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.notifications.INotificationsService;
import org.osivia.portal.api.notifications.NotificationsType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import java.text.Normalizer;
import java.util.*;

/**
 * @author Loïc Billon
 *
 */
@Service
public class PersonCreationServiceImpl implements PersonCreationService {

    /** Logger. */
    private static final Log logger = LogFactory.getLog(PersonCreationServiceImpl.class);

    @Autowired
    private PersonUpdateService personService;

    /** Forms service. */
    @Autowired
    private IFormsService formsService;


    /** Bundle factory. */
    @Autowired
    private IBundleFactory bundleFactory;

    /** Notifications service. */
    @Autowired
    private INotificationsService notificationsService;

    @Override
    public void validatePasswordRules(Errors errors, String field, String password) {
        Map<String, String> messages = this.personService.validatePasswordRules(password);

        if (MapUtils.isNotEmpty(messages)) {
            for (Map.Entry<String, String> entry : messages.entrySet()) {
                errors.rejectValue(field, entry.getKey(), entry.getValue());
            }
        }
    }

    /* (non-Javadoc)
     * @see fr.index.cloud.ens.directory.person.renew.portlet.service.RenewPasswordService#getPasswordRulesInformation(org.osivia.portal.api.context.PortalControllerContext, java.lang.String)
     */
    @Override
    public Element getPasswordRulesInformation(PortalControllerContext portalControllerContext, String password) {
        // Information
        Map<String, Boolean> information = this.personService.getPasswordRulesInformation(password);

        // Container
        Element container = DOM4JUtils.generateDivElement(StringUtils.EMPTY);

        if (MapUtils.isNotEmpty(information)) {
            Element ul = DOM4JUtils.generateElement("ul", "list-unstyled", StringUtils.EMPTY);
            container.add(ul);

            for (Map.Entry<String, Boolean> entry : information.entrySet()) {
                Element li = DOM4JUtils.generateElement("li", null, StringUtils.EMPTY);
                ul.add(li);

                String htmlClass;
                String icon;
                if (BooleanUtils.isTrue(entry.getValue())) {
                    htmlClass = "text-success";
                    icon = "glyphicons glyphicons-check";
                } else {
                    htmlClass = null;
                    icon = "glyphicons glyphicons-unchecked";
                }
                Element item = DOM4JUtils.generateElement("span", htmlClass, entry.getKey(), icon, null);
                li.add(item);
            }
        }

        return container;
    }

    /* (non-Javadoc)
     * @see fr.index.cloud.ens.directory.person.creation.portlet.controller.PersonCreationService#proceedInit(org.osivia.portal.api.context.PortalControllerContext, fr.index.cloud.ens.directory.person.creation.portlet.controller.PersonCreationForm)
     */
    @Override
    public void proceedInit(PortalControllerContext portalControllerContext, PersonCreationForm form) {

        // A person can be in directory but his account should not be valid
        Person searchByMail = this.personService.getEmptyPerson();
        searchByMail.setMail(form.getMail());
        List<Person> findByCriteria = personService.findByCriteria(searchByMail);

        Person person = null;
        String uid = null;
        boolean reuseAccount = false;
        if (findByCriteria.size() > 0) {

            Person personFound = findByCriteria.get(0);
            person = personFound;
            uid = person.getUid();
            reuseAccount = true;

        } else {
            person = this.personService.getEmptyPerson();
            uid = genUid(form);
            person.setUid(uid);
            // Person is created with passed current date validity. 
            // It can not be logged in until the mail is checked, which will remove this attribute.
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_YEAR, -1);
            person.setValidity(new Date(cal.getTimeInMillis()));
        }

        person.setGivenName(form.getFirstname());
        person.setSn(form.getLastname());
        person.setMail(form.getMail());
        person.setDisplayName(form.getNickname());
        person.setCn(person.getGivenName() + " " + person.getSn());

        if (reuseAccount) {
            this.personService.update(person);
        } else {
            this.personService.create(person);

        }

        // Record password
        this.personService.updatePassword(person, form.getNewpassword());


        // Variables
        Map<String, String> variables = new HashMap<>();
        variables.put("uid", uid);
        variables.put("mail", form.getMail());

        // Start
        try {
            formsService.start(portalControllerContext, MODEL_ID, variables);
        } catch (PortalException | FormFilterException e) {

            logger.error("Error during person cration", e);

            Bundle bundle = this.bundleFactory.getBundle(portalControllerContext.getRequest().getLocale());

            String message = bundle.getString("createaccount.mail.error");
            this.notificationsService.addSimpleNotification(portalControllerContext, message, NotificationsType.ERROR);

        }

    }


    /**
     * UID is first letter of the firstname + lastname without maj, or special characters, accents, .... + a single number
     * @return the login
     */
    private String genUid(PersonCreationForm form) {

        // firstname : trim white spaces, remove accents then check the regex
        String firstname = removeAccents(form.getFirstname());
        String firstNameInitial = StringUtils.substring(firstname, 0, 1);

        // lastname, idem
        String lastname = removeAccents(form.getLastname());
        String lastNameClean = StringUtils.replaceEach(lastname, new String[]{" ", "'"}, new String[]{"-", "-"});

        // uid, build first le
        String uid = StringUtils.lowerCase(firstNameInitial + lastNameClean);

        Person searchByUid = this.personService.getEmptyPerson();
        searchByUid.setUid(uid + "*");
        List<Person> findByCriteria = personService.findByCriteria(searchByUid);

        int suffix = 0;
        for (Person p : findByCriteria) {

            String suffixStr = StringUtils.remove(p.getUid(), uid);
            if (!StringUtils.isEmpty(suffixStr)) {
                int parseInt = Integer.parseInt(suffixStr);
                if (parseInt > suffix) {
                    suffix = parseInt;
                }
            }


        }

        if (findByCriteria.size() > 0) {
            suffix++;
            uid += Integer.toString(suffix);
        }

        return uid;
    }


    public String removeAccents(String input) {
        // Suppression d'accents
        input = Normalizer.normalize(input, Normalizer.Form.NFD);
        return input.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
    }

    /* (non-Javadoc)
     * @see fr.index.cloud.ens.directory.person.creation.portlet.controller.PersonCreationService#proceedRegistration(org.osivia.portal.api.context.PortalControllerContext, java.lang.String)
     */
    @Override
    public void proceedRegistration(PortalControllerContext portalControllerContext, String uid) {

        Person person = personService.getPerson(uid);

        // Account is valid unlimited time
        person.setValidity(null);

        personService.update(person);
    }
}
