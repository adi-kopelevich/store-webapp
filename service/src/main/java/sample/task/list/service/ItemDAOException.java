package sample.task.list.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by kopelevi on 28/10/2015.
 */
public class ItemDAOException extends RuntimeException {

    private static final Logger LOGGER = LoggerFactory.getLogger(ItemDAOException.class);

    public ItemDAOException(String msg) {
        super(msg);
        LOGGER.error(msg);
    }

    public ItemDAOException(String msg,  Exception e) {
        super(msg, e);
        LOGGER.error(msg, e);
    }
}
