package fr.index.cloud.ens.antivirus;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.portlet.PortletContext;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.Documents;
import org.osivia.portal.api.batch.AbstractBatch;
import org.osivia.portal.api.cache.services.CacheInfo;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.internationalization.IInternationalizationService;
import org.osivia.portal.api.locator.Locator;

import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.forms.IFormsService;
import fr.toutatice.portail.cms.nuxeo.api.services.NuxeoCommandContext;
import fr.toutatice.portail.cms.nuxeo.api.services.NuxeoServiceFactory;

/**
 * The Class AntivirusBatch.
 * 
 * Is executed periodically and scan the files which are in quarantine.
 */

public class AntivirusBatch extends AbstractBatch {

    /** Portlet context. */
    private static PortletContext portletContext;

    /** The logger. */
    protected static Log log = LogFactory.getLog(AntivirusBatch.class);

    /** The scheduler definition property name */
    private static final String ANTIVIRUS_CRON = "index.antivirus.batch.cron";

    /** The Constant ERROR_VIRUS_FOUND. */
    public static final int ERROR_VIRUS_FOUND = 1;

    /** The bundle . */
    private Bundle bundle;
    
    
    /** The form service. */
    private IFormsService formService;

    /** model identifier. */
    public static final String MODEL_ID = IFormsService.FORMS_WEB_ID_PREFIX + "notification";

    private final boolean enabled;

    public AntivirusBatch() {
        if (StringUtils.isNotEmpty(System.getProperty(ANTIVIRUS_CRON))) {
            enabled = true;
        } else
            enabled = false;
    }


    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public boolean isRunningOnMasterOnly() {
        return true;
    }

    @Override
    public String getJobScheduling() {
        // exemple "0/20 * * * * ?";
        return System.getProperty(ANTIVIRUS_CRON);
    }


    /**
     * Generic french bundle
     * 
     * @return
     */
    private Bundle getBundle() {

        if (bundle == null) {
            // Internationalization bundle factory
            IInternationalizationService internationalizationService = Locator.findMBean(IInternationalizationService.class,
                    IInternationalizationService.MBEAN_NAME);
            IBundleFactory bundleFactory = internationalizationService.getBundleFactory(this.getClass().getClassLoader());
            // Internationalization bundle
            bundle = bundleFactory.getBundle(Locale.FRENCH);
        }
        
        return bundle;

    }



    private NuxeoController getNuxeoController() {
        NuxeoController nuxeoController = new NuxeoController(portletContext);
        nuxeoController.setAuthType(NuxeoCommandContext.AUTH_TYPE_SUPERUSER);
        nuxeoController.setCacheType(CacheInfo.CACHE_SCOPE_NONE);
        return nuxeoController;
    }

    @Override
    public void execute(Map<String, Object> parameters) {

        log.info("Starting quarantine batch.");

        NuxeoController ctx = getNuxeoController();

        Documents docs = (Documents) ctx.executeNuxeoCommand(new GetQuaratineFilesCommand());
        
  
        if (log.isDebugEnabled()) {
            log.debug(docs.size() + " files are in quarantine");
        }

        for (Document doc : docs) {

            int errorCode = (Integer) ctx.executeNuxeoCommand(new ScanFileCommand(doc));

            if (log.isDebugEnabled()) {
                log.debug(doc.getPath() + " : " + errorCode);
            }

            if (errorCode == ERROR_VIRUS_FOUND) {
                log.warn("File " + doc.getPath() + " contains a virus . It is deleted.");
                ctx.executeNuxeoCommand(new DeleteFileCommand(doc)); 
                
                notifyAuthor(ctx, doc);
            }
        }

        log.info("Ending quarantine batch.");
    }


    private void notifyAuthor(NuxeoController ctx, Document doc) {


        /* Send notification to the owner */

        String lastContributor = doc.getProperties().getString("dc:lastContributor");
        String title = doc.getProperties().getString("dc:title");
         
        PortalControllerContext portalControllerContext = new PortalControllerContext(AntivirusBatch.portletContext);

        Map<String, String> variables = new HashMap<>();
        variables.put("userId", lastContributor);
        variables.put("message",  getBundle().getString("ERROR_MESSAGE_VIRUS_FOUND_BATCH", title));


        try {
            variables = NuxeoServiceFactory.getFormsService().start(portalControllerContext, MODEL_ID, variables);
        } catch (Exception e) {
            System.out.println(e);
        }
    }


    public void setPortletContext(PortletContext portletContext) {
        AntivirusBatch.portletContext = portletContext;
    }


}
