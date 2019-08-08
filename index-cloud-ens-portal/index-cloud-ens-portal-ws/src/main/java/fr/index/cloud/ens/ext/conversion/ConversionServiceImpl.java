package fr.index.cloud.ens.ext.conversion;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.portlet.PortletException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osivia.portal.api.cache.services.CacheInfo;
import org.osivia.portal.api.context.PortalControllerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.index.cloud.ens.ext.etb.EtablissementService;
import fr.index.cloud.ens.ws.DriveRestController;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.services.NuxeoCommandContext;

@Service
public class ConversionServiceImpl implements IConversionService {

    @Autowired
    ConversionRepository conversionRepository;

    /** The patch executor. */
    ExecutorService patchExecutor = null;

    /**
     * Post-construct.
     *
     * 
     */
    @PostConstruct
    public void postConstruct() {
        patchExecutor = Executors.newSingleThreadExecutor();
    }


    /**
     * Pre-destroy.
     */
    @PreDestroy
    public void preDestroy() {
        patchExecutor.shutdown();
    }


    /** portal Logger. */
    private static final Log logger = LogFactory.getLog(ConversionServiceImpl.class);


    /** conversion Logger. */
    protected static final Log conversionlogger = LogFactory.getLog("PORTAL_CONVERSION");

    private static Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");


    /**
     * Gets the nuxeo controller.
     *
     * @return the nuxeo controller
     */
    private NuxeoController getNuxeoController() {
        NuxeoController nuxeoController = new NuxeoController(DriveRestController.portletContext);
        nuxeoController.setAuthType(NuxeoCommandContext.AUTH_TYPE_SUPERUSER);
        nuxeoController.setCacheType(CacheInfo.CACHE_SCOPE_NONE);
        return nuxeoController;
    }


    @Override
    public String convert(PortalControllerContext ctx, String docId, String clientId, String field, String key, String label) {

        String resultCode = null;

        if( StringUtils.isNotEmpty(key) || StringUtils.isNotEmpty(label)) {
            try {
                List<ConversionRecord> records = conversionRepository.getRecords(ctx);
    
                if (StringUtils.startsWith(clientId, EtablissementService.PRONOTE_CLIENT_PREFIX)) {
    
                    String etablissement = clientId.substring(EtablissementService.PRONOTE_CLIENT_PREFIX.length());
    
                    resultCode = convertBatch(records, docId, etablissement, field, key, label);
                }
    
            } catch (PortletException e) {
                logger.error("Technical error in conversion tool ", e);
            }
        }

        return resultCode;
    }


    /**
     * Convert batch (outside request)
     *
     * @param records the records
     * @param docId the doc id
     * @param etablissementId the etablissement id
     * @param field the field
     * @param key the key
     * @param label the label
     * @return the string
     */
    protected String convertBatch(List<ConversionRecord> records, String docId, String etablissementId, String field, String key, String label) {

        String resultCode = null;

        // No accents + uppercase
        if (label != null) {
            String strTemp = Normalizer.normalize(label, Normalizer.Form.NFD);
            label = pattern.matcher(strTemp).replaceAll("").toUpperCase();
        }

        resultCode = convertInternal(records, etablissementId, field, key, label);

        conversionlogger.info(dateFormat.format(new Date()) + ";" + docId + ";" + etablissementId + ";" + field + ";" + key + ";" + label + ";"
                + (resultCode != null ? resultCode : ""));

        return resultCode;
    }


    /**
     * Convert internal.
     *
     * @param records the records
     * @param etablissement the etablissement
     * @param field the field
     * @param key the key
     * @param label the label
     * @return the string
     */
    private String convertInternal(List<ConversionRecord> records, String etablissement, String field, String key, String label) {
        String resultCode = null;

        // ETB + CODE
        resultCode = check(records, etablissement, field, key, label, true, true, false);
        // ETB + LABEL
        if (resultCode == null)
            resultCode = check(records, etablissement, field, key, label, true, false, true);

        // NO ETB + CODE
        if (resultCode == null)
            resultCode = check(records, etablissement, field, key, label, false, true, false);
        // NO ETB + LABEL
        if (resultCode == null)
            resultCode = check(records, etablissement, field, key, label, false, false, true);
        return resultCode;
    }


    /**
     * Check.
     *
     * @param records the records
     * @param etablissement the etablissement
     * @param field the field
     * @param key the key
     * @param label the label
     * @param includeEtb the include etb
     * @param checkCode the check code
     * @param checkLabel the check label
     * @return the string
     */
    private String check(List<ConversionRecord> records, String etablissement, String field, String key, String label, boolean includeEtb, boolean checkCode,
            boolean checkLabel) {

        String resultCode = null;

        // Check for code
        for (ConversionRecord record : records) {
            if ((!includeEtb && StringUtils.isEmpty(record.getEtablissement()))
                    || (includeEtb && StringUtils.equals(record.getEtablissement(), etablissement))) {

                if ((checkCode && StringUtils.isNotBlank(key) && StringUtils.equals(record.getPublishCode(), key))
                        || (checkLabel && StringUtils.isNotBlank(label) && StringUtils.equals(record.getPublishLabel(), label))) {

                    resultCode = record.getResultCode();
                    break;
                }
            }
        }

        return resultCode;
    }

    @Override
    public void applyPatch(PortalControllerContext ctx, File file) throws FileNotFoundException, IOException, MalformedLineException, PortletException {

        List<PatchRecord> patchRecords = checkPatch(ctx, file);
        List<ConversionRecord> conversionRecords = conversionRepository.getRecords(ctx);

        patchExecutor.submit(new PatchCallable(this, getNuxeoController(), patchRecords, conversionRecords));

    }


    @Override
    public List<PatchRecord> checkPatch(PortalControllerContext ctx, File file) throws FileNotFoundException, IOException, MalformedLineException {

        return conversionRepository.extractRecordsFromPatch(ctx, file);
    }


}
