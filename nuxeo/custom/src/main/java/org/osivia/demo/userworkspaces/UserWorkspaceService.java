package org.osivia.demo.userworkspaces;

import java.security.Principal;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.PathRef;
import org.nuxeo.ecm.core.api.security.ACE;
import org.nuxeo.ecm.core.api.security.ACL;
import org.nuxeo.ecm.core.api.security.ACP;
import org.nuxeo.ecm.core.api.security.SecurityConstants;
import org.nuxeo.ecm.core.api.security.impl.ACLImpl;
import org.nuxeo.ecm.core.api.security.impl.ACPImpl;

import fr.toutatice.ecm.platform.core.userworkspace.ToutaticeUserWorkspaceServiceImpl;

public class UserWorkspaceService extends ToutaticeUserWorkspaceServiceImpl {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6465767707468908495L;
	/**
	 * 
	 */
	private static final String TTC_SHOW_IN_MENU = "ttc:showInMenu";
	/**
	 * 
	 */
	private static final String DC_TITLE = "dc:title";

	@Override
	protected DocumentModel doCreateUserWorkspace(
			CoreSession unrestrictedSession, PathRef wsRef,
			Principal principal, String userName) throws ClientException {
		DocumentModel userWorkspace = super.doCreateUserWorkspace(
				unrestrictedSession, wsRef, principal, userName);

		// Paramétrage affichage portail
		// userWorkspace.setProperty("toutatice", "pageTemplate",
		// "/templates/userWorkspace");
		userWorkspace.setProperty("toutatice", "tabOrder", "100");
		userWorkspace.setPropertyValue(DC_TITLE, "Mon espace");

		unrestrictedSession.saveDocument(userWorkspace);

		// Initialisation Mes documents
		DocumentModel mesDocs = unrestrictedSession.createDocumentModel(
				userWorkspace.getPathAsString(), "documents", "Folder");
		mesDocs.setPropertyValue(DC_TITLE, "Documents");
		mesDocs.setPropertyValue(TTC_SHOW_IN_MENU, Boolean.TRUE);

		unrestrictedSession.createDocument(mesDocs);

		// Initialisation Mes photos
		DocumentModel mesPhotos = unrestrictedSession.createDocumentModel(
				userWorkspace.getPathAsString(), "gallery",
				"PictureBook");
		mesPhotos.setPropertyValue(DC_TITLE, "Galerie d'images");
		mesPhotos.setPropertyValue(TTC_SHOW_IN_MENU, Boolean.TRUE);

		unrestrictedSession.createDocument(mesPhotos);

		// Initialisation Mon agenda
		DocumentModel monAgenda = unrestrictedSession.createDocumentModel(
				userWorkspace.getPathAsString(), "calendar", "Agenda");
		monAgenda.setPropertyValue(DC_TITLE, "Agenda");
		monAgenda.setPropertyValue(TTC_SHOW_IN_MENU, Boolean.TRUE);

		unrestrictedSession.createDocument(monAgenda);

		// Initialisation du dossier public
		DocumentModel publicFolder = unrestrictedSession.createDocumentModel(
				userWorkspace.getPathAsString(), "public", "Folder");
		publicFolder.setPropertyValue(DC_TITLE, "Documents partagés");
		publicFolder.setPropertyValue(TTC_SHOW_IN_MENU, Boolean.TRUE);

		unrestrictedSession.createDocument(publicFolder);
		setFoldersACL(publicFolder, userName);

		return userWorkspace;
	}

	protected void setFoldersACL(DocumentModel doc, String userName)
			throws ClientException {

		ACP acp = new ACPImpl();

		ACE grantMembersRead = new ACE(SecurityConstants.EVERYONE,
				SecurityConstants.READ, true);
		ACE grantEverything = new ACE(userName, SecurityConstants.EVERYTHING,
				true);
		ACL acl = new ACLImpl();
		acl.setACEs(new ACE[] { grantMembersRead, grantEverything });
		acp.addACL(acl);
		doc.setACP(acp, true);
	}
}
