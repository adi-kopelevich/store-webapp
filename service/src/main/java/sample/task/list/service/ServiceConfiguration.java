package sample.task.list.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by kopelevi on 18/10/2015.
 */
public class ServiceConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceConfiguration.class.getName());

    private static final String MONGO_HOST_ENV_KEY = "mongo.host";
    private static final String MONGO_PORT_ENV_KEY = "mongo.port";
    private static final String MONGO_ENABLED_ENV_KEY = "mongo.enabled";

    private static final String DEFAULT_MONGO_HOST = "localhost";
    private static final String DEFAULT_MONGO_PORT = "27017";
    private static final String DEFAULT_MONGO_ENABLED = "false";

    private static final String mongoHost = getProperty(MONGO_HOST_ENV_KEY, DEFAULT_MONGO_HOST);
    private static final String mongoPort = getProperty(MONGO_PORT_ENV_KEY, DEFAULT_MONGO_PORT);
    private static final boolean isMongoEnabled = Boolean.valueOf(getProperty(MONGO_ENABLED_ENV_KEY, DEFAULT_MONGO_ENABLED));

    private ServiceConfiguration() {
    }

    private static String getProperty(String envKey, String defVal) {
        String propertyValue = System.getProperty(envKey);
        if (propertyValue == null) {
            propertyValue = defVal;
            LOGGER.debug("Failed to retrieve " + envKey + " from environment variables, going to use default: " + defVal);
        }
        return propertyValue;
    }

    public static String getMongoHost() {
        return mongoHost;
    }

    public static String getMongoPort() {
        return mongoPort;
    }

    public static boolean isMongoEnabled() {
        return isMongoEnabled;
    }

}
