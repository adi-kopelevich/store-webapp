package sample.grocery.store.service.pojo;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by kopelevi on 16/10/2015.
 */

@XmlRootElement
public class StoreItems {

    private List<StoreItem> items;

    public StoreItems() { //for serialization

    }

    public StoreItems(List<StoreItem> items) { //for serialization
        this.items = items;
    }

    public List<StoreItem> getItems() {
        return items;
    }

    public void setItems(List<StoreItem> items) {
        this.items = items;
    }
}
