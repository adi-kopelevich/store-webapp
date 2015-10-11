package sample.grocery.store.server;

import org.apache.log4j.PropertyConfigurator;
import org.eclipse.jetty.server.NCSARequestLog;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.RequestLogHandler;
import org.eclipse.jetty.webapp.WebAppContext;
import org.eclipse.jetty.xml.XmlConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;

/**
 * Created by kopelevi on 06/10/2015.
 */
public class EmbeddedServer {

    private final Logger LOGGER = LoggerFactory.getLogger(EmbeddedServer.class);

    private static final int DEFAULT_PORT = 8080;
    private static final String DEFAULT_SERVER_ROOT = "..";
    private static final String WAR_PATH = "/webapp";
    private static final String CONF_FOLDER = "/conf";
    private static final String LOG4J_CONF_FILENAME = "log4j.properties";
    private static final String JETTY_CONF_FILENAME = "jetty.xml";
    private static final String LOGS_FOLDER = "/logs";
    private static final String LOG_FILENAME_FORMAT = "yyyy_MM_dd.request.log";

    private final String serverRootPath;
    private final int serverPort;
    private final Server server;

    public EmbeddedServer() {
        this(DEFAULT_PORT, DEFAULT_SERVER_ROOT);
    }

    public EmbeddedServer(int port, String serverRootPath) {
        this.serverPort = port;
        this.serverRootPath = serverRootPath;

        enableLog4j();

        // validate arguments
        validatePort();
        validateWarFilePath();

        // init server
        server = new Server(serverPort);


        configureServer();


        //todo
        // double jars in webap/web-inf/lib
// deploy UI

    }

    private String getConfFolderPath() {
        return serverRootPath + CONF_FOLDER;
    }

    private String getLog4jConfFilePath() {
        return getConfFolderPath() + "/" + LOG4J_CONF_FILENAME;
    }

    private String getJettyConfFilePath() {
        return getConfFolderPath() + "/" + JETTY_CONF_FILENAME;
    }

    private String getWarPath() {
        return serverRootPath + WAR_PATH;
    }

    private String getLogsFolderPath() {
        return serverRootPath + LOGS_FOLDER;
    }

    private String getLogFilenameFormat() {
        return getLogsFolderPath() + "/" + LOG_FILENAME_FORMAT;
    }

    private void enableLog4j() {
        System.setProperty("log.folder.path", getLogsFolderPath());
        String log4jPropsFilePath = getLog4jConfFilePath();
        final File log4jPropertiesFile = new File(log4jPropsFilePath);
        if (log4jPropertiesFile.exists()) {
            System.out.println("Using log4j configuration: " + log4jPropertiesFile.getAbsolutePath());
            PropertyConfigurator.configure(new File(log4jPropsFilePath).getAbsolutePath());
        } else {
            System.out.println("Failed to find log4j.properties in: " + log4jPropertiesFile.getAbsolutePath());
        }
    }


    private void configureServer() {
        HandlerCollection handlers = new HandlerCollection();

        // add webapp context handler to handlers collection
        WebAppContext webAppContext = new WebAppContext(new File(getWarPath()).getAbsolutePath(), "/");
        // change class loader priority for server first
        webAppContext.setParentLoaderPriority(true);
        handlers.addHandler(webAppContext);

        // init access log
        NCSARequestLog requestLog = new NCSARequestLog();
        requestLog.setFilename(getLogFilenameFormat());
        // init request log handler
        RequestLogHandler requestLogHandler = new RequestLogHandler();
        requestLogHandler.setRequestLog(requestLog);
        // add request log jandler to handler's list
        handlers.addHandler(requestLogHandler);

        // set handlers to server
        server.setHandler(handlers);
        LOGGER.info("Added webapp context handler to server.");

        // configure server from jetty conf XML
        String log4jPropsFilePath = getJettyConfFilePath();
        final File jettyXmlConfFile = new File(log4jPropsFilePath);
        if (jettyXmlConfFile.exists()) {
            try (FileInputStream jettyXmlConfInputStream = new FileInputStream(jettyXmlConfFile)) {
                XmlConfiguration jettyXmlConf = new XmlConfiguration(jettyXmlConfInputStream);
                jettyXmlConf.configure(server);
                LOGGER.info("Configured server according to: " + jettyXmlConfFile.getAbsolutePath());
            } catch (Exception e) {
                LOGGER.error("Failed to parse " + JETTY_CONF_FILENAME + " in: " + jettyXmlConfFile.getAbsolutePath() + ". Going to use jetty defaults...", e);
            }
        }
    }

    private void validatePort() {
        try {
            new ServerSocket(serverPort).close();
            LOGGER.info("Using port: " + serverPort);
        } catch (IOException e) {
            String errorMsg = "Failed to bind port: " + serverPort + ". Either port is invalid or already bounded by another process...";
            LOGGER.error(errorMsg);
            throw new IllegalArgumentException(errorMsg, e);
        }
    }

    private void validateWarFilePath() {
        final File warFile = new File(getWarPath());
        if (warFile.exists()) {
            LOGGER.info("Using war: " + warFile.getAbsolutePath());
        } else {
            String errorMsg = "Failed to find war file path: " + getWarPath();
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
            LOGGER.error("Failed to start server.", e);
            server.destroy();
        }
    }

    public void stopServer() {
        try {
            server.stop();
            LOGGER.info("Server is stopped.");
        } catch (Exception e) {
            LOGGER.error("Failed to stop server.", e);
            server.destroy();
        }
    }

}
