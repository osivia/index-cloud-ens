package fr.index.cloud.ens.levels.portlet.model.comparator;

import fr.index.cloud.ens.levels.portlet.model.HighestLevelsItem;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Comparator;

/**
 * Highest levels items comparator.
 * @author Cédric Krommenhoek
 * @see Comparator
 * @see HighestLevelsItem
 */
@Component
public class HighestLevelsItemsComparator implements Comparator<HighestLevelsItem> {

    /**
     * Constructor.
     */
    public HighestLevelsItemsComparator() {
        super();
    }


    @Override
    public int compare(HighestLevelsItem item1, HighestLevelsItem item2) {
        int result;

        if (item1 == null) {
            result = -1;
        } else if (item2 == null) {
            result = 1;
        } else if (item1.getCount() == item2.getCount()) {
            result = item1.getLabel().compareTo(item2.getLabel());
        } else {
            result = item2.getCount() - item1.getCount();
        }

        return result;
    }

}
