package org.osivia.demo.customizer.plugin.cms;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.PortletContext;

import org.apache.commons.lang.StringUtils;
import org.jboss.portal.core.controller.ControllerContext;
import org.jboss.portal.core.model.portal.Page;
import org.jboss.portal.core.model.portal.Portal;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.Documents;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.portal.api.cache.services.CacheInfo;
import org.osivia.portal.api.cms.EcmDocument;
import org.osivia.portal.api.cms.IVirtualNavigationService;
import org.osivia.portal.api.cms.Symlink;
import org.osivia.portal.api.cms.Symlinks;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.locator.Locator;
import org.osivia.portal.core.cms.CMSException;
import org.osivia.portal.core.cms.CMSItem;
import org.osivia.portal.core.context.ControllerContextAdapter;
import org.osivia.portal.core.portalobjects.PortalObjectUtils;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.domain.INavigationAdapterModule;
import fr.toutatice.portail.cms.nuxeo.api.services.NuxeoCommandContext;

/**
 * Extranet navigation adapter module.
 * 
 * @author Cédric Krommenhoek
 * @see INavigationAdapterModule
 */
public class ExtranetNavigationAdapterModule implements INavigationAdapterModule {

    /** Virtual navigation service. */
    private final IVirtualNavigationService virtualNavigationService;


    /** Portlet context. */
    private PortletContext portletContext;


    /**
     * Constructor.
     * 
     * @param portletContext portlet context
     */
    public ExtranetNavigationAdapterModule(PortletContext portletContext) {
        super();
        this.portletContext = portletContext;

        // Virtual navigation service
        this.virtualNavigationService = Locator.findMBean(IVirtualNavigationService.class, IVirtualNavigationService.MBEAN_NAME);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String adaptNavigationPath(PortalControllerContext portalControllerContext, EcmDocument document) throws CMSException {
        return null;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Symlinks getSymlinks(PortalControllerContext portalControllerContext) throws CMSException {
        // Symlinks
        Symlinks symlinks = null;

        // Controller context
        ControllerContext controllerContext = ControllerContextAdapter.getControllerContext(portalControllerContext);
        // Current portal
        Portal portal = PortalObjectUtils.getPortal(controllerContext);

        if (PortalObjectUtils.isSpaceSite(portal)) {
            // Default page
            Page defaultPage = portal.getDefaultPage();
            // Base path
            String basePath = defaultPage.getDeclaredProperty("osivia.cms.basePath");

            if (StringUtils.isNotEmpty(basePath)) {
                symlinks = new Symlinks();

                // Nuxeo controller
                NuxeoController nuxeoController = this.getNuxeoController();

                // Nuxeo command
                INuxeoCommand command = new GetRecordsCommand();
                Documents documents = (Documents) nuxeoController.executeNuxeoCommand(command);

                if (!documents.isEmpty()) {
                    List<Symlink> links = new ArrayList<>(documents.size());
                    symlinks.setLinks(links);

                    // Products page path
                    String productsPath = nuxeoController.getDocumentContext("page_produits").getCmsPath();

                    // Product symlinks
                    Map<String, Symlink> products = new HashMap<>();
                    // Product linked records
                    List<Document> productLinkedRecords = new ArrayList<>();
                    
                    for (Document document : documents) {
                        if ("RecordFolder".equals(document.getType())) {
                            symlinks.getPaths().add(document.getPath());
                        } else if ("Record".equals(document.getType())) {
                            if ("record_news".equals(document.getString("rcd:procedureModelWebId"))
                                    || "record_documents".equals(document.getString("rcd:procedureModelWebId"))) {
                                productLinkedRecords.add(document);
                            } else {
                                // Product indicator
                                boolean product = "record_produits".equals(document.getString("rcd:procedureModelWebId"));

                                // Symlink parent path
                                String parentPath;
                                if (product) {
                                    parentPath = productsPath;
                                } else {
                                    parentPath = basePath;
                                }
                                String segment = this.generateSegmentFromTitle(document.getTitle());
                                String targetPath = document.getPath();
                                String targetWebId = document.getString("ttc:webid");

                                // Symlink
                                Symlink symlink = this.virtualNavigationService.createSymlink(parentPath, segment, targetPath, targetWebId);
                                links.add(symlink);

                                if (product) {
                                    products.put(document.getPath(), symlink);
                                }
                            }
                        }
                    }
                    
                    for (Document document : productLinkedRecords) {
                        // Record data
                        PropertyMap data = document.getProperties().getMap("rcd:data");
                        // Product properties
                        PropertyMap product = data.getMap("produit");
                        // Product symlink
                        Symlink productSymlink = products.get(product.getString("ecm:path"));
                        
                        if (productSymlink != null) {
                            String segment = this.generateSegmentFromTitle(document.getTitle());
                            String targetPath = document.getPath();
                            String targetWebId = document.getString("ttc:webid");

                            // Symlink
                            Symlink symlink = this.virtualNavigationService.createSymlink(productSymlink, segment, targetPath, targetWebId);
                            links.add(symlink);
                        }
                    }
                }
            }
        }

        return symlinks;
    }


    /**
     * Generate segment from title.
     *
     * @param title document title
     * @return segment
     */
    private String generateSegmentFromTitle(String title) {
        String name = title;

        // Lower case
        name = StringUtils.lowerCase(name);

        // Remove accents
        name = Normalizer.normalize(name, Normalizer.Form.NFD);
        name = name.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");

        // Remove special characters
        name = name.replaceAll("[^a-z0-9]", "-");

        // Remove "-" prefix
        while (StringUtils.startsWith(name, "-")) {
            name = StringUtils.removeStart(name, "-");
        }

        // Remove "-" suffix
        while (StringUtils.endsWith(name, "-")) {
            name = StringUtils.removeEnd(name, "-");
        }

        // Remove consecutive "-"
        while (StringUtils.contains(name, "--")) {
            name = StringUtils.replace(name, "--", "-");
        }

        return name;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void adaptNavigationItem(PortalControllerContext portalControllerContext, CMSItem navigationItem) throws CMSException {
        // Controller context
        ControllerContext controllerContext = ControllerContextAdapter.getControllerContext(portalControllerContext);
        // Current portal
        Portal portal = PortalObjectUtils.getPortal(controllerContext);

        if (PortalObjectUtils.isSpaceSite(portal)) {
            // Navigation item properties
            Map<String, String> properties = navigationItem.getProperties();
            // Navigation item Nuxeo document
            Document document = (Document) navigationItem.getNativeItem();

            if ("RecordFolder".equals(document.getType())) {
                properties.remove("menuItem");
            } else if ("Record".equals(document.getType())) {
                // Record model webId
                String modelWebId = document.getString("rcd:procedureModelWebId");

                if ("record_produits".equals(modelWebId)) {
                    properties.put("menuItem", "1");

                    properties.put("navigationElement", "1");
                    properties.put("pageDisplayMode", "1");
                    properties.put("pageTemplate", "/default/templates/record-produit");
                }
            }
        }
    }


    /**
     * Get Nuxeo controller.
     * 
     * @return Nuxeo controller
     */
    private NuxeoController getNuxeoController() {
        NuxeoController nuxeoController = new NuxeoController(this.portletContext);
        nuxeoController.setAuthType(NuxeoCommandContext.AUTH_TYPE_SUPERUSER);
        nuxeoController.setCacheType(CacheInfo.CACHE_SCOPE_PORTLET_CONTEXT);
        return nuxeoController;
    }

}
