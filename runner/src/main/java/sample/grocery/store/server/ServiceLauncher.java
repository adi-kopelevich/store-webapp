package sample.grocery.store.server;

import org.apache.log4j.PropertyConfigurator;

import java.io.File;

/**
 * Created by kopelevi on 07/10/2015.
 */
public class ServiceLauncher {

    private static final String CONF_FOLDER_PATH = "../conf";
    private static final String LOG4J_CONF_FILENAME = "log4j.properties";

    public static void main(String[] args) {
        enableLog4j();
        EmbeddedServer embeddedServer = new EmbeddedServer();
        embeddedServer.startServer(true);
    }

    private static void enableLog4j() {
        String log4jPropsFilePath = CONF_FOLDER_PATH + "/" + LOG4J_CONF_FILENAME;
        File log4jPropertiesFile = new File(log4jPropsFilePath);
        if (log4jPropertiesFile.exists()) {
            System.out.println("Using log4j configuration: " + log4jPropertiesFile.getAbsolutePath());
            PropertyConfigurator.configure(new File(log4jPropsFilePath).getAbsolutePath());
        } else {
            System.out.println("Failed to find log4j.properties in: " + log4jPropertiesFile);
        }
    }

}
