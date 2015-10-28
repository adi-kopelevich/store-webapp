package sample.task.list.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.*;
import com.mongodb.util.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kopelevi on 12/10/2015.
 */
public class ItemDAOMongoDBImpl implements ItemDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(ItemDAOMongoDBImpl.class);

    private static final String DB_NAME = "taskLists";
    private static final String COLLECTION_NAME = "items";
    private static final String UNIQUE_INDEX = "id";

    private final DBCollection collection;  //thread safe...

    private static final ItemDAOMongoDBImpl INSTANCE = new ItemDAOMongoDBImpl();

    // hide constructor
    private ItemDAOMongoDBImpl() {
        this.collection = initDBCollection();
    }

    public static ItemDAOMongoDBImpl getInstance() {
        return INSTANCE;
    }

    private DBCollection initDBCollection() {
        try {
            String mongoHost = ServiceConfiguration.getMongoHost();
            String mongoPort = ServiceConfiguration.getMongoPort();
            LOGGER.debug("Going to initiate MongoDB client with host= " + mongoHost + "and port=" + mongoPort);

            MongoClient mongoClient = new MongoClient(mongoHost, Integer.valueOf(mongoPort));
            mongoClient.setWriteConcern(WriteConcern.ACKNOWLEDGED);
            DB db = mongoClient.getDB(DB_NAME);
            DBCollection collection = db.getCollection(COLLECTION_NAME);
            LOGGER.debug("Initiated MongoDB collection= " + COLLECTION_NAME + ", with DB=" + DB_NAME);
            collection.createIndex(new BasicDBObject(UNIQUE_INDEX, 1), new BasicDBObject("unique", true));
            LOGGER.debug("Created unique index - " + UNIQUE_INDEX);
            return collection;
        } catch (Exception e) {
            throw new ItemDAOException("Failed to init MongoDB collection, aborting ItemDAL init...", e);
        }
    }

    @Override
    public TaskItem getItem(int itemId) {
        try {
            TaskItem ret;
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
            return ret;
        } catch (Exception e) {
            throw new ItemDAOException("Failed to get item with with ID: " + itemId, e);
        }
    }

    @Override
    public List<TaskItem> getItems() {
        try {
            List<TaskItem> taskItems = new ArrayList<TaskItem>();
            DBCursor queryResult = collection.find();
            while (queryResult.hasNext()) {
                String taskItemJsonFormat = queryResult.next().toString();
                TaskItem taskItem = toTaskItem(taskItemJsonFormat);
                taskItems.add(taskItem);
            }
            return taskItems;
        } catch (Exception e) {
            throw new ItemDAOException("Failed to get all items", e);
        }
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
                    throw new ItemDAOException("Failed to update, resource with ID: " + item.getId() + " was not found, probably removed by another thread...");
                }
            } catch (Exception e) {
                throw new ItemDAOException("Failed to update item with ID: " + item.getId(), e);
            }
        } catch (Exception e) {
            throw new ItemDAOException("Failed to insert item with ID: " + item.getId(), e);
        }
    }

    @Override
    public void removeItem(int itemId) {
        try {
            BasicDBObject searchQuery = getUniqueContactSearchQuery(itemId);
            collection.remove(searchQuery);
        } catch (Exception e) {
            throw new ItemDAOException("Failed to delete item with ID: " + itemId, e);
        }
    }

    @Override
    public void clear() {
        try {
            collection.drop();
            collection.createIndex(new BasicDBObject(UNIQUE_INDEX, 1), new BasicDBObject("unique", true));
        } catch (Exception e) {
            throw new ItemDAOException("Failed to clear collection  " + collection.getName(), e);
        }
    }

    private DBObject toDBObject(TaskItem taskItem) throws IOException {
        String taskItemJson = new ObjectMapper()
                .writeValueAsString(taskItem);
        return (DBObject) JSON.parse(taskItemJson);
    }

    private TaskItem toTaskItem(String taskItemJson) throws IOException {
        return new ObjectMapper()
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .readValue(taskItemJson, TaskItem.class);
    }

    private BasicDBObject getUniqueContactSearchQuery(int taskItemId) {
        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.put(UNIQUE_INDEX, taskItemId);
        return searchQuery;
    }

}
