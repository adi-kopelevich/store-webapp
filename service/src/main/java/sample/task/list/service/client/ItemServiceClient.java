package sample.task.list.service.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sample.task.list.service.ItemsService;
import sample.task.list.service.model.TaskItem;
import sample.task.list.service.model.TaskList;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;

/**
 * Created by kopelevi on 04/09/2015.
 */
public class ItemServiceClient implements ItemsService {

    private final Logger LOGGER = LoggerFactory.getLogger(ItemServiceClient.class);

    private static final String DEFAULT_PROTOCOL = "http";
    private static final String DEFAULT_HOST = "localhost";
    private static final int DEFAULT_PORT = 8080;
    private static final int MAX_PORT = 65535;
    private static final String DEFAULT_CONTEXT = "";
    private static final String RESOUCEE = "items";
    private static final String APP_PATH = "rest";
    private final MediaType mediaType;
    private final WebTarget webTarget;


    public ItemServiceClient(String protpcol, String hostname, int port, String context, MediaType mediaType) {
        //init client
        try {
            validateMediaType(mediaType);
            this.mediaType = mediaType;
            String serviceURL = buildServiceURL(protpcol, hostname, port, context);
            Client client = ClientBuilder.newClient();
            this.webTarget = client.target(serviceURL.toString());
            LOGGER.info("Initiated client with target " + serviceURL);
        } catch (Exception e) {
            String msg = "Failed to init client";
            LOGGER.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }

    private void validateMediaType(MediaType mediaType) {
        if (!MediaType.APPLICATION_JSON_TYPE.equals(mediaType) && !MediaType.APPLICATION_XML_TYPE.equals(mediaType)) {
            String msg = "Given MediaType: " + mediaType + " is invalid, only the following are supported: " + MediaType.APPLICATION_JSON + "," + MediaType.APPLICATION_XML;
            LOGGER.error(msg);
            throw new IllegalArgumentException(msg);
        }
    }

    private String buildServiceURL(String protocol, String hostname, int port, String context) {
        StringBuffer serviceURL = new StringBuffer(protocol).append("://").append(hostname);

        if (port > 0 && port <= MAX_PORT) {
            serviceURL.append(":").append(port);
        } else {
            String msg = "Given port: " + port + " is invalid, should be in the range of 1-" + MAX_PORT;
            LOGGER.error(msg);
            throw new IllegalArgumentException(msg);
        }

        if (context != null && !context.isEmpty()) {
            serviceURL.append("/").append(context);
        }

        serviceURL.append("/").append(APP_PATH).append("/").append(RESOUCEE);

        return serviceURL.toString();
    }

    public ItemServiceClient(String hostname, int port, MediaType mediaType) {
        this(DEFAULT_PROTOCOL, hostname, port, DEFAULT_CONTEXT, mediaType);
    }

    public ItemServiceClient(MediaType mediaType) {
        this(DEFAULT_PROTOCOL, DEFAULT_HOST, DEFAULT_PORT, DEFAULT_CONTEXT, mediaType);
    }


    public TaskList getAllItems() {
        Invocation.Builder invocationBuilder = webTarget.request(mediaType);
        return invocationBuilder.get(TaskList.class);
    }

    public TaskItem getItem(int itemId) {
        Invocation.Builder invocationBuilder = webTarget.path(String.valueOf(itemId)).request(mediaType);
        return invocationBuilder.get(TaskItem.class);
    }

    public void addItem(TaskItem item) {
        Invocation.Builder invocationBuilder = webTarget.request(mediaType);
        invocationBuilder.post(Entity.entity(item, mediaType));
    }

    public void removeItem(int itemId) {
        Invocation.Builder invocationBuilder = webTarget.path(String.valueOf(itemId)).request();
        invocationBuilder.delete();
    }

    public void updateItem(TaskItem item) {
        Invocation.Builder invocationBuilder = webTarget.request(mediaType);
        invocationBuilder.put(Entity.entity(item, mediaType));
    }

    public void clearAll() {
        Invocation.Builder invocationBuilder = webTarget.request();
        invocationBuilder.delete();
    }
}
