package sample.grocery.store.persistency;

import sample.grocery.store.service.pojo.StoreItem;

import java.util.List;

/**
 * Created by kopelevi on 04/09/2015.
 */
public interface ItemPersistency {

    public StoreItem getItem(int itemId);

    public List<StoreItem> getItems();

    public void putItem(StoreItem item);

    public void removeItem(int itemId);

    public void clear();
}
