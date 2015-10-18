package sample.task.list.service.dao;

import com.google.gson.Gson;
import com.mongodb.*;
import com.mongodb.util.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sample.task.list.service.pojo.TaskItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kopelevi on 12/10/2015.
 */
public class ItemDAOMongoDBImpl implements ItemDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(ItemDAOMongoDBImpl.class);

    private static final String MONGO_HOST_ENV_KEY = "mongo.host";
    private static final String MONGO_PORT_ENV_KEY = "mongo.port";
    private static final String MONGO_ENABLED_ENV_KEY = "mongo.enabled";

    private static final String DEFAULT_MONGO_HOST = "localhost";
    private static final String DEFAULT_MONGO_PORT = "27017";
    private static final String DEFAULT_MONGO_ENABLED = "false";

    private static final String DB_NAME = "taskLists";
    private static final String COLLECTION_NAME = "items";
    private static final String UNIQUE_INDEX = "id";

    private final DBCollection collection;

    private static final ItemDAOMongoDBImpl INSTANCE = new ItemDAOMongoDBImpl();

    private ItemDAOMongoDBImpl() {
        this.collection = initDBCollection();
    }

    public static ItemDAOMongoDBImpl getInstance() {
        return INSTANCE;
    }

    private DBCollection initDBCollection() {
        DBCollection collection = null;
        try {
            String mongoHost = getMongoProperty(MONGO_HOST_ENV_KEY, DEFAULT_MONGO_HOST);
            String mongoPort = getMongoProperty(MONGO_PORT_ENV_KEY, DEFAULT_MONGO_PORT);
            LOGGER.debug("Going to initiate MongoDB client with host= " + mongoHost + "and port=" + mongoPort);

            MongoClient mongoClient = new MongoClient(mongoHost, Integer.valueOf(mongoPort));
            mongoClient.setWriteConcern(WriteConcern.ACKNOWLEDGED);
            DB db = mongoClient.getDB(DB_NAME);
            collection = db.getCollection(COLLECTION_NAME);
            LOGGER.debug("Initiated MongoDB collection= " + COLLECTION_NAME + ", with DB=" + DB_NAME);
            collection.createIndex(new BasicDBObject(UNIQUE_INDEX, 1), new BasicDBObject("unique", true));
            LOGGER.debug("Created unique index - " + UNIQUE_INDEX);
        } catch (Exception e) {
            logAndThrowErr("Failed to init MongoDB collection, aborting ItemDAL init...", e);
        }
        return collection;
    }

    @Override
    public TaskItem getItem(int itemId) {
        TaskItem ret = null;
        try {
            BasicDBObject searchQuery = getUniqueContactSearchQuery(itemId);
            DBCursor queryResult = collection.find(searchQuery);
            if (queryResult.hasNext()) {
                DBObject retObj = queryResult.next();
                ret = toTaskItem(retObj.toString());
                LOGGER.debug("Found: " + retObj + " for query: " + searchQuery);
            } else {
                ret = null;
                LOGGER.debug("Failed to find object for query: " + searchQuery);
            }
        } catch (Exception e) {
            logAndThrowErr("Failed to get item with with ID: " + itemId, e);
        }
        return ret;
    }

    @Override
    public List<TaskItem> getItems() {
        List<TaskItem> taskItems = null;
        try {
            taskItems = new ArrayList<TaskItem>();
            DBCursor queryResult = collection.find();
            while (queryResult.hasNext()) {
                String taskItemJsonFormat = queryResult.next().toString();
                TaskItem taskItem = toTaskItem(taskItemJsonFormat);
                taskItems.add(taskItem);
            }
        } catch (Exception e) {
            logAndThrowErr("Failed to get all items", e);
        }
        return taskItems;
    }

    @Override
    public void putItem(TaskItem item) {
        try {
            collection.insert(toDBObject(item));
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Item inserted successfully: " + toDBObject(item).toString());
            }
        } catch (DuplicateKeyException duplicateKeyException) {
            try {
                WriteResult result = collection.update(getUniqueContactSearchQuery(item.getId()), toDBObject(item));
                if (result.getN() == 1) {
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("Item updated successfully: " + toDBObject(item).toString());
                    }
                } else {
                    logAndThrowErr("Failed to update, resource with ID: " + item.getId() + " was not found, probably removed by another thread...");
                }
            } catch (Exception e) {
                logAndThrowErr("Failed to update item with ID: " + item.getId(), e);
            }
        } catch (Exception e) {
            logAndThrowErr("Failed to insert item with ID: " + item.getId(), e);
        }
    }

    @Override
    public void removeItem(int itemId) {
        try {
            BasicDBObject searchQuery = getUniqueContactSearchQuery(itemId);
            collection.remove(searchQuery);
        } catch (Exception e) {
            logAndThrowErr("Failed to delete item with ID: " + itemId, e);
        }
    }

    @Override
    public void clear() {
        try {
            collection.drop();
            collection.createIndex(new BasicDBObject(UNIQUE_INDEX, 1), new BasicDBObject("unique", true));
        } catch (Exception e) {
            logAndThrowErr("Failed to clear collection  " + collection.getName(), e);
        }
    }

    private static String getMongoProperty(String envKey, String defVal) {
        String mongoPropertyValue = System.getProperty(envKey);
        if (mongoPropertyValue == null) {
            mongoPropertyValue = defVal;
            LOGGER.debug("Failed to retrieve " + envKey + " from environment variables, going to use default: " + defVal);
        }
        return mongoPropertyValue;
    }

    private DBObject toDBObject(TaskItem taskItem) {
        String json = new Gson().toJson(taskItem);
        return (DBObject) JSON.parse(json);
    }

    private TaskItem toTaskItem(String taskItemJson) {
        return new Gson().fromJson(taskItemJson, TaskItem.class);
    }

    private BasicDBObject getUniqueContactSearchQuery(int taskItemId) {
        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.put(UNIQUE_INDEX, taskItemId);
        return searchQuery;
    }

    private void logAndThrowErr(String errMsg) {
        logAndThrowErr(errMsg, null);
    }

    private void logAndThrowErr(String errMsg, Exception e) {
        if (e == null) {
            LOGGER.error(errMsg);
            throw new RuntimeException(errMsg);
        } else {
            LOGGER.error(errMsg, e);
            throw new RuntimeException(errMsg, e);
        }
    }

    public static boolean isMongoConfEnabled() {
        String mongoEnabledString = getMongoProperty(MONGO_ENABLED_ENV_KEY, DEFAULT_MONGO_ENABLED);
        return Boolean.valueOf(mongoEnabledString);
    }

}
