package org.osivia.demo.customizer.plugin.menubar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.cms.DocumentContext;
import org.osivia.portal.api.cms.DocumentType;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.menubar.MenubarContainer;
import org.osivia.portal.api.menubar.MenubarDropdown;
import org.osivia.portal.api.menubar.MenubarItem;
import org.osivia.portal.api.menubar.MenubarModule;

import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;

public class DemoMenubarModule implements MenubarModule {

    /**
     * Constructor.
     */
    public DemoMenubarModule() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void customizeSpace(PortalControllerContext portalControllerContext, List<MenubarItem> menubar, DocumentContext spaceDocumentContext)
            throws PortalException {
        // Base path
        String basePath;
        if (spaceDocumentContext == null) {
            basePath = null;
        } else {
            basePath = spaceDocumentContext.getCmsPath();
        }
        
        
        if (StringUtils.startsWith(basePath, "/default-domain/UserWorkspaces/")) {
            // Inside user workspace
            Iterator<MenubarItem> iterator = menubar.iterator();
            while (iterator.hasNext()) {
                MenubarItem item = iterator.next();
                MenubarContainer parent = item.getParent();
                
                if ((parent != null) && (parent instanceof MenubarDropdown)) {
                    MenubarDropdown dropdown = (MenubarDropdown) parent;
                    if (StringUtils.equals("CONFIGURATION", dropdown.getId())) {
                        iterator.remove();
                    }
                }
            }
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void customizeDocument(PortalControllerContext portalControllerContext, List<MenubarItem> menubar, DocumentContext documentContext)
            throws PortalException {
        // Path
        String path;
        if (documentContext == null) {
            path = null;
        } else {
            path = documentContext.getCmsPath();
        }

        if (StringUtils.startsWith(path, "/default-domain/UserWorkspaces/")) {
            // Inside user workspace
            NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

            // Document type
            DocumentType documentType = documentContext.getDocumentType();

            // Removed menubar item identifiers
            List<String> itemIdentifiers = new ArrayList<>(Arrays.asList(new String[]{"WORKSPACE_ACL_MANAGEMENT", "SUBSCRIBE_URL"}));
            // Removed menubar dropdown menu identifiers
            List<String> dropdownIdentifiers = new ArrayList<>();

            if ((documentType != null) && documentType.isRoot()) {
                // Remove add item
                itemIdentifiers.add("ADD");
            } else {
                if (!documentContext.getPermissions().isAnonymouslyReadable()) {
                    // Remove share menu
                    dropdownIdentifiers.add("SHARE");
                }

                if (StringUtils.equals(nuxeoController.getBasePath(), StringUtils.substringBeforeLast(path, "/"))) {
                    // Remove delete and move items
                    itemIdentifiers.add("DELETE");
                    itemIdentifiers.add("MOVE");
                }
            }

            Iterator<MenubarItem> iterator = menubar.iterator();
            while (iterator.hasNext()) {
                MenubarItem item = iterator.next();
                MenubarContainer parent = item.getParent();

                if (itemIdentifiers.contains(item.getId())) {
                    iterator.remove();
                } else if ((parent != null) && (parent instanceof MenubarDropdown)) {
                    MenubarDropdown dropdown = (MenubarDropdown) parent;
                    if (dropdownIdentifiers.contains(dropdown.getId())) {
                        iterator.remove();
                    }
                }
            }
        }
    }

}
