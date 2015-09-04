package sample.grocery.store.service;

import sample.grocery.store.service.pojo.StoreItem;

import java.util.List;

/**
 * Created by kopelevi on 04/09/2015.
 */
public interface ItemsService {

    public List<StoreItem> getAllItems();

    public StoreItem getItem(int itemId);

    public void addItem(StoreItem item);

    public void removeItem(int itemId);

    public void updateItem(StoreItem item);

    public void clearAll();

}
