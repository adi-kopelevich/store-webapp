package sample.task.list.service;

import org.junit.*;
import org.junit.rules.ExpectedException;
import sample.task.list.server.EmbeddedServer;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * Created by kopelevi on 04/09/2015.
 */
public class ItemsServiceClientIT {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private static final String SERVER_ROOT = "./runner/target/tmp";
    private static final int PORT = 8888;
    private static final String HOST = "localhost";

    @BeforeClass
    public static void startServer() {
        startServerAsDaemon();
        validateServerIsUp(5, 1000);
    }

    @Before
    public void setUp() {
//        itemsService = new ItemServiceClientImpl("https", "powerful-woodland-5357.herokuapp.com", "", "");  // Heroku
//                ItemsService itemsService = new ItemServiceClientImpl("ec2-54-165-228-48.compute-1.amazonaws.com", 8080);  // Amazon WS
        ItemsService itemsService = new ItemServiceClientImpl(HOST, PORT);
        itemsService.clearAll();
    }

    private static void startServerAsDaemon() {
        Thread serverDaemonThread = new Thread(new Runnable() {
            @Override
            public void run() {
                new EmbeddedServer(PORT, SERVER_ROOT);
            }
        });
        serverDaemonThread.setDaemon(true);
        serverDaemonThread.start();
    }

    private static void validateServerIsUp(final int numOfRetries, final int cycleIntervalInMillis) {
        ItemsService itemsService = new ItemServiceClientImpl(HOST, PORT);
        // wait for server to load
        for (int i = 0; i < numOfRetries; i++) {
            try {
                itemsService.getAllItems();
                break;
            } catch (Exception e) {
                // go to sleep
                try {
                    Thread.sleep(cycleIntervalInMillis);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    @Test
    public void whenAddItemThenItIsRetrivable() {
        ItemsService itemsService = new ItemServiceClientImpl(HOST, PORT);

        int itemId = getPositiveRandom();
        String itemName = UUID.randomUUID().toString();
        String itemCategory = UUID.randomUUID().toString();
        long itemReminder = new Random().nextLong();
        List<String> itemNotes = Arrays.asList(UUID.randomUUID().toString(), UUID.randomUUID().toString());

        TaskItem item = new TaskItem(itemId, itemName, itemCategory, itemReminder, itemNotes);
        itemsService.addItem(item);
        TaskItem retItem = itemsService.getItem(itemId);
        Assert.assertEquals(item, retItem);
    }

    @Test
    public void whenAddingMultiItemsThenTheyAreRetrivable() {
        ItemsService itemsService = new ItemServiceClientImpl(HOST, PORT);

        int firstItemId = getPositiveRandom();
        String firstItemName = UUID.randomUUID().toString();
        String firstItemCategory = UUID.randomUUID().toString();
        long firstItemReminder = new Random().nextLong();
        List<String> firstItemTags = Arrays.asList(UUID.randomUUID().toString(), UUID.randomUUID().toString());

        int secondItemId = getPositiveRandom();
        String secondItemName = UUID.randomUUID().toString();
        String secondItemCategory = UUID.randomUUID().toString();
        long secondItemReminder = new Random().nextLong();
        List<String> secondItemTags = Arrays.asList(UUID.randomUUID().toString(), UUID.randomUUID().toString());

        TaskItem firstItem = new TaskItem(firstItemId, firstItemName, firstItemCategory, firstItemReminder, firstItemTags);
        TaskItem secondItem = new TaskItem(secondItemId, secondItemName, secondItemCategory, secondItemReminder, secondItemTags);

        itemsService.addItem(firstItem);
        itemsService.addItem(secondItem);

        List<TaskItem> retItems = itemsService.getAllItems();
        Assert.assertNotNull(retItems);
        Assert.assertEquals(2, retItems.size());
        Assert.assertEquals(true, retItems.contains(firstItem));
        Assert.assertEquals(true, retItems.contains(secondItem));
    }

    @Test(expected = ItemServiceItemNotFoundException.class)
    public void whenGettingNotExistsThenNotFoundExecptionWillBeThrown() {
        ItemsService itemsService = new ItemServiceClientImpl(HOST, PORT);
        int itemId = getPositiveRandom();
        itemsService.getItem(itemId);
    }

    @Test(expected = ItemServiceItemNotFoundException.class)
    public void whenDeletingItemThenItIsNotRetrivable() {
        ItemsService itemsService = new ItemServiceClientImpl(HOST, PORT);
        int itemId = getPositiveRandom();
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
    public void whenUpdatingAnItemThenChangesAreRetrivable() {
        ItemsService itemsService = new ItemServiceClientImpl(HOST, PORT);
        int itemId = getPositiveRandom();
        String itemName = UUID.randomUUID().toString();
        String itemCategory = UUID.randomUUID().toString();
        long itemReminder = new Random().nextLong();
        List<String> itemNotes = Arrays.asList(UUID.randomUUID().toString(), UUID.randomUUID().toString());

        TaskItem originalItem = new TaskItem(itemId, itemName, itemCategory, itemReminder, itemNotes);
        itemsService.addItem(originalItem);

        TaskItem updatedItem = new TaskItem(itemId, itemName, itemCategory, itemReminder + 10000, itemNotes);
        itemsService.updateItem(updatedItem);

        TaskItem retItem = itemsService.getItem(itemId);
        Assert.assertEquals(updatedItem, retItem);
    }

    @Test
    public void whenGivingNegativeTaskIdThenItemServiceException() {
        ItemsService itemsService = new ItemServiceClientImpl(HOST, PORT);
        int itemId = getPositiveRandom() * -1;
        String itemName = UUID.randomUUID().toString();
        String itemCategory = UUID.randomUUID().toString();
        long itemReminder = new Random().nextLong();
        List<String> itemNotes = Arrays.asList(UUID.randomUUID().toString(), UUID.randomUUID().toString());

        TaskItem item = new TaskItem(itemId, itemName, itemCategory, itemReminder, itemNotes);

        expectedException.expect(ItemServiceInvalidParamException.class);
//        expectedException.expectMessage(ItemServiceErrorMessages.INVALID_PARAM_NON_POSITIVE_ID);

        itemsService.addItem(item);
    }

    private int getPositiveRandom() {
        return new Random().nextInt(Integer.MAX_VALUE) + 1;
    }

}
