package sample.grocery.store.service.impl;

import com.sun.jersey.api.NotFoundException;
import sample.grocery.store.persistency.ItemPersistency;
import sample.grocery.store.persistency.ItemPersistencyMapImpl;
import sample.grocery.store.service.ItemsService;
import sample.grocery.store.service.pojo.StoreItem;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.JAXBElement;
import java.util.List;

/**
 * Created by kopelevi on 04/09/2015.
 */
@Path("/items")
public class ItemsServiceImpl implements ItemsService {

    private static final String RESOURCE_NOT_FOUND_MSG = "Resource with given ID not found - ";

    ItemPersistency persistency = ItemPersistencyMapImpl.getInstance();

    @Context
    UriInfo uriInfo;

    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<StoreItem> getAllItems() {
        return persistency.getItems();
    }

    @GET
    @Path("/{paramId}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public StoreItem getItem(@PathParam("paramId") int itemId) {
        StoreItem item = persistency.getItem(itemId);
        if (item == null) {
            throw new NotFoundException(RESOURCE_NOT_FOUND_MSG + itemId);
        }
        return item;
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    public void addItem(StoreItem storeItem) {
//        boolean alreadyExists = persistency.getItem(storeItem.getId()) != null;
        persistency.putItem(storeItem);
    }

    @POST
    @Consumes({MediaType.APPLICATION_XML})
    public void addItem(JAXBElement<StoreItem> itemJAXBElement) {
        addItem(itemJAXBElement.getValue());
    }

    @DELETE
    @Path("/{paramId}")
    public void removeItem(@PathParam("paramId") int itemId) {
        persistency.removeItem(itemId);
    }

    @PUT
    @Consumes({MediaType.APPLICATION_JSON})
    public void updateItem(StoreItem storeItem) {
        persistency.putItem(storeItem);
    }

    @PUT
    @Consumes({MediaType.APPLICATION_XML})
    public void updateItem(JAXBElement<StoreItem> itemJAXBElement) {
        updateItem(itemJAXBElement.getValue());

    }

    @DELETE
    public void clearAll() {
        persistency.clear();
    }
}
