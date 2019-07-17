package fr.index.cloud.ens.application.management;

import java.util.List;

import org.osivia.portal.api.portlet.Refreshable;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

/**
 * Dashboard form.
 * 
 * @author JS Steux
 */
@Component
@Scope(WebApplicationContext.SCOPE_SESSION)
@Refreshable
public class ApplicationForm {

    private String code;


    /**
     * Getter for code.
     * 
     * @return the code
     */
    public String getCode() {
        return code;
    }


    /**
     * Setter for code.
     * 
     * @param code the code to set
     */
    public void setCode(String code) {
        this.code = code;
    }


    /**
     * Constructor.
     */
    public ApplicationForm() {
        super();
    }


}
