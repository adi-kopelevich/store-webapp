package sample.grocery.store.service;

import sample.grocery.store.service.pojo.StoreItem;

import java.util.List;

/**
 * Created by kopelevi on 04/09/2015.
 */
public interface ItemsService {

    List<StoreItem> getAllItems();

    StoreItem getItem(int itemId);

    void addItem(StoreItem item);

    void removeItem(int itemId);

    void updateItem(StoreItem item);

    void clearAll();

}
