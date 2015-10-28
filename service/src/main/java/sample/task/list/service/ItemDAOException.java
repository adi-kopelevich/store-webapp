package sample.task.list.service;

/**
 * Created by kopelevi on 28/10/2015.
 */
public class ItemDAOException extends RuntimeException {

    public ItemDAOException(String msg) {
        super(msg);
    }

    public ItemDAOException(String msg, Exception e) {
        super(msg, e);
    }
}
