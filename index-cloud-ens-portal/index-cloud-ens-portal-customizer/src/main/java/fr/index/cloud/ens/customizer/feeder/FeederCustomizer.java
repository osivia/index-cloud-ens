/*
 *
 */
package fr.index.cloud.ens.customizer.feeder;

import java.io.StringReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.portlet.GenericPortlet;
import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osivia.directory.v2.service.PersonUpdateService;
import org.osivia.portal.api.customization.CustomizationContext;
import org.osivia.portal.api.customization.CustomizationModuleMetadatas;
import org.osivia.portal.api.customization.ICustomizationModule;
import org.osivia.portal.api.customization.ICustomizationModulesRepository;
import org.osivia.portal.api.directory.v2.DirServiceFactory;
import org.osivia.portal.api.directory.v2.model.Person;
import org.osivia.portal.api.feeder.IFeederService;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;


/**
 * The Class FeederCustomizer.
 */
public class FeederCustomizer extends GenericPortlet implements ICustomizationModule {

    /** Customizer name. */
    private static final String CUSTOMIZER_NAME = "cloud-ens.customizer.feeder";
    /** Customization modules repository attribute name. */
    private static final String ATTRIBUTE_CUSTOMIZATION_MODULES_REPOSITORY = "CustomizationModulesRepository";

    /** The Constant CAS_ATTRIBUTE_PREFIX. */
    private static final String CAS_ATTRIBUTE_PREFIX = "cas:";


    /** Customization modules repository. */
    private ICustomizationModulesRepository repository;

    /** Customization module metadatas. */
    private final CustomizationModuleMetadatas metadatas;

    /** The logger. */
    protected static Log logger = LogFactory.getLog(FeederCustomizer.class);

    /**
     * Constructor.
     */
    public FeederCustomizer() {
        super();
        this.metadatas = this.generateMetadatas();
    }


    /**
     * Generate customization module metadatas.
     *
     * @return metadatas
     */
    private CustomizationModuleMetadatas generateMetadatas() {
        final CustomizationModuleMetadatas metadatas = new CustomizationModuleMetadatas();
        metadatas.setName(CUSTOMIZER_NAME);
        metadatas.setModule(this);
        metadatas.setCustomizationIDs(Arrays.asList(IFeederService.CUSTOMIZER_ID));
        return metadatas;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void init(PortletConfig portletConfig) throws PortletException {
        super.init(portletConfig);
        this.repository = (ICustomizationModulesRepository) this.getPortletContext().getAttribute(ATTRIBUTE_CUSTOMIZATION_MODULES_REPOSITORY);
        this.repository.register(this.metadatas);

    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void destroy() {
        super.destroy();
        this.repository.unregister(this.metadatas);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void customize(CustomizationContext customizationContext) {
        // Parsing reponse cas
        try {
            Map<String, Object> attributes = customizationContext.getAttributes();

            HttpServletRequest request = (HttpServletRequest) attributes.get(IFeederService.CUSTOMIZER_ATTRIBUTE_REQUEST);

            String response = (String) request.getAttribute("casresponse");

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(new InputSource(new StringReader(response)));

            Element authentication = (Element) doc.getElementsByTagName("cas:serviceResponse").item(0);

            String userId = "";

            Map<String, String> personAttributes = new HashMap<String, String>();

            // User is Mandatory
            userId = authentication.getElementsByTagName("cas:user").item(0).getTextContent();


            NodeList casAttributesList = authentication.getElementsByTagName("cas:attributes");
            if (casAttributesList != null) {
                Node casAttributes = casAttributesList.item(0);
                if (casAttributes != null) {
                    for (int i = 0; i < casAttributes.getChildNodes().getLength(); i++) {
                        Node casAttribute = casAttributes.getChildNodes().item(i);
                        if (casAttribute.getNodeType() == Node.ELEMENT_NODE) {
                            String attributeName = casAttribute.getNodeName();
                            if (attributeName.startsWith(CAS_ATTRIBUTE_PREFIX)) {
                                // Store attribute
                                personAttributes.put(attributeName.substring(CAS_ATTRIBUTE_PREFIX.length()), casAttribute.getTextContent());
                            }
                        }
                    }
                }
            }

            // Create, Update LDAP
            PersonUpdateService service = DirServiceFactory.getService(PersonUpdateService.class);
            if (service != null) {
                Person p = service.getPerson(userId);
                if (p == null) {
                    p = service.getEmptyPerson();
                    p.setCn(userId);

                    p.setUid(userId);
                    p.setSn(personAttributes.get("sn"));
                    p.setCn(personAttributes.get("cn"));
                    p.setDisplayName(personAttributes.get("displayName"));
                    p.setMail(personAttributes.get("mail"));
                    p.setGivenName(personAttributes.get("givenName"));

                    service.create(p);
                } else {
                    if (personAttributes.size() > 0) {
                        if (personAttributes.get("sn") != null) {
                            p.setSn(personAttributes.get("sn"));
                        }
                        if (personAttributes.get("cn") != null) {
                            p.setCn(personAttributes.get("cn"));
                        }
                        if (personAttributes.get("displayName") != null) {
                            p.setDisplayName(personAttributes.get("displayName"));
                        }
                        if (personAttributes.get("mail") != null) {
                            p.setMail(personAttributes.get("mail"));
                        }
                        if (personAttributes.get("givenName") != null) {
                            p.setGivenName(personAttributes.get("givenName"));
                        }
                    }

                    service.update(p);
                }


            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
