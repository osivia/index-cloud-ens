/**
 * 
 */
package org.osivia.cas.fim;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

/**
 * Rewrite url service for FIM and serviceUrl target
 * @author Loïc Billon
 *
 */
@Service
public class FimRewriteUrlService {

	@Autowired
	private ApplicationContext context;

	public SelectorForm getUrls(String service) throws UnsupportedEncodingException, MalformedURLException, URISyntaxException {
		SelectorForm sf = context.getBean(SelectorForm.class);
		
		String redirectAcaService = "";
		String redirectSupService = "";
		if(StringUtils.isNotBlank(service)) {
			
			// Skip escape chars (in nuxeo drive eg)
			service = service.replace("+", "%2B");
			
			URI uri = new URI(URLDecoder.decode(service, "UTF-8"));
			String path = uri.getPath();
			
			// Append query if needed
			if(uri.getQuery() != null) {
				path = path + "?" +uri.getQuery();
			}
			
			URL redirectedAcaUrl = new URL("https",sf.getBaseAcaUrl(), path);
			redirectAcaService = "&service="+URLEncoder.encode(redirectedAcaUrl.toString(), "UTF-8");
			
			URL redirectedSupUrl = new URL("https",sf.getBaseSupUrl(), path);
			redirectSupService = "&service="+URLEncoder.encode(redirectedSupUrl.toString(), "UTF-8");
							
		}
		
		sf.setAcaUrl(new URL("https",sf.getBaseAcaUrl() ,"/cas/fim?s=fim"+redirectAcaService).toString());
		sf.setSupUrl(new URL("https",sf.getBaseSupUrl() ,"/cas/fim?s=fim"+redirectSupService).toString());

		

		return sf;
		
	}
	
}
