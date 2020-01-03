package fr.index.cloud.ens.portal.discussion.portlet.repository;

import java.util.List;

import javax.portlet.PortletException;

import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.context.PortalControllerContext;

import fr.index.cloud.ens.portal.discussion.portlet.model.DetailForm;
import fr.index.cloud.ens.portal.discussion.portlet.model.DiscussionCreation;
import fr.index.cloud.ens.portal.discussion.portlet.model.DiscussionDocument;
import fr.toutatice.portail.cms.nuxeo.api.forms.IFormsService;

/**
 * Discussion portlet repository interface.
 * 
 * @author Jean-sébastien Steux
 */
public interface DiscussionRepository {

    /** model identifier. */
    public static final String MODEL_ID = IFormsService.FORMS_WEB_ID_PREFIX + "discussion";
    
    
    /**
     * Get discussion.
     * 
     * @param portalControllerContext portal controller context
     * @return trashed documents
     * @throws PortalException 
     * @throws PortletException
     */

    List<DiscussionDocument> getDiscussions(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Delete all items.
     * 
     * @param portalControllerContext portal controller context
     * @return rejected documents
     * @throws PortletException
     */
    List<DiscussionDocument> deleteAll(PortalControllerContext portalControllerContext) throws PortletException;


      /**
     * Delete selected items.
     * 
     * @param portalControllerContext portal controller context
     * @param selectedPaths selected item paths
     * @return rejected documents
     * @throws PortletException
     */
    List<DiscussionDocument> delete(PortalControllerContext portalControllerContext, List<String> selectedPaths) throws PortletException;


    /**
     * Create new Discussion
     *
     * @param portalControllerContext portal controller context
     * @param form                    trash form
     */
    void createDiscussion(PortalControllerContext portalControllerContext, DiscussionCreation discution) throws PortletException;



    /**
     * Adds the message.
     *
     * @param portalControllerContext the portal controller context
     * @param form the form
     * @throws PortletException the portlet exception
     */
    void addMessage(PortalControllerContext portalControllerContext, DetailForm form) throws PortletException;


    /**
     * Gets the discussion.
     *
     * @param portalControllerContext the portal controller context
     * @param id the id
     * @return the discussion
     * @throws PortletException the portlet exception
     */
    DiscussionDocument getDiscussion(PortalControllerContext portalControllerContext, String id) throws PortletException;


    /**
     * Delete message.
     *
     * @param portalControllerContext the portal controller context
     * @param form the form
     * @param messageId the message id
     * @throws PortletException the portlet exception
     */
    void deleteMessage(PortalControllerContext portalControllerContext, DetailForm form, String messageId) throws PortletException;


    /**
     * Check user pref.
     *
     * @param portalControllerContext the portal controller context
     * @param id the document id
     * @param lastMessageId the last message id 
     */
    void checkUserReadPreference(PortalControllerContext portalControllerContext, String id, int lastMessageId) throws PortletException;


    /**
     * Gets the discussion by participant.
     *
     * @param portalControllerContext the portal controller context
     * @param participant the participant
     * @return the discussion by participant
     * @throws PortletException the portlet exception
     */
    DiscussionDocument getDiscussionByParticipant(PortalControllerContext portalControllerContext, String participant) throws PortletException;

 
}
