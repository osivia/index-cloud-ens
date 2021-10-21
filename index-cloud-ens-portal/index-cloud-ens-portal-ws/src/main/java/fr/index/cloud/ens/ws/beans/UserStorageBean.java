package fr.index.cloud.ens.ws.beans;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class UserStorageBean {
    String userId="";
    String firstName="";
    String lastName="";
    String mail="";
    Set<String> clientId=new TreeSet<String>();
    long fileSize = 0;
    long quota = 0;    
    
    
    /**
     * Getter for firstName.
     * @return the firstName
     */
    public String getFirstName() {
        return firstName;
    }







    
    /**
     * Setter for firstName.
     * @param firstName the firstName to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }







    
    /**
     * Getter for lastName.
     * @return the lastName
     */
    public String getLastName() {
        return lastName;
    }







    
    /**
     * Setter for lastName.
     * @param lastName the lastName to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }







    
    /**
     * Getter for mail.
     * @return the mail
     */
    public String getMail() {
        return mail;
    }







    
    /**
     * Setter for mail.
     * @param mail the mail to set
     */
    public void setMail(String mail) {
        this.mail = mail;
    }




    
    
    
    
    /**
     * Getter for quota.
     * @return the quota
     */
    public long getQuota() {
        return quota;
    }






    
    /**
     * Setter for quota.
     * @param quota the quota to set
     */
    public void setQuota(long quota) {
        this.quota = quota;
    }






    /**
     * Getter for fileSize.
     * @return the fileSize
     */
    public long getFileSize() {
        return fileSize;
    }





    
    /**
     * Setter for fileSize.
     * @param fileSize the fileSize to set
     */
    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }





    /**
     * Getter for clientId.
     * @return the clientId
     */
    public Set<String> getClientId() {
        return clientId;
    }





    /**
     * Getter for userId.
     * @return the userId
     */
    public String getUserId() {
        return userId;
    }

    
    /**
     * Setter for userId.
     * @param userId the userId to set
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }
    

}
