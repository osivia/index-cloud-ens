package fr.index.cloud.ens.ws.commands;

import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.PropertyList;
import org.nuxeo.ecm.automation.client.model.PropertyMap;

public class CommandUtils {
    

    /**
     * Add new item to PropertyList
     * 
     * @param properties
     * @param item
     * @param listName
     */
    
    
    public static void addToList(Document doc, PropertyMap properties, String item, String listName) {

        if( item != null)    {
            PropertyList items = doc.getProperties().getList(listName);        
            if( items == null)
                items = new PropertyList();
            if( !items.getList().contains(item))    {
                StringBuffer sItems = new StringBuffer();
                for (Object sLevel : items.getList())  {
                    if( sItems.length()> 0)
                        sItems.append(",");
                    sItems.append(sLevel);
                }
                
                if( sItems.length()> 0)
                    sItems.append(",");
                sItems.append(item);
                properties.set( listName, sItems.toString());                
            }
        }
    }


}
