package sample.grocery.store.service.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sample.grocery.store.service.ItemsService;
import sample.grocery.store.service.pojo.StoreItem;
import sample.grocery.store.service.pojo.StoreItems;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;

/**
 * Created by kopelevi on 04/09/2015.
 */
public class ItemServiceClient implements ItemsService {

    private final Logger LOGGER = LoggerFactory.getLogger(ItemServiceClient.class);

    private static final String DEFAULT_PROTOCOL = "http";
    private static final String DEFAULT_HOST = "localhost";
    private static final String DEFAULT_PORT = "8080";
    private static final String DEFAULT_CONTEXT = "store-webapp";
    private static final String RESOUCEE = "items";
    private static final String APP_PATH = "rest";
    private final String mediaType;
    private final WebTarget webTarget;


    public ItemServiceClient(String protpcol, String hostname, String port, String context, String mediaType) {
        StringBuffer serviceURL = new StringBuffer(protpcol).append("://").
                append(hostname);

        if (!port.isEmpty()) {
            serviceURL.append(":").append(port);
        }
        if (!context.isEmpty()) {
            serviceURL.append("/").append(DEFAULT_CONTEXT);
        }

        serviceURL.append("/").append(APP_PATH).append("/").append(RESOUCEE);

        this.mediaType = mediaType;

        Client client = ClientBuilder.newClient();
        this.webTarget = client.target(serviceURL.toString());
        LOGGER.info("Initiated client with target " + serviceURL);
    }

    public ItemServiceClient(String protpcol, String hostname, String port, String context) {
        this(protpcol, hostname, port, context, MediaType.APPLICATION_XML);
    }

    public ItemServiceClient(String mediaType) {
        this(DEFAULT_PROTOCOL, DEFAULT_HOST, DEFAULT_PORT, "", mediaType);
    }


    public StoreItems getAllItems() {
        Invocation.Builder invocationBuilder = webTarget.request(mediaType);
        return invocationBuilder.get(StoreItems.class);
    }

    public StoreItem getItem(int itemId) {
        Invocation.Builder invocationBuilder = webTarget.path(String.valueOf(itemId)).request(mediaType);
        return invocationBuilder.get(StoreItem.class);
    }

    public void addItem(StoreItem item) {
        Invocation.Builder invocationBuilder = webTarget.request(mediaType);
        invocationBuilder.post(Entity.entity(item, mediaType));
    }

    public void removeItem(int itemId) {
        Invocation.Builder invocationBuilder = webTarget.path(String.valueOf(itemId)).request();
        invocationBuilder.delete();
    }

    public void updateItem(StoreItem item) {
        Invocation.Builder invocationBuilder = webTarget.request(mediaType);
        invocationBuilder.put(Entity.entity(item, mediaType));
    }

    public void clearAll() {
        Invocation.Builder invocationBuilder = webTarget.request();
        invocationBuilder.delete();
    }
}
