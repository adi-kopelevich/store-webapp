package sample.grocery.store.service;

import sample.grocery.store.service.pojo.TaskItem;
import sample.grocery.store.service.pojo.TaskList;

/**
 * Created by kopelevi on 04/09/2015.
 */
public interface ItemsService {

    TaskList getAllItems();

    TaskItem getItem(int itemId);

    void addItem(TaskItem item);

    void removeItem(int itemId);

    void updateItem(TaskItem item);

    void clearAll();

}
