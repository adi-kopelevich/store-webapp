package sample.grocery.store.service;

import org.junit.*;
import sample.grocery.store.service.client.ItemServiceClient;

import javax.ws.rs.core.MediaType;

/**
 * Created by kopelevi on 16/10/2015.
 */
public class ItemServicePerfIT {





    @BeforeClass
    public static void startServer() {

    }

    @AfterClass
    public static void stopServer(){
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void whenAddItemThenItIsRetrivable() throws Exception {

        ItemsService itemsService = new ItemServiceClient(MediaType.APPLICATION_JSON);

        for(int i =0; i< 1000000 ; i++){
            itemsService.getAllItems();
            System.out.println(i);
        }
    }

}
