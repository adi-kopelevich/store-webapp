package sample.task.list.rest.resource;

import sample.task.list.service.*;

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

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public TaskItems getAllItems() {
        try {
            List<TaskItem> items = new ItemsServiceImpl().getAllItems();
            return new TaskItems(items);
        } catch (Exception e) {
            throw new ItemsServiceUnavailableException(e);
        }
    }

    @GET
    @Path("/{paramId}")
    @Produces({MediaType.APPLICATION_JSON})
    public TaskItem getItem(@PathParam("paramId") int itemId) {
        try {
            return new ItemsServiceImpl().getItem(itemId);
        } catch (ItemServiceItemNotFoundException itemServiceItemNotFoundException) {
            throw new ItemNotFoundException(itemId);
        } catch (Exception e) {
            throw new ItemsServiceUnavailableException(e);
        }
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    public Response addItem(TaskItem taskItem, @Context UriInfo uriInfo) {
        try {
            new ItemsServiceImpl().addItem(taskItem);
            return Response.created(uriInfo.getRequestUri()).build();
        } catch (ItemServiceInvalidParamException itemServiceInvalidParamException) {
            throw new ItemInvalidException(itemServiceInvalidParamException.getMessage());
        } catch (Exception e) {
            throw new ItemsServiceUnavailableException(e);
        }
    }

    @DELETE
    @Path("/{paramId}")
    public Response removeItem(@PathParam("paramId") int itemId) {
        try {
            new ItemsServiceImpl().removeItem(itemId);
            return Response.noContent().build();
        } catch (Exception e) {
            throw new ItemsServiceUnavailableException(e);
        }
    }

    @PUT
    @Consumes({MediaType.APPLICATION_JSON})
    public Response updateItem(TaskItem taskItem) {
        try {
            new ItemsServiceImpl().updateItem(taskItem);
            return Response.ok().build();
        } catch (ItemServiceInvalidParamException itemServiceInvalidParamException) {
            throw new ItemInvalidException(itemServiceInvalidParamException.getMessage());
        } catch (Exception e) {
            throw new ItemsServiceUnavailableException(e);
        }
    }

    @DELETE
    public Response clearAll() {
        try {
            new ItemsServiceImpl().clearAll();
            return Response.noContent().build();
        } catch (Exception e) {
            throw new ItemsServiceUnavailableException(e);
        }
    }

}
