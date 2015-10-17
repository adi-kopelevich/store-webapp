package sample.task.list.service.impl;

import org.junit.*;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;
import sample.task.list.service.ItemsService;
import sample.task.list.service.dao.ItemDAL;
import sample.task.list.service.pojo.TaskItem;

import javax.ws.rs.NotFoundException;
import java.util.*;

/**
 * Created by kopelevi on 04/09/2015.
 */
public class ItemsServiceImplTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void whenAddItemThenItIsRetrivable() throws Exception {
        int itemId = new Random().nextInt();
        String itemName = UUID.randomUUID().toString();
        String itemCategory = UUID.randomUUID().toString();
        long itemReminder = new Random().nextLong();
        List<String> itemNotes = Arrays.asList(UUID.randomUUID().toString(), UUID.randomUUID().toString());

        TaskItem item = new TaskItem(itemId, itemName, itemCategory, itemReminder, itemNotes);

        ItemDAL itemDAL = Mockito.mock(ItemDAL.class);
        Mockito.when(itemDAL.getItem(itemId)).thenReturn(item);
        ItemsService itemsService = new ItemsServiceImpl(itemDAL);

        itemsService.addItem(item);
        Mockito.verify(itemDAL, Mockito.times(1)).putItem(item);

        TaskItem retItem = itemsService.getItem(itemId);
        Assert.assertEquals(item.getId(), retItem.getId());
        Assert.assertEquals(item.getName(), retItem.getName());
        Assert.assertEquals(item.getCategory(), retItem.getCategory());
        Assert.assertEquals(item.getReminder(), retItem.getReminder());
        Assert.assertEquals(item.getNotes(), retItem.getNotes());
    }

    @Test(expected = NotFoundException.class)
    public void whenGettingNotExistsThenNotFoundExecptionWillBeThrown() throws Exception {
        ItemDAL itemDAL = Mockito.mock(ItemDAL.class);
        Mockito.when(itemDAL.getItem(Mockito.anyInt())).thenReturn(null);
        ItemsService itemsService = new ItemsServiceImpl(itemDAL);

        int itemId = new Random().nextInt();
        itemsService.getItem(itemId);
    }

    @Test(expected = NotFoundException.class)
    public void whenDeletingItemThenItIsNotRetrivable() throws Exception {
        int itemId = new Random().nextInt();
        String itemName = UUID.randomUUID().toString();
        String itemCategory = UUID.randomUUID().toString();
        long itemReminder = new Random().nextLong();
        List<String> itemNotes = Arrays.asList(UUID.randomUUID().toString(), UUID.randomUUID().toString());

        TaskItem item = new TaskItem(itemId, itemName, itemCategory, itemReminder, itemNotes);


        ItemDAL itemDAL = Mockito.mock(ItemDAL.class);
        Mockito.when(itemDAL.getItem(itemId)).thenReturn(null);
        ItemsService itemsService = new ItemsServiceImpl(itemDAL);

        itemsService.addItem(item);
        Mockito.verify(itemDAL, Mockito.times(1)).putItem(item);

        itemsService.removeItem(itemId);
        Mockito.verify(itemDAL, Mockito.times(1)).removeItem(item.getId());

        itemsService.getItem(itemId); // should throw exception
    }

    @Test
    public void whenUpdateingAnItemThenChangesAreRetrivable() throws Exception {
        int itemId = new Random().nextInt();
        String itemName = UUID.randomUUID().toString();
        String itemCategory = UUID.randomUUID().toString();
        long itemReminder = new Random().nextLong();
        List<String> itemNotes = Arrays.asList(UUID.randomUUID().toString(), UUID.randomUUID().toString());

        TaskItem originalItem = new TaskItem(itemId, itemName, itemCategory, itemReminder, itemNotes);
        TaskItem updatedItem = new TaskItem(itemId, itemName, itemCategory, itemReminder + 10000, itemNotes);

        ItemDAL itemDAL = Mockito.mock(ItemDAL.class);
        Mockito.when(itemDAL.getItem(itemId)).thenReturn(updatedItem);
        ItemsService itemsService = new ItemsServiceImpl(itemDAL);

        itemsService.addItem(originalItem);
        Mockito.verify(itemDAL, Mockito.times(1)).putItem(originalItem);

        itemsService.updateItem(updatedItem);
        Mockito.verify(itemDAL, Mockito.times(1)).putItem(updatedItem);

        TaskItem retItem = itemsService.getItem(itemId);
        Assert.assertEquals(updatedItem.getId(), retItem.getId());
        Assert.assertEquals(updatedItem.getName(), retItem.getName());
        Assert.assertEquals(updatedItem.getCategory(), retItem.getCategory());
        Assert.assertEquals(updatedItem.getReminder(), retItem.getReminder());
        Assert.assertEquals(updatedItem.getNotes(), retItem.getNotes());
    }

    @Test
    public void whenAddingMultiItemsThenAllItemsAreReturned() throws Exception {
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

        ItemDAL itemDAL = Mockito.mock(ItemDAL.class);
        Mockito.when(itemDAL.getItems()).thenReturn(items);
        ItemsService itemsService = new ItemsServiceImpl(itemDAL);

        itemsService.addItem(firstItem);
        Mockito.verify(itemDAL, Mockito.times(1)).putItem(firstItem);
        itemsService.addItem(secondItem);
        Mockito.verify(itemDAL, Mockito.times(1)).putItem(secondItem);
        List<TaskItem> retItems = itemsService.getAllItems().getItems();

        Assert.assertEquals(2, retItems.size());
        Assert.assertEquals(true, retItems.contains(firstItem));
        Assert.assertEquals(true, retItems.contains(secondItem));
    }

    @Test
    public void whenGetItemThrowExceptionThenItIsPropagatedToClient() throws Exception {
        int itemId = new Random().nextInt();

        ItemDAL itemDAL = Mockito.mock(ItemDAL.class);
        String throwableMsg = UUID.randomUUID().toString();
        Mockito.doThrow(new RuntimeException(throwableMsg)).when(itemDAL).getItem(itemId);

        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage(throwableMsg);

        ItemsService itemsService = new ItemsServiceImpl(itemDAL);
        itemsService.getItem(itemId);
    }

    @Test
    public void whenPutItemThrowExceptionThenItIsPropagatedToClient() throws Exception {
        int itemId = new Random().nextInt();
        String itemName = UUID.randomUUID().toString();
        String itemCategory = UUID.randomUUID().toString();
        long itemReminder = new Random().nextLong();
        List<String> itemNotes = Arrays.asList(UUID.randomUUID().toString(), UUID.randomUUID().toString());

        TaskItem item = new TaskItem(itemId, itemName, itemCategory, itemReminder, itemNotes);

        ItemDAL itemDAL = Mockito.mock(ItemDAL.class);
        String throwableMsg = UUID.randomUUID().toString();
        Mockito.doThrow(new RuntimeException(throwableMsg)).when(itemDAL).putItem(item);

        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage(throwableMsg);

        ItemsService itemsService = new ItemsServiceImpl(itemDAL);
        itemsService.addItem(item);
    }

    @Test
    public void whenDeleteItemThrowExceptionThenItIsPropagatedToClient() throws Exception {
        int itemId = new Random().nextInt();

        ItemDAL itemDAL = Mockito.mock(ItemDAL.class);
        String throwableMsg = UUID.randomUUID().toString();
        Mockito.doThrow(new RuntimeException(throwableMsg)).when(itemDAL).removeItem(itemId);

        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage(throwableMsg);

        ItemsService itemsService = new ItemsServiceImpl(itemDAL);
        itemsService.removeItem(itemId);
    }

}
