package sample.grocery.store.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sample.grocery.store.service.ItemsService;
import sample.grocery.store.service.dao.ItemDAL;
import sample.grocery.store.service.dao.ItemDALMongoDBImpl;
import sample.grocery.store.service.pojo.StoreItem;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBElement;
import java.util.List;

/**
 * Created by kopelevi on 04/09/2015.
 */
@Path("/items")
public class ItemsServiceImpl implements ItemsService {

    private final Logger LOGGER = LoggerFactory.getLogger(ItemDALMongoDBImpl.class);

    private final ItemDAL persistency;

    public ItemsServiceImpl() {
        this.persistency = ItemDALMongoDBImpl.getInstance();
    }

    public ItemsServiceImpl(ItemDAL itemDAL) {
        this.persistency = itemDAL;
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public List<StoreItem> getAllItems() {
        List<StoreItem> items = null;
        try {
            items = persistency.getItems();
        } catch (Exception e) {
            logAndThrowServiceUnavailableException(e);
        }
        return items;
    }

    @GET
    @Path("/{paramId}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public StoreItem getItem(@PathParam("paramId") int itemId) {
        StoreItem item = null;
        try {
            item = persistency.getItem(itemId);
        } catch (Exception e) {
            logAndThrowServiceUnavailableException(e);
        }
        if (item == null) {
            logAndThrowSNotFoundException(itemId);
        }
        return item;
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    public void addItem(StoreItem storeItem) {
        try {
            persistency.putItem(storeItem);
        } catch (Exception e) {
            logAndThrowServiceUnavailableException(e);
        }
    }

    @POST
    @Consumes({MediaType.APPLICATION_XML})
    public void addItem(JAXBElement<StoreItem> itemJAXBElement) {
        addItem(itemJAXBElement.getValue());
    }

    @DELETE
    @Path("/{paramId}")
    public void removeItem(@PathParam("paramId") int itemId) {
        try {
            persistency.removeItem(itemId);
        } catch (Exception e) {
            logAndThrowServiceUnavailableException(e);
        }
    }

    @PUT
    @Consumes({MediaType.APPLICATION_JSON})
    public void updateItem(StoreItem storeItem) {
        try {
            persistency.putItem(storeItem);
        } catch (Exception e) {
            logAndThrowServiceUnavailableException(e);
        }
    }

    @PUT
    @Consumes({MediaType.APPLICATION_XML})
    public void updateItem(JAXBElement<StoreItem> itemJAXBElement) {
        updateItem(itemJAXBElement.getValue());
    }

    @DELETE
    public void clearAll() {
        try {
            persistency.clear();
        } catch (Exception e) {
            logAndThrowServiceUnavailableException(e);
        }
    }

    private void logAndThrowServiceUnavailableException(Exception e) {
        String errMsg = Response.Status.SERVICE_UNAVAILABLE + " - " + e.getMessage();
        LOGGER.error(errMsg, e);
        throw new ServiceUnavailableException(errMsg);
    }

    private void logAndThrowSNotFoundException(int itemId) {
        String errMsg = Response.Status.NOT_FOUND + " - " + "Item wth ID: " + itemId;
        LOGGER.error(errMsg);
        throw new NotFoundException(errMsg);
    }
}
