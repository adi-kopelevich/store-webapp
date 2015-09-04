package sample.grocery.store.service.utils;

import sample.grocery.store.service.pojo.StoreItem;

import javax.xml.bind.*;
import javax.xml.namespace.QName;

/**
 * Created by kopelevi on 04/09/2015.
 */
public class StoreItmeUtils {

    public static StoreItem fromJAXBtoStoreItem(JAXBElement<StoreItem> itemJAXBElement) {
        return itemJAXBElement.getValue();
    }

    public static JAXBElement<StoreItem> fromStoreItemtoJAXB(StoreItem item) {

        return new JAXBElement(new QName("ROOT"), StoreItem.class, item);
    }

}
