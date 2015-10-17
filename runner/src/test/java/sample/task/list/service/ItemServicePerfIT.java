package sample.task.list.service;

import org.junit.*;
import sample.task.list.service.client.ItemServiceClient;

import javax.ws.rs.core.MediaType;

/**
 * Created by kopelevi on 16/10/2015.
 */
public class ItemServicePerfIT {

    @Test
    public void whenAddItemThenItIsRetrivable() throws Exception {

        ItemsService itemsService = new ItemServiceClient(MediaType.APPLICATION_JSON_TYPE);

        for(int i =0; i< 1000000 ; i++){
            itemsService.getAllItems();
            System.out.println(i);
        }
    }

}
