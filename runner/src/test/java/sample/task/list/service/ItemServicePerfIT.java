package sample.task.list.service;

/**
 * Created by kopelevi on 16/10/2015.
 */
public class ItemServicePerfIT {

    private static final int PORT = 8080;
    private static final String HOST = "localhost";

//    @Test
    public void whenAddItemThenItIsRetrievable() {
        ItemsService itemsService = new ItemServiceClient(HOST, PORT);
        for (int i = 0; i < 1000000; i++) {
            itemsService.getAllItems();
            System.out.println(i);
        }
    }

}
