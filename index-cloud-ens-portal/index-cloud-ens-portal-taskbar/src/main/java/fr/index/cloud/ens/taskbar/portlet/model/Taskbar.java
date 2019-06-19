package fr.index.cloud.ens.taskbar.portlet.model;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Taskbar java-bean.
 *
 * @author Cédric Krommenhoek
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Taskbar {

    /**
     * Add dropdown items.
     */
    private List<AddDropdownItem> addDropdownItems;

    /**
     * Tasks.
     */
    private List<Task> tasks;


    /**
     * Constructor.
     */
    public Taskbar() {
        super();
    }


    public List<AddDropdownItem> getAddDropdownItems() {
        return addDropdownItems;
    }

    public void setAddDropdownItems(List<AddDropdownItem> addDropdownItems) {
        this.addDropdownItems = addDropdownItems;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

}
