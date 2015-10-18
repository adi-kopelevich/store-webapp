package sample.task.list.service.dao;

import sample.task.list.service.model.TaskItem;

import java.util.List;

/**
 * Created by kopelevi on 04/09/2015.
 */
public interface ItemDAO {

    TaskItem getItem(int itemId);

    List<TaskItem> getItems();

    void putItem(TaskItem item);

    void removeItem(int itemId);

    void clear();
}
