package fr.index.cloud.ens.directory.person.creation.plugin.service;

import java.text.Normalizer;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.osivia.directory.v2.service.PersonUpdateService;
import org.osivia.portal.api.directory.v2.model.Person;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.urls.IPortalUrlFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterContext;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterException;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterExecutor;

/**
 * Person creation plugin service implementation.
 * 
 * @author Loïc Billon
 * @see PersonCreationPluginService
 */
@Service
public class PersonCreationPluginServiceImpl implements PersonCreationPluginService {

    /** regex for first and lastname. Must fit alpha characters and spaces, quotes or hyphens */
    private static final String NAME_REGEX = "^[a-zA-Z-' ]*$";
    /** regex for mails */
    private static final String MAIL_REGEX = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	private static final String UID = "uid";
	private static final String FIELD_FIRSTNAME = "firstname";
	private static final String FIELD_LASTNAME = "lastname";
	private static final String FIELD_MAIL = "mail";
	private static final String FIELD_PWD = "password";
	private static final String FIELD_PWD_CONFIRM = "confirmpassword";
	
	//private static final String REDIRECT_CONFIRMVIEW_URL = "redirectUrl";


    /** Person service. */
    @Autowired
    private PersonUpdateService personService;

    /** Internationalization bundle factory. */
    @Autowired
    private IBundleFactory bundleFactory;
    
    /** Url factory. */
    @Autowired    
    private IPortalUrlFactory urlFactory;

    
    /** Name pattern. */
    private final Pattern namePattern;
    /** Mail pattern. */
    private final Pattern mailPattern;


    /**
     * Constructor.
     */
    public PersonCreationPluginServiceImpl() {
        super();

        // Name pattern
        this.namePattern = Pattern.compile(NAME_REGEX);
        // Mail pattern
        this.mailPattern = Pattern.compile(MAIL_REGEX);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void createPerson(FormFilterContext context, FormFilterExecutor executor) throws FormFilterException {

        // HTTP servlet request
        HttpServletRequest servletRequest = context.getPortalControllerContext().getHttpServletRequest();
        // Internationalization bundle
        Bundle bundle = this.bundleFactory.getBundle(servletRequest.getLocale());
    	
        // form controls 
        controls(context, executor);
        
        String mail = context.getVariables().get( FIELD_MAIL).trim();
        
        // A person can be in directory but his account should not be valid
		Person searchByMail = this.personService.getEmptyPerson();
        searchByMail.setMail(mail);
        List<Person> findByCriteria = personService.findByCriteria(searchByMail);
        
        Person person = null;
        String uid = null;
        boolean reuseAccount = false;
        if(findByCriteria.size() > 0) {
        	
        	Person personFound = findByCriteria.get(0);
			if(personFound.getValidity() == null) {
            	       		
                String message = bundle.getString("PERSON_CREATION_FORM_FILTER_MESSAGE_ERROR_ALREADY_EXISTS");
                throw new FormFilterException(message);
        	}
        	else {
        		person = personFound;
        		uid = person.getUid();
        		reuseAccount = true;
        	}

        }
        else {
        	person = this.personService.getEmptyPerson();
            uid = genUid(context, executor);
            person.setUid(uid);
            // Person is created with passed current date validity. 
            // It can not be logged in until the mail is checked, which will remove this attribute.
            person.setValidity(new Date());
        }

        person.setGivenName(context.getVariables().get(FIELD_FIRSTNAME).trim());
        person.setSn(context.getVariables().get(FIELD_LASTNAME).trim());
        person.setMail(mail);
        person.setDisplayName(person.getGivenName() + " " + person.getSn());
        person.setCn(person.getSn() + " " + person.getGivenName());
        
        if(reuseAccount) {
        	this.personService.update(person);
        }
        else {
        	this.personService.create(person);
        	
        }
        
        // Record password
        String password = context.getVariables().get(FIELD_PWD);
        this.personService.updatePassword(person, password);

        // TODO send a redirection to a confirmation page ?
        
//        Map<String, String> windowProperties = new HashMap<String, String>();
//        windowProperties.put(InternalConstants.PROP_WINDOW_TITLE, bundle.getString("SUCCESS_VALIDATION"));
//        windowProperties.put("osivia.hideTitle", "1");
//        windowProperties.put("osivia.ajaxLink", "1");
//		try {
//			Map<String, String> params = new HashMap<>();
//			String startPortletUrl = urlFactory.getStartPortletInNewPage(context.getPortalControllerContext(),
//					"confirm",bundle.getString("SUCCESS_VALIDATION"),"cloudens-person-creation-portlet-instance", windowProperties, params); 
//			
//	        context.getVariables().put(REDIRECT_CONFIRMVIEW_URL, startPortletUrl);
//
//			
//		} catch (PortalException e) {
//			throw new FormFilterException(e.getMessage());
//		}
		
		
        
        
        context.getVariables().put(UID, uid);
    }


	/**
	 * UID is first letter of the firstname + lastname without maj, or special characters, accents, .... + a single number
	 * @param context
	 * @param executor
	 * @return the login
	 */
	private String genUid(FormFilterContext context, FormFilterExecutor executor) {
		
		// firstname : trim white spaces, remove accents then check the regex
		String firstname = context.getVariables().get( FIELD_FIRSTNAME).trim();
		firstname = removeAccents(firstname);
		String firstNameInitial = StringUtils.substring(firstname, 0, 1);
				
		// lastname, idem
        String lastname = context.getVariables().get( FIELD_LASTNAME).trim();
        lastname = removeAccents(lastname).trim();
		String lastNameClean = StringUtils.replaceEach(lastname, new String[] {" ", "'"}, new String[]{"-", "-"});
		
		// uid, build first le
		String uid = StringUtils.lowerCase(firstNameInitial+lastNameClean);
		
		Person searchByUid = this.personService.getEmptyPerson();
        searchByUid.setUid(uid+"*");
        List<Person> findByCriteria = personService.findByCriteria(searchByUid);
        
        int suffix = 0;
        for(Person p : findByCriteria) {
        	
        	String suffixStr = StringUtils.remove(p.getUid(), uid);
        	if(!StringUtils.isEmpty(suffixStr)) {
        		int parseInt = Integer.parseInt(suffixStr);
            	if(parseInt > suffix) {
            		suffix = parseInt;
            	}
        	}
        	

        }
        
        if(findByCriteria.size() > 0) {
        	suffix++;
        	uid += Integer.toString(suffix);
        }
		
        return uid;
	}


	private void controls(FormFilterContext context, FormFilterExecutor executor) throws FormFilterException {
		
        // HTTP servlet request
        HttpServletRequest servletRequest = context.getPortalControllerContext().getHttpServletRequest();
        // Internationalization bundle
        Bundle bundle = this.bundleFactory.getBundle(servletRequest.getLocale());
		
        // Get form
        String firstname = context.getVariables().get(FIELD_FIRSTNAME).trim();
        String lastname = context.getVariables().get( FIELD_LASTNAME).trim();
        String mail = context.getVariables().get(FIELD_MAIL).trim();
        String password = context.getVariables().get( FIELD_PWD);
        String confirmpassword = context.getVariables().get(FIELD_PWD_CONFIRM);
        
        // Check first and last name syntax
        firstname = removeAccents(firstname);
        Matcher matcher = this.namePattern.matcher(firstname);
        if (!matcher.matches()) {
            String message = bundle.getString("PERSON_CREATION_FORM_FILTER_MESSAGE_ERROR_INVALID_FIRSTNAME");
            throw new FormFilterException(message);
        }
        
        lastname = removeAccents(lastname);
        matcher = this.namePattern.matcher(lastname);
        if (!matcher.matches()) {
            String message = bundle.getString("PERSON_CREATION_FORM_FILTER_MESSAGE_ERROR_INVALID_LASTNAME");
            throw new FormFilterException(message);
        }        
        
        // Check mail syntax
        matcher = this.mailPattern.matcher(mail);
        if (!matcher.matches()) {
            String message = bundle.getString("PERSON_CREATION_FORM_FILTER_MESSAGE_ERROR_INVALID_MAIL");
            throw new FormFilterException(message);
        }
                
        if(password.compareTo(confirmpassword) != 0) {
            String message = bundle.getString("PERSON_CREATION_FORM_FILTER_MESSAGE_ERROR_INVALID_CONFIRMATION_PASSWORD");
            throw new FormFilterException(message);
        }
	}

	private String removeAccents(String input) {
		// Suppression d'accents
		input = Normalizer.normalize(input, Normalizer.Form.NFD);
		return input.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
	}


	/* (non-Javadoc)
	 * @see fr.index.cloud.ens.directory.person.creation.plugin.service.PersonCreationPluginService#verifyMail(fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterContext, fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterExecutor)
	 */
	@Override
	public void verifyMail(FormFilterContext context, FormFilterExecutor executor) {
		
		String uid= context.getVariables().get(UID);
		Person person = personService.getPerson(uid);
		
		// Account is valid unlimited time
		person.setValidity(null);
		
		personService.update(person);
		
		
	}
}
