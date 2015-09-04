package sample.grocery.store.service.impl;

import sample.grocery.store.persistency.ItemPersistency;
import sample.grocery.store.persistency.ItemPersistencyMapImpl;
import sample.grocery.store.service.ItemsService;
import sample.grocery.store.service.pojo.StoreItem;
import sample.grocery.store.service.utils.StoreItmeUtils;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBElement;
import java.util.List;

/**
 * Created by kopelevi on 04/09/2015.
 */
@Path("/items")
public class ItemsServiceImpl implements ItemsService {

    private static final String RESOURCE_NOT_FOUND_MSG = "Resource Not Found";

    ItemPersistency persistency = ItemPersistencyMapImpl.getInstance();

    public List<StoreItem> getAllItems() {
        return persistency.getItems();
    }

    @GET
    @Path("/{paramId}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public StoreItem getItem(@PathParam("paramId") int itemId) {
        StoreItem item = persistency.getItem(itemId);
        if (item == null) {
            throw new RuntimeException(RESOURCE_NOT_FOUND_MSG);
        }
        return item;
    }

    public void addItem(StoreItem item) {
        addItem(StoreItmeUtils.fromStoreItemtoJAXB(item));
    }

    @POST
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void addItem(JAXBElement<StoreItem> itemJAXBElement) {
        StoreItem storeItem = StoreItmeUtils.fromJAXBtoStoreItem(itemJAXBElement);
        persistency.putItem(storeItem);
    }

    @DELETE
    @Path("/{paramId}")
    public void removeItem(@PathParam("paramId") int itemId) {
        persistency.removeItem(itemId);
    }

    public void updateItem(StoreItem item) {
        updateItem(StoreItmeUtils.fromStoreItemtoJAXB(item));
    }

    @PUT
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void updateItem(JAXBElement<StoreItem> itemJAXBElement) {
        StoreItem storeItem = StoreItmeUtils.fromJAXBtoStoreItem(itemJAXBElement);
        persistency.putItem(storeItem);
    }

    @DELETE
    public void clearAll() {
        persistency.clear();
    }
}
