package sample.task.list.rest;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import sample.task.list.server.EmbeddedServer;
import sample.task.list.service.*;

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

    private static final String SERVER_ROOT = "./runner/target/tmp";
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
    public void setUp() {
//        itemsService = new ItemServiceClient("https", "powerful-woodland-5357.herokuapp.com", "", "");  // Heroku
//        ItemsService itemsService = new ItemServiceClientImpl("ec2-54-165-228-48.compute-1.amazonaws.com", 8080);  // Amazon WS
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
    public void whenGettingNotExistsThenNotFoundExecptionWillBeThrown() {
        Client client = ClientBuilder.newBuilder().register(JacksonFeature.class).build();
        WebTarget webTarget = client.target(RESOURCE_URL);
        int itemId = new Random().nextInt();
        Response response = webTarget.path(String.valueOf(itemId)).request(MEDIA_TYPE).get();
        Assert.assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }

    @Test
    public void whenDeletingItemThenItIsNotRetrivable() {
        Client client = ClientBuilder.newBuilder().register(JacksonFeature.class).build();
        WebTarget webTarget = client.target(RESOURCE_URL);

        int itemId = getPositiveRandom();
        String itemName = UUID.randomUUID().toString();
        String itemCategory = UUID.randomUUID().toString();
        long itemReminder = new Random().nextLong();
        List<String> itemNotes = Arrays.asList(UUID.randomUUID().toString(), UUID.randomUUID().toString());

        TaskItem item = new TaskItem(itemId, itemName, itemCategory, itemReminder, itemNotes);

        // add object and verify status
        Response postResponse = webTarget.request().post(Entity.entity(item, MEDIA_TYPE));
        Assert.assertEquals(Response.Status.CREATED.getStatusCode(), postResponse.getStatus());

        // remove object and verify status
        Response deleteREsponse = webTarget.path(String.valueOf(itemId)).request(MEDIA_TYPE).delete();
        Assert.assertEquals(Response.Status.NO_CONTENT.getStatusCode(), deleteREsponse.getStatus());

        // get object and verify status
        Response getResponse = webTarget.path(String.valueOf(itemId)).request(MEDIA_TYPE).get();
        Assert.assertEquals(Response.Status.NOT_FOUND.getStatusCode(), getResponse.getStatus());
    }

    @Test
    public void whenAddItemThenItIsRetrievable() {
        Client client = ClientBuilder.newBuilder().register(JacksonFeature.class).build();
        WebTarget webTarget = client.target(RESOURCE_URL);

        int itemId = getPositiveRandom();
        String itemName = UUID.randomUUID().toString();
        String itemCategory = UUID.randomUUID().toString();
        long itemReminder = new Random().nextLong();
        List<String> itemNotes = Arrays.asList(UUID.randomUUID().toString(), UUID.randomUUID().toString());
        TaskItem item = new TaskItem(itemId, itemName, itemCategory, itemReminder, itemNotes);

        // add object and verify status
        Response postResponse = webTarget.request().post(Entity.entity(item, MEDIA_TYPE));
        Assert.assertEquals(Response.Status.CREATED.getStatusCode(), postResponse.getStatus());

        // get object and verify its status and content
        Response getResponse = webTarget.path(String.valueOf(itemId)).request(MEDIA_TYPE).get();
        Assert.assertEquals(Response.Status.OK.getStatusCode(), getResponse.getStatus());

        TaskItem retItem = getResponse.readEntity(TaskItem.class);
        Assert.assertEquals(item, retItem);
    }

    @Test
    public void whenAddItemWithNegativeIdThenItemServiceException() {
        Client client = ClientBuilder.newBuilder().register(JacksonFeature.class).build();
        WebTarget webTarget = client.target(RESOURCE_URL);

        int itemId = getPositiveRandom() * -1;
        String itemName = UUID.randomUUID().toString();
        String itemCategory = UUID.randomUUID().toString();
        long itemReminder = new Random().nextLong();
        List<String> itemNotes = Arrays.asList(UUID.randomUUID().toString(), UUID.randomUUID().toString());

        TaskItem item = new TaskItem(itemId, itemName, itemCategory, itemReminder, itemNotes);

        // add object and verify status
        Response postResponse = webTarget.request().post(Entity.entity(item, MEDIA_TYPE));
        Assert.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), postResponse.getStatus());
    }

    private int getPositiveRandom() {
        return new Random().nextInt(Integer.MAX_VALUE) + 1;
    }

}
