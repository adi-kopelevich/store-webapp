package sample.grocery.store;


import org.eclipse.jetty.server.Server;
import org.glassfish.jersey.jetty.JettyHttpContainerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import sample.grocery.store.service.impl.ItemsServiceImpl;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;

/**
 * Created by kopelevi on 06/10/2015.
 */
public class EmbeddedServer {

    private static final String DEFAULT_PROTOCOL = "http";
    private static final String DEFAULT_HOST = "localhost";
    private static final String DEFAULT_PORT = "8080";

    private final Server server;

    public EmbeddedServer() {
        StringBuffer serviceURL = new StringBuffer(DEFAULT_PROTOCOL).append("://").append(DEFAULT_HOST);
        URI baseUri = UriBuilder.fromUri(serviceURL.toString()).port(Integer.valueOf(DEFAULT_PORT)).build();
        ResourceConfig config = new ResourceConfig(ItemsServiceImpl.class);
        server = JettyHttpContainerFactory.createServer(baseUri, config);
    }

    public void startServer() {
        try {
            server.start();
        } catch (Exception e) {
            e.printStackTrace();
            server.destroy();
        }
    }

    public void stopServer() {
        try {
            server.stop();
        } catch (Exception e) {
            e.printStackTrace();
            server.destroy();
        }
    }

}
