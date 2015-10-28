package sample.task.list.service;

/**
 * Created by kopelevi on 19/10/2015.
 */
public class ItemServiceItemNotFoundException extends RuntimeException {

    public ItemServiceItemNotFoundException(int itemId) {
        super("Item with ID: " + itemId + ", was not found.");
    }
}
