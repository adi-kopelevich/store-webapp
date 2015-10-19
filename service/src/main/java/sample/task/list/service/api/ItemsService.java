package sample.task.list.service.api;

import sample.task.list.service.model.TaskItem;

import java.util.List;

/**
 * Created by kopelevi on 04/09/2015.
 */
public interface ItemsService {

    List<TaskItem> getAllItems();

    TaskItem getItem(int itemId);

    void addItem(TaskItem item);

    void removeItem(int itemId);

    void updateItem(TaskItem item);

    void clearAll();

}
