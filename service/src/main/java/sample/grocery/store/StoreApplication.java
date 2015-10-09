package sample.grocery.store;

import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.ApplicationPath;

/**
 * Created by kopelevi on 09/10/2015.
 */
@ApplicationPath("/rest")
public class StoreApplication extends ResourceConfig {
    public StoreApplication() {
        packages("sample.grocery.store");
    }

}
