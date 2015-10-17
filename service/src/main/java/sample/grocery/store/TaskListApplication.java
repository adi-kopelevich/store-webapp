package sample.grocery.store;

import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.ApplicationPath;

/**
 * Created by kopelevi on 09/10/2015.
 */
@ApplicationPath("/rest")
public class TaskListApplication extends ResourceConfig {
    public TaskListApplication() {
        packages("sample.grocery.store");
    }

}
