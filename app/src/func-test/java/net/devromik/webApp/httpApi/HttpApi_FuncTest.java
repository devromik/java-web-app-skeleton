package net.devromik.app.httpApi;

import org.junit.*;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import static com.google.common.base.Preconditions.*;
import static net.devromik.app.AppInstanceInfo.APP_HTTP_API_URI_PREFIX;
import static net.devromik.app.Launcher.*;
import static net.devromik.app.springAppConfig.AppConfig.BUILD_NUMBER_JVM_PROPERTY_NAME;
import static net.devromik.app.utils.json.JsonUtils.*;
import static org.springframework.http.HttpMethod.*;
import static org.springframework.http.MediaType.*;

/**
 * @author Shulnyaev Roman
 */
public abstract class HttpApi_FuncTest {

    // ****************************** //

    @BeforeClass
    public static synchronized void launchApp() {
        if (!appLaunched) {
            System.setProperty(BUILD_NUMBER_JVM_PROPERTY_NAME, "func-test");

            launch(ANY_AVAILABLE_JETTY_PORT, false);
            appLaunched = true;
        }
    }

    // ****************************** //

    /**
     * Request URL: http://host:port + APP_HTTP_API_URI_PREFIX + /urlPartAfterBase.
     */
    public static JsonNode getForJsonNode(String urlPartAfterBase) {
        checkState(appLaunched);

        String requestUrl = makeRequestUrl(urlPartAfterBase);
        String responseJson = new RestTemplate().getForObject(requestUrl, String.class);

        return jsonToNode(responseJson);
    }

    public static JsonNode postForJsonNode(String urlPartAfterBase, Object data) {
        return postForJsonNode(urlPartAfterBase, data, true);
    }

    public static JsonNode postForJsonNode(String urlPartAfterBase, Object data, boolean needSerializeDataIntoJson) {
        return executeRequest(POST, urlPartAfterBase, data, needSerializeDataIntoJson);
    }

    public static JsonNode putForJsonNode(String urlPartAfterBase, Object data) {
        return putForJsonNode(urlPartAfterBase, data, true);
    }

    public static JsonNode putForJsonNode(String urlPartAfterBase, Object data, boolean needSerializeDataIntoJson) {
        return executeRequest(PUT, urlPartAfterBase, data, needSerializeDataIntoJson);
    }

    public static JsonNode deleteForJsonNode(String urlPartAfterBase) throws Exception {
        return executeRequest(DELETE, urlPartAfterBase, null, true);
    }

    // ****************************** //

    private static String makeBaseUrl() {
        return "http://" + getJettyHostName() + ":" + getJettyPort() + APP_HTTP_API_URI_PREFIX + "/";
    }

    private static String makeRequestUrl(String urlPartAfterBase) {
        return makeBaseUrl() + urlPartAfterBase;
    }

    private static JsonNode executeRequest(
        HttpMethod method,
        String urlPartAfterBase,
        Object data,
        boolean needSerializeDataIntoJson) {

        checkState(appLaunched);

        String requestUrl = makeRequestUrl(urlPartAfterBase);
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(APPLICATION_JSON);

        if (data != null && needSerializeDataIntoJson) {
            data = toJson(data);
        }

        ResponseEntity<String> response = new RestTemplate().exchange(
            requestUrl,
            method,
            new HttpEntity<>(data, requestHeaders),
            String.class);

        return jsonToNode(response.getBody());
    }

    // ****************************** //

    private static volatile boolean appLaunched;
}
