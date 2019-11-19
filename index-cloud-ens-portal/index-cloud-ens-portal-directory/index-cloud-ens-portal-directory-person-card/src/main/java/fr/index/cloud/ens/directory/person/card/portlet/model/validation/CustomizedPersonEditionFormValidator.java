package fr.index.cloud.ens.directory.person.card.portlet.model.validation;

import org.osivia.services.person.card.portlet.model.validator.PersonEditionFormValidator;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

/**
 * Person edition customized form validator.
 *
 * @author Cédric Krommenhoek
 * @see PersonEditionFormValidator
 */
@Component
@Primary
public class CustomizedPersonEditionFormValidator extends PersonEditionFormValidator {

    /**
     * Constructor.
     */
    public CustomizedPersonEditionFormValidator() {
        super();
    }


    @Override
    public void validate(Object target, Errors errors) {
        super.validate(target, errors);

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "nickname", "NotEmpty");
    }

}
