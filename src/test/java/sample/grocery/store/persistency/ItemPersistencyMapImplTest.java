package sample.grocery.store.persistency;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import sample.grocery.store.service.pojo.StoreItem;

import java.util.Arrays;
import java.util.List;

/**
 * Created by kopelevi on 04/09/2015.
 */
public class ItemPersistencyMapImplTest {

    ItemPersistency store;

    @Before
    public void setUp() throws Exception {
        store = ItemPersistencyMapImpl.getInstance();
        store.clear();
    }

    @After
    public void tearDown() throws Exception {
        store = null;
    }

    @Test
    public void whenAddItemThenItIsRetrivable() throws Exception {
        int itemId = 3;
        String itemName = "Milk";
        String itemBrand = "Tnuva";
        int itemPrice = 10;
        int itemqQuantity = 200;
        List<String> itemTags = Arrays.asList("star", "sale");

        StoreItem item = new StoreItem(itemId, itemName, itemBrand, itemPrice, itemqQuantity, itemTags);

        store.putItem(item);
        StoreItem retItem = store.getItem(itemId);

        Assert.assertEquals(item, retItem);
    }

    @Test
    public void whenGettingNotExistsThenNullWillBeReturned() throws Exception {
        int itemId = 3;
        StoreItem retItem = store.getItem(itemId);
        Assert.assertEquals(null, retItem);
    }

    @Test
    public void whenDeletingItemThenItIsNotRetrivable() throws Exception {
        int itemId = 3;
        String itemName = "Milk";
        String itemBrand = "Tnuva";
        int itemPrice = 10;
        int itemqQuantity = 200;
        List<String> itemTags = Arrays.asList("star", "sale");

        StoreItem item = new StoreItem(itemId, itemName, itemBrand, itemPrice, itemqQuantity, itemTags);

        store.putItem(item);
        store.removeItem(itemId);
        StoreItem retItem = store.getItem(itemId);

        Assert.assertEquals(null, retItem);
    }

    @Test
    public void whenUpdateingAnItemThenChangesAreRetrivable() throws Exception {
        int itemId = 3;
        String itemName = "Milk";
        String itemBrand = "Tnuva";
        int itemPrice = 10;
        int itemqQuantity = 200;
        List<String> itemTags = Arrays.asList("star", "sale");

        StoreItem originalItem = new StoreItem(itemId, itemName, itemBrand, itemPrice, itemqQuantity, itemTags);
        store.putItem(originalItem);

        StoreItem updatedItem = new StoreItem(itemId, itemName, itemBrand, itemPrice, itemqQuantity + 100, itemTags);
        store.putItem(updatedItem);

        StoreItem retItem = store.getItem(itemId);
        Assert.assertEquals(updatedItem, retItem);
    }

    @Test
    public void whenAddingMultiItemsThenAllItemsAreReturned() throws Exception {
        int firstItemId = 3;
        String firstItemName = "Milk";
        String firstItemBrand = "Tnuva";
        int firstItemPrice = 10;
        int firstItemqQuantity = 200;
        List<String> firstItemTags = Arrays.asList("star", "sale");

        int secondItemId = 5;
        String sencondItemName = "Bread";
        String secondItemBrand = "Erez";
        int secondItemPrice = 5;
        int secondItemqQuantity = 300;
        List<String> secondItemTags = Arrays.asList("quality", "holiday");

        StoreItem firstItem = new StoreItem(firstItemId, firstItemName, firstItemBrand, firstItemPrice, firstItemqQuantity, firstItemTags);
        StoreItem secondItem = new StoreItem(secondItemId, sencondItemName, secondItemBrand, secondItemPrice, secondItemqQuantity, secondItemTags);

        store.putItem(firstItem);
        store.putItem(secondItem);
        List<StoreItem> retItems = store.getItems();

        Assert.assertEquals(2, retItems.size());
        Assert.assertEquals(true, retItems.contains(firstItem));
        Assert.assertEquals(true, retItems.contains(secondItem));
    }

}
