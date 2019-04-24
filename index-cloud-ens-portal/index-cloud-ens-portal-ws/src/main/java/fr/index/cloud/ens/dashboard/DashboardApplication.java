package fr.index.cloud.ens.dashboard;

import java.util.Date;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import fr.index.cloud.oauth.tokenStore.AggregateRefreshTokenInfos;
import fr.index.cloud.oauth.tokenStore.PortalRefreshTokenAuthenticationDatas;

/**
 * Dashboard datas java-bean.
 * 
 * @author JS Steux
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class DashboardApplication  {


    /** The selected. */
    private boolean selected;
    
    /** The token. */
    private AggregateRefreshTokenInfos token;
    
    /** The client name. */
    private String clientName;

    

    /**
     * Gets the client name.
     *
     * @return the client name
     */
    public String getClientName() {
        return clientName;
    }


    
    /**
     * Setter for clientName.
     * @param clientName the clientName to set
     */
    public void setClientName(String clientName) {
        this.clientName = clientName;
    }


    /**
     * Getter for token.
     * @return the token
     */
    public AggregateRefreshTokenInfos getToken() {
        return token;
    }

    
    /**
     * Setter for token.
     * @param token the token to set
     */
    public void setToken(AggregateRefreshTokenInfos token) {
        this.token = token;
    }



    /**
     * Constructor.
     * 
     * @param path document path
     */
    public DashboardApplication( AggregateRefreshTokenInfos token, String clientName) {
        this.token = token;
        this.clientName = clientName;
        
    }


  
    /**
     * Getter for selected.
     * 
     * @return the selected
     */
    public boolean isSelected() {
        return selected;
    }

    /**
     * Setter for selected.
     * 
     * @param selected the selected to set
     */
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

}
