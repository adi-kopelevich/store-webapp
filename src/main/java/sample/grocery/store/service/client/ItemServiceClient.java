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

    private static final String PROTOCOL = "http";
    private static final String HOST = "localhost";
    private static final String PORT = "8080";
    private static final String CONTEXT = "store-webapp";
    private static final String RESOUCEE = "items";
    private static WebResource r;

    private String mediaType = MediaType.APPLICATION_XML;

    public ItemServiceClient() {
        String serviceURL = new StringBuffer(PROTOCOL).append("://").
                append(HOST).append(":").append(PORT).
                append("/").append(CONTEXT).append("/rest/").append(RESOUCEE).toString();
        this.r = Client.create().resource(serviceURL);
    }

    public ItemServiceClient(String mediaType) {
        new ItemServiceClient();
        this.mediaType = mediaType;
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
