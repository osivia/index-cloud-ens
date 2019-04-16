package fr.index.cloud.ens.dashboard;

import java.util.Date;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import fr.index.cloud.oauth.tokenStore.AggregateRefreshTokenInfos;
import fr.index.cloud.oauth.tokenStore.PortalRefreshTokenAuthenticationDatas;

/**
 * Trashed document java-bean.
 * 
 * @author JS Steux
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class DashboardApplication  {


    private boolean selected;
    private AggregateRefreshTokenInfos token;


    
    
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
    public DashboardApplication( AggregateRefreshTokenInfos token) {
        this.token = token;
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
