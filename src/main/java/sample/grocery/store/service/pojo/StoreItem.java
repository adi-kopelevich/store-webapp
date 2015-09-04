package sample.grocery.store.service.pojo;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
public class StoreItem {

    private int id;
    private String name;
    private String brand;
    private int price;
    private int quantity;
    private List<String> tags;

    public StoreItem() { //for serlization

    }

    public StoreItem(int id, String name, String brand, int price, int quantity, List<String> tags) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.price = price;
        this.quantity = quantity;
        this.tags = tags;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

}
