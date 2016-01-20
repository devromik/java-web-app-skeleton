package net.devromik.app.httpApi.model;

import java.io.IOException;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import static com.fasterxml.jackson.core.JsonToken.*;
import static net.devromik.app.httpApi.model.Status.*;

/**
 * @author Shulnyaev Roman
 */
public final class StatusJsonDeserializer extends JsonDeserializer<Status> {

    @Override
    public Status deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        StatusCode code = null;
        String message = null;

        while (parser.nextToken() != END_OBJECT) {
            String attrName = parser.getCurrentName();

            if (attrName.equals(CODE_JSON_ATTR_NAME)) {
                parser.nextToken();
                code = StatusCode.forCode(parser.getIntValue());
            }
            else if (attrName.equals(MESSAGE_JSON_ATTR_NAME)) {
                JsonToken messageToken = parser.nextToken();
                message = messageToken != VALUE_NULL ? parser.getText() : null;
            }
        }

        return new Status(code, message);
    }
}
