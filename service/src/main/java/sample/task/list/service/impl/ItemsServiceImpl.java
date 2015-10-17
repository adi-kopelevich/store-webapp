package sample.task.list.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sample.task.list.service.ItemsService;
import sample.task.list.service.dao.ItemDAL;
import sample.task.list.service.dao.ItemDALMapImpl;
import sample.task.list.service.dao.ItemDALMongoDBImpl;
import sample.task.list.service.pojo.TaskItem;
import sample.task.list.service.pojo.TaskList;

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

    private static final Logger LOGGER = LoggerFactory.getLogger(ItemDALMongoDBImpl.class);

    private final ItemDAL persistency;

    public ItemsServiceImpl() {
        ItemDAL itemDAL;
        if (ItemDALMongoDBImpl.isMongoConfEnabled()) {
            LOGGER.info("MongoDB conf is set to enabled, going to use mongoDB store...");
            itemDAL = ItemDALMongoDBImpl.getInstance();
        } else {
            LOGGER.info("MongoDB conf is set to disabled, going to use local map store...");
            itemDAL = ItemDALMapImpl.getInstance();
        }
        this.persistency = itemDAL;
    }

    public ItemsServiceImpl(ItemDAL itemDAL) {
        this.persistency = itemDAL;
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public TaskList getAllItems() {
        List<TaskItem> items = null;
        try {
            items = persistency.getItems();
        } catch (Exception e) {
            logAndThrowServiceUnavailableException(e);
        }
        return new TaskList(items);
    }

    @GET
    @Path("/{paramId}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public TaskItem getItem(@PathParam("paramId") int itemId) {
        TaskItem item = null;
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
    public void addItem(TaskItem taskItem) {
        try {
            persistency.putItem(taskItem);
        } catch (Exception e) {
            logAndThrowServiceUnavailableException(e);
        }
    }

    @POST
    @Consumes({MediaType.APPLICATION_XML})
    public void addItem(JAXBElement<TaskItem> itemJAXBElement) {
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
    public void updateItem(TaskItem taskItem) {
        try {
            persistency.putItem(taskItem);
        } catch (Exception e) {
            logAndThrowServiceUnavailableException(e);
        }
    }

    @PUT
    @Consumes({MediaType.APPLICATION_XML})
    public void updateItem(JAXBElement<TaskItem> itemJAXBElement) {
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
