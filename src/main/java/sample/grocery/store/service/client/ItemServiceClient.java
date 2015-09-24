package sample.grocery.store.service.client;

import com.sun.jersey.api.NotFoundException;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import sample.grocery.store.service.ItemsService;
import sample.grocery.store.service.pojo.StoreItem;

import javax.ws.rs.core.MediaType;
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
    private static WebResource r;

    private String mediaType;

    public ItemServiceClient(String protpcol, String hostname, String port, String context, String mediaType) {
        StringBuffer serviceURL = new StringBuffer(protpcol).append("://").
                append(hostname);

        if (!port.isEmpty()) {
            serviceURL.append(":").append(port);
        }
        if (!context.isEmpty()) {
            serviceURL.append("/").append(DEFAULT_CONTEXT);
        }
        serviceURL.append("/rest/").append(RESOUCEE).toString();

        this.mediaType = mediaType;

        this.r = Client.create().resource(serviceURL.toString());
    }

    public ItemServiceClient(String protpcol, String hostname, String port, String context) {
        this(protpcol, hostname, port, context, MediaType.APPLICATION_XML);
    }

    public ItemServiceClient(String mediaType) {
        this(DEFAULT_PROTOCOL, DEFAULT_HOST, DEFAULT_PORT, DEFAULT_CONTEXT, mediaType);
    }


    public List<StoreItem> getAllItems() {
        return null;
    }

    public StoreItem getItem(int itemId) {
        ClientResponse response = r.path(String.valueOf(itemId)).accept(mediaType)
                .get(ClientResponse.class);

        int responseStatus = response.getStatus();
        if (responseStatus == ClientResponse.Status.NOT_FOUND.getStatusCode()) {
            throw new NotFoundException();
        } else if (responseStatus != ClientResponse.Status.OK.getStatusCode()) {
            throw new RuntimeException("Failed to get a valid resposne from the server");
        }
        return response.getEntity(StoreItem.class);
    }

    public void addItem(StoreItem item) {
        ClientResponse response = r.type(mediaType)
                .post(ClientResponse.class, item);

        if (response.getStatus() != ClientResponse.Status.NO_CONTENT.getStatusCode()) {
            throw new RuntimeException("Failed to get a valid resposne from the server");
        }
    }

    public void removeItem(int itemId) {
        ClientResponse response = r.path(String.valueOf(itemId))
                .delete(ClientResponse.class);

        int responseStatus = response.getStatus();
        if (responseStatus == ClientResponse.Status.NOT_FOUND.getStatusCode()) {
            throw new NotFoundException();
        } else if (responseStatus != ClientResponse.Status.NO_CONTENT.getStatusCode()) {
            throw new RuntimeException("Failed to get a valid resposne from the server");
        }
    }

    public void updateItem(StoreItem item) {
        ClientResponse response = r.type(mediaType)
                .put(ClientResponse.class, item);

        if (response.getStatus() != ClientResponse.Status.NO_CONTENT.getStatusCode()) {
            throw new RuntimeException("Failed to get a valid resposne from the server");
        }
    }

    public void clearAll() {
        ClientResponse response = r.delete(ClientResponse.class);

        if (response.getStatus() != ClientResponse.Status.NO_CONTENT.getStatusCode()) {
            throw new RuntimeException("Failed to get a valid resposne from the server");
        }
    }
}
