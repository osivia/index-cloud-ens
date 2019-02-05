package fr.index.cloud.ens.ws.beans;

import fr.index.cloud.ens.ws.CloudRestController;
import fr.index.cloud.ens.ws.GenericErrors;

/**
 * The base bean for all Web Services
 * 
 * @author Jean-Sébastien
 */
public class BaseBean {
    
    private int returnCode = GenericErrors.ERR_OK;

    
    public BaseBean(int returnCode) {
        super();
        this.returnCode = returnCode;
    }

    public BaseBean() {
        super();
    }
    
    /**
     * Getter for returnCode.
     * @return the returnCode
     */
    public int getReturnCode() {
        return returnCode;
    }

    
    /**
     * Setter for returnCode.
     * @param returnCode the returnCode to set
     */
    public void setReturnCode(int returnCode) {
        this.returnCode = returnCode;
    }
}
