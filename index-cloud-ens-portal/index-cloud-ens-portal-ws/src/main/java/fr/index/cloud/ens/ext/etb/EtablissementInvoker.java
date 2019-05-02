package fr.index.cloud.ens.ext.etb;

import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.cache.services.IServiceInvoker;
import org.springframework.web.client.RestTemplate;

/**
 * Appel du web service PRONOTE (pattern cache OSIVIA)
 * 
 * @author Jean-Sébastien
 */

public class EtablissementInvoker implements IServiceInvoker {

    private static final long serialVersionUID = 4596239357982004761L;
    
    String etablissementCode;
    String pronoteUrl;

    public EtablissementInvoker(String pronoteUrl,String etablissementCode ) {
        super();
        this.etablissementCode = etablissementCode;
        this.pronoteUrl = pronoteUrl;
    }

    @Override
    public Object invoke() throws PortalException {

        RestTemplate restTemplate = new RestTemplate();
        String url = pronoteUrl.replaceAll("\\{idEtb\\}",etablissementCode);
        EtablissementResponse etablissement = restTemplate.getForObject(url, EtablissementResponse.class);

        return new EtablissementBean(etablissementCode, etablissement.getNom());
    }

}
