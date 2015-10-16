package sample.grocery.store.service;

import sample.grocery.store.service.pojo.StoreItem;
import sample.grocery.store.service.pojo.StoreItems;

/**
 * Created by kopelevi on 04/09/2015.
 */
public interface ItemsService {

    StoreItems getAllItems();

    StoreItem getItem(int itemId);

    void addItem(StoreItem item);

    void removeItem(int itemId);

    void updateItem(StoreItem item);

    void clearAll();

}
