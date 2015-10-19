package sample.task.list.service.resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sample.task.list.service.ItemNotFoundException;
import sample.task.list.service.impl.ItemsServiceImpl;
import sample.task.list.service.model.TaskItem;
import sample.task.list.service.model.TaskList;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;

/**
 * Created by kopelevi on 04/09/2015.
 */
@Path("/items")
public class ItemsResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(ItemsResource.class);

    @Context
    UriInfo uriInfo;

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
        } catch (ItemNotFoundException itemNotFoundException) {
            logAndThrowNotFoundException(itemId);
        } catch (Exception e) {
            logAndThrowServiceUnavailableException(e);
        }
        return item;
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    public Response addItem(TaskItem taskItem) {
        Response response = null;
        try {
            new ItemsServiceImpl().addItem(taskItem);
            response = Response.created(uriInfo.getRequestUri()).build();
        } catch (Exception e) {
            logAndThrowServiceUnavailableException(e);
        }
        return response;
    }

    @DELETE
    @Path("/{paramId}")
    public Response removeItem(@PathParam("paramId") int itemId) {
        Response response = null;
        try {
            new ItemsServiceImpl().removeItem(itemId);
            response = Response.noContent().build();
        } catch (Exception e) {
            logAndThrowServiceUnavailableException(e);
        }
        return response;
    }

    @PUT
    @Consumes({MediaType.APPLICATION_JSON})
    public Response updateItem(TaskItem taskItem) {
        Response response = null;
        try {
            new ItemsServiceImpl().updateItem(taskItem);
            response = Response.ok().build();
        } catch (Exception e) {
            logAndThrowServiceUnavailableException(e);
        }
        return response;
    }

    @DELETE
    public Response clearAll() {
        Response response = null;
        try {
            new ItemsServiceImpl().clearAll();
            response = Response.noContent().build();
        } catch (Exception e) {
            logAndThrowServiceUnavailableException(e);
        }
        return response;
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
