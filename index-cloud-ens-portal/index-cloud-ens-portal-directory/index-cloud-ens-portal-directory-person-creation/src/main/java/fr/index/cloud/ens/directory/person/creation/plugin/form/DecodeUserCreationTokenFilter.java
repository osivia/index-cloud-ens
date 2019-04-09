/**
 * 
 */
package fr.index.cloud.ens.directory.person.creation.plugin.form;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.locator.Locator;
import org.osivia.portal.api.tokens.ITokenService;
import org.springframework.stereotype.Component;

import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilter;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterContext;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterException;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterExecutor;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterParameterType;

/**
 * 
 * Filter used to decode a token ID (drive the name and e-mail of the user who wants to create ab account).
 * @author Loïc Billon
 *
 */
@Component
public class DecodeUserCreationTokenFilter implements FormFilter {

	public static final String IDENTIFIER = DecodeUserCreationTokenFilter.class.getSimpleName();
	
	private static final String LABEL_KEY = "DECODE_USER_TOKEN_LABEL";

	private static final String DESCRIPTION_KEY = "DECODE_USER_TOKEN_DESCRIPTION";

	/* (non-Javadoc)
	 * @see fr.toutatice.portail.cms.nuxeo.api.forms.FormFilter#getId()
	 */
	@Override
	public String getId() {
		return IDENTIFIER;
	}

	/* (non-Javadoc)
	 * @see fr.toutatice.portail.cms.nuxeo.api.forms.FormFilter#getLabelKey()
	 */
	@Override
	public String getLabelKey() {
		return LABEL_KEY;
	}

	/* (non-Javadoc)
	 * @see fr.toutatice.portail.cms.nuxeo.api.forms.FormFilter#getDescriptionKey()
	 */
	@Override
	public String getDescriptionKey() {
		return DESCRIPTION_KEY;
	}

	/* (non-Javadoc)
	 * @see fr.toutatice.portail.cms.nuxeo.api.forms.FormFilter#getParameters()
	 */
	@Override
	public Map<String, FormFilterParameterType> getParameters() {
		 Map<String, FormFilterParameterType> parameters = new HashMap<String, FormFilterParameterType>();
		 return parameters;
	}

	/* (non-Javadoc)
	 * @see fr.toutatice.portail.cms.nuxeo.api.forms.FormFilter#hasChildren()
	 */
	@Override
	public boolean hasChildren() {
		return false;
	}

	/* (non-Javadoc)
	 * @see fr.toutatice.portail.cms.nuxeo.api.forms.FormFilter#execute(fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterContext, fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterExecutor)
	 */
	@Override
	public void execute(FormFilterContext context, FormFilterExecutor executor)
			throws FormFilterException, PortalException {
		

		PortalControllerContext portalControllerContext = context.getPortalControllerContext();
		
		HttpServletRequest request = portalControllerContext.getHttpServletRequest();
		if(request != null) {
			String token = request.getParameter("token");
			
			ITokenService tokenService = Locator.findMBean(ITokenService.class, ITokenService.MBEAN_NAME);
			Map<String, String> validateToken = tokenService.validateToken(token, false);
			
			if(validateToken != null) {
				context.getVariables().putAll(validateToken);
			}
			else {
				throw new FormFilterException("No token found or the token has expired.");
			}
			
			// TODO redirection si erreur
			
		}
		
		

	}

}
