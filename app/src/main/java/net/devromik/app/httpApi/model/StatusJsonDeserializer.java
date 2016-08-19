package net.devromik.app.httpApi.model;

import java.io.IOException;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import static net.devromik.app.httpApi.model.Status.*;
import static net.devromik.app.httpApi.model.StatusCode.forCode;

/**
 * @author Shulnyaev Roman
 */
public final class StatusJsonDeserializer extends JsonDeserializer<Status> {

    @Override
    public Status deserialize(JsonParser reader, DeserializationContext context) throws IOException {
        JsonNode statusJsonNode = reader.readValueAsTree();

        StatusCode code = codeFrom(statusJsonNode);
        String message = messageFrom(statusJsonNode);

        return new Status(code, message);
    }

    StatusCode codeFrom(JsonNode statusJsonNode) {
        return forCode(statusJsonNode.get(CODE_JSON_ATTR_NAME).intValue());
    }

    String messageFrom(JsonNode statusJsonNode) {
        JsonNode messageJsonNode = statusJsonNode.get(MESSAGE_JSON_ATTR_NAME);
        return messageJsonNode != null ? messageJsonNode.textValue() : null;
    }
}
