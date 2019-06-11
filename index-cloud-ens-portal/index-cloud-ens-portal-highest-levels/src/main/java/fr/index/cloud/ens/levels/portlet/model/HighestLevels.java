package fr.index.cloud.ens.levels.portlet.model;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * Levels java-bean.
 *
 * @author Cédric Krommenhoek
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class HighestLevels {

    /**
     * Highest levels items.
     */
    private Set<HighestLevelsItem> items;


    /**
     * Constructor.
     */
    public HighestLevels() {
        super();
    }


    public Set<HighestLevelsItem> getItems() {
        return items;
    }

    public void setItems(Set<HighestLevelsItem> items) {
        this.items = items;
    }

}
