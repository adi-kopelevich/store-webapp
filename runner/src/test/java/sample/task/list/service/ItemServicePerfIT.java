package sample.task.list.service;

import org.junit.Test;
import sample.task.list.service.client.ItemServiceClient;

/**
 * Created by kopelevi on 16/10/2015.
 */
public class ItemServicePerfIT {

    private static final int PORT = 8080;
    private static final String HOST = "localhost";

    @Test
    public void whenAddItemThenItIsRetrivable() throws Exception {

        ItemsService itemsService = new ItemServiceClient(HOST, PORT);

        for (int i = 0; i < 1000000; i++) {
            itemsService.getAllItems();
            System.out.println(i);
        }
    }

}
