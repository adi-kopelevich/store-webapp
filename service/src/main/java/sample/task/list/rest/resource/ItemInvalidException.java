package sample.task.list.rest.resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.Response;

/**
 * Created by kopelevi on 06/11/2015.
 */
public class ItemInvalidException extends BadRequestException {

    private static final Logger LOGGER = LoggerFactory.getLogger(ItemInvalidException.class.getName());

    public ItemInvalidException(String msg) {
        super(getErrorMsg(msg));
    }

    private static String getErrorMsg(String msg) {
        String errMsg = Response.Status.BAD_REQUEST + " - " + msg;
        LOGGER.error(errMsg);
        return errMsg;
    }

}
