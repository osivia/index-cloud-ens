package fr.index.cloud.ens.portal.discussion.portlet.service;

import org.dom4j.Element;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.panels.PanelPlayer;

import fr.index.cloud.ens.portal.discussion.portlet.model.DetailForm;
import fr.index.cloud.ens.portal.discussion.portlet.model.DiscussionCreation;
import fr.index.cloud.ens.portal.discussion.portlet.model.DiscussionsForm;
import fr.index.cloud.ens.portal.discussion.portlet.model.DiscussionsFormSort;

import javax.portlet.PortletException;
import java.io.IOException;
import java.util.List;

/**
 * Discussion service interface.
 *
 * @author Jean-Sébastien Steux
 */
public interface DiscussionService {

 
    /**
     * Get dicussions form.
     *
     * @param portalControllerContext portal controller context
     * @return discussions form
     */
    DiscussionsForm getDiscussionsForm(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Sort discussions form.
     *
     * @param portalControllerContext portal controller context
     * @param form                    discussion form
     * @param sort                    sort property
     * @param alt                     alternative sort indicator
     */
    void sort(PortalControllerContext portalControllerContext, DiscussionsForm form, DiscussionsFormSort sort, boolean alt) throws PortletException;


    /**
     * Create new Discussion
     *
     * @param portalControllerContext portal controller context
     * @param form                    discussion bean
     */
    void createDiscussion(PortalControllerContext portalControllerContext, DiscussionCreation discution) throws PortletException;

 

  

    /**
     * Get toolbar DOM element.
     *
     * @param portalControllerContext portal controller context
     * @param indexes                 selected row indexes
     * @return DOM element
     */
    Element getToolbar(PortalControllerContext portalControllerContext, List<String> indexes, DiscussionsForm form) throws PortletException, IOException;

   

    /**
     * Get detail form.
     *
     * @param portalControllerContext portal controller context
     * @param id the id
     * @param anchor the anchor
     * @return detail form
     * @throws PortletException the portlet exception
     */
    
    DetailForm getDetailForm(PortalControllerContext portalControllerContext, String id, String participant, String publicationId, String anchor) throws PortletException;


    /**
     * Adds the new message.
     *
     * @param portalControllerContext the portal controller context
     * @param form the form
     * @throws PortletException 
     */
    
    void addMessage(PortalControllerContext portalControllerContext, DetailForm form) throws PortletException;


    /**
     * Delete message.
     *
     * @param portalControllerContext the portal controller context
     * @param form the form
     * @param idMessage the id message
     * @throws PortletException the portlet exception
     */
    void deleteMessage(PortalControllerContext portalControllerContext, DetailForm form, String idMessage) throws PortletException;


    /**
     * Delete discussions.
     *
     * @param portalControllerContext the portal controller context
     * @param form the form
     * @param identifiers the identifiers
     * @throws PortletException the portlet exception
     */
    void deleteDiscussions(PortalControllerContext portalControllerContext, DiscussionsForm form, String[] identifiers) throws PortletException;



    /**
     * Delete discussion.
     *
     * @param portalControllerContext the portal controller context
     * @param form the form
     * @throws PortletException the portlet exception
     */
    void deleteDiscussion(PortalControllerContext portalControllerContext, DetailForm form) throws PortletException;








}
