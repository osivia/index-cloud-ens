package org.osivia.cas.fim;

import java.util.HashMap;
import java.util.Map;

import org.jasig.cas.authentication.Credential;
import org.jasig.cas.web.flow.AbstractNonInteractiveCredentialsAction;
import org.springframework.webflow.execution.RequestContext;

public class FimSingleSignOnAction extends AbstractNonInteractiveCredentialsAction {


	private String FIM_PREFIX = "fim_attr_";	
	
	@Override
	protected Credential constructCredentialsFromRequest(RequestContext context) {

        Map<String, Object> attributes = new HashMap<String, Object>();

        String id = (String) context.getExternalContext().getSessionMap().get("fim_Uid");


        Map<String, Object> sessionMap = context.getExternalContext().getSessionMap().asMap();
        for (String key : sessionMap.keySet()) {
            if (key.startsWith(FIM_PREFIX)) {
                String name = key.substring(FIM_PREFIX.length());
                String value = (String) context.getExternalContext().getSessionMap().get(key);
                attributes.put(name, value);
            }

        }


        attributes.put("source", "fim");

        return new FimCredentials(id, attributes);
	}

}
