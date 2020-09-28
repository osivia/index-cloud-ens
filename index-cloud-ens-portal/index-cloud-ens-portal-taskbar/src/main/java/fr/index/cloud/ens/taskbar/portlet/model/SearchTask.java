package fr.index.cloud.ens.taskbar.portlet.model;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Search task.
 *
 * @author Cédric Krommenhoek
 * @see Task
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SearchTask extends Task {

    /**
     * Advanced search.
     */
    private Task advancedSearch;


    /**
     * Constructor.
     */
    public SearchTask() {
        super();
    }


    @Override
    public boolean isSearch() {
        return true;
    }

    public Task getAdvancedSearch() {
        return advancedSearch;
    }

    public void setAdvancedSearch(Task advancedSearch) {
        this.advancedSearch = advancedSearch;
    }
}
