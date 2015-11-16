package sample.task.list.service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Created by kopelevi on 04/09/2015.
 */
public class ItemDAOMapImpl implements ItemDAO {

    // ConcurrentHashMap for handing concurrency and ensure thread safety (non-null keys)
    private final Map<Integer, TaskItem> itemsMap = new ConcurrentHashMap<>();

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

    public List<TaskItem> getItems() {
        return itemsMap.entrySet().stream().parallel()
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
