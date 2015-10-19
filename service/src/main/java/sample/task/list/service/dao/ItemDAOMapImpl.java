package sample.task.list.service.dao;

import sample.task.list.service.model.TaskItem;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Created by kopelevi on 04/09/2015.
 */
public class ItemDAOMapImpl implements ItemDAO {

    // ConcurrentHashMap for handing concurrency,
    // its drawbacks are:
    //      - larger memory footprint compared to a regular has map and
    //      - handling null keys which are allowed as contrast to regular hash map, as it  might be returned while iterating over them
    private final Map<Integer, TaskItem> itemsMap = new ConcurrentHashMap<>(); //thread safe...

    private static final ItemDAOMapImpl INSTANCE = new ItemDAOMapImpl();

    // hide constructor
    private ItemDAOMapImpl() {
    }

    public static ItemDAO getInstance() {
        return INSTANCE;
    }

    public TaskItem getItem(int itemId) {
        return itemsMap.get(itemId);
    }

    public List<TaskItem> getItems() {// ConcurrentHashMap might contain null keys
        return itemsMap.entrySet().stream()
                .filter(x -> x != null)
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }

    public void putItem(TaskItem item) {
        itemsMap.put(item.getId(), item);
    }

    public void removeItem(int itemId) {
        itemsMap.remove(itemId);
    }

    public void clear() {
        itemsMap.clear();
    }
}
