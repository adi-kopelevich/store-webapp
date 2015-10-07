package sample.grocery.store.service.client;

import sample.grocery.store.service.ItemsService;
import sample.grocery.store.service.pojo.StoreItem;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by kopelevi on 04/09/2015.
 */
public class ItemServiceClient implements ItemsService {

    private static final String DEFAULT_PROTOCOL = "http";
    private static final String DEFAULT_HOST = "localhost";
    private static final String DEFAULT_PORT = "8080";
    private static final String DEFAULT_CONTEXT = "store-webapp";
    private static final String RESOUCEE = "items";
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

        serviceURL.append("/").append(RESOUCEE);

        this.mediaType = mediaType;

        Client client = ClientBuilder.newClient();
        this.webTarget = client.target(serviceURL.toString());
    }

    public ItemServiceClient(String protpcol, String hostname, String port, String context) {
        this(protpcol, hostname, port, context, MediaType.APPLICATION_XML);
    }

    public ItemServiceClient(String mediaType) {
        this(DEFAULT_PROTOCOL, DEFAULT_HOST, DEFAULT_PORT, "", mediaType);
    }


    public List<StoreItem> getAllItems() {
        return null;
    }

    public StoreItem getItem(int itemId) {
        Invocation.Builder invocationBuilder = webTarget.path(String.valueOf(itemId)).request(mediaType);
        Response response = invocationBuilder.get();

        int responseStatus = response.getStatus();
        if (responseStatus == Response.Status.NOT_FOUND.getStatusCode()) {
            throw new NotFoundException();
        } else if (responseStatus != Response.Status.OK.getStatusCode()) {
            throw new RuntimeException("Failed to get a valid resposne from the server");
        }
        return invocationBuilder.get(StoreItem.class);
    }

    public void addItem(StoreItem item) {
        Invocation.Builder invocationBuilder = webTarget.request(mediaType);
        Response response = invocationBuilder.post(Entity.entity(item, mediaType));

        if (response.getStatus() != Response.Status.NO_CONTENT.getStatusCode()) {
            throw new RuntimeException("Failed to get a valid resposne from the server");
        }
    }

    public void removeItem(int itemId) {
        Invocation.Builder invocationBuilder = webTarget.path(String.valueOf(itemId)).request();
        Response response = invocationBuilder.delete();

        int responseStatus = response.getStatus();
        if (responseStatus == Response.Status.NOT_FOUND.getStatusCode()) {
            throw new NotFoundException();
        } else if (responseStatus != Response.Status.NO_CONTENT.getStatusCode()) {
            throw new RuntimeException("Failed to get a valid resposne from the server");
        }
    }

    public void updateItem(StoreItem item) {
        Invocation.Builder invocationBuilder = webTarget.request(mediaType);
        Response response = invocationBuilder.put(Entity.entity(item, mediaType));

        if (response.getStatus() != Response.Status.NO_CONTENT.getStatusCode()) {
            throw new RuntimeException("Failed to get a valid resposne from the server");
        }
    }

    public void clearAll() {
        Invocation.Builder invocationBuilder = webTarget.request();
        Response response = invocationBuilder.delete();

        if (response.getStatus() != Response.Status.NO_CONTENT.getStatusCode()) {
            throw new RuntimeException("Failed to get a valid resposne from the server");
        }
    }
}
