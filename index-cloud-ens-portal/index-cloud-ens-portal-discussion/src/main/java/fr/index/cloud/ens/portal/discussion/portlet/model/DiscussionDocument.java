package fr.index.cloud.ens.portal.discussion.portlet.model;

import java.util.Date;

import fr.toutatice.portail.cms.nuxeo.api.domain.DocumentDTO;

import org.apache.commons.lang.BooleanUtils;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.portal.api.directory.v2.model.Person;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Trashed document java-bean.
 * 
 * @author Cédric Krommenhoek
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class DiscussionDocument {

    private String id;
    private String path;
    private String title;
    private Date lastModified;
    private String lastContributor;
    
    /**
     * Constructor.
     * @param document document DTO
     */
    public DiscussionDocument(Document document) {
        // Variables
//        PropertyMap variables = document.getProperties().getMap("pi:globalVariablesValues");

//        id =document.getId();
//        path = document.getPath();
//        title = variables.getString("message");
//        lastModified = document.getDate("dc:modified");
//        lastContributor = document.getString("dc:lastContributor");
        
        
        // Task variables
        PropertyMap taskVariables = document.getProperties().getMap("nt:task_variables");
//
//        // Task initiator
//        Person initiator = this.personService.getPerson(document.getString("nt:initiator"));
//
//        // Task
//        Task task = this.applicationContext.getBean(Task.class);
//        task.setDocument(document);
//        task.setDisplay(display);
//        task.setInitiator(initiator);
//        task.setDate(document.getDate("dc:created"));
//        task.setAcknowledgeable(BooleanUtils.isTrue(taskVariables.getBoolean("acquitable")));
//        task.setCloseable(BooleanUtils.isTrue(taskVariables.getBoolean("closable")));

//        id =document.getId();
//        path = document.getPath();
//        title = document.getProperties().getMap("nt:pi").getMap("pi:globalVariablesValues").getString("message");
//        lastModified = document.getDate("dc:modified");
//        lastContributor = document.getString("dc:lastContributor");    
//        
          id =document.getId();
          path = document.getPath();
          title = document.getTitle();
          lastModified = document.getDate("dc:modified");
          lastContributor = document.getString("dc:lastContributor");
        
    }

    
    
    /**
     * Getter for id.
     * @return the id
     */
    public String getId() {
        return id;
    }


    
    /**
     * Getter for path.
     * @return the path
     */
    public String getPath() {
        return path;
    }


    /**
     * Getter for title.
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    
    /**
     * Getter for lastModified.
     * @return the lastModified
     */
    public Date getLastModified() {
        return lastModified;
    }

    
    /**
     * Getter for lastContributor.
     * @return the lastContributor
     */
    public String getLastContributor() {
        return lastContributor;
    }


}
