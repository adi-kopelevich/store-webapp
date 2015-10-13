package sample.grocery.store.service.dao;

import com.google.gson.Gson;
import com.mongodb.*;
import com.mongodb.util.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sample.grocery.store.service.pojo.StoreItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kopelevi on 12/10/2015.
 */
public class ItemDALMongoDBImpl implements ItemDAL {

    private final Logger LOGGER = LoggerFactory.getLogger(ItemDALMongoDBImpl.class);

    private static final String MONGO_HOST_ENV_KEY = "mongo.host";
    private static final String MONGO_PORT_ENV_KEY = "mongo.port";

    private static final String DEFAULT_MONGO_HOST = "localhost";
    private static final String DEFAULT_MONGO_PORT = "27017";
    private static final String DB_NAME = "mystore";
    private static final String COLLECTION_NAME = "items";
    private static final String UNIQUE_INDEX = "id";

    private final DBCollection collection;

    private static final ItemDALMongoDBImpl INSTANCE = new ItemDALMongoDBImpl();

    private ItemDALMongoDBImpl() {
        this.collection = initDBCollection();
        clear();
    }

    public static ItemDALMongoDBImpl getInstance() {
        return INSTANCE;
    }

    private DBCollection initDBCollection(){
        String mongoHost = System.getProperty(MONGO_HOST_ENV_KEY);
        if (mongoHost == null) {
            mongoHost = DEFAULT_MONGO_HOST;
        }

        String mongoPort = System.getProperty(MONGO_PORT_ENV_KEY);
        if (mongoPort == null) {
            mongoPort = DEFAULT_MONGO_PORT;
        }

        MongoClient mongoClient = new MongoClient(mongoHost, Integer.valueOf(mongoPort));
        DB db = mongoClient.getDB(DB_NAME);
        return db.getCollection(COLLECTION_NAME);
    }

    private DBObject toDBObject(StoreItem storeItem) {
        String json = new Gson().toJson(storeItem);
        return (DBObject) JSON.parse(json);
    }

    private StoreItem toStoreItem(String storeItemJson) {
        return new Gson().fromJson(storeItemJson, StoreItem.class);
    }

    private BasicDBObject getUniqueContactSearchQuery(int storeItemId) {
        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.put(UNIQUE_INDEX, storeItemId);
        return searchQuery;
    }

    @Override
    public StoreItem getItem(int itemId) {
        StoreItem ret;
        BasicDBObject searchQuery = getUniqueContactSearchQuery(itemId);
        DBCursor queryResult = collection.find(searchQuery);
        if (queryResult.hasNext()) {
            DBObject retObj = queryResult.next();
            ret = toStoreItem(retObj.toString());
        } else {
            ret = null;
        }
        return ret;
    }

    @Override
    public List<StoreItem> getItems() {
        List<StoreItem> storeItems = new ArrayList<StoreItem>();
        DBCursor queryResult = collection.find();
        while (queryResult.hasNext()) {
            String storeItemJsonFormat = queryResult.next().toString();
            StoreItem storeItem = toStoreItem(storeItemJsonFormat);
            storeItems.add(storeItem);
        }
        return storeItems;
    }

    public List<StoreItem> getItems(int... storeItemIDs) {
        List<StoreItem> storeItems = new ArrayList<StoreItem>();
        for (int id : storeItemIDs) {
            StoreItem storeItem = getItem(id);
            if (storeItem != null) {
                storeItems.add(storeItem);
            }
        }
        return storeItems;
    }

    @Override
    public void putItem(StoreItem item) {
        WriteResult result;
        try {
            result = collection.insert(toDBObject(item));
        } catch (DuplicateKeyException e) {
            result = collection.update(getUniqueContactSearchQuery(item.getId()), toDBObject(item));
            if (result.getN() != 1) {
                throw new RuntimeException("Failed to update, resource with ID: " + item.getId() + " was not found");
            }
        }
    }

    @Override
    public void removeItem(int itemId) {
        BasicDBObject searchQuery = getUniqueContactSearchQuery(itemId);
        collection.remove(searchQuery);
    }

    @Override
    public void clear() {
        collection.drop();
        collection.createIndex(new BasicDBObject(UNIQUE_INDEX, 1), new BasicDBObject("unique", true));
    }

}
