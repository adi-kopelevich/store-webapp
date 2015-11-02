package sample.task.list.rest.resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;

/**
 * Created by kopelevi on 23/10/2015.
 */
public class TaskItemNotFoundException extends NotFoundException {


    private static final Logger LOGGER = LoggerFactory.getLogger(TaskItemNotFoundException.class.getName());

    public TaskItemNotFoundException(int itemId) {
        super(getErrorMsg(itemId));
    }

    private static String getErrorMsg(int itemId) {
        String errMsg = Response.Status.NOT_FOUND + " - " + "Item with ID: " + itemId;
        LOGGER.error(errMsg);
        return errMsg;
    }
}
