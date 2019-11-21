package fr.index.cloud.ens.portal.discussion.portlet.service;

import org.dom4j.Element;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.panels.PanelPlayer;

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
     * Task identifier.
     */
    String TASK_ID = "TRASH";


    /**
     * Get trash form.
     *
     * @param portalControllerContext portal controller context
     * @return trash form
     */
    DiscussionsForm getDiscussionsForm(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Sort trash form.
     *
     * @param portalControllerContext portal controller context
     * @param form                    trash form
     * @param sort                    sort property
     * @param alt                     alternative sort indicator
     */
    void sort(PortalControllerContext portalControllerContext, DiscussionsForm form, DiscussionsFormSort sort, boolean alt) throws PortletException;


    /**
     * Empty trash.
     *
     * @param portalControllerContext portal controller context
     * @param form                    trash form
     */
    void emptyTrash(PortalControllerContext portalControllerContext, DiscussionsForm form) throws PortletException;

    /**
     * Create new Discussion
     *
     * @param portalControllerContext portal controller context
     * @param form                    trash form
     */
    void createDiscussion(PortalControllerContext portalControllerContext, DiscussionCreation discution) throws PortletException;

 

    /**
     * Delete selected items.
     *
     * @param portalControllerContext portal controller context
     * @param form                    trash form
     * @param identifiers             selection identifiers
     */
    void delete(PortalControllerContext portalControllerContext, DiscussionsForm form, String[] identifiers) throws PortletException;


  

    /**
     * Get toolbar DOM element.
     *
     * @param portalControllerContext portal controller context
     * @param indexes                 selected row indexes
     * @return DOM element
     */
    Element getToolbar(PortalControllerContext portalControllerContext, List<String> indexes) throws PortletException, IOException;




}
