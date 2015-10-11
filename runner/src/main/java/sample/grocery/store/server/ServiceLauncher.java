package sample.grocery.store.server;

/**
 * Created by kopelevi on 07/10/2015.
 */
public class ServiceLauncher {

    public static void main(String[] args) {
        EmbeddedServer embeddedServer = new EmbeddedServer();
        embeddedServer.startServer(true);
    }





}
