package fr.index.cloud.ens.portal.discussion.portlet.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.PropertyList;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.portal.api.directory.v2.model.Person;
import org.osivia.portal.api.directory.v2.service.PersonService;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Discussion document java-bean.
 * 
 * @author Jean-Sébastien Steux
 */
public class DiscussionDocument {
    
    public static final String TYPE_USER_COPY = "USER_COPY";


    /** The id. */
    private String id;

    /** The path. */
    private String path;

    /** The title. */
    private String title;

    /** The last modified. */
    private Date lastModified;

    /** The last contributor. */
    private String lastContributor;

    /** The web id. */
    private String webId;
    

    /** The target. */
    private String target;
    
    /** The type. */
    private String type;
    

    /** The participants. */
    private List<String> participants;

    /** The document. */
    private Document document;



    /** The messages. */
    private List<DiscussionMessage> messages;
    
    /** The mark as deleted. */
    private boolean markAsDeleted = false;

    
    
 
    /**
     * Gets the deleted key.
     *
     * @param id the id
     * @return the deleted key
     */
    public static String getDeletedKey( String id)   {
        return "discussion." + id + ".deleted.messageId";
    }

    
    /**
     * Getter for markAsDeleted.
     * @return the markAsDeleted
     */
    public boolean isMarkAsDeleted() {
        return markAsDeleted;
    }


    /**
     * Getter for messages.
     * 
     * @return the messages
     */
    public List<DiscussionMessage> getMessages() {

        if (messages == null) {

            messages = new ArrayList<DiscussionMessage>();

            PropertyList messagesProp = document.getProperties().getList("disc:messages");
            PropertyList removedMessagesProp = document.getProperties().getList("disc:removedMessages");


            for (int i = 0; i < messagesProp.size(); i++) {

                PropertyMap messageProp = messagesProp.getMap(i);

                DiscussionMessage message = new DiscussionMessage();
                String content = messageProp.getString("content");
                message.setContent(content.replaceAll("\\n", "<br>"));
                message.setDate(messageProp.getDate("date"));
                message.setAuthor(messageProp.getString("author"));
                message.setId(Integer.toString(i));
                message.setDiscussionDeleted(false);

                // Removed messages

                for (int j = 0; j < removedMessagesProp.size(); j++) {
                    PropertyMap removedMessageProp = removedMessagesProp.getMap(j);
                    String removedId = removedMessageProp.getString("messageId");

                    if (StringUtils.equalsIgnoreCase(message.getId(), removedId)) {
                        message.setDeleted(true);
                        message.setRemovalDate(removedMessageProp.getDate("date"));

                        break;
                    }
                }

                messages.add(message);

            }

        }
        return messages;
    }


    /**
     * Constructor.
     * 
     * @param document document DTO
     */
    public DiscussionDocument(Map<String, String> userProperties, PersonService personService, String currentUser, Document document, String title) {

        id = document.getId();
        path = document.getPath();

        webId = document.getString("ttc:webid");
        PropertyList participantsProp = document.getProperties().getList("disc:participants");
        participants = new ArrayList<>();
        for (Object name : participantsProp.list()) {
            participants.add((String) name);
        }
        
        target = document.getProperties().getString("disc:target");
        type = document.getProperties().getString("disc:type");
        this.title = title;
             
        
        this.document = document;


        // last message
        for (int iMessage = getMessages().size() - 1; iMessage >= 0 && lastContributor == null; iMessage--) {
            if (!getMessages().get(iMessage).isDeleted()) {
                lastContributor = getMessages().get(iMessage).getAuthor();
                lastModified = getMessages().get(iMessage).getDate();
            }
        }
        
        String deletedMessageId = userProperties.get(getDeletedKey(webId));
        if (deletedMessageId != null) {

            int deleteMsgId = Integer.parseInt(deletedMessageId);

            if (getMessages().size() <= deleteMsgId + 1) {
                markAsDeleted = true;
            }

            // mark messages as not displayable
            for (int iMessage = 0; iMessage <= deleteMsgId; iMessage++) {
                getMessages().get(iMessage).setDiscussionDeleted(true);
            }
        }


    }



    /**
     * Constructor for new discussion
     * 
     * @param document document DTO
     */
    public DiscussionDocument(Map<String, String> userProperties, PersonService personService, String currentUser, String title, List<String> participants) {
        this.messages = new ArrayList<DiscussionMessage>();
        this.participants = participants;
        this.title = title;
     }

    
    /**
     * Constructor for new discussion
     * 
     * @param document document DTO
     */
    public DiscussionDocument(Map<String, String> userProperties, PersonService personService, String currentUser, String type, String target, String publicationTitle) {
        this.messages = new ArrayList<DiscussionMessage>();
        
        this.type = type;
        this.target = target;
        this.title = publicationTitle;

    }

    /**
     * Getter for id.
     * 
     * @return the id
     */
    public String getId() {
        return id;
    }


    /**
     * Getter for path.
     * 
     * @return the path
     */
    public String getPath() {
        return path;
    }


    /**
     * Getter for title.
     * 
     * @return the title
     */
    public String getTitle() {
        return title;
    }


    /**
     * Getter for lastModified.
     * 
     * @return the lastModified
     */
    public Date getLastModified() {
        return lastModified;
    }


    /**
     * Getter for lastContributor.
     * 
     * @return the lastContributor
     */
    public String getLastContributor() {
        return lastContributor;
    }


    /**
     * Getter for webId.
     * 
     * @return the webId
     */
    public String getWebId() {
        return webId;
    }


    /**
     * Getter for participant.
     * 
     * @return the participant
     */
    public List<String> getParticipants() {
        return participants;
    }
    
    
    /**
     * Getter for document.
     * @return the document
     */
    public Document getDocument() {
        return document;
    }


    
    /**
     * Getter for target.
     * @return the target
     */
    public String getTarget() {
        return target;
    }


    
    /**
     * Getter for type.
     * @return the type
     */
    public String getType() {
        return type;
    }


}
