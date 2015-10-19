package sample.task.list.service;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import sample.task.list.server.EmbeddedServer;
import sample.task.list.service.api.ItemsService;
import sample.task.list.service.client.ItemServiceClient;
import sample.task.list.service.model.TaskItem;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * Created by kopelevi on 19/10/2015.
 */
public class ItemsResourceIT {

    private static final String SERVER_ROOT = "./target/tmp";
    private static final String PROTOCOL = "http";
    private static final String HOST = "localhost";
    private static final int PORT = 8888;
    private static final String RESOUCEE = "items";
    private static final String APP_PATH = "rest";

    private static final String RESOURCE_URL = new StringBuffer(PROTOCOL).append("://")
            .append(HOST).append(":").append(PORT)
            .append("/").append(APP_PATH).append("/").append(RESOUCEE).toString();

    private static final String MEDIA_TYPE = MediaType.APPLICATION_JSON;

    @BeforeClass
    public static void startServer() {
        startServerAsDaemon();
        validateServerIsUp(5, 1000);
    }

    @Before
    public void setUp() throws Exception {
//        itemsService = new ItemServiceClient("https", "powerful-woodland-5357.herokuapp.com", "", "");  // Heroku
//        itemsService = new ItemServiceClient("http", "ec2-54-165-228-48.compute-1.amazonaws.com", "8080", "task-list");  // Amazon WS
        ItemsService itemsService = new ItemServiceClient(HOST, PORT);
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
        ItemsService itemsService = new ItemServiceClient(HOST, PORT);
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
    public void whenAddItemThenItIsRetrievable() throws Exception {
        Client client = ClientBuilder.newBuilder().register(JacksonFeature.class).build();
        WebTarget webTarget = client.target(RESOURCE_URL);

        int itemId = new Random().nextInt();
        String itemName = UUID.randomUUID().toString();
        String itemCategory = UUID.randomUUID().toString();
        long itemReminder = new Random().nextLong();
        List<String> itemNotes = Arrays.asList(UUID.randomUUID().toString(), UUID.randomUUID().toString());
        TaskItem item = new TaskItem(itemId, itemName, itemCategory, itemReminder, itemNotes);

        // add object and verify status
        Response postResponse = webTarget.request().post(Entity.entity(item, MEDIA_TYPE));
        Assert.assertEquals(Response.Status.NO_CONTENT.getStatusCode(), postResponse.getStatus());

        // get object and verify its status and content
        Response getResponse = webTarget.path(String.valueOf(itemId)).request(MEDIA_TYPE).get();
        Assert.assertEquals(Response.Status.OK.getStatusCode(), getResponse.getStatus());

        TaskItem retItem = getResponse.readEntity(TaskItem.class);
        Assert.assertEquals(item.getId(), retItem.getId());
        Assert.assertEquals(item.getName(), retItem.getName());
        Assert.assertEquals(item.getCategory(), retItem.getCategory());
        Assert.assertEquals(item.getReminder(), retItem.getReminder());
        Assert.assertEquals(item.getNotes(), retItem.getNotes());
    }


}
