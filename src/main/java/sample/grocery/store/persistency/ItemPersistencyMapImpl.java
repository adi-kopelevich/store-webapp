package sample.grocery.store.persistency;

import sample.grocery.store.service.pojo.StoreItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kopelevi on 04/09/2015.
 */
public class ItemPersistencyMapImpl implements ItemPersistency {

    private Map<Integer, StoreItem> itemsMap;

    private static ItemPersistencyMapImpl _instance;


    private ItemPersistencyMapImpl() {
        itemsMap = new HashMap<Integer, StoreItem>();
    }

    public static synchronized ItemPersistency getInstance() {
        if (_instance == null) {
            _instance = new ItemPersistencyMapImpl();
        }
        return _instance;
    }

    public StoreItem getItem(int itemId) {
        return itemsMap.get(itemId);
    }

    public List<StoreItem> getItems() {
        return new ArrayList(itemsMap.values());
    }

    public void putItem(StoreItem item) {
        itemsMap.put(item.getId(), item);
    }

    public void removeItem(int itemId) {
        itemsMap.remove(itemId);
    }

    public void clear() {
        itemsMap.clear();
    }
}
