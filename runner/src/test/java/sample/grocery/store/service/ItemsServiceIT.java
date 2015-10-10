package sample.grocery.store.service;

import org.apache.log4j.PropertyConfigurator;
import org.junit.*;
import sample.grocery.store.server.EmbeddedServer;
import sample.grocery.store.service.client.ItemServiceClient;
import sample.grocery.store.service.pojo.StoreItem;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.MediaType;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * Created by kopelevi on 04/09/2015.
 */
public class ItemsServiceIT {

    private static final String LOG4J_PROPERTIES_PATH = "./target/tmp/conf/log4j.properties";
    private static final String WEBAPP_PATH = "./target/tmp/webapp";
    private static final String LOG4J_PROPS_DIR_PATH = "./target/tmp/conf";
    private static final int PORT = 8080;

    static EmbeddedServer embeddedServer;
    ItemsService itemsService;

    @BeforeClass
    public static void startServer() {
        // set log4j
        File log4jPropertiesFile = new File(LOG4J_PROPERTIES_PATH);
        PropertyConfigurator.configure(log4jPropertiesFile.getAbsolutePath());
        // start embedded server
        embeddedServer = new EmbeddedServer(PORT, WEBAPP_PATH);
        embeddedServer.startServer();
    }

    @AfterClass
    public static void stopServer() {
        embeddedServer.stopServer();
    }

    @Before
    public void setUp() throws Exception {
        itemsService = new ItemServiceClient(MediaType.APPLICATION_XML);
//        itemsService = new ItemServiceClient(MediaType.APPLICATION_JSON);
//        itemsService = new ItemServiceClient("https", "powerful-woodland-5357.herokuapp.com", "", "");  // Heroku
//        itemsService = new ItemServiceClient("http", "ec2-54-165-228-48.compute-1.amazonaws.com", "8080", "store-webapp");  // Amazon WS
        itemsService.clearAll();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void whenAddItemThenItIsRetrivable() throws Exception {
        int itemId = new Random().nextInt();
        String itemName = UUID.randomUUID().toString();
        String itemBrand = UUID.randomUUID().toString();
        int itemPrice = new Random().nextInt();
        int itemqQuantity = new Random().nextInt();
        List<String> itemTags = Arrays.asList(UUID.randomUUID().toString(), UUID.randomUUID().toString());

        StoreItem item = new StoreItem(itemId, itemName, itemBrand, itemPrice, itemqQuantity, itemTags);

        itemsService.addItem(item);
        StoreItem retItem = itemsService.getItem(itemId);
        Assert.assertEquals(item.getId(), retItem.getId());
        Assert.assertEquals(item.getName(), retItem.getName());
        Assert.assertEquals(item.getBrand(), retItem.getBrand());
        Assert.assertEquals(item.getPrice(), retItem.getPrice());
        Assert.assertEquals(item.getQuantity(), retItem.getQuantity());
        Assert.assertEquals(item.getTags(), retItem.getTags());
    }

    @Test(expected = NotFoundException.class)
    public void whenGettingNotExistsThenNotFoundExecptionWillBeThrown() throws Exception {
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

        StoreItem item = new StoreItem(itemId, itemName, itemBrand, itemPrice, itemqQuantity, itemTags);

        itemsService.addItem(item);
        itemsService.removeItem(itemId);
        itemsService.getItem(itemId); // should throw exception
    }

    @Test
    public void whenUpdatingAnItemThenChangesAreRetrivable() throws Exception {
        int itemId = new Random().nextInt();
        String itemName = UUID.randomUUID().toString();
        String itemBrand = UUID.randomUUID().toString();
        int itemPrice = new Random().nextInt();
        int itemqQuantity = new Random().nextInt();
        List<String> itemTags = Arrays.asList(UUID.randomUUID().toString(), UUID.randomUUID().toString());

        StoreItem originalItem = new StoreItem(itemId, itemName, itemBrand, itemPrice, itemqQuantity, itemTags);
        itemsService.addItem(originalItem);

        StoreItem updatedItem = new StoreItem(itemId, itemName, itemBrand, itemPrice, itemqQuantity + 100, itemTags);
        itemsService.updateItem(updatedItem);

        StoreItem retItem = itemsService.getItem(itemId);
        Assert.assertEquals(updatedItem.getId(), retItem.getId());
        Assert.assertEquals(updatedItem.getName(), retItem.getName());
        Assert.assertEquals(updatedItem.getBrand(), retItem.getBrand());
        Assert.assertEquals(updatedItem.getPrice(), retItem.getPrice());
        Assert.assertEquals(updatedItem.getQuantity(), retItem.getQuantity());
        Assert.assertEquals(updatedItem.getTags(), retItem.getTags());
    }

}
