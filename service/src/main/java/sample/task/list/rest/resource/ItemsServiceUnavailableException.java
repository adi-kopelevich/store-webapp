package sample.task.list.rest.resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.ServiceUnavailableException;
import javax.ws.rs.core.Response;

/**
 * Created by kopelevi on 23/10/2015.
 */
public class ItemsServiceUnavailableException extends ServiceUnavailableException {

    private static final Logger LOGGER = LoggerFactory.getLogger(ItemsServiceUnavailableException.class.getName());

    public ItemsServiceUnavailableException(Exception e) {
        super(getErrorMsg(e));
    }

    private static String getErrorMsg(Exception e) {
        String errMsg = Response.Status.SERVICE_UNAVAILABLE + " - " + e.getMessage();
        LOGGER.error(errMsg, e);
        return errMsg;
    }
}
