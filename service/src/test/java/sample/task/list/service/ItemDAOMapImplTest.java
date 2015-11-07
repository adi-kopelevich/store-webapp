package sample.task.list.service;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * Created by kopelevi on 04/09/2015.
 */
public class ItemDAOMapImplTest {

    private static final ItemDAO persistency = ItemDAOMapImpl.getInstance();

    @Before
    public void setUp() {
        persistency.clear();
    }

    @After
    public void tearDown()  {
    }

    @Test
    public void whenAddItemThenItIsRetrivable() {
        int itemId = new Random().nextInt();
        String itemName = UUID.randomUUID().toString();
        String itemCategory = UUID.randomUUID().toString();
        long itemReminder = new Random().nextLong();
        List<String> itemNotes = Arrays.asList(UUID.randomUUID().toString(), UUID.randomUUID().toString());

        TaskItem item = new TaskItem(itemId, itemName, itemCategory, itemReminder, itemNotes);

        persistency.putItem(item);
        TaskItem retItem = persistency.getItem(itemId);

        Assert.assertEquals(item, retItem);
    }

    @Test
    public void whenGettingNotExistsThenNullWillBeReturned() {
        int itemId = new Random().nextInt();
        TaskItem retItem = persistency.getItem(itemId);
        Assert.assertEquals(null, retItem);
    }

    @Test
    public void whenDeletingItemThenItIsNotRetrivable() {
        int itemId = new Random().nextInt();
        String itemName = UUID.randomUUID().toString();
        String itemCategory = UUID.randomUUID().toString();
        long itemReminder = new Random().nextLong();
        List<String> itemNotes = Arrays.asList(UUID.randomUUID().toString(), UUID.randomUUID().toString());

        TaskItem item = new TaskItem(itemId, itemName, itemCategory, itemReminder, itemNotes);

        persistency.putItem(item);
        persistency.removeItem(itemId);
        TaskItem retItem = persistency.getItem(itemId);

        Assert.assertEquals(null, retItem);
    }

    @Test
    public void whenUpdateingAnItemThenChangesAreRetrivable() {
        int itemId = new Random().nextInt();
        String itemName = UUID.randomUUID().toString();
        String itemCategory = UUID.randomUUID().toString();
        long itemReminder = new Random().nextLong();
        List<String> itemNotes = Arrays.asList(UUID.randomUUID().toString(), UUID.randomUUID().toString());

        TaskItem originalItem = new TaskItem(itemId, itemName, itemCategory, itemReminder, itemNotes);
        persistency.putItem(originalItem);

        TaskItem updatedItem = new TaskItem(itemId, itemName, itemCategory, itemReminder + 10000, itemNotes);
        persistency.putItem(updatedItem);

        TaskItem retItem = persistency.getItem(itemId);

        Assert.assertEquals(updatedItem, retItem);
    }

    @Test
    public void whenAddingMultiItemsThenAllItemsAreReturned() {
        int firstItemId = new Random().nextInt();
        String firstItemName = UUID.randomUUID().toString();
        String firstItemCategory = UUID.randomUUID().toString();
        long firstItemReminder = new Random().nextLong();
        List<String> firstItemTags = Arrays.asList(UUID.randomUUID().toString(), UUID.randomUUID().toString());

        int secondItemId = firstItemId + new Random().nextInt();
        String secondItemName = UUID.randomUUID().toString();
        String secondItemCategory = UUID.randomUUID().toString();
        long secondItemReminder = new Random().nextLong();
        List<String> secondItemTags = Arrays.asList(UUID.randomUUID().toString(), UUID.randomUUID().toString());

        TaskItem firstItem = new TaskItem(firstItemId, firstItemName, firstItemCategory, firstItemReminder, firstItemTags);
        TaskItem secondItem = new TaskItem(secondItemId, secondItemName, secondItemCategory, secondItemReminder, secondItemTags);

        persistency.putItem(firstItem);
        persistency.putItem(secondItem);
        List<TaskItem> retItems = persistency.getItems();

        Assert.assertNotNull(retItems);
        Assert.assertEquals(2, retItems.size());
        Assert.assertEquals(true, retItems.contains(firstItem));
        Assert.assertEquals(true, retItems.contains(secondItem));
    }

}
