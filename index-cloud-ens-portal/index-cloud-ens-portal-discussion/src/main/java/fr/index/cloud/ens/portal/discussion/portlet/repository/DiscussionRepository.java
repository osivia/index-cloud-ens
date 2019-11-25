package fr.index.cloud.ens.portal.discussion.portlet.repository;

import java.util.List;

import javax.portlet.PortletException;

import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.context.PortalControllerContext;

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

 
}
