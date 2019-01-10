package org.osivia.demo.directory.model;

import org.osivia.portal.api.directory.v2.model.Person;

/**
 * Person customized interface.
 * 
 * @author Cédric Krommenhoek
 * @see Person
 */
public interface CustomizedPerson extends Person {

    /**
     * Get organization.
     * 
     * @return organization
     */
    String getOrganization();


    /**
     * Set organization.
     * 
     * @param organization organization
     */
    void setOrganization(String organization);

}
