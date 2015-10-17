package sample.task.list.service;

import org.junit.*;
import sample.task.list.server.EmbeddedServer;
import sample.task.list.service.client.ItemServiceClient;
import sample.task.list.service.pojo.TaskItem;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.MediaType;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * Created by kopelevi on 04/09/2015.
 */
public class ItemsServiceIT {

    private static final String SERVER_ROOT = "./target/tmp";
    private static final int PORT = 8989;
    private static final String HOST = "localhost";

    static EmbeddedServer embeddedServer;
    ItemsService itemsService;

    @BeforeClass
    public static void startServer() {
        // start embedded server
        embeddedServer = new EmbeddedServer(PORT, SERVER_ROOT);
        embeddedServer.startServer();
    }

    @AfterClass
    public static void stopServer() {
        embeddedServer.stopServer();
    }

    @Before
    public void setUp() throws Exception {
//        itemsService = new ItemServiceClient(MediaType.APPLICATION_XML);
//        itemsService = new ItemServiceClient("https", "powerful-woodland-5357.herokuapp.com", "", "");  // Heroku
//        itemsService = new ItemServiceClient("http", "ec2-54-165-228-48.compute-1.amazonaws.com", "8080", "task-list");  // Amazon WS
        itemsService = new ItemServiceClient(HOST, PORT, MediaType.APPLICATION_JSON_TYPE);
        itemsService.clearAll();
    }

    @Test
    public void whenAddItemThenItIsRetrivable() throws Exception {
        int itemId = new Random().nextInt();
        String itemName = UUID.randomUUID().toString();
        String itemCategory = UUID.randomUUID().toString();
        long itemReminder = new Random().nextLong();
        List<String> itemNotes = Arrays.asList(UUID.randomUUID().toString(), UUID.randomUUID().toString());

        TaskItem item = new TaskItem(itemId, itemName, itemCategory, itemReminder, itemNotes);
        itemsService.addItem(item);
        TaskItem retItem = itemsService.getItem(itemId);
        Assert.assertEquals(item.getId(), retItem.getId());
        Assert.assertEquals(item.getName(), retItem.getName());
        Assert.assertEquals(item.getCategory(), retItem.getCategory());
        Assert.assertEquals(item.getReminder(), retItem.getReminder());
        Assert.assertEquals(item.getNotes(), retItem.getNotes());
    }

    @Test
    public void whenAddingMultiItemsThenTheyAreRetrivable() throws Exception {
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

        itemsService.addItem(firstItem);
        itemsService.addItem(secondItem);

        List<TaskItem> retItems = itemsService.getAllItems().getItems();
        Assert.assertEquals(2, retItems.size());
        Assert.assertEquals(true, retItems.contains(firstItem));
        Assert.assertEquals(true, retItems.contains(secondItem));
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
        String itemCategory = UUID.randomUUID().toString();
        long itemReminder = new Random().nextLong();
        List<String> itemNotes = Arrays.asList(UUID.randomUUID().toString(), UUID.randomUUID().toString());

        TaskItem item = new TaskItem(itemId, itemName, itemCategory, itemReminder, itemNotes);

        itemsService.addItem(item);
        itemsService.removeItem(itemId);
        itemsService.getItem(itemId); // should throw exception
    }

    @Test
    public void whenUpdatingAnItemThenChangesAreRetrivable() throws Exception {
        int itemId = new Random().nextInt();
        String itemName = UUID.randomUUID().toString();
        String itemCategory = UUID.randomUUID().toString();
        long itemReminder = new Random().nextLong();
        List<String> itemNotes = Arrays.asList(UUID.randomUUID().toString(), UUID.randomUUID().toString());

        TaskItem originalItem = new TaskItem(itemId, itemName, itemCategory, itemReminder, itemNotes);
        itemsService.addItem(originalItem);

        TaskItem updatedItem = new TaskItem(itemId, itemName, itemCategory, itemReminder + 10000, itemNotes);
        itemsService.updateItem(updatedItem);

        TaskItem retItem = itemsService.getItem(itemId);
        Assert.assertEquals(updatedItem.getId(), retItem.getId());
        Assert.assertEquals(updatedItem.getName(), retItem.getName());
        Assert.assertEquals(updatedItem.getCategory(), retItem.getCategory());
        Assert.assertEquals(updatedItem.getReminder(), retItem.getReminder());
        Assert.assertEquals(updatedItem.getNotes(), retItem.getNotes());
    }

}
