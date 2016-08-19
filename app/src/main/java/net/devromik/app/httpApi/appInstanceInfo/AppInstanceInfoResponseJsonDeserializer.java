package net.devromik.app.httpApi.appInstanceInfo;

import java.io.IOException;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import net.devromik.app.AppInstanceInfo;
import static net.devromik.app.AppInstanceInfo.*;
import static net.devromik.app.httpApi.appInstanceInfo.AppInstanceInfoResponse.APP_INSTANCE_INFO_JSON_ATTR_NAME;
import static net.devromik.app.springAppConfig.AppConfig.BUILD_NUMBER_JVM_PROPERTY_NAME;

/**
 * @author Shulnyaev Roman
 */
public final class AppInstanceInfoResponseJsonDeserializer extends JsonDeserializer<AppInstanceInfoResponse> {

    @Override
    public AppInstanceInfoResponse deserialize(JsonParser reader, DeserializationContext context) throws IOException {
        JsonNode responseJsonNode = reader.readValueAsTree();
        JsonNode infoJsonNode = responseJsonNode.get(APP_INSTANCE_INFO_JSON_ATTR_NAME);

        DateTime localStartUpTime = localStartUpTimeFrom(infoJsonNode);
        String buildNumber = infoJsonNode.get(BUILD_NUMBER_JVM_PROPERTY_NAME).asText();

        AppInstanceInfo info = new AppInstanceInfo(localStartUpTime, buildNumber);

        return new AppInstanceInfoResponse(info);
    }

    DateTime localStartUpTimeFrom(JsonNode infoJsonNode) {
        return DateTime.parse(
            infoJsonNode.get(APP_LOCAL_START_UP_TIME_JSON_ATTR_NAME).asText(),
            DateTimeFormat.forPattern(APP_LOCAL_START_UP_DATE_TIME_FORMAT));
    }
}