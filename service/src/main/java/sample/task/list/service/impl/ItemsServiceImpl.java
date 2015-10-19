package sample.task.list.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sample.task.list.ApplicationConfiguration;
import sample.task.list.service.ItemsService;
import sample.task.list.service.dao.ItemDAO;
import sample.task.list.service.dao.ItemDAOMapImpl;
import sample.task.list.service.dao.ItemDAOMongoDBImpl;
import sample.task.list.service.model.TaskItem;
import sample.task.list.service.model.TaskList;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by kopelevi on 04/09/2015.
 */
@Path("/items")
public class ItemsServiceImpl implements ItemsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ItemsServiceImpl.class);

    private static final boolean IS_MONGO_ENABLED = ApplicationConfiguration.isMongoEnabled();
    private final ItemDAO persistency;

    public ItemsServiceImpl() {
        ItemDAO itemDAO = null;
        try {
            if (IS_MONGO_ENABLED) {
                LOGGER.debug("MongoDB conf is set to enabled, going to use mongoDB store...");
                itemDAO = ItemDAOMongoDBImpl.getInstance();
            } else {
                LOGGER.debug("MongoDB conf is set to disabled, going to use local map store...");
                itemDAO = ItemDAOMapImpl.getInstance();
            }
        } catch (Exception e) {
            logAndThrowServerInitException(e);
        }
        this.persistency = itemDAO;
    }

    protected ItemsServiceImpl(ItemDAO itemDAO) {
        this.persistency = itemDAO;
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
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
    @Produces({MediaType.APPLICATION_JSON})
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

    private void logAndThrowServerInitException(Exception e) {
        String errMsg = Response.Status.INTERNAL_SERVER_ERROR + " - Failed to init server. Please check the logs...";
        LOGGER.error(errMsg, e);
        throw new InternalServerErrorException(errMsg, e);
    }
}
