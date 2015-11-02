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

    private final Logger LOGGER = LoggerFactory.getLogger(EmbeddedServer.class.getName());

    private static final int DEFAULT_PORT = 8080;
    private static final int HTTP_IDLE_TIMEOUT = 30000;
    private static final String DEFAULT_SERVER_ROOT = "..";
    private static final String WAR_PATH = "/webapp";
    private static final String CONF_FOLDER = "/conf";
    private static final String LOG4J_CONF_FILENAME = "log4j.properties";
    private static final String APP_CONF_FILENAME = "app.properties";
    private static final String JETTY_CONF_FILENAME = "jetty.xml";
    private static final String LOGS_FOLDER = "/logs";
    private static final String LOG_FILENAME_FORMAT = "yyyy_MM_dd.request.log";

    public EmbeddedServer() {
        this(DEFAULT_PORT, DEFAULT_SERVER_ROOT);
    }

    public EmbeddedServer(int port, String appRootPath) {
        validateParams(port, appRootPath);
        enableLog4j(appRootPath);
        publishAppConf(appRootPath);
        configureServer(port, appRootPath);
    }

    private void enableLog4j(String appRootPath) {
        System.setProperty("log.folder.path", getLogsFolderPath(appRootPath));
        String log4jPropsFilePath = getLog4jConfFilePath(appRootPath);
        final File log4jPropertiesFile = new File(log4jPropsFilePath);
        if (log4jPropertiesFile.exists()) {
            PropertyConfigurator.configure(new File(log4jPropsFilePath).getAbsolutePath());
            LOGGER.info("Using log4j configuration: " + log4jPropertiesFile.getAbsolutePath());
        } else {
            String errorMsg = "Failed to find log4j.properties in: " + log4jPropertiesFile.getAbsolutePath();
            System.err.println(errorMsg);
            throw new RuntimeException(errorMsg);
        }
    }

    private void publishAppConf(String appRootPath) {
        try (FileInputStream fis = new FileInputStream(getAppConfFilePath(appRootPath))) {
            Properties appProperties = new Properties();
            appProperties.load(fis);
            appProperties.stringPropertyNames().stream()
                    .map(key -> setAsEnvParam(appProperties, key))
                    .count();
        } catch (Exception e) {
            String errorMsg = "Failed to process application conf file, path: " + getAppConfFilePath(appRootPath);
            LOGGER.error(errorMsg, e);
            throw new RuntimeException(errorMsg, e);
        }
    }

    private String setAsEnvParam(Properties props, String key) {
        return System.setProperty(key, props.getProperty(key));
    }

    private void validateParams(int port, String appRootPath) {
        validatePort(port);
        validateFileExists(getConfFolderPath(appRootPath));
        validateFileExists(getWarPath(appRootPath));
    }

    private void configureServer(int serverPort, String appRootPath) {
        Server server = new Server();
        configureAdditionalServerFromXml(server, appRootPath);
        configureJMX(server);
        configureServerConnectors(server, serverPort);
        configureServerHandlers(server, appRootPath);
        startServer(server);
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
        WebAppContext webAppContext = new WebAppContext(new File(getWarPath(appRootPath)).getAbsolutePath(), "/");
        // change class loader priority for server first
        webAppContext.setParentLoaderPriority(true);
        handlers.addHandler(webAppContext);
        // init access log
        NCSARequestLog requestLog = new NCSARequestLog();
        requestLog.setFilename(getLogFilenameFormat(appRootPath));
        // init request log handler
        RequestLogHandler requestLogHandler = new RequestLogHandler();
        requestLogHandler.setRequestLog(requestLog);
        // add request log jandler to handler's list
        handlers.addHandler(requestLogHandler);
        server.setHandler(handlers);
        LOGGER.info("Added handlers to server.");
    }

    private void configureAdditionalServerFromXml(Server server, String appRootPath) {
        String log4jPropsFilePath = getJettyConfFilePath(appRootPath);
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

    private void validateFileExists(String filePath) {
        final File file = new File(filePath);
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

    private String getLog4jConfFilePath(String appRootPath) {
        return getConfFolderPath(appRootPath) + "/" + LOG4J_CONF_FILENAME;
    }

    private String getJettyConfFilePath(String appRootPath) {
        return getConfFolderPath(appRootPath) + "/" + JETTY_CONF_FILENAME;
    }

    private String getAppConfFilePath(String appRootPath) {
        return getConfFolderPath(appRootPath) + "/" + APP_CONF_FILENAME;
    }

    private String getWarPath(String appRootPath) {
        return appRootPath + WAR_PATH;
    }

    private String getLogsFolderPath(String appRootPath) {
        return appRootPath + LOGS_FOLDER;
    }

    private String getLogFilenameFormat(String appRootPath) {
        return getLogsFolderPath(appRootPath) + "/" + LOG_FILENAME_FORMAT;
    }

    public void startServer(Server server) {
        try {
            server.start();
            LOGGER.info("Server is started.");
            server.join();
        } catch (Exception e) {
            LOGGER.error("Failed to start server.", e);
            server.destroy();
        }
    }

}
