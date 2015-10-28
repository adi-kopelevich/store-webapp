package sample.task.list.rest.resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sample.task.list.service.ItemServiceItemNotFoundException;
import sample.task.list.service.ItemsServiceImpl;
import sample.task.list.service.TaskItem;
import sample.task.list.service.TaskList;

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
        try {
            List<TaskItem> items = new ItemsServiceImpl().getAllItems();
            return new TaskList(items);
        } catch (Exception e) {
            throw new TaskServiceUnavailableException(e);
        }
    }

    @GET
    @Path("/{paramId}")
    @Produces({MediaType.APPLICATION_JSON})
    public TaskItem getItem(@PathParam("paramId") int itemId) {
        try {
            return new ItemsServiceImpl().getItem(itemId);
        } catch (ItemServiceItemNotFoundException itemServiceItemNotFoundException) {
            throw new TaskItemNotFoundException(itemId);
        } catch (Exception e) {
            throw new TaskServiceUnavailableException(e);
        }
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    public Response addItem(TaskItem taskItem) {
        try {
            new ItemsServiceImpl().addItem(taskItem);
            return Response.created(uriInfo.getRequestUri()).build();
        } catch (Exception e) {
            throw new TaskServiceUnavailableException(e);
        }
    }

    @DELETE
    @Path("/{paramId}")
    public Response removeItem(@PathParam("paramId") int itemId) {
        try {
            new ItemsServiceImpl().removeItem(itemId);
            return Response.noContent().build();
        } catch (Exception e) {
            throw new TaskServiceUnavailableException(e);
        }
    }

    @PUT
    @Consumes({MediaType.APPLICATION_JSON})
    public Response updateItem(TaskItem taskItem) {
        try {
            new ItemsServiceImpl().updateItem(taskItem);
            return Response.ok().build();
        } catch (Exception e) {
            throw new TaskServiceUnavailableException(e);
        }
    }

    @DELETE
    public Response clearAll() {
        try {
            new ItemsServiceImpl().clearAll();
            return Response.noContent().build();
        } catch (Exception e) {
            throw new TaskServiceUnavailableException(e);
        }
    }

}
