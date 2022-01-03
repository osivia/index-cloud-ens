package fr.index.cloud.ens.ws.nuxeo;

import java.util.Map;

import org.apache.commons.collections.keyvalue.AbstractMapEntry;
import org.apache.commons.collections.map.ListOrderedMap;
import org.nuxeo.ecm.automation.client.Constants;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.model.DocRef;
import org.nuxeo.ecm.automation.client.model.OperationInput;
import org.nuxeo.ecm.automation.client.model.PropertyMap;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoQueryFilter;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoQueryFilterContext;
import net.sf.json.JSONObject;


/**
 * Generic Drive command proxy
 * 
 * @author Jean-Sébastien
 */
public class GetProxyCommand implements INuxeoCommand {

    private String command;
    private String requestBody;
    private OperationInput input;
    private Map<String,String> parameters = null;
    
    

    
    /**
     * Setter for parameters.
     * @param parameters the parameters to set
     */
    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    
    public void setOperationInput(OperationInput input) {

        this.input = input;
    }
    
    public GetProxyCommand(String command, String requestBody) {
        super();
        this.command = command;
        this.requestBody = requestBody;

    }

    @Override
    public Object execute(Session nuxeoSession) throws Exception {

        // Operation request
        OperationRequest request = nuxeoSession.newRequest(this.command);
        
        if( requestBody != null)    {
            final JSONObject obj = JSONObject.fromObject(requestBody);
            JSONObject params =  obj.getJSONObject("params");
    
            for( Object child : params.entrySet())  {
                if( child instanceof AbstractMapEntry)  {
                    AbstractMapEntry entry = ((AbstractMapEntry) child);
                    request.getParameters().put((String)entry.getKey(), entry.getValue().toString());
                }
            }
        }
        
        if( parameters != null) {
            for (String key: parameters.keySet()) {
                request.getParameters().put(key, parameters.get(key));
            }
        }

        if(input != null)   {
            request.setInput(input);
        }

        return request.execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.getClass().getSimpleName());
        builder.append("/"+command);
        builder.append("/"+requestBody);

        return builder.toString();
    }
}
