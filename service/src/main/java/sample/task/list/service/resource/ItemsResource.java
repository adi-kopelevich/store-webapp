package sample.task.list.service.resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sample.task.list.service.impl.ItemsServiceImpl;
import sample.task.list.service.model.TaskItem;
import sample.task.list.service.model.TaskList;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by kopelevi on 04/09/2015.
 */
@Path("/items")
public class ItemsResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(ItemsResource.class);

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public TaskList getAllItems() {
        List<TaskItem> items = null;
        try {
            items = new ItemsServiceImpl().getAllItems();
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
            item = new ItemsServiceImpl().getItem(itemId);
        } catch (Exception e) {
            logAndThrowServiceUnavailableException(e);
        }
        if (item == null) {
            logAndThrowNotFoundException(itemId);
        }
        return item;
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    public void addItem(TaskItem taskItem, @Context final HttpServletResponse response) {
        try {
            new ItemsServiceImpl().addItem(taskItem);
            response.setStatus(Response.Status.CREATED.getStatusCode());
        } catch (Exception e) {
            logAndThrowServiceUnavailableException(e);
        }
    }

    @DELETE
    @Path("/{paramId}")
    public void removeItem(@PathParam("paramId") int itemId) {
        try {
            new ItemsServiceImpl().removeItem(itemId);
        } catch (Exception e) {
            logAndThrowServiceUnavailableException(e);
        }
    }

    @PUT
    @Consumes({MediaType.APPLICATION_JSON})
    public void updateItem(TaskItem taskItem) {
        try {
            new ItemsServiceImpl().updateItem(taskItem);
        } catch (Exception e) {
            logAndThrowServiceUnavailableException(e);
        }
    }

    @DELETE
    public void clearAll() {
        try {
            new ItemsServiceImpl().clearAll();
        } catch (Exception e) {
            logAndThrowServiceUnavailableException(e);
        }
    }

    private void logAndThrowServiceUnavailableException(Exception e) {
        String errMsg = Response.Status.SERVICE_UNAVAILABLE + " - " + e.getMessage();
        LOGGER.error(errMsg, e);
        throw new ServiceUnavailableException(errMsg);
    }

    private void logAndThrowNotFoundException(int itemId) {
        String errMsg = Response.Status.NOT_FOUND + " - " + "Item wth ID: " + itemId;
        LOGGER.error(errMsg);
        throw new NotFoundException(errMsg);
    }

}
