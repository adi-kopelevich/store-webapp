package sample.task.list.service;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;

import java.util.*;

/**
 * Created by kopelevi on 04/09/2015.
 */
public class ItemsServiceImplTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void whenAddItemThenItIsRetrivable()  {
        int itemId = new Random().nextInt();
        String itemName = UUID.randomUUID().toString();
        String itemCategory = UUID.randomUUID().toString();
        long itemReminder = new Random().nextLong();
        List<String> itemNotes = Arrays.asList(UUID.randomUUID().toString(), UUID.randomUUID().toString());

        TaskItem item = new TaskItem(itemId, itemName, itemCategory, itemReminder, itemNotes);

        ItemDAO itemDAO = Mockito.mock(ItemDAO.class);
        Mockito.when(itemDAO.getItem(itemId)).thenReturn(item);
        ItemsService itemsService = new ItemsServiceImpl(itemDAO);

        itemsService.addItem(item);
        Mockito.verify(itemDAO, Mockito.times(1)).putItem(item);

        TaskItem retItem = itemsService.getItem(itemId);
        Assert.assertEquals(item.getId(), retItem.getId());
        Assert.assertEquals(item.getName(), retItem.getName());
        Assert.assertEquals(item.getCategory(), retItem.getCategory());
        Assert.assertEquals(item.getReminder(), retItem.getReminder());
        Assert.assertEquals(item.getNotes(), retItem.getNotes());
    }

    @Test(expected = ItemServiceItemNotFoundException.class)
    public void whenGettingNotExistsThenNullIsReturned()  {
        ItemDAO itemDAO = Mockito.mock(ItemDAO.class);
        Mockito.when(itemDAO.getItem(Mockito.anyInt())).thenReturn(null);
        ItemsService itemsService = new ItemsServiceImpl(itemDAO);

        int itemId = new Random().nextInt();
        itemsService.getItem(itemId);
    }

    @Test(expected = ItemServiceItemNotFoundException.class)
    public void whenDeletingExistingItemAndRetriveThenItemThenNullIsReturned()  {
        int itemId = new Random().nextInt();
        String itemName = UUID.randomUUID().toString();
        String itemCategory = UUID.randomUUID().toString();
        long itemReminder = new Random().nextLong();
        List<String> itemNotes = Arrays.asList(UUID.randomUUID().toString(), UUID.randomUUID().toString());

        TaskItem item = new TaskItem(itemId, itemName, itemCategory, itemReminder, itemNotes);

        ItemDAO itemDAO = Mockito.mock(ItemDAO.class);
        Mockito.when(itemDAO.getItem(itemId)).thenReturn(null);
        ItemsService itemsService = new ItemsServiceImpl(itemDAO);

        itemsService.addItem(item);
        Mockito.verify(itemDAO, Mockito.times(1)).putItem(item);

        itemsService.removeItem(itemId);
        Mockito.verify(itemDAO, Mockito.times(1)).removeItem(item.getId());

        TaskItem retItem = itemsService.getItem(itemId);
        Assert.assertNull(retItem);
    }

    @Test
    public void whenUpdateingAnItemThenChangesAreRetrivable()  {
        int itemId = new Random().nextInt();
        String itemName = UUID.randomUUID().toString();
        String itemCategory = UUID.randomUUID().toString();
        long itemReminder = new Random().nextLong();
        List<String> itemNotes = Arrays.asList(UUID.randomUUID().toString(), UUID.randomUUID().toString());

        TaskItem originalItem = new TaskItem(itemId, itemName, itemCategory, itemReminder, itemNotes);
        TaskItem updatedItem = new TaskItem(itemId, itemName, itemCategory, itemReminder + 10000, itemNotes);

        ItemDAO itemDAO = Mockito.mock(ItemDAO.class);
        Mockito.when(itemDAO.getItem(itemId)).thenReturn(updatedItem);
        ItemsService itemsService = new ItemsServiceImpl(itemDAO);

        itemsService.addItem(originalItem);
        Mockito.verify(itemDAO, Mockito.times(1)).putItem(originalItem);

        itemsService.updateItem(updatedItem);
        Mockito.verify(itemDAO, Mockito.times(1)).putItem(updatedItem);

        TaskItem retItem = itemsService.getItem(itemId);
        Assert.assertEquals(updatedItem.getId(), retItem.getId());
        Assert.assertEquals(updatedItem.getName(), retItem.getName());
        Assert.assertEquals(updatedItem.getCategory(), retItem.getCategory());
        Assert.assertEquals(updatedItem.getReminder(), retItem.getReminder());
        Assert.assertEquals(updatedItem.getNotes(), retItem.getNotes());
    }

    @Test
    public void whenAddingMultiItemsThenAllItemsAreReturned()  {
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

        List<TaskItem> items = new ArrayList<TaskItem>();
        items.add(firstItem);
        items.add(secondItem);

        ItemDAO itemDAO = Mockito.mock(ItemDAO.class);
        Mockito.when(itemDAO.getItems()).thenReturn(items);
        ItemsService itemsService = new ItemsServiceImpl(itemDAO);

        itemsService.addItem(firstItem);
        Mockito.verify(itemDAO, Mockito.times(1)).putItem(firstItem);
        itemsService.addItem(secondItem);
        Mockito.verify(itemDAO, Mockito.times(1)).putItem(secondItem);
        List<TaskItem> retItems = itemsService.getAllItems();

        Assert.assertNotNull(retItems);
        Assert.assertEquals(2, retItems.size());
        Assert.assertEquals(true, retItems.contains(firstItem));
        Assert.assertEquals(true, retItems.contains(secondItem));
    }

    @Test
    public void whenGetItemThrowExceptionThenItIsPropagatedToClientAsItemServiceException()  {
        int itemId = new Random().nextInt();

        ItemDAO itemDAO = Mockito.mock(ItemDAO.class);
        String throwableMsg = UUID.randomUUID().toString();
        Mockito.doThrow(new RuntimeException(throwableMsg)).when(itemDAO).getItem(itemId);

        expectedException.expect(ItemServiceException.class);
        expectedException.expectMessage("Failed to get item");

        ItemsService itemsService = new ItemsServiceImpl(itemDAO);
        itemsService.getItem(itemId);
    }

    @Test
    public void whenPutItemThrowExceptionThenItIsPropagatedToClientAsItemServiceException()  {
        int itemId = new Random().nextInt();
        String itemName = UUID.randomUUID().toString();
        String itemCategory = UUID.randomUUID().toString();
        long itemReminder = new Random().nextLong();
        List<String> itemNotes = Arrays.asList(UUID.randomUUID().toString(), UUID.randomUUID().toString());

        TaskItem item = new TaskItem(itemId, itemName, itemCategory, itemReminder, itemNotes);

        ItemDAO itemDAO = Mockito.mock(ItemDAO.class);
        String throwableMsg = UUID.randomUUID().toString();
        Mockito.doThrow(new RuntimeException(throwableMsg)).when(itemDAO).putItem(item);

        expectedException.expect(ItemServiceException.class);
        expectedException.expectMessage("Failed to add item");

        ItemsService itemsService = new ItemsServiceImpl(itemDAO);
        itemsService.addItem(item);
    }

    @Test
    public void whenDeleteItemThrowExceptionThenItIsPropagatedToClientAsItemServiceException()  {
        int itemId = new Random().nextInt();

        ItemDAO itemDAO = Mockito.mock(ItemDAO.class);
        String throwableMsg = UUID.randomUUID().toString();
        Mockito.doThrow(new RuntimeException(throwableMsg)).when(itemDAO).removeItem(itemId);

        expectedException.expect(ItemServiceException.class);
        expectedException.expectMessage("Failed to remove item");

        ItemsService itemsService = new ItemsServiceImpl(itemDAO);
        itemsService.removeItem(itemId);
    }

}
