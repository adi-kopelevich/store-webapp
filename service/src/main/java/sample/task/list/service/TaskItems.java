package sample.task.list.service;

import java.util.List;

/**
 * Created by kopelevi on 16/10/2015.
 */
public class TaskItems {

    private List<TaskItem> items;

    public TaskItems() { //for serialization
    }

    public TaskItems(List<TaskItem> items) { //for serialization
        this.items = items;
    }

    public List<TaskItem> getItems() {
        return items;
    }

    public void setItems(List<TaskItem> items) {
        this.items = items;
    }
}
