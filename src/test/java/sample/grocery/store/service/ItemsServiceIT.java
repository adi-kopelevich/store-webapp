package sample.grocery.store.service;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import sample.grocery.store.service.client.ItemServiceClient;
import sample.grocery.store.service.pojo.StoreItem;

import java.util.Arrays;
import java.util.List;

/**
 * Created by kopelevi on 04/09/2015.
 */
public class ItemsServiceIT {

    ItemsService itemsService = new ItemServiceClient();

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

}
