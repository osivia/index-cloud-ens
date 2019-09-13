package fr.index.cloud.ens.directory.person.deleting.portlet.model;

import org.osivia.portal.api.directory.v2.model.Person;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Person deleting form java-bean.
 *
 * @author Loïc Billon
 * @author Cédric Krommenhoek
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PersonDeletingForm {

    /**
     * Person.
     */
    private Person person;
    /**
     * Validated indicator.
     */
    private boolean validated;

    /**
     * Mail.
     */
    private String mail;
    /**
     * Accepted indicator.
     */
    private Boolean accepted;


    /**
     * Constructor.
     */
    public PersonDeletingForm() {
        super();
    }


    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public boolean isValidated() {
        return validated;
    }

    public void setValidated(boolean validated) {
        this.validated = validated;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public Boolean getAccepted() {
        return accepted;
    }

    public void setAccepted(Boolean accepted) {
        this.accepted = accepted;
    }
}
