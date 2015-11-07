package sample.task.list.service;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by kopelevi on 16/10/2015.
 */
public class ItemServicePerfIT {

    private static final int PORT = 8080;
    private static final String HOST = "localhost";

    @Test
    public void whenAddItemThenItIsRetrievable() {
//        ItemsService itemsService = new ItemServiceClientImpl("ec2-54-165-228-48.compute-1.amazonaws.com", 8080);  // Amazon WS
        ItemsService itemsService = new ItemServiceClientImpl(HOST, PORT);
        itemsService.clearAll();
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        for (int i = 0; i < 1000; i++) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName() + ": started...");
                    int itemId = getPositiveRandom();
                    String itemName = UUID.randomUUID().toString();
                    String itemCategory = UUID.randomUUID().toString();
                    long itemReminder = new Random().nextLong();
                    List<String> itemNotes = Arrays.asList(UUID.randomUUID().toString(), UUID.randomUUID().toString());

                    TaskItem item = new TaskItem(itemId, itemName, itemCategory, itemReminder, itemNotes);

                    itemsService.addItem(item);

                    int numOfItems = itemsService.getAllItems().size();
                    System.out.println(Thread.currentThread().getName() + ": numOfItems=" + numOfItems);
                }
            });

        }
        executorService.shutdown();

        try {
            executorService.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            itemsService.clearAll();
        }
    }

    private int getPositiveRandom() {
        return new Random().nextInt(Integer.MAX_VALUE) + 1;
    }

}
