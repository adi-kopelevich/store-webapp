package sample.grocery.store.service.impl;

import org.junit.*;
import org.junit.rules.ExpectedException;
import sample.grocery.store.service.ItemsService;
import sample.grocery.store.service.pojo.StoreItem;

import java.util.Arrays;
import java.util.List;

/**
 * Created by kopelevi on 04/09/2015.
 */
public class ItemsServiceImplTest {

    ItemsService itemsService = new ItemsServiceImpl();

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        itemsService.clearAll();
    }

    @After
    public void tearDown() throws Exception {

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

        itemsService.addItem(item);
        StoreItem retItem = itemsService.getItem(itemId);
        Assert.assertEquals(item, retItem);
    }

    @Test
    public void whenGettingNotExistsThenNotFoundExecptionWillBeThrown() throws Exception {
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("Not Found");
        int itemId = 3;
        StoreItem retItem = itemsService.getItem(itemId);
    }

    @Test
    public void whenDeletingItemThenItIsNotRetrivable() throws Exception {
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("Not Found");
        int itemId = 3;
        String itemName = "Milk";
        String itemBrand = "Tnuva";
        int itemPrice = 10;
        int itemqQuantity = 200;
        List<String> itemTags = Arrays.asList("star", "sale");

        StoreItem item = new StoreItem(itemId, itemName, itemBrand, itemPrice, itemqQuantity, itemTags);

        itemsService.addItem(item);
        itemsService.removeItem(itemId);
        itemsService.getItem(itemId); // should throw exception
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
        itemsService.addItem(originalItem);

        StoreItem updatedItem = new StoreItem(itemId, itemName, itemBrand, itemPrice, itemqQuantity + 100, itemTags);
        itemsService.updateItem(updatedItem);

        StoreItem retItem = itemsService.getItem(itemId);
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

        itemsService.addItem(firstItem);
        itemsService.addItem(secondItem);
        List<StoreItem> retItems = itemsService.getAllItems();

        Assert.assertEquals(2, retItems.size());
        Assert.assertEquals(true, retItems.contains(firstItem));
        Assert.assertEquals(true, retItems.contains(secondItem));
    }

}
