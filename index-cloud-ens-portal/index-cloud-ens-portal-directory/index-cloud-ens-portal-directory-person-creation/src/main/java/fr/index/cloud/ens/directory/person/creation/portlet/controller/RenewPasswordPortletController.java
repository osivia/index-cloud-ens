/**
 * 
 */
package fr.index.cloud.ens.directory.person.creation.portlet.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.cache.services.CacheInfo;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.tokens.ITokenService;
import org.osivia.portal.core.web.IWebIdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.portlet.bind.PortletRequestDataBinder;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import com.sun.mail.smtp.SMTPTransport;

import fr.toutatice.portail.cms.nuxeo.api.CMSPortlet;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoDocumentContext;
import fr.toutatice.portail.cms.nuxeo.api.services.NuxeoCommandContext;

/**
 * @author Loïc Billon
 *
 */
@Controller
@RequestMapping(value = "VIEW")
@SessionAttributes("form")
public class RenewPasswordPortletController extends CMSPortlet {

    /** Application context. */
    @Autowired
    private ApplicationContext applicationContext;
	
    /** Portlet context. */
    @Autowired
    private PortletContext portletContext;
    
    /** Internationalization bundle factory. */
    @Autowired
    private IBundleFactory bundleFactory;
    
    @Autowired
    private ITokenService tokenService;    
	
	@Autowired
	private RenewPasswordFormValidator validator;
	
    /** Portlet config. */
    @Autowired
    private PortletConfig portletConfig;	
	
	@RenderMapping
	public String view() {
		return "view";
	}
	
	@ActionMapping(name = "save")
	public void save(ActionRequest request, ActionResponse response, @Validated @ModelAttribute("form") RenewPasswordForm form, BindingResult result,
			SessionStatus session) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        if (result.hasErrors()) {
            // Copy render parameters
            Map<String, String[]> parameters = request.getPrivateParameterMap();
            if (MapUtils.isNotEmpty(parameters)) {
                for (Entry<String, String[]> entry : parameters.entrySet()) {
                    response.setRenderParameter(entry.getKey(), entry.getValue());
                }
            }
        } else {
        	
            // 3 - Create the portal token
        	Map<String, String> attributes = new HashMap<String, String>();
        	attributes.put("mail", form.getMail());
            String webToken = tokenService.generateToken(attributes);

            // 4 - Compute and return a link to start UserCreation procedure
            String url = computeRenewPwdProcUrl(portalControllerContext, webToken);
        	
        	sendMail(portalControllerContext, form.getMail(), url);
        	
        	form.setSent(true);
        	        	
        }
	}
	
    /**
     * Compute and return a link to start UserCreation procedure
     * @param request
     * @param returnObject
     * @param webToken
     */
    private String computeRenewPwdProcUrl(PortalControllerContext context, String webToken) {
        
        NuxeoController nuxeoController = new NuxeoController(portletContext);
        nuxeoController.setServletRequest(context.getHttpServletRequest());
        nuxeoController.setAuthType(NuxeoCommandContext.AUTH_TYPE_SUPERUSER);
        nuxeoController.setCacheType(CacheInfo.CACHE_SCOPE_NONE);
        NuxeoDocumentContext ctx = nuxeoController.getDocumentContext(IWebIdService.FETCH_PATH_PREFIX + "procedure_renew_password");

        // Get parent doc
        Document userCreationProcedure = ctx.getDocument();

        String url = "/portal/cms" + userCreationProcedure.getPath();

        String publicHost = System.getProperty("osivia.tasks.host");
        url = publicHost + url + "?displayContext=uncluttered&token=" + webToken;
        return url;
        
    }   
	
	/**
	 * @throws PortletException 
	 * 
	 */
	private void sendMail(PortalControllerContext portalControllerContext, String mailToVar, String url)
			throws PortletException {

        // Locale
        Locale locale = null;
        if(portalControllerContext.getRequest() != null) {
        	locale = portalControllerContext.getRequest().getLocale();
        }
        
        // Internationalization bundle
        Bundle bundle = this.bundleFactory.getBundle(locale);

        // System properties
        Properties properties = System.getProperties();
		String userName = properties.getProperty("mail.smtp.user");
		String password = properties.getProperty("mail.smtp.password"); 
        
        // Parameters
        String mailObjectVar = bundle.getString("renew.mail.object");
        String mailBodyVar = bundle.getString("renew.mail.content", url);
        
        // Body
        StringBuilder body = new StringBuilder();
        for (String line : StringUtils.split(mailBodyVar, System.lineSeparator())) {
            body.append("<p>");
            body.append(line);
            body.append("</p>");
        }

		Authenticator auth = null;
		if( userName != null && password !=null)
			auth = new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("demo@osivia.com", "demo-osivia");
			}
		  };
			
		
		Session mailSession = Session.getInstance(properties, auth);

        // Message
        MimeMessage message = new MimeMessage(mailSession);

        // "Mail from" address
        InternetAddress mailFromAddr = null;
        if (StringUtils.isNotBlank(userName)) {
            try {
                mailFromAddr = new InternetAddress(userName);
            } catch (AddressException e1) {
                throw new PortletException(bundle.getString("SEND_MAIL_FILTER_MAILFROM_MISSING_ERROR"));
            }
        }
        // "Mail to" address
        InternetAddress[] mailToAddr;
        try {
            mailToAddr = InternetAddress.parse(mailToVar, false);
        } catch (AddressException e1) {
            throw new PortletException(bundle.getString("SEND_MAIL_FILTER_MAILTO_MISSING_ERROR"));
        }

        try {
            message.setFrom(mailFromAddr);
            message.setRecipients(Message.RecipientType.TO, mailToAddr);
            message.setSubject(mailObjectVar, "UTF-8");

            // Multipart
            Multipart multipart = new MimeMultipart();
            MimeBodyPart htmlPart = new MimeBodyPart();
            htmlPart.setContent(body.toString(), "text/html; charset=UTF-8");
            multipart.addBodyPart(htmlPart);
            message.setContent(multipart);

            message.setSentDate(new Date());

            if (mailFromAddr != null) {
                InternetAddress[] replyToTab = new InternetAddress[1];
                replyToTab[0] = mailFromAddr;
                message.setReplyTo(replyToTab);
            }
            // SMTP transport
            SMTPTransport transport = (SMTPTransport) mailSession.getTransport();
            transport.connect();
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        } catch (MessagingException e) {
            // Exception
            throw new PortletException(e);

        }
		
	}

	@ModelAttribute("form")
	public RenewPasswordForm getForm() {
		return applicationContext.getBean(RenewPasswordForm.class);
	}
	
    /**
     * Person edition form init binder.
     *
     * @param binder web data binder
     */
    @InitBinder("form")
    public void initBinder(PortletRequestDataBinder binder) {
        binder.addValidators(this.validator);
    }
    
    /**
     * Post-construct.
     *
     * @throws PortletException
     */
    @PostConstruct
    public void postConstruct() throws PortletException {
        super.init(this.portletConfig);
    }    
}
