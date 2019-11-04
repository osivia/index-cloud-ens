package fr.index.cloud.ens.directory.person.card.portlet.model;

import org.osivia.services.person.card.portlet.model.PersonEditionForm;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Person edition customized form.
 *
 * @author Cédric Krommenhoek
 * @see PersonEditionForm
 */
@Component
@Primary
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CustomizedPersonEditionForm extends PersonEditionForm {

    /**
     * Nickname.
     */
    private String nickname;


    /**
     * Constructor.
     */
    public CustomizedPersonEditionForm() {
        super();
    }


    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

}
