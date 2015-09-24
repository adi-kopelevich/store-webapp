package sample.grocery.store.dao;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import sample.grocery.store.service.pojo.StoreItem;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * Created by kopelevi on 04/09/2015.
 */
public class ItemDAOMapImplTest {

    ItemDAO store;

    @Before
    public void setUp() throws Exception {
        store = ItemDAOMapImpl.getInstance();
        store.clear();
    }

    @After
    public void tearDown() throws Exception {
        store = null;
    }

    @Test
    public void whenAddItemThenItIsRetrivable() throws Exception {
        int itemId = new Random().nextInt();
        String itemName = UUID.randomUUID().toString();
        String itemBrand = UUID.randomUUID().toString();
        int itemPrice = new Random().nextInt();
        int itemqQuantity = new Random().nextInt();
        ;
        List<String> itemTags = Arrays.asList(UUID.randomUUID().toString(), UUID.randomUUID().toString());

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
        int itemId = new Random().nextInt();
        String itemName = UUID.randomUUID().toString();
        String itemBrand = UUID.randomUUID().toString();
        int itemPrice = new Random().nextInt();
        int itemqQuantity = new Random().nextInt();
        List<String> itemTags = Arrays.asList(UUID.randomUUID().toString(), UUID.randomUUID().toString());

        StoreItem item = new StoreItem(itemId, itemName, itemBrand, itemPrice, itemqQuantity, itemTags);

        store.putItem(item);
        store.removeItem(itemId);
        StoreItem retItem = store.getItem(itemId);

        Assert.assertEquals(null, retItem);
    }

    @Test
    public void whenUpdateingAnItemThenChangesAreRetrivable() throws Exception {
        int itemId = new Random().nextInt();
        String itemName = UUID.randomUUID().toString();
        String itemBrand = UUID.randomUUID().toString();
        int itemPrice = new Random().nextInt();
        int itemqQuantity = new Random().nextInt();
        List<String> itemTags = Arrays.asList(UUID.randomUUID().toString(), UUID.randomUUID().toString());

        StoreItem originalItem = new StoreItem(itemId, itemName, itemBrand, itemPrice, itemqQuantity, itemTags);
        store.putItem(originalItem);

        StoreItem updatedItem = new StoreItem(itemId, itemName, itemBrand, itemPrice, itemqQuantity + 100, itemTags);
        store.putItem(updatedItem);

        StoreItem retItem = store.getItem(itemId);
        Assert.assertEquals(updatedItem, retItem); // todo replace equals...
    }

    @Test
    public void whenAddingMultiItemsThenAllItemsAreReturned() throws Exception {
        int firstItemId = new Random().nextInt();
        String firstItemName = UUID.randomUUID().toString();
        String firstItemBrand = UUID.randomUUID().toString();
        int firstItemPrice = new Random().nextInt();
        int firstItemqQuantity = new Random().nextInt();
        List<String> firstItemTags = Arrays.asList(UUID.randomUUID().toString(), UUID.randomUUID().toString());

        int secondItemId = firstItemId + new Random().nextInt();
        String secondItemName = UUID.randomUUID().toString();
        String secondItemBrand = UUID.randomUUID().toString();
        int secondItemPrice = new Random().nextInt();
        int secondItemqQuantity = new Random().nextInt();
        List<String> secondItemTags = Arrays.asList(UUID.randomUUID().toString(), UUID.randomUUID().toString());

        StoreItem firstItem = new StoreItem(firstItemId, firstItemName, firstItemBrand, firstItemPrice, firstItemqQuantity, firstItemTags);
        StoreItem secondItem = new StoreItem(secondItemId, secondItemName, secondItemBrand, secondItemPrice, secondItemqQuantity, secondItemTags);

        store.putItem(firstItem);
        store.putItem(secondItem);
        List<StoreItem> retItems = store.getItems();

        Assert.assertEquals(2, retItems.size());
        Assert.assertEquals(true, retItems.contains(firstItem));
        Assert.assertEquals(true, retItems.contains(secondItem));
    }

}
