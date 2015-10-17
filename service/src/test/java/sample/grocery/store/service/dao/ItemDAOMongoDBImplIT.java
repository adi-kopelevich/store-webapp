package sample.grocery.store.service.dao;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import sample.grocery.store.service.pojo.TaskItem;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * Created by kopelevi on 13/10/2015.
 */
public class ItemDAOMongoDBImplIT {

    private static final ItemDAL persistency = ItemDALMongoDBImpl.getInstance();

    @Before
    public void setUp() throws Exception {
        persistency.clear();
    }

    @After
    public void tearDown() throws Exception {
        persistency.clear();
    }

    @Test
    public void whenAddItemThenItIsRetrivable() throws Exception {
        int itemId = new Random().nextInt();
        String itemName = UUID.randomUUID().toString();
        String itemBrand = UUID.randomUUID().toString();
        int itemPrice = new Random().nextInt();
        int itemqQuantity = new Random().nextInt();

        List<String> itemTags = Arrays.asList(UUID.randomUUID().toString(), UUID.randomUUID().toString());

        TaskItem item = new TaskItem(itemId, itemName, itemBrand, itemPrice, itemqQuantity, itemTags);

        persistency.putItem(item);
        TaskItem retItem = persistency.getItem(itemId);

        Assert.assertEquals(item.getId(), retItem.getId());
        Assert.assertEquals(item.getName(), retItem.getName());
        Assert.assertEquals(item.getBrand(), retItem.getBrand());
        Assert.assertEquals(item.getPrice(), retItem.getPrice());
        Assert.assertEquals(item.getQuantity(), retItem.getQuantity());
        Assert.assertEquals(item.getTags(), retItem.getTags());
    }

    @Test
    public void whenGettingNotExistsThenNullWillBeReturned() throws Exception {
        int itemId = new Random().nextInt();
        TaskItem retItem = persistency.getItem(itemId);
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

        TaskItem item = new TaskItem(itemId, itemName, itemBrand, itemPrice, itemqQuantity, itemTags);

        persistency.putItem(item);
        persistency.removeItem(itemId);
        TaskItem retItem = persistency.getItem(itemId);

        Assert.assertEquals(null, retItem);
    }

    @Test
    public void whenUpdateingAnItemThenChangesAreRetrivable() throws Exception {
        int itemId = new Random().nextInt(1000);
        String itemName = UUID.randomUUID().toString();
        String itemBrand = UUID.randomUUID().toString();
        int itemPrice = new Random().nextInt(100);
        int itemqQuantity = new Random().nextInt(1000);
        List<String> itemTags = Arrays.asList(UUID.randomUUID().toString(), UUID.randomUUID().toString());

        TaskItem originalItem = new TaskItem(itemId, itemName, itemBrand, itemPrice, itemqQuantity, itemTags);
        persistency.putItem(originalItem);

        TaskItem updatedItem = new TaskItem(itemId, itemName, itemBrand, itemPrice, itemqQuantity + 100, itemTags);
        persistency.putItem(updatedItem);

        TaskItem retItem = persistency.getItem(itemId);

        Assert.assertEquals(updatedItem.getId(), retItem.getId());
        Assert.assertEquals(updatedItem.getName(), retItem.getName());
        Assert.assertEquals(updatedItem.getBrand(), retItem.getBrand());
        Assert.assertEquals(updatedItem.getPrice(), retItem.getPrice());
        Assert.assertEquals(updatedItem.getQuantity(), retItem.getQuantity());
        Assert.assertEquals(updatedItem.getTags(), retItem.getTags());
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

        TaskItem firstItem = new TaskItem(firstItemId, firstItemName, firstItemBrand, firstItemPrice, firstItemqQuantity, firstItemTags);
        TaskItem secondItem = new TaskItem(secondItemId, secondItemName, secondItemBrand, secondItemPrice, secondItemqQuantity, secondItemTags);

        persistency.putItem(firstItem);
        persistency.putItem(secondItem);
        List<TaskItem> retItems = persistency.getItems();

        Assert.assertEquals(2, retItems.size());
        Assert.assertEquals(true, retItems.contains(firstItem));
        Assert.assertEquals(true, retItems.contains(secondItem));
    }
}
