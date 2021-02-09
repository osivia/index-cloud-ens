package fr.index.cloud.ens.utils.ldap;

import javax.naming.NamingEnumeration;
import javax.naming.directory.SearchResult;


public class LdapClientTest {
    public static final void main(String[] args){
        
        //URL : ldaps://CLDENS-LDS-D01.dev.index.france:636/CN=Annuaire,DC=Cloud,DC=Enseignant
        System.out.println("Start ldap test.");
        // Please replace LDAP url, admin user dn and admin user password with your value.
//        LdapClient client = new LdapClient("ldap://CLDENS-LDS-D01.dev.index.france:389", "CN=ldapAdmin,CN=Utilisateurs,CN=Annuaire,DC=Cloud,DC=Enseignant", "WkNaElVfaxi9kOCbv3Q5");
        LdapClient client = new LdapClient("ldaps://CLDENS-LDS-D01.dev.index.france:636", "CN=ldapAdmin,CN=Utilisateurs,CN=Annuaire,DC=Cloud,DC=Enseignant", "WkNaElVfaxi9kOCbv3Q5");
        
        /*
        
        System.out.println("Create ldap ou");
        client.createLDAPUsersOU(); 
*/
        
        /*
         * user creation update -> can't start with uid 
         */
     
/*
        String userName = "demo";        
        System.out.println("Create ldap user." + userName);
        client.createLDAPUser(userName, "romChar1234!");
*/       
        
/*     
        System.out.println("Search ldap user.");
        NamingEnumeration<SearchResult> searchResults = client.searchLDAPUser("CN=Utilisateurs,CN=Annuaire,DC=Cloud,DC=Enseignant", "(CN=*)");
        System.out.println("Search result: " + searchResults.nextElement().getNameInNamespace());
        while( searchResults.hasMoreElements()) {
            System.out.println(" --> " + searchResults.nextElement().getClassName());
        }
        

        System.out.println("Modify attribute description.");
        client.updateLDAPUserAttribute("cn="+userName+",CN=Utilisateurs,CN=Annuaire,DC=Cloud,DC=Enseignant", "sn", "Name updated");

        searchResults = client.searchLDAPUser("CN=Utilisateurs,CN=Annuaire,DC=Cloud,DC=Enseignant", "(CN="+userName+")");
        while( searchResults.hasMoreElements()) {
            System.out.println(" --> " + searchResults.nextElement().getAttributes().get("sn"));
        }

 */
        
        
/*
        System.out.println("Delete ldap user.");
        client.deleteLDAPUser("uid=baikaitest,ou=People,dc=asiainfo,dc=com");
        
*/        

        

        

        System.out.println("Create ldap user group.");
        client.createLDAPUserGroup("GPA");


        /*
        
        System.out.println("Delete ldap user group.");
        client.deleteLDAPUserGroup("cn=baikaiGroup,ou=People,dc=asiainfo,dc=com");

        System.out.println("LDAP test done.");
        */
    }
}
