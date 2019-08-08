package fr.index.cloud.ens.ws.commands;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.model.PropertyList;


/**
 * The Class CommandUtils.
 */
public class CommandUtils {


    /**
     * Adds  to list.
     *
     * @param properties the properties
     * @param item the item
     * @return true, if successful
     */
    public static boolean addToList(PropertyList properties, String item) {

        boolean inserted = false;

        // Insert items
        if (StringUtils.isNotEmpty(item) && !properties.getList().contains(item)) {
            properties.list().add(item);
            inserted = true;
        }

        return inserted;
    }


    /**
     * Removes from list.
     *
     * @param properties the properties
     * @param item the item
     * @return true, if successful
     */
    public static boolean removeFromList(PropertyList properties, String item) {

        boolean removed = false;

        if (StringUtils.isNotEmpty(item) && properties.getList().contains(item)) {
            properties.list().remove(item);
            removed = true;
        }

        return removed;
    }

    /**
     * Convert to string.
     *
     * @param properties the properties
     * @return the string
     */
    public static String convertToString(PropertyList properties) {

        StringBuffer sItems = new StringBuffer();


        for (Object sValue : properties.getList()) {

            if (sItems.length() > 0)
                sItems.append(",");
            sItems.append(sValue);


        }
        return sItems.toString();

    }


}
