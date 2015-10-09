package sample.grocery.store;


import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

import java.io.File;

/**
 * Created by kopelevi on 06/10/2015.
 */
public class EmbeddedServer {

    private static final String DEFAULT_PROTOCOL = "http";
    private static final String DEFAULT_HOST = "localhost";
    private static final String DEFAULT_PORT = "8080";

    private final Server server;

    public EmbeddedServer() {
//        StringBuffer serviceURL = new StringBuffer(DEFAULT_PROTOCOL).append("://").append(DEFAULT_HOST);
//        URI baseUri = UriBuilder.fromUri(serviceURL.toString()).port(Integer.valueOf(DEFAULT_PORT)).build();
//        ResourceConfig config = new ResourceConfig(ItemsServiceImpl.class);
//        server = JettyHttpContainerFactory.createServer(baseUri, config);

        server = new Server(8080);
        File warFile = new File("../webapp");
        WebAppContext webAppContext = new WebAppContext(warFile.getAbsolutePath(), "/");
//        webAppContext.addAliasCheck(new AllowSymLinkAliasChecker());

        server.setHandler(webAppContext);


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
