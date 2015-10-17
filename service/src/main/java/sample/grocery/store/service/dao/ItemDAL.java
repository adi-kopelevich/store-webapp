package sample.grocery.store.service.dao;

import sample.grocery.store.service.pojo.TaskItem;

import java.util.List;

/**
 * Created by kopelevi on 04/09/2015.
 */
public interface ItemDAL {

    TaskItem getItem(int itemId);

    List<TaskItem> getItems();

    void putItem(TaskItem item);

    void removeItem(int itemId);

    void clear();
}
