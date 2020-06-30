package fr.index.cloud.ens.portal.discussion.portlet.repository;


import java.util.ArrayList;
import java.util.Date;
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
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.tasks.CustomTask;
import org.osivia.portal.core.page.PageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Repository;

import fr.index.cloud.ens.portal.discussion.portlet.model.DetailForm;
import fr.index.cloud.ens.portal.discussion.portlet.model.DiscussionCreation;
import fr.index.cloud.ens.portal.discussion.portlet.model.DiscussionDocument;
import fr.index.cloud.ens.portal.discussion.portlet.model.Options;
import fr.index.cloud.ens.portal.discussion.portlet.model.PublicationUse;
import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.discussions.DiscussionHelper;
import fr.toutatice.portail.cms.nuxeo.api.domain.DocumentDTO;
import fr.toutatice.portail.cms.nuxeo.api.services.NuxeoCommandContext;
import fr.toutatice.portail.cms.nuxeo.api.services.dao.DocumentDAO;

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


    /**
     * Document DAO.
     */
    @Autowired
    private DocumentDAO documentDao;

    
    
    /**
     * Internationalization bundle factory.
     */
    @Autowired
    private IBundleFactory bundleFactory;
    

    /** The logger. */
    protected static Log logger = LogFactory.getLog(DiscussionRepositoryImpl.class);


    /**
     * Constructor.
     */
    public DiscussionRepositoryImpl() {
        super();
    }


    /**
     * Save web id and older date.
     *
     * @param webIds the web ids
     * @param webId the web id
     * @param createdDate the created date
     */
    private void saveWebIdAndOlderCopyDate( Map<String, Date> webIds, String webId, Date createdDate)   {
        
        if( createdDate == null)    {
            webIds.put(webId, null);
        } else  {
            if( webIds.get(webId) == null)  {
                webIds.put(webId, createdDate);
            }   else    {
                if( createdDate.before(webIds.get(webId)))  {
                    webIds.put(webId, createdDate);
                }
            }
        }
    }
    
    /**
     * Gets the local publication discussions web id.
     *
     * @param portalControllerContext the portal controller context
     * @return the local publication discussions web id
     * @throws PortalException the portal exception
     */
    @Override
    public Map<String, PublicationUse> getLocalPublicationDiscussionsWebId(PortalControllerContext portalControllerContext, String adminModeId) throws PortalException {
        // Tasks count

        HttpSession session = portalControllerContext.getHttpServletRequest().getSession();

        @SuppressWarnings("unchecked")
        Map<String, PublicationUse> publicationsInfo = (Map<String, PublicationUse>) session.getAttribute(getCacheName(adminModeId));


        // Refresh indicator
        boolean refresh;

        if (publicationsInfo == null) {
            refresh = true;
        } else {
            // Check class loader
            boolean checkClassPath = true;
            try {
                for (PublicationUse values : publicationsInfo.values()) {

                }
            } catch (ClassCastException e) {
                checkClassPath = false;
            }
            
            if( ! checkClassPath)  {
                refresh = true;
            }   else    {
            

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

                if (pageRefresh) {
                    Boolean hasBeenRefreshed = (Boolean) portalControllerContext.getHttpServletRequest()
                            .getAttribute(ATTR_LOCAL_PUBLICATIONS_WEB_ID_RESFRESHED);
                    // Has been reloaded since PageResfresh ?
                    if (BooleanUtils.isTrue(hasBeenRefreshed)) {
                        pageRefresh = false;
                    } else {
                        portalControllerContext.getHttpServletRequest().setAttribute(ATTR_LOCAL_PUBLICATIONS_WEB_ID_RESFRESHED, true);
                    }

                }

                if (pageRefresh) {
                    refresh = true;
                } else {
                    refresh = ((currentTimestamp - savedTimestamp) > TimeUnit.MINUTES.toMillis(3));
                }
            }
        }


        if (refresh) {
            NuxeoController nuxeoController = getNuxeoController(portalControllerContext);
            String remoteUser = portalControllerContext.getHttpServletRequest().getRemoteUser();
            Document userWorkspace = (Document) nuxeoController.executeNuxeoCommand(new GetUserProfileCommand(remoteUser));
            String rootPath = userWorkspace.getPath().substring(0, userWorkspace.getPath().lastIndexOf('/')) + "/documents";

            // Search local copy of publications
            Map<String, Date> webIds = new HashMap<String, Date>();


            Documents publications = (Documents) nuxeoController.executeNuxeoCommand(new GetLocalPublicationsCommand(rootPath));
            for (Document document : publications) {
                String webId;

                // for author
                webId = document.getProperties().getString("ttc:webid");
                webIds.put(webId, null);

                // for reader
                String sourceId = document.getProperties().getString("mtz:sourceWebId");
                if (StringUtils.isNotEmpty(sourceId)) {
                    saveWebIdAndOlderCopyDate(webIds, sourceId, document.getProperties().getDate("dc:created"));
                }
   
            }

            if (adminModeId != null)
                webIds.put(adminModeId, null);

            // Build titles
            publicationsInfo = new ConcurrentHashMap<String, PublicationUse>();

            if (webIds.size() > 0) {
                Documents publicationsTitle = (Documents) nuxeoController.executeNuxeoCommand(new GetPublicationsTitle(webIds.keySet()));
                for (Document publicationTitle : publicationsTitle) {
                    DocumentDTO documentDto = this.documentDao.toDTO(publicationTitle);
                    
                    PublicationUse use = new PublicationUse(documentDto.getDisplayTitle());
                    
                    String copiedWebId = publicationTitle.getProperties().getString("ttc:webid");
                    if( copiedWebId != null)    {
                        if( webIds.get(copiedWebId) != null)    {
                            use.setLastRecopy(webIds.get(copiedWebId));
                        }
                    }

                    publicationsInfo.put(publicationTitle.getProperties().getString("ttc:webid"), use);
                }
            }


            session.setAttribute(getCacheName(adminModeId), publicationsInfo);
            session.setAttribute(TIMESTAMP_ATTRIBUTE, System.currentTimeMillis());
        }

        return publicationsInfo;
    }


    private String getCacheName(String adminModeId) {
        return DiscussionHelper.ATTR_LOCAL_PUBLICATION_CACHE + "/" + adminModeId;
    }


    /**
     * Update title.
     *
     * @param personService the person service
     * @param currentUser the current user
     */
    private String getTitleByParticipants(String currentUser, List<String> participants) {

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
    private String getTitle(PortalControllerContext portalControllerContext, Document discussion, boolean adminMode) throws PortletException {
        try {

            String currentUser = portalControllerContext.getHttpServletRequest().getRemoteUser();
            String target = discussion.getProperties().getString("disc:target");
            
            String title=null;

            /* title by publication */
            if (StringUtils.isNotEmpty(target)) {
                Map<String, PublicationUse> publicationInfos;
                if (adminMode)
                    publicationInfos = getLocalPublicationDiscussionsWebId(portalControllerContext, target);
                else
                    publicationInfos = getLocalPublicationDiscussionsWebId(portalControllerContext, null);

                if( publicationInfos.get(target) != null)   {
                    title = publicationInfos.get(target).getTitle();
                }
           }

            /* tile by participant */
            
            if( title == null)  {
                PropertyList participantsProp = discussion.getProperties().getList("disc:participants");
                List<String> participants = new ArrayList<>();
                for (Object name : participantsProp.list()) {
                    participants.add((String) name);
                }

                if (participants.size() > 0)
                    title = getTitleByParticipants(currentUser, participants);
            }
            
            
            // Internationalization bundle
            Bundle bundle = this.bundleFactory.getBundle(portalControllerContext.getHttpServletRequest().getLocale());
            
            // Généric title
            if (title == null)
                title = bundle.getString("DISCUSSION_DISCUSSIONS_TITLE_DETAIL");
            
            // Groupe suffix
            String type = discussion.getProperties().getString("disc:type");
            if (StringUtils.equals(type, DiscussionDocument.TYPE_USER_COPY))
                title = title + " (" + bundle.getString("DISCUSSION_GROUP") + ")";
            

            return title;


        } catch (PortalException e) {
            throw new PortletException(e);
        }
    }


    private String getTitle(PortalControllerContext portalControllerContext, Document discussion) throws PortletException {
        return getTitle(portalControllerContext, discussion, false);
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
             Map<String, PublicationUse> webIds = getLocalPublicationDiscussionsWebId(portalControllerContext, null);
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

        long begin = System.currentTimeMillis();


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
            String lastMessage = null;

            for (int iMessage = discussion.getMessages().size() - 1; iMessage > readId && newAuthor == null; iMessage--) {
                String author = discussion.getMessages().get(iMessage).getAuthor();
                if (author != null && !author.equals(portalControllerContext.getHttpServletRequest().getRemoteUser())) {
                    if (!discussion.getMessages().get(iMessage).isDeleted()) {
                        newAuthor = author;
                        lastMessage = discussion.getMessages().get(iMessage).getContent();
                    }
                }
            }

            if (newAuthor != null) {
                Map<String, String> properties = new HashMap<String, String>();
                properties.put("author", newAuthor);
                properties.put("message", lastMessage);
                properties.put("pubTitle", discussion.getTitle());
                properties.put("pubTarget", discussion.getTarget());
                properties.put("pubType", discussion.getType());

                tasks.add(new CustomTask(discussion.getDocument().getTitle(), discussion.getDocument(), properties));
            }


        }

        if (logger.isDebugEnabled()) {
            long end = System.currentTimeMillis();

            logger.debug("getTask : elapsed time = " + (end - begin) + " ms.");
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
    public Document createDiscussion(PortalControllerContext portalControllerContext, DiscussionCreation discution) throws PortletException {
        NuxeoController nuxeoController = getNuxeoController(portalControllerContext);

        // Nuxeo command
        INuxeoCommand command = this.applicationContext.getBean(CreateDiscussionCommand.class, discution);
        return (Document) nuxeoController.executeNuxeoCommand(command);
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
    public DiscussionDocument getDiscussion(PortalControllerContext portalControllerContext, DetailForm form) throws PortletException {

        NuxeoController nuxeoController = getNuxeoController(portalControllerContext);
        Document document = nuxeoController.getDocumentContext(form.getId()).getDocument();

        String remoteUser = portalControllerContext.getRequest().getRemoteUser();

        String title;
        if (Options.MODE_ADMIN.equals(form.getOptions().getMode()))
            title = getTitle(portalControllerContext, document, true);
        else
            title = getTitle(portalControllerContext, document);

        DiscussionDocument discussion = new DiscussionDocument(getUserProperties(portalControllerContext), personService, remoteUser, document, title);

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
    public DiscussionDocument getDiscussionByParticipant(PortalControllerContext portalControllerContext, String participant, String publicationId,
            boolean joinConversation) throws PortletException {
        NuxeoController nuxeoController = getNuxeoController(portalControllerContext);


        String remoteUser = portalControllerContext.getRequest().getRemoteUser();


        DiscussionDocument discussion;

        List<String> participants = new ArrayList<>();
        participants.add(participant);
        participants.add(portalControllerContext.getRequest().getRemoteUser());

        String title;

        if (publicationId != null) {

            title = getPublicationTitle(portalControllerContext, publicationId);
        } else {
            title = getTitleByParticipants(remoteUser, participants);
        }
        getPublicationTitle(portalControllerContext, publicationId);


        if (joinConversation == false) {
            discussion = createDiscussionByParticipant(portalControllerContext, participants, remoteUser, publicationId, title);
        } else {

            // Nuxeo command

            INuxeoCommand command = this.applicationContext.getBean(GetDiscussionsByParticipantCommand.class, remoteUser, participant);
            Documents docs = (Documents) nuxeoController.executeNuxeoCommand(command);


            if (docs.size() == 0) {
                discussion = createDiscussionByParticipant(portalControllerContext, participants, remoteUser, publicationId, title);
            } else if (docs.size() > 1) {
                throw new PortletException("more than one discussion (" + remoteUser + "," + participant + ")");
            } else
                discussion = new DiscussionDocument(getUserProperties(portalControllerContext), personService, remoteUser, docs.get(0),
                        getTitle(portalControllerContext, docs.get(0)));
        }

        return discussion;
    }


    /**
     * Creates the discussion by participant.
     *
     * @param portalControllerContext the portal controller context
     * @param participant the participant
     * @param remoteUser the remote user
     * @return the discussion document
     * @throws PortletException the portlet exception
     */
    private DiscussionDocument createDiscussionByParticipant(PortalControllerContext portalControllerContext, List<String> participants, String remoteUser,
            String publicationId, String title) throws PortletException {
        DiscussionDocument discussion;

        discussion = new DiscussionDocument(getUserProperties(portalControllerContext), personService, remoteUser, title, participants, publicationId);
        return discussion;
    }


    /**
     * {@inheritDoc}
     * 
     * @throws PortletException
     */
    @Override
    public DiscussionDocument getDiscussionByPublication(PortalControllerContext portalControllerContext, String publicationId, boolean joinConversation)
            throws PortletException {
        NuxeoController nuxeoController = getNuxeoController(portalControllerContext);

        String remoteUser = portalControllerContext.getRequest().getRemoteUser();

        // Nuxeo command
        INuxeoCommand command = this.applicationContext.getBean(GetDiscussionsByPublicationCommand.class, publicationId);
        Documents docs = (Documents) nuxeoController.executeNuxeoCommand(command);


        String title = getPublicationTitle(portalControllerContext, publicationId);

        DiscussionDocument discussion;
        if (joinConversation == false) {
            discussion = creationDiscussionByPublication(portalControllerContext, publicationId, remoteUser, title);
        } else {


            if (docs.size() == 0) {
                discussion = creationDiscussionByPublication(portalControllerContext, publicationId, remoteUser, title);
            } else if (docs.size() > 1) {
                throw new PortletException("more than one discussion by publication (" + publicationId + ")");
            } else
                discussion = new DiscussionDocument(getUserProperties(portalControllerContext), personService, remoteUser, docs.get(0), title);
        }

        return discussion;
    }


    private String getPublicationTitle(PortalControllerContext portalControllerContext, String publicationId) throws PortletException {
        Map<String, PublicationUse> publications;
        try {


            publications = getLocalPublicationDiscussionsWebId(portalControllerContext, null);
        } catch (PortalException e) {
            throw new PortletException(e);
        }

        PublicationUse pubUse = publications.get(publicationId);

        String title;
        if (pubUse != null)
            title = pubUse.getTitle();
        else
            title = null;
        return title;
    }


    /**
     * Creation discussion by publication.
     *
     * @param portalControllerContext the portal controller context
     * @param publicationId the publication id
     * @param remoteUser the remote user
     * @param title the title
     * @return the discussion document
     * @throws PortletException the portlet exception
     */
    private DiscussionDocument creationDiscussionByPublication(PortalControllerContext portalControllerContext, String publicationId, String remoteUser,
            String title) throws PortletException {
        DiscussionDocument discussion;
        discussion = new DiscussionDocument(getUserProperties(portalControllerContext), personService, remoteUser, DiscussionDocument.TYPE_USER_COPY,
                publicationId, title);
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
