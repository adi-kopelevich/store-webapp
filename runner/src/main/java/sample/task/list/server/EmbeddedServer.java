package sample.task.list.server;

import org.apache.log4j.PropertyConfigurator;
import org.eclipse.jetty.jmx.MBeanContainer;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.RequestLogHandler;
import org.eclipse.jetty.webapp.WebAppContext;
import org.eclipse.jetty.xml.XmlConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.ServerSocket;
import java.util.Properties;

/**
 * Created by kopelevi on 06/10/2015.
 */
public class EmbeddedServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmbeddedServer.class.getName());

    private static final String DEAFULT_APP_CONTEXT = "/";
    private static final int DEFAULT_PORT = 8080;
    private static final int HTTP_IDLE_TIMEOUT = 30000;
    private static final String DEFAULT_SERVER_ROOT_PATH = "../";
    private static final String WAR_PATH = "/webapp";
    private static final String CONF_FOLDER = "/conf";
    private static final String LOG4J_CONF_FILENAME = "log4j.properties";
    private static final String LOG4J_CONF_ENV_KEY_LOG_FOLDER = "log.folder.path";
    private static final String APP_CONF_FILENAME = "app.properties";
    private static final String JETTY_CONF_FILENAME = "jetty.xml";
    private static final String LOGS_FOLDER = "/logs";
    private static final String ACCESS_LOG_FILENAME_FORMAT = "yyyy_MM_dd.request.log";

    public EmbeddedServer() {
        this(DEFAULT_PORT, DEFAULT_SERVER_ROOT_PATH);
    }

    public EmbeddedServer(int port, String appRootPath) {
        validateParams(port, appRootPath);
        enableLog4j(appRootPath);
        publishAppConfAsEnvVars(appRootPath);
        configureServer(port, appRootPath);
    }

    private void validateParams(int port, String appRootPath) {
        validatePort(port);
        validatePathExists(getConfFolderPath(appRootPath));
        validatePathExists(getWarPath(appRootPath));
    }

    private void validatePort(int serverPort) {
        try {
            new ServerSocket(serverPort).close();
            System.out.println("Using port: " + serverPort);
        } catch (IOException e) {
            String errorMsg = "Failed to bind port: " + serverPort + ". Either port is invalid or already bounded by another process...";
            System.err.println(errorMsg);
            throw new IllegalArgumentException(errorMsg, e);
        }
    }

    private void validatePathExists(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            System.out.println("Using: " + file.getAbsolutePath());
        } else {
            String errorMsg = "Failed to find file path: " + filePath;
            System.err.println(errorMsg);
            throw new IllegalArgumentException(errorMsg);
        }
    }

    private String getConfFolderPath(String appRootPath) {
        return appRootPath + CONF_FOLDER;
    }

    private String getLogsFolderPath(String appRootPath) {
        return appRootPath + LOGS_FOLDER;
    }

    private String getWarPath(String appRootPath) {
        return appRootPath + WAR_PATH;
    }


    private void enableLog4j(String appRootPath) {
        System.setProperty(LOG4J_CONF_ENV_KEY_LOG_FOLDER, getLogsFolderPath(appRootPath));  // to be used inside log4j.properties
        String log4jPropsFilePath = getLog4jConfFilePath(appRootPath);
        File log4jPropertiesFile = new File(log4jPropsFilePath);
        if (log4jPropertiesFile.exists()) {
            PropertyConfigurator.configure(new File(log4jPropsFilePath).getAbsolutePath());
            LOGGER.info("Using log4j configuration: " + log4jPropertiesFile.getAbsolutePath());
        } else {
            String errorMsg = "Failed to find log4j.properties in: " + log4jPropertiesFile.getAbsolutePath();
            System.err.println(errorMsg);
            throw new RuntimeException(errorMsg);
        }
    }

    private String getLog4jConfFilePath(String appRootPath) {
        return getConfFolderPath(appRootPath) + "/" + LOG4J_CONF_FILENAME;
    }

    private void publishAppConfAsEnvVars(String appRootPath) {
        try (FileInputStream fis = new FileInputStream(getAppConfFilePath(appRootPath))) {
            Properties appProperties = new Properties();
            appProperties.load(fis);
            appProperties.stringPropertyNames().stream().parallel()
                    .map(key -> System.setProperty(key, appProperties.getProperty(key)))
                    .count();
        } catch (Exception e) {
            String errorMsg = "Failed to process application conf file, path: " + getAppConfFilePath(appRootPath);
            LOGGER.error(errorMsg, e);
            throw new RuntimeException(errorMsg, e);
        }
    }

    private String getAppConfFilePath(String appRootPath) {
        return getConfFolderPath(appRootPath) + "/" + APP_CONF_FILENAME;
    }

    private void configureServer(int serverPort, String appRootPath) {
        Server server = new Server();
        configureServerCommonConfigurationFromXml(server, appRootPath);
        configureJMX(server);
        configureServerConnectors(server, serverPort);
        configureServerHandlers(server, appRootPath);
        startServer(server);
    }

    private void configureServerCommonConfigurationFromXml(Server server, String appRootPath) {
        String log4jPropsFilePath = getJettyConfFilePath(appRootPath);
        File jettyXmlConfFile = new File(log4jPropsFilePath);
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

    private String getJettyConfFilePath(String appRootPath) {
        return getConfFolderPath(appRootPath) + "/" + JETTY_CONF_FILENAME;
    }

    private void configureJMX(Server server) {
        MBeanContainer mbContainer = new MBeanContainer(ManagementFactory.getPlatformMBeanServer());
        server.addEventListener(mbContainer);
        server.addBean(mbContainer);
    }

    private void configureServerConnectors(Server server, int serverPort) {
        HttpConfiguration http_config = new HttpConfiguration();
        ServerConnector http = new ServerConnector(server,
                new HttpConnectionFactory(http_config));
        http.setPort(serverPort);
        http.setIdleTimeout(HTTP_IDLE_TIMEOUT);
        server.setConnectors(new Connector[]{http});
        LOGGER.info("Added connectors to server.");
    }

    private void configureServerHandlers(Server server, String appRootPath) {
        HandlerCollection handlers = new HandlerCollection();
        // add webapp context handler to handlers collection
        WebAppContext webAppContext = new WebAppContext(getWarPath(appRootPath), DEAFULT_APP_CONTEXT);
        webAppContext.setParentLoaderPriority(true);    // change class loader priority for parent first
        handlers.addHandler(webAppContext);
        // init daily access log and add its handler
        NCSARequestLog requestLog = new NCSARequestLog();
        requestLog.setFilename(getLogFilenameFormat(appRootPath));
        requestLog.setRetainDays(7);
        RequestLogHandler requestLogHandler = new RequestLogHandler();
        requestLogHandler.setRequestLog(requestLog);
        handlers.addHandler(requestLogHandler);
        // set handlers to server instance
        server.setHandler(handlers);
        LOGGER.info("Added handlers to server.");
    }

    private String getLogFilenameFormat(String appRootPath) {
        return getLogsFolderPath(appRootPath) + "/" + ACCESS_LOG_FILENAME_FORMAT;
    }

    public void startServer(Server server) {
        try {
            server.start();
            server.join();  // guarantees that after it the server is indeed ready.
            LOGGER.info("Server is started.");
        } catch (Exception e) {
            LOGGER.error("Failed to start server.", e);
            server.destroy();
        }
    }

}
