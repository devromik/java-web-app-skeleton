package net.devromik.app.httpApi.appInstanceInfo;

import java.io.IOException;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import net.devromik.app.AppInstanceInfo;
import static net.devromik.app.AppInstanceInfo.*;
import static net.devromik.app.springAppConfig.AppConfig.BUILD_NUMBER_JVM_PROPERTY_NAME;

/**
 * @author Shulnyaev Roman
 */
public final class AppInstanceInfoResponseJsonDeserializer extends JsonDeserializer<AppInstanceInfoResponse> {

    @Override
    public AppInstanceInfoResponse deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        JsonNode responseJsonNode = parser.readValueAsTree();
        JsonNode infoJsonNode = responseJsonNode.get("appInstanceInfo");

        DateTime localStartUpTime = DateTime.parse(
            infoJsonNode.get("localStartUpTime").asText(),
            DateTimeFormat.forPattern(APP_LOCAL_START_UP_DATE_TIME_FORMAT));

        String buildNumber = infoJsonNode.get(BUILD_NUMBER_JVM_PROPERTY_NAME).asText();

        return new AppInstanceInfoResponse(new AppInstanceInfo(localStartUpTime, buildNumber));
    }
}
