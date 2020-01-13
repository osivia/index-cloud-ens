package fr.index.cloud.ens.portal.discussion.portlet.model;

import java.util.List;

import org.osivia.portal.api.portlet.Refreshable;
import org.osivia.portal.api.portlet.RequestLifeCycle;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

/**
 * Discussion detail form java-bean.
 * 
 * @author Jean-sébastien steux
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class DetailForm {
    
    /**
     * Constructor.
     */
    public DetailForm() {
        super();
    }    

    /** The id. */
    private String id;
    
    
    /** The participant. */
    private String participant;

    /** The author. */
    private String author;

    /** The id. */
    private String publicationId;

    /** The new message. */
    private String newMessage;

    
    /** Loaded indicator. */
    private boolean loaded;
    
    
    /** The document. */
    private DiscussionDocument document;
    

    /** The anchor. */
    private String anchor;
 
    
    
    /**
     * Getter for anchor.
     * @return the anchor
     */
    public String getAnchor() {
        return anchor;
    }



    
    /**
     * Setter for anchor.
     * @param anchor the anchor to set
     */
    public void setAnchor(String anchor) {
        this.anchor = anchor;
    }



    /**
     * Getter for document.
     * @return the document
     */
    public DiscussionDocument getDocument() {
        return document;
    }


    
    /**
     * Setter for document.
     * @param document the document to set
     */
    public void setDocument(DiscussionDocument document) {
        this.document = document;
    }


    /**
     * Getter for newMessage.
     * @return the newMessage
     */
    public String getNewMessage() {
        return newMessage;
    }

    
    /**
     * Setter for newMessage.
     * @param newMessage the newMessage to set
     */
    public void setNewMessage(String newMessage) {
        this.newMessage = newMessage;
    }

    /**
     * Getter for loaded.
     * 
     * @return the loaded
     */
    public boolean isLoaded() {
        return loaded;
    }

    /**
     * Setter for loaded.
     * 
     * @param loaded the loaded to set
     */
    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }

    
    /**
     * Getter for id.
     * @return the id
     */
    public String getId() {
        return id;
    }


    
    /**
     * Setter for id.
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }
  
    
    
    /**
     * Getter for author.
     * @return the author
     */
    public String getAuthor() {
        return author;
    }


    
    /**
     * Setter for author.
     * @param author the author to set
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    
    
    
    
    /**
     * Getter for participant.
     * @return the participant
     */
    public String getParticipant() {
        return participant;
    }




    
    /**
     * Setter for participant.
     * @param participant the participant to set
     */
    public void setParticipant(String participant) {
        this.participant = participant;
    }




    
    /**
     * Getter for publicationId.
     * @return the publicationId
     */
    public String getPublicationId() {
        return publicationId;
    }




    
    /**
     * Setter for publicationId.
     * @param publicationId the publicationId to set
     */
    public void setPublicationId(String publicationId) {
        this.publicationId = publicationId;
    }

    
}
