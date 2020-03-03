package fr.index.cloud.ens.generator.repository;

import io.codearte.jfairy.Fairy;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import javax.naming.NamingException;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.directory.v2.model.ext.WorkspaceRole;
import org.osivia.directory.v2.service.PersonUpdateService;
import org.osivia.directory.v2.service.WorkspaceService;
import org.osivia.directory.v2.service.preferences.UserPreferencesService;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.cache.services.CacheInfo;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.directory.v2.model.Person;
import org.osivia.portal.api.windows.PortalWindow;
import org.osivia.portal.api.windows.WindowFactory;
import org.osivia.services.workspace.edition.portlet.model.WorkspaceEditionForm;
import org.osivia.services.workspace.edition.portlet.service.WorkspaceEditionService;
import org.osivia.services.workspace.portlet.model.WorkspaceCreationForm;
import org.osivia.services.workspace.portlet.service.WorkspaceCreationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import fr.index.cloud.ens.generator.model.Configuration;

import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoException;
import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoDocumentContext;
import fr.toutatice.portail.cms.nuxeo.api.services.NuxeoCommandContext;

/**
 * Generator repository implementation.
 *
 * @author Cédric Krommenhoek
 * @see GeneratorRepository
 */
@Repository
public class GeneratorRepositoryImpl implements GeneratorRepository {

    private static Logger LOGGER = Logger.getLogger(GeneratorRepositoryImpl.class);

    /** Generator properties file name. */
    private static final String PROPERTIES_FILE_NAME = "generator.properties";

    private static final String NB_USERS = "generator.nbOfUsers";

    private static final String NB_FOLDERS = "generator.nbOfRootFolers";
    private static final String NB_SUBFOLDERS = "generator.nbOfSubFolers";
    private static final String NB_SUBITEMS = "generator.nbOfSubItems";
    
    private String USERID_PREFIX = "utilisateur-";


    /** Generator properties. */
    private final Properties properties;
    @Autowired
    private PersonUpdateService personService;
    /** User preferences service. */
    @Autowired
    private UserPreferencesService userPreferencesService;

    /**
     * Constructor.
     *
     * @throws IOException
     * @throws NamingException
     */
    public GeneratorRepositoryImpl() throws IOException, NamingException {
        super();

        // Generator properties
        this.properties = new Properties();
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(PROPERTIES_FILE_NAME);
        if (inputStream != null) {
            this.properties.load(inputStream);
        } else {
            throw new FileNotFoundException(PROPERTIES_FILE_NAME);
        }


    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Configuration getConfiguration(PortalControllerContext portalControllerContext) throws PortletException {
        // Window
        PortalWindow window = WindowFactory.getWindow(portalControllerContext.getRequest());


        int nbOfUsers = NumberUtils.toInt(StringUtils.defaultIfEmpty(window.getProperty(NB_USERS), this.properties.getProperty(NB_USERS)));


        int nbOfRootFolers = NumberUtils.toInt(StringUtils.defaultIfEmpty(window.getProperty(NB_FOLDERS), this.properties.getProperty(NB_FOLDERS)));

        int nbOfSubFolers = NumberUtils.toInt(StringUtils.defaultIfEmpty(window.getProperty(NB_SUBFOLDERS), this.properties.getProperty(NB_SUBFOLDERS)));

        int nbOfSubItems = NumberUtils.toInt(StringUtils.defaultIfEmpty(window.getProperty(NB_SUBITEMS), this.properties.getProperty(NB_SUBITEMS)));

        // Configuration
        Configuration configuration = new Configuration();

        configuration.setNbOfUsers(nbOfUsers);
        configuration.setNbOfRootFolers(nbOfRootFolers);
        configuration.setNbOfSubFolers(nbOfSubFolers);
        configuration.setNbOfSubItems(nbOfSubItems);


        return configuration;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setConfiguration(PortalControllerContext portalControllerContext, Configuration configuration) throws PortletException {


        if (configuration.getNbOfUsers() == null) {
            configuration.setNbOfUsers(NumberUtils.toInt(this.properties.getProperty(NB_USERS)));

        }

        if (configuration.getNbOfRootFolers() == null) {
            configuration.setNbOfRootFolers(NumberUtils.toInt(this.properties.getProperty(NB_FOLDERS)));

        }
        if (configuration.getNbOfSubFolers() == null) {
            configuration.setNbOfSubFolers(NumberUtils.toInt(this.properties.getProperty(NB_SUBFOLDERS)));

        }
        if (configuration.getNbOfSubItems() == null) {
            configuration.setNbOfSubItems(NumberUtils.toInt(this.properties.getProperty(NB_SUBITEMS)));

        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void generate(PortalControllerContext portalControllerContext) throws PortletException {

        URL exampleFile = this.getClass().getResource("/WEB-INF/classes/example.doc");

        // Configuration
        Configuration configuration = this.getConfiguration(portalControllerContext);

        // Nuxeo controller
        NuxeoController nuxeoController = this.getNuxeoController(portalControllerContext);
        nuxeoController.setAuthType(NuxeoCommandContext.AUTH_TYPE_SUPERUSER);
        nuxeoController.setCacheType(CacheInfo.CACHE_SCOPE_PORTLET_CONTEXT);
        nuxeoController.setAsynchronousCommand(true);

        Locale locale = nuxeoController.getRequest().getLocale();
        
        PortalWindow window = WindowFactory.getWindow(portalControllerContext.getRequest());
        String termsOfService = window.getPageProperty("osivia.services.cgu.level");

        Fairy fairy = Fairy.create(locale);
        nuxeoController.executeNuxeoCommand(new GenerateCommand( configuration, personService,  termsOfService, fairy, exampleFile, USERID_PREFIX));
        
        
      

    }



    /**
     * {@inheritDoc}
     */
    @Override
    public void purge(PortalControllerContext portalControllerContext) throws PortletException {

        // Configuration
        Configuration configuration = this.getConfiguration(portalControllerContext);

        // Nuxeo controller
        NuxeoController nuxeoController = this.getNuxeoController(portalControllerContext);
        nuxeoController.setAuthType(NuxeoCommandContext.AUTH_TYPE_SUPERUSER);
        nuxeoController.setCacheType(CacheInfo.CACHE_SCOPE_PORTLET_CONTEXT);
        nuxeoController.setAsynchronousCommand(true);

        
        nuxeoController.executeNuxeoCommand(new PurgeCommand(configuration, personService, USERID_PREFIX));
   }

    /**
     * Get Nuxeo controller
     *
     * @param portalControllerContext portal controller context
     * @return Nuxeo controller
     */
    private NuxeoController getNuxeoController(PortalControllerContext portalControllerContext) {
        PortletRequest request = portalControllerContext.getRequest();
        PortletResponse response = portalControllerContext.getResponse();
        PortletContext portletContext = portalControllerContext.getPortletCtx();
        return new NuxeoController(request, response, portletContext);
    }

}
