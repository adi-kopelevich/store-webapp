package sample.task.list.service.model;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by kopelevi on 16/10/2015.
 */

@XmlRootElement
public class TaskList {

    private List<TaskItem> items;

    public TaskList() { //for serialization
    }

    public TaskList(List<TaskItem> items) { //for serialization
        this.items = items;
    }

    public List<TaskItem> getItems() {
        return items;
    }

    public void setItems(List<TaskItem> items) {
        this.items = items;
    }
}
