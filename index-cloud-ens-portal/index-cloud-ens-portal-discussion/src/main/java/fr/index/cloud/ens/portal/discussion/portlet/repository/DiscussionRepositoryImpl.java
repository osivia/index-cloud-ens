package fr.index.cloud.ens.portal.discussion.portlet.repository;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.Documents;
import org.nuxeo.ecm.automation.client.model.PropertyList;
import org.osivia.directory.v2.model.preferences.UserPreferences;
import org.osivia.directory.v2.service.preferences.UserPreferencesService;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.cache.services.CacheInfo;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.directory.v2.model.Person;
import org.osivia.portal.api.directory.v2.service.PersonService;
import org.osivia.portal.api.tasks.CustomTask;
import org.osivia.portal.core.page.PageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Repository;

import fr.index.cloud.ens.portal.discussion.portlet.model.DetailForm;
import fr.index.cloud.ens.portal.discussion.portlet.model.DiscussionCreation;
import fr.index.cloud.ens.portal.discussion.portlet.model.DiscussionDocument;
import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.discussions.DiscussionHelper;
import fr.toutatice.portail.cms.nuxeo.api.services.NuxeoCommandContext;

/**
 * Discussion repository implementation.
 *
 * @author Jean-Sébastien Steux
 * @see DiscussionRepository
 */
@Repository
public class DiscussionRepositoryImpl implements DiscussionRepository {
    
    private static final String ATTR_LOCAL_PUBLICATIONS_WEB_ID_RESFRESHED = "localPublicationsWebId.resfreshed";

    private static final String TIMESTAMP_ATTRIBUTE = "discussions.publication.timeStamp";    

    /**
     * Application context.
     */
    @Autowired
    private ApplicationContext applicationContext;


    @Autowired
    private UserPreferencesService userPreferencesService;


    /**
     * Application context.
     */
    @Autowired
    private PortletContext portletContext;


    /**
     * Person service.
     */
    @Autowired
    public PersonService personService;
    
    /** The logger. */
    protected static Log logger = LogFactory.getLog(DiscussionRepositoryImpl.class);
 

    /**
     * Constructor.
     */
    public DiscussionRepositoryImpl() {
        super();
    }

    

    /**
     * Gets the local publication discussions web id.
     *
     * @param portalControllerContext the portal controller context
     * @return the local publication discussions web id
     * @throws PortalException the portal exception
     */
    @Override
    public Map<String, String> getLocalPublicationDiscussionsWebId(PortalControllerContext portalControllerContext) throws PortalException {
       // Tasks count
        
        HttpSession session = portalControllerContext.getHttpServletRequest().getSession();

        @SuppressWarnings("unchecked")
        Map<String, String> publicationsInfo = (Map<String, String>) session.getAttribute(DiscussionHelper.ATTR_LOCAL_PUBLICATION_CACHE);


        // Refresh indicator
        boolean refresh;

        if (publicationsInfo == null) {
            refresh = true;
        } else {
            // Timestamps
            long currentTimestamp = System.currentTimeMillis();
            long savedTimestamp;
            Object savedTimestampAttribute = session.getAttribute(TIMESTAMP_ATTRIBUTE);
            if ((savedTimestampAttribute != null) && (savedTimestampAttribute instanceof Long)) {
                savedTimestamp = (Long) savedTimestampAttribute;
            } else {
                savedTimestamp = 0;
            }

            // Page refresh indicator
            boolean pageRefresh = PageProperties.getProperties().isRefreshingPage();
            
            if( pageRefresh) {
                Boolean hasBeenRefreshed = (Boolean)portalControllerContext.getHttpServletRequest().getAttribute(ATTR_LOCAL_PUBLICATIONS_WEB_ID_RESFRESHED);
                // Has been reloaded since PageResfresh ?
                if( BooleanUtils.isTrue(hasBeenRefreshed))   {
                    pageRefresh = false;
                }   else    {
                    portalControllerContext.getHttpServletRequest().setAttribute(ATTR_LOCAL_PUBLICATIONS_WEB_ID_RESFRESHED, true);
                }

            }

            if (pageRefresh) {
                refresh = true;
            } else {
                refresh = ((currentTimestamp - savedTimestamp) > TimeUnit.MINUTES.toMillis(3));
            }
        }

        
        if (refresh) {
            NuxeoController nuxeoController = getNuxeoController(portalControllerContext);
            String remoteUser = portalControllerContext.getHttpServletRequest().getRemoteUser();
            Document userWorkspace = (Document) nuxeoController.executeNuxeoCommand(new GetUserProfileCommand(remoteUser));
            String rootPath = userWorkspace.getPath().substring(0, userWorkspace.getPath().lastIndexOf('/')) + "/documents";   
            
            // Search local copy of publications
            Set<String> webIds = new HashSet<String>();
            Documents publications = (Documents) nuxeoController.executeNuxeoCommand(new GetLocalPublicationsCommand(rootPath));
            for(Document document: publications) {
                String webId;
                
                // for author
                webId = document.getProperties().getString("ttc:webid");
                webIds.add(webId);               
                
                // for reader
                String sourceId = document.getProperties().getString("mtz:sourceWebId");
                if( StringUtils.isNotEmpty(sourceId))   {
                    webIds.add(webId);
                } 
                 webIds.add(sourceId);
            }
            
            // Build titles
            publicationsInfo = new ConcurrentHashMap<String, String>();
            
            if (webIds.size() > 0) {
                Documents publicationsTitle = (Documents) nuxeoController.executeNuxeoCommand(new GetPublicationsTitle(webIds));
                for (Document publicationTitle : publicationsTitle) {
                    publicationsInfo.put(publicationTitle.getProperties().getString("ttc:webid"), publicationTitle.getTitle());
                }
            }
      
            
            
            session.setAttribute(DiscussionHelper.ATTR_LOCAL_PUBLICATION_CACHE, publicationsInfo);
            session.setAttribute(TIMESTAMP_ATTRIBUTE, System.currentTimeMillis());
        }

        return publicationsInfo;
    }

    
    
    
    
    /**
     * Update title.
     *
     * @param personService the person service
     * @param currentUser the current user
     */
    private String getTitle( String currentUser, List<String> participants) {
        
        String title = null;
        
        if (participants != null) {
            // Title
            for (String name : participants) {
                if (!StringUtils.equals(name, currentUser)) {
                    Person person = personService.getPerson(name);
                    if (person != null && person.getDisplayName() != null)
                        title = person.getDisplayName();
                }
            }
        }
        
        return title;
    }

    
    
    /**
     * Gets the title.
     *
     * @param portalControllerContext the portal controller context
     * @param discussion the discussion
     * @return the title
     * @throws PortletException the portlet exception
     */
    private String getTitle( PortalControllerContext portalControllerContext, Document discussion) throws PortletException {
        try {
            
            String currentUser = portalControllerContext.getHttpServletRequest().getRemoteUser();
            
            Map<String,String> publicationInfos = getLocalPublicationDiscussionsWebId(portalControllerContext);
            String type = discussion.getProperties().getString("disc:type");
            String target = discussion.getProperties().getString("disc:target");
            
            if( StringUtils.equals(type, DiscussionDocument.TYPE_USER_COPY) && StringUtils.isNotEmpty(target)) 
                return publicationInfos.get(target);
            
            
            PropertyList participantsProp = discussion.getProperties().getList("disc:participants");
            List<String>participants = new ArrayList<>();
            for (Object name : participantsProp.list()) {
                participants.add((String) name);
            }
            if( participants.size() > 0)
                return getTitle( currentUser,  participants);
            
            return null;
            

        } catch (PortalException e) {
            throw new PortletException(e);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @throws PortletException
     */
    @Override
    public List<DiscussionDocument> getDiscussions(PortalControllerContext portalControllerContext) throws PortletException {

        List<DiscussionDocument> discussions;

        try {
            Set<String> webIds = getLocalPublicationDiscussionsWebId(portalControllerContext).keySet();
            NuxeoController nuxeoController = getNuxeoController(portalControllerContext);


            // Nuxeo command
            String remoteUser = portalControllerContext.getHttpServletRequest().getRemoteUser();
            INuxeoCommand command = this.applicationContext.getBean(GetDiscussionsCommand.class, remoteUser, webIds);
            Documents documents = (Documents) nuxeoController.executeNuxeoCommand(command);


            discussions = new ArrayList<>(documents.size());

            for (Document document : documents.list()) {

                DiscussionDocument discussion = new DiscussionDocument(getUserProperties(portalControllerContext), personService,
                        portalControllerContext.getHttpServletRequest().getRemoteUser(), document, getTitle(portalControllerContext, document));
                if (discussion != null && !discussion.isMarkAsDeleted()) {
                    discussions.add(discussion);
                }

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        return discussions;
    }

    /**
     * {@inheritDoc}
     * 
     * @throws PortletException
     */
    @Override
    public List<CustomTask> getTasks(PortalControllerContext portalControllerContext) throws PortalException, PortletException {
        
        long  begin = System.currentTimeMillis();

        
        List<CustomTask> tasks = new ArrayList<>();
        List<DiscussionDocument> discussions = getDiscussions(portalControllerContext);


        for (DiscussionDocument discussion : discussions) {

            // Last message read by current user ?
            UserPreferences userPreferences = userPreferencesService.getUserPreferences(portalControllerContext);
            Map<String, String> userProperties = userPreferences.getUserProperties();

            String propName = "discussion." + discussion.getWebId() + ".lastReadMessage.id";
            String sReadId = userProperties.get(propName);

            int readId = -1;
            if (sReadId != null)
                readId = Integer.parseInt(sReadId);


            // We check the last message which is :
            // - not from the author
            // - not deleted

            String newAuthor = null;
            for (int iMessage = discussion.getMessages().size() - 1; iMessage > readId && newAuthor == null; iMessage--) {
                String author = discussion.getMessages().get(iMessage).getAuthor();
                if (author != null && !author.equals(portalControllerContext.getHttpServletRequest().getRemoteUser())) {
                    if (!discussion.getMessages().get(iMessage).isDeleted()) {
                        newAuthor = author;
                    }
                }
            }

            if (newAuthor != null) {
                Map<String, String> properties = new HashMap<String, String>();
                properties.put("author", newAuthor);

                tasks.add(new CustomTask(discussion.getDocument().getTitle(), discussion.getDocument(), properties));
            }


        }
        
        if( logger.isDebugEnabled())    {
            long end = System.currentTimeMillis();
            
            logger.debug("getTask : elapsed time = " + (end- begin) + " ms.");
        }

        return tasks;
    }


    private NuxeoController getNuxeoController(PortalControllerContext portalControllerContext) {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portletContext);
        nuxeoController.setServletRequest(portalControllerContext.getHttpServletRequest());
        nuxeoController.setAuthType(NuxeoCommandContext.AUTH_TYPE_SUPERUSER);
        nuxeoController.setCacheType(CacheInfo.CACHE_SCOPE_NONE);
        return nuxeoController;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void markAsDeleted(PortalControllerContext portalControllerContext, List<DiscussionDocument> documents) throws PortletException {

        try {
            UserPreferences userPreferences = userPreferencesService.getUserPreferences(portalControllerContext);
            Map<String, String> userProperties = userPreferences.getUserProperties();

            for (DiscussionDocument document : documents) {
                String propName = DiscussionDocument.getDeletedKey(document.getWebId());
                userProperties.put(propName, Integer.toString(document.getMessages().size() - 1));
            }

            userPreferencesService.saveUserPreferences(portalControllerContext, userPreferences);
            userPreferences.setUpdated(true);
        } catch (PortalException e) {
            throw new PortletException(e);
        }
    }


    @Override
    public void createDiscussion(PortalControllerContext portalControllerContext, DiscussionCreation discution) throws PortletException {
        NuxeoController nuxeoController = getNuxeoController(portalControllerContext);

        // Nuxeo command
        INuxeoCommand command = this.applicationContext.getBean(CreateDiscussionCommand.class, discution);
        nuxeoController.executeNuxeoCommand(command);
    }


    @Override
    public void addMessage(PortalControllerContext portalControllerContext, DetailForm form) throws PortletException {
        NuxeoController nuxeoController = getNuxeoController(portalControllerContext);

        // Nuxeo command
        INuxeoCommand command = this.applicationContext.getBean(AddMessageCommand.class, form);
        nuxeoController.executeNuxeoCommand(command);

        // reload cache
        nuxeoController.getDocumentContext(form.getId()).reload();
    }


    @Override
    public void deleteMessage(PortalControllerContext portalControllerContext, DetailForm form, String messageId) throws PortletException {
        NuxeoController nuxeoController = getNuxeoController(portalControllerContext);

        // Nuxeo command
        INuxeoCommand command = this.applicationContext.getBean(DeleteMessageCommand.class, form, messageId);
        nuxeoController.executeNuxeoCommand(command);

        // reload cache
        nuxeoController.getDocumentContext(form.getId()).reload();
    }


    /**
     * {@inheritDoc}
     * 
     * @throws PortletException
     */
    @Override
    public DiscussionDocument getDiscussion(PortalControllerContext portalControllerContext, String id) throws PortletException {

        NuxeoController nuxeoController = getNuxeoController(portalControllerContext);
        Document document = nuxeoController.getDocumentContext(id).getDocument();

        String remoteUser = portalControllerContext.getRequest().getRemoteUser();

        DiscussionDocument discussion = new DiscussionDocument(getUserProperties(portalControllerContext), personService, remoteUser, document, getTitle(portalControllerContext, document));

        return discussion;
    }


    /**
     * Gets the user properties.
     *
     * @param portalControllerContext the portal controller context
     * @return the user properties
     * @throws PortletException the portlet exception
     */
    private Map<String, String> getUserProperties(PortalControllerContext portalControllerContext) throws PortletException {
        UserPreferences userPreferences;
        try {
            userPreferences = userPreferencesService.getUserPreferences(portalControllerContext);
        } catch (PortalException e) {
            throw new PortletException(e);
        }
        return userPreferences.getUserProperties();
    }


    /**
     * {@inheritDoc}
     * 
     * @throws PortletException
     */
    @Override
    public DiscussionDocument getDiscussionByParticipant(PortalControllerContext portalControllerContext, String participant) throws PortletException {
        NuxeoController nuxeoController = getNuxeoController(portalControllerContext);


        String remoteUser = portalControllerContext.getRequest().getRemoteUser();
        // Nuxeo command

        INuxeoCommand command = this.applicationContext.getBean(GetDiscussionsByParticipantCommand.class, remoteUser, participant);
        Documents docs = (Documents) nuxeoController.executeNuxeoCommand(command);

        DiscussionDocument discussion;

        if (docs.size() == 0) {
            List<String> participants = new ArrayList<>();
            participants.add(participant);
            participants.add(portalControllerContext.getRequest().getRemoteUser());
            
            discussion = new DiscussionDocument(getUserProperties(portalControllerContext), personService, remoteUser, getTitle( remoteUser,  participants), participants);
        } else if (docs.size() > 1) {
            throw new PortletException("more than one discussion (" + remoteUser + "," + participant+ ")");
        } else
            discussion = new DiscussionDocument(getUserProperties(portalControllerContext), personService, remoteUser, docs.get(0),  getTitle(portalControllerContext, docs.get(0)));

        return discussion;
    }


    
    /**
     * {@inheritDoc}
     * 
     * @throws PortletException
     */
    @Override
    public DiscussionDocument getDiscussionByPublication(PortalControllerContext portalControllerContext, String publicationId) throws PortletException {
        NuxeoController nuxeoController = getNuxeoController(portalControllerContext);

        String remoteUser = portalControllerContext.getRequest().getRemoteUser();
        
        // Nuxeo command
        INuxeoCommand command = this.applicationContext.getBean(GetDiscussionsByPublicationCommand.class, publicationId);
        Documents docs = (Documents) nuxeoController.executeNuxeoCommand(command);


        Map<String, String> titles;
        
        try {
            titles = getLocalPublicationDiscussionsWebId(portalControllerContext);
        } catch (PortalException e) {
           throw new PortletException(e);
        }
        
        String title = titles.get(publicationId);
        
        DiscussionDocument discussion;

        if (docs.size() == 0) {
              discussion = new DiscussionDocument(getUserProperties(portalControllerContext), personService, remoteUser, DiscussionDocument.TYPE_USER_COPY, publicationId, title);
        } else if (docs.size() > 1) {
            throw new PortletException("more than one discussion by publication (" + publicationId +")");
        } else
            discussion = new DiscussionDocument(getUserProperties(portalControllerContext), personService, remoteUser, docs.get(0), title);

        return discussion;
    }
    
    
    
    @Override
    public void checkUserReadPreference(PortalControllerContext portalControllerContext, String documentId, int nbMessages) throws PortletException {


        try {
            UserPreferences userPreferences = userPreferencesService.getUserPreferences(portalControllerContext);
            Map<String, String> userProperties = userPreferences.getUserProperties();

            String propName = "discussion." + documentId + ".lastReadMessage.id";
            String readId = userProperties.get(propName);

            int lastMessageId = nbMessages - 1;

            if (readId == null || Integer.parseInt(readId) < lastMessageId) {
                userProperties.put(propName, Integer.toString(lastMessageId));

                userPreferencesService.saveUserPreferences(portalControllerContext, userPreferences);
                userPreferences.setUpdated(true);
            }

        } catch (PortalException e) {
            throw new PortletException(e);
        }
    }


}
