package sample.task.list.rest.resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(ItemsResource.class.getName());

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public TaskItems getAllItems() {
        try {
            List<TaskItem> items = new ItemsServiceImpl().getAllItems();
            return new TaskItems(items);
        } catch (Exception e) {
            String errMsg = Response.Status.SERVICE_UNAVAILABLE + " - " + e.getMessage();
            LOGGER.error(errMsg, e);
            throw new ServiceUnavailableException(errMsg);
        }
    }

    @GET
    @Path("/{paramId}")
    @Produces({MediaType.APPLICATION_JSON})
    public TaskItem getItem(@PathParam("paramId") int itemId) {
        try {
            return new ItemsServiceImpl().getItem(itemId);
        } catch (ItemServiceItemNotFoundException itemServiceItemNotFoundException) {
            String errMsg = Response.Status.NOT_FOUND + " - " + "Item with ID: " + itemId;
            LOGGER.error(errMsg, itemServiceItemNotFoundException);
            throw new NotFoundException(errMsg, itemServiceItemNotFoundException);
        } catch (Exception e) {
            String errMsg = Response.Status.SERVICE_UNAVAILABLE + " - " + e.getMessage();
            LOGGER.error(errMsg, e);
            throw new ServiceUnavailableException(errMsg);
        }
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    public Response addItem(TaskItem taskItem, @Context UriInfo uriInfo) {
        try {
            new ItemsServiceImpl().addItem(taskItem);
            return Response.created(uriInfo.getRequestUri()).build();
        } catch (ItemServiceInvalidParamException itemServiceInvalidParamException) {
            String errMsg = Response.Status.BAD_REQUEST + " - " + itemServiceInvalidParamException.getMessage();
            LOGGER.error(errMsg, itemServiceInvalidParamException);
            throw new BadRequestException(errMsg);
        } catch (Exception e) {
            String errMsg = Response.Status.SERVICE_UNAVAILABLE + " - " + e.getMessage();
            LOGGER.error(errMsg, e);
            throw new ServiceUnavailableException(errMsg);
        }
    }

    @DELETE
    @Path("/{paramId}")
    public Response removeItem(@PathParam("paramId") int itemId) {
        try {
            new ItemsServiceImpl().removeItem(itemId);
            return Response.noContent().build();
        } catch (Exception e) {
            String errMsg = Response.Status.SERVICE_UNAVAILABLE + " - " + e.getMessage();
            LOGGER.error(errMsg, e);
            throw new ServiceUnavailableException(errMsg);
        }
    }

    @PUT
    @Consumes({MediaType.APPLICATION_JSON})
    public Response updateItem(TaskItem taskItem) {
        try {
            new ItemsServiceImpl().updateItem(taskItem);
            return Response.ok().build();
        } catch (ItemServiceInvalidParamException itemServiceInvalidParamException) {
            String errMsg = Response.Status.BAD_REQUEST + " - " + itemServiceInvalidParamException.getMessage();
            LOGGER.error(errMsg, itemServiceInvalidParamException);
            throw new BadRequestException(errMsg, itemServiceInvalidParamException);
        } catch (Exception e) {
            String errMsg = Response.Status.SERVICE_UNAVAILABLE + " - " + e.getMessage();
            LOGGER.error(errMsg, e);
            throw new ServiceUnavailableException(errMsg);
        }
    }

    @DELETE
    public Response clearAll() {
        try {
            new ItemsServiceImpl().clearAll();
            return Response.noContent().build();
        } catch (Exception e) {
            String errMsg = Response.Status.SERVICE_UNAVAILABLE + " - " + e.getMessage();
            LOGGER.error(errMsg, e);
            throw new ServiceUnavailableException(errMsg);
        }
    }

}
