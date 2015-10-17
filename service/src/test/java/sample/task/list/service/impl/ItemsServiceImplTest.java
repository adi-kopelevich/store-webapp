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
        String itemBrand = UUID.randomUUID().toString();
        int itemPrice = new Random().nextInt();
        int itemqQuantity = new Random().nextInt();
        List<String> itemTags = Arrays.asList(UUID.randomUUID().toString(), UUID.randomUUID().toString());
        TaskItem item = new TaskItem(itemId, itemName, itemBrand, itemPrice, itemqQuantity, itemTags);

        ItemDAL itemDAL = Mockito.mock(ItemDAL.class);
        Mockito.when(itemDAL.getItem(itemId)).thenReturn(item);
        ItemsService itemsService = new ItemsServiceImpl(itemDAL);

        itemsService.addItem(item);
        Mockito.verify(itemDAL, Mockito.times(1)).putItem(item);

        TaskItem retItem = itemsService.getItem(itemId);
        Assert.assertEquals(item.getId(), retItem.getId());
        Assert.assertEquals(item.getName(), retItem.getName());
        Assert.assertEquals(item.getBrand(), retItem.getBrand());
        Assert.assertEquals(item.getPrice(), retItem.getPrice());
        Assert.assertEquals(item.getQuantity(), retItem.getQuantity());
        Assert.assertEquals(item.getTags(), retItem.getTags());
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
        String itemBrand = UUID.randomUUID().toString();
        int itemPrice = new Random().nextInt();
        int itemqQuantity = new Random().nextInt();
        List<String> itemTags = Arrays.asList(UUID.randomUUID().toString(), UUID.randomUUID().toString());

        TaskItem item = new TaskItem(itemId, itemName, itemBrand, itemPrice, itemqQuantity, itemTags);

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
        String itemBrand = UUID.randomUUID().toString();
        int itemPrice = new Random().nextInt();
        int itemqQuantity = new Random().nextInt();
        List<String> itemTags = Arrays.asList(UUID.randomUUID().toString(), UUID.randomUUID().toString());

        TaskItem originalItem = new TaskItem(itemId, itemName, itemBrand, itemPrice, itemqQuantity, itemTags);
        TaskItem updatedItem = new TaskItem(itemId, itemName, itemBrand, itemPrice, itemqQuantity + 100, itemTags);

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
        String itemBrand = UUID.randomUUID().toString();
        int itemPrice = new Random().nextInt();
        int itemqQuantity = new Random().nextInt();
        List<String> itemTags = Arrays.asList(UUID.randomUUID().toString(), UUID.randomUUID().toString());
        TaskItem item = new TaskItem(itemId, itemName, itemBrand, itemPrice, itemqQuantity, itemTags);

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
