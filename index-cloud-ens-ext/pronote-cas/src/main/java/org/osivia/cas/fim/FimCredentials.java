package org.osivia.cas.fim;

import java.util.Map;
import org.jasig.cas.authentication.AbstractCredential;

public class FimCredentials extends AbstractCredential {

    /**
     * Unique Id for Serialization
     */
    private static final long serialVersionUID = -6535869729412406133L;


    private final String id;
    private final Map<String, Object> attributes;    
    

    
    public Map<String, Object> getAttributes() {
        return attributes;
    }


    public FimCredentials( final String id, Map<String, Object> attributes) {

        this.id = id;
        this.attributes = attributes;
    }
    

//    public String getUsername() {
//        return this.username;
//    }
    
    public String toString() {
        return "id: " + this.id;
}


	public String getId() {
		return this.id;
	}

}
