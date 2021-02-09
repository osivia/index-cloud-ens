package fr.index.cloud.ens.utils.ldap;

import java.util.Properties;
import javax.naming.NamingException;
import javax.naming.NamingEnumeration;
import javax.naming.directory.*;
import javax.naming.ldap.*;

/**
 * Created by baikai on 8/17/16.
 */
public class LdapClient {

    private String ldapUrl;
    private String ldapUserDN;
    private String ldapPwd;

    public LdapClient(String ldapUrl, String ldapUserDN, String ldapPwd){
        this.ldapUrl = ldapUrl;
        this.ldapUserDN = ldapUserDN;
        this.ldapPwd = ldapPwd;
    }

    /**
     * Create LDAP user
     * @param userName
     * @param password
     * @param uidNumber
     * @param gidNumber
     */
    public void createLDAPUser(String userName, String password){
        LdapContext context = this.initLDAPContext();
        Attributes matchAttrs = new BasicAttributes(true);
        BasicAttribute objclassSet = new BasicAttribute("objectClass");

        objclassSet.add("organizationalPerson");
        objclassSet.add("inetOrgPerson");
        objclassSet.add("top");
        objclassSet.add("portalPerson");
        matchAttrs.put(objclassSet);
        matchAttrs.put(new BasicAttribute("uid", userName));
        matchAttrs.put(new BasicAttribute("cn", userName));
        matchAttrs.put(new BasicAttribute("userpassword", password));
        matchAttrs.put(new BasicAttribute("sn", "name" + userName));
        try {
            context.bind("uid=" + userName + ",CN=Utilisateurs,CN=Annuaire,DC=Cloud,DC=Enseignant", null, matchAttrs);
        } catch (NamingException e) {
            e.printStackTrace();
        }finally {
            this.closeLdapContext(context);
        }
    }


    /**
     * Create LDAP user group
     * @param groupName
     * @param password
     * @param gidNumber
     */
    public void createLDAPUsersOU(){
        LdapContext context = this.initLDAPContext();
        Attributes matchAttrs = new BasicAttributes(true);
        matchAttrs.put(new BasicAttribute("objectclass", "organizationalUnit"));
        matchAttrs.put(new BasicAttribute("objectclass", "top"));        
        matchAttrs.put(new BasicAttribute("ou", "users"));
        matchAttrs.put(new BasicAttribute("description", "Utilisateurs"));

        try {
            context.bind("OU=users,DC=Cloud,DC=Enseignant", null, matchAttrs);
        } catch (NamingException e) {
            e.printStackTrace();
        }finally {
            this.closeLdapContext(context);
        }
    }
    
    
    /**
     * Create LDAP user group
     * @param groupName
     * @param password
     * @param gidNumber
     */
    public void createLDAPUserGroup(String groupName){
        LdapContext context = this.initLDAPContext();
        Attributes matchAttrs = new BasicAttributes(true);
        matchAttrs.put(new BasicAttribute("objectclass", "groupOfUniqueNames"));
        matchAttrs.put(new BasicAttribute("objectclass", "top"));        
        matchAttrs.put(new BasicAttribute("cn", groupName));
        try {
            context.bind("CN=" + groupName + ",CN=Roles,CN=Annuaire,DC=Cloud,DC=Enseignant", null, matchAttrs);
        } catch (NamingException e) {
            e.printStackTrace();
        }finally {
            this.closeLdapContext(context);
        }
    }

    /**
     * Delete LDAP user
     * @param userName
     */
    public void deleteLDAPUser(String userName){
        LdapContext context = this.initLDAPContext();
        try {
            context.unbind(userName);
        } catch (NamingException e) {
            e.printStackTrace();
        }finally {
            this.closeLdapContext(context);
        }
    }

    /**
     * Delete LDAP user group
     * @param groupName
     */
    public void deleteLDAPUserGroup(String groupName){
        this.deleteLDAPUser(groupName);
    }

    /**
     * Modify LDAP user attribute with new value
     * @param userName
     * @param attributeName
     * @param attributeNewValue
     */
    public void updateLDAPUserAttribute(String userName, String attributeName, String attributeNewValue){
        LdapContext context = this.initLDAPContext();
        ModificationItem[] mods = new ModificationItem[1];
        mods[0] = new ModificationItem(context.REPLACE_ATTRIBUTE, new BasicAttribute(attributeName, attributeNewValue));
        try{
            context.modifyAttributes(userName, mods);
        }catch (NamingException e) {
            e.printStackTrace();
        }finally {
            this.closeLdapContext(context);
        }
    }

    /**
     * Search LDAP users by user dn and filter
     * @param userName
     * @param filter
     * @return NamingEnumeration<SearchResult>
     */
    public NamingEnumeration<SearchResult> searchLDAPUser(String userName, String filter){
        NamingEnumeration<SearchResult> searchResults = null;
        LdapContext context = this.initLDAPContext();
        SearchControls ctrl = new SearchControls();
        ctrl.setSearchScope(SearchControls.SUBTREE_SCOPE);
        try {
            searchResults = context.search(userName, filter, ctrl);
        } catch (NamingException e) {
            e.printStackTrace();
        }finally {
            this.closeLdapContext(context);
        }
        return searchResults;
    }

    private LdapContext initLDAPContext(){
        LdapContext context = null;
        Properties mEnv = new Properties();
        mEnv.put(LdapContext.AUTHORITATIVE, "true");
        mEnv.put(LdapContext.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        mEnv.put(LdapContext.PROVIDER_URL, this.ldapUrl);
        mEnv.put(LdapContext.SECURITY_AUTHENTICATION, "simple");
        mEnv.put(LdapContext.SECURITY_PRINCIPAL, this.ldapUserDN);
        mEnv.put(LdapContext.SECURITY_CREDENTIALS, this.ldapPwd);
        try {
            System.out.println("initLDAPContext.");
            context = new InitialLdapContext(mEnv,null);
            System.out.println("end initLDAPContext.");
        } catch (NamingException e) {
            e.printStackTrace();
        }
        return context;
    }

    private void closeLdapContext(LdapContext context){
        try {
            context.close();
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }
}
