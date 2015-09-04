package sample.grocery.store.service.client;

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

    public ItemServiceClient() {
        String serviceURL = new StringBuffer(PROTOCOL).append("://").
                append(HOST).append(":").append(PORT).
                append("/").append(CONTEXT).append("/rest/").append(RESOUCEE).toString();
        this.r = Client.create().resource(serviceURL);
    }


    public List<StoreItem> getAllItems() {
        return null;
    }

    public StoreItem getItem(int itemId) {
        ClientResponse response = r.path(String.valueOf(itemId)). accept(MediaType.APPLICATION_XML)
                .get(ClientResponse.class);

        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + response.getStatus());
        }
//
//        GenericType<JAXBElement<StoreItem>> generic = new GenericType<JAXBElement<StoreItem>>() {};
//        JAXBElement<StoreItem> jaxbContact = r.path(itemId).get(generic);
//        return jaxbContact.getValue();

        return response.getEntity(StoreItem.class);
    }

    public void addItem(StoreItem item) {
        ClientResponse response = r.type(MediaType.APPLICATION_XML)
                .post(ClientResponse.class, item);

        if (response.getStatus() != 204) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + response.getStatus());
        }
    }

    public void removeItem(int itemId) {

    }

    public void updateItem(StoreItem item) {

    }

    public void clearAll() {
        ClientResponse response = r.delete(ClientResponse.class);

        if (response.getStatus() != 204) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + response.getStatus());
        }
    }
}
