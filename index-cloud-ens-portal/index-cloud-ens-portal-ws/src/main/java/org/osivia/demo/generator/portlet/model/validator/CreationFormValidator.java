package org.osivia.demo.generator.portlet.model.validator;

import org.osivia.demo.generator.portlet.model.CreationForm;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Creation form validator.
 * 
 * @author Cédric Krommenhoek
 * @see Validator
 */
@Component
public class CreationFormValidator implements Validator {

    /**
     * Constructor.
     */
    public CreationFormValidator() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean supports(Class<?> clazz) {
        return CreationForm.class.isAssignableFrom(clazz);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "parentPath", "NotEmpty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "type", "NotEmpty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "title", "NotEmpty");
    }

}
