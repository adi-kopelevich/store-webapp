package sample.task.list.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by kopelevi on 04/09/2015.
 */
public class ItemsServiceImpl implements ItemsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ItemsServiceImpl.class);

    private static final boolean IS_MONGO_ENABLED = ServiceConfiguration.isMongoEnabled();

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
        try {
            return persistency.getItems();
        } catch (Exception e) {
            throw new ItemServiceException(ItemServiceErrorMessages.FAILED_TO_GET_ALL, e);
        }
    }

    public TaskItem getItem(int itemId) {
        TaskItem taskItem;
        try {
            taskItem = persistency.getItem(itemId);
        } catch (Exception e) {
            throw new ItemServiceException(ItemServiceErrorMessages.FAILED_TO_GET_ITEM + itemId, e);
        }
        if (taskItem == null) {
            throw new ItemServiceItemNotFoundException(itemId);
        }
        return taskItem;
    }

    public void addItem(TaskItem taskItem) {
        try {
            persistency.putItem(taskItem);
        } catch (Exception e) {
            throw new ItemServiceException(ItemServiceErrorMessages.FAILED_TO_ADD_ITEM + taskItem.toString(), e);
        }
    }

    public void removeItem(int itemId) {
        try {
            persistency.removeItem(itemId);
        } catch (Exception e) {
            throw new ItemServiceException(ItemServiceErrorMessages.FAILED_TO_REMOVE_ITEM + itemId, e);
        }
    }

    public void updateItem(TaskItem taskItem) {
        try {
            persistency.putItem(taskItem);
        } catch (Exception e) {
            throw new ItemServiceException(ItemServiceErrorMessages.FAILED_TO_UPDATE_ITEM + taskItem.toString(), e);
        }
    }

    public void clearAll() {
        try {
            persistency.clear();
        } catch (Exception e) {
            throw new ItemServiceException(ItemServiceErrorMessages.FAILED_TO_CLEAR_ALL, e);
        }
    }


}
