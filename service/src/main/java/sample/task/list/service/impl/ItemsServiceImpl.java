package sample.task.list.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sample.task.list.ApplicationConfiguration;
import sample.task.list.service.api.ItemsService;
import sample.task.list.service.dao.ItemDAO;
import sample.task.list.service.dao.ItemDAOMapImpl;
import sample.task.list.service.dao.ItemDAOMongoDBImpl;
import sample.task.list.service.model.TaskItem;

import java.util.List;

/**
 * Created by kopelevi on 04/09/2015.
 */
public class ItemsServiceImpl implements ItemsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ItemsServiceImpl.class);

    private static final boolean IS_MONGO_ENABLED = ApplicationConfiguration.isMongoEnabled();
    private final ItemDAO persistency;

    public ItemsServiceImpl() {
        ItemDAO itemDAO;
        if (IS_MONGO_ENABLED) {
            LOGGER.debug("MongoDB conf is set to enabled, going to use mongoDB store...");
            itemDAO = ItemDAOMongoDBImpl.getInstance();
        } else {
            LOGGER.debug("MongoDB conf is set to disabled, going to use local map store...");
            itemDAO = ItemDAOMapImpl.getInstance();
        }
        this.persistency = itemDAO;
    }

    protected ItemsServiceImpl(ItemDAO itemDAO) {
        this.persistency = itemDAO;
    }

    public List<TaskItem> getAllItems() {
        return persistency.getItems();
    }

    public TaskItem getItem(int itemId) {
        return persistency.getItem(itemId);
    }

    public void addItem(TaskItem taskItem) {
        persistency.putItem(taskItem);
    }

    public void removeItem(int itemId) {
        persistency.removeItem(itemId);
    }

    public void updateItem(TaskItem taskItem) {
        persistency.putItem(taskItem);
    }

    public void clearAll() {
        persistency.clear();
    }


}
