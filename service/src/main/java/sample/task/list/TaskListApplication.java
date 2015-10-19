package sample.task.list;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.ApplicationPath;

/**
 * Created by kopelevi on 09/10/2015.
 */
@ApplicationPath("/rest")
public class TaskListApplication extends ResourceConfig {
    public TaskListApplication() {
        packages("sample.task.list");   // register packahes for Jeresy resources
        register(JacksonFeature.class); // register Jackson JSON providers to automatically handled Object --> JSON
    }

}
