package fr.index.cloud.ens.ext.etb;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;
import javax.portlet.PortletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.cache.services.CacheInfo;
import org.osivia.portal.api.cache.services.ICacheService;
import org.osivia.portal.api.status.IStatusService;
import org.osivia.portal.api.status.UnavailableServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;

import fr.index.cloud.ens.ws.DriveRestController;

/**
 * Service appelant et mémorisant les étalissements PRONOTE
 * 
 * @author Jean-Sébastien
 */


@Service
public class EtablissementService implements IEtablissementService {

    @Autowired
    ICacheService cacheService;

    @Autowired
    IStatusService statusService;

    public static PortletContext portletContext;

    /** Logger. */
    private static final Log logger = LogFactory.getLog(EtablissementService.class);

    /** Préfixe cache. */
    private static String CACHE_KEY_PREFIX = "ETB_";

    public static long CACHE_TIMEOUT = 3600 * 1000;

    
    public String pronoteEtablissementCheckUrl;
    public String pronoteEtablissementUrl;
    public long pronoteCacheTimeout = CACHE_TIMEOUT;

 
    private static Map<String, EtablissementBean> cacheEtbs = new ConcurrentHashMap<>();
    
    @PostConstruct
    private void postConstruct() {
        pronoteEtablissementUrl = System.getProperty("pronote.etablissement.url");
        if( pronoteEtablissementUrl == null)
            logger.warn("pronote.etablissement.url is not specified in properties");
        pronoteEtablissementCheckUrl = System.getProperty("pronote.etablissement.checkUrl");
        if( pronoteEtablissementCheckUrl == null)
            logger.warn("pronote.etablissement.checkUrl is not specified in properties");
    }

    /*
     * (non-Javadoc)
     * 
     * @see fr.index.cloud.ens.ext.etb.IEtablissementService#getEtablissement(java.lang.String)
     */
    

    public EtablissementBean getEtablissement(String code) {

        EtablissementBean etablissement = null;
        String cacheKey = CACHE_KEY_PREFIX + code;

        CacheInfo cacheInfos = new CacheInfo(cacheKey, CacheInfo.CACHE_SCOPE_PORTLET_CONTEXT, new EtablissementInvoker(pronoteEtablissementUrl, code), null, portletContext, false);

        if (statusService.isReady(pronoteEtablissementCheckUrl)) {
            try {
                cacheInfos.setExpirationDelay(CACHE_TIMEOUT);
                etablissement = (EtablissementBean) cacheService.getCache(cacheInfos);
            } catch (HttpClientErrorException e) {
                statusService.notifyError(pronoteEtablissementCheckUrl, new UnavailableServer(e.getStatusText()));
                logger.error("can't retrieve etablissement #" + code, e);
            } catch (Exception e) {
                logger.error("can't retrieve etablissement #" + code, e);
            }
        }

        // Applicative cache (if service cache has expired and pronote is unavailable)
        if (etablissement != null) {
            EtablissementBean oldEtb = cacheEtbs.get(code);
            if (oldEtb == null || !etablissement.equals(oldEtb)) {
                cacheEtbs.put(code, etablissement);
            }
        }
        return cacheEtbs.get(code);

    }


}
