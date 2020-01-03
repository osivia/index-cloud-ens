package fr.index.cloud.ens.portal.discussion.portlet.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.PropertyList;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Discussion document java-bean.
 * 
 * @author Jean-Sébastien Steux
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class DiscussionDocument {

    private String id;
    private String path;
    private String title;
    private Date lastModified;
    private String lastContributor;
    private String webId;




    private Document document;

    /** The messages. */
    private List<DiscussionMessage> messages;


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
                // value.set("author", form.getAuthor());
            }

        }
        return messages;
    }


    /**
     * Constructor.
     * 
     * @param document document DTO
     */
    public DiscussionDocument(Document document) {

        id = document.getId();
        path = document.getPath();
        title = document.getTitle();
        lastModified = document.getDate("dc:modified");
        lastContributor = document.getString("dc:lastContributor");
        webId = document.getString("ttc:webid");
        
        
        this.document = document;
        
    }

    /**
     * Constructor.
     * 
     * @param document document DTO
     */
    public DiscussionDocument() {
        messages = new ArrayList<DiscussionMessage>();
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
     * @return the webId
     */
    public String getWebId() {
        return webId;
    }

    
}
