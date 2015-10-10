package sample.grocery.store.server;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;

/**
 * Created by kopelevi on 06/10/2015.
 */
public class EmbeddedServer {

    private final Logger LOGGER = LoggerFactory.getLogger(EmbeddedServer.class);

    private static final int DEFAULT_PORT = 8080;
    private static final String DEFAULT_WAR_PATH = "../webapp";

    private final Server server;

    public EmbeddedServer() {
        this(DEFAULT_PORT, DEFAULT_WAR_PATH);
    }

    public EmbeddedServer(int port, String warFilePath) {

        // validate arguments
        validatePort(port);
        validateWarFilePath(warFilePath);

        // init server
        server = new Server(port);
        // init webapp
        WebAppContext webAppContext = new WebAppContext(new File(warFilePath).getAbsolutePath(), "/");
        // change class loader priority for server first
        webAppContext.setParentLoaderPriority(true);
        // set webapp handler to server
        server.setHandler(webAppContext);

        //todo
// re-arrange all versions/names in place hodlers
// add jetty.server xml for general configurations
// deploy UI

    }

    private void validatePort(int port) {
        try {
            new ServerSocket(port).close();
            LOGGER.info("Using port: " + port);
        } catch (IOException e) {
            String errorMsg = "Failed to bind port: " + port + ". Either port is invalid or already bounded by another process...";
            LOGGER.error(errorMsg);
            throw new IllegalArgumentException(errorMsg, e);
        }
    }

    private void validateWarFilePath(String warFilePath) {
        File warFile = new File(warFilePath);
        if (warFile.exists()) {
            LOGGER.info("Using war: " + warFile.getAbsolutePath());
        } else {
            String errorMsg = "Failed to find war file path: " + warFilePath;
            LOGGER.error(errorMsg);
            throw new IllegalArgumentException(errorMsg);
        }
    }

    public void startServer() {
        startServer(false);
    }

    public void startServer(boolean shouldJoin) {
        try {
            server.start();
            LOGGER.info("Server is started.");
            if (shouldJoin) {
                server.join();
            }
        } catch (Exception e) {
            e.printStackTrace();
            server.destroy();
        }
    }

    public void stopServer() {
        try {
            server.stop();
            LOGGER.info("Server is stopped.");
        } catch (Exception e) {
            e.printStackTrace();
            server.destroy();
        }
    }

}
