package fr.index.cloud.ens.directory.person.deleting.portlet.model.validation;

import fr.index.cloud.ens.directory.person.deleting.portlet.model.PersonDeletingForm;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Person deleting form validator.
 *
 * @author Cédric Krommenhoek
 * @see Validator
 */
@Component
public class PersonDeletingFormValidator implements Validator {

    /**
     * Constructor.
     */
    public PersonDeletingFormValidator() {
        super();
    }


    @Override
    public boolean supports(Class<?> clazz) {
        return PersonDeletingForm.class.isAssignableFrom(clazz);
    }


    @Override
    public void validate(Object target, Errors errors) {
        PersonDeletingForm form = (PersonDeletingForm) target;

        // Mail
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "mail", "empty");
        if (StringUtils.isNotBlank(form.getMail())) {
            if (!StringUtils.equals(form.getPerson().getMail(), form.getMail())) {
                errors.rejectValue("mail", "invalid");
            }
        }

        if (BooleanUtils.isNotTrue(form.getAccepted())) {
            errors.rejectValue("accepted", "empty");
        }
    }

}
