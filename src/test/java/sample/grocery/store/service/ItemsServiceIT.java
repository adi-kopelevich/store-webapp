package sample.grocery.store.service;

import com.sun.jersey.api.NotFoundException;
import org.junit.*;
import org.junit.rules.ExpectedException;
import sample.grocery.store.service.client.ItemServiceClient;
import sample.grocery.store.service.pojo.StoreItem;

import javax.ws.rs.core.MediaType;
import java.util.Arrays;
import java.util.List;

/**
 * Created by kopelevi on 04/09/2015.
 */
public class ItemsServiceIT {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    ItemsService itemsService;

    @Before
    public void setUp() throws Exception {
        itemsService = new ItemServiceClient();
        itemsService = new ItemServiceClient(MediaType.APPLICATION_JSON);
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
        List<String> itemTags = Arrays.asList("quality", "holiday");
        StoreItem item = new StoreItem(itemId, itemName, itemBrand, itemPrice, itemqQuantity, itemTags);

        itemsService.addItem(item);
        StoreItem retItem = itemsService.getItem(itemId);
        Assert.assertEquals(item, retItem);
    }

    @Test
    public void whenGettingNotExistsThenNotFoundExecptionWillBeThrown() throws Exception {
        expectedException.expect(NotFoundException.class);
        int itemId = 333;
        itemsService.getItem(itemId);
    }

    @Test
    public void whenDeletingItemThenItIsNotRetrivable() throws Exception {
        expectedException.expect(NotFoundException.class);
        int itemId = 3;
        String itemName = "Milk";
        String itemBrand = "Tnuva";
        int itemPrice = 10;
        int itemqQuantity = 200;
        List<String> itemTags = Arrays.asList("quality", "holiday");

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
        List<String> itemTags = Arrays.asList("quality", "holiday");

        StoreItem originalItem = new StoreItem(itemId, itemName, itemBrand, itemPrice, itemqQuantity, itemTags);
        itemsService.addItem(originalItem);

        StoreItem updatedItem = new StoreItem(itemId, itemName, itemBrand, itemPrice, itemqQuantity + 100, itemTags);
        itemsService.updateItem(updatedItem);

        StoreItem retItem = itemsService.getItem(itemId);
        Assert.assertEquals(updatedItem, retItem);
    }

}
