package net.devromik.app.httpApi.model;

import java.io.IOException;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import static net.devromik.app.httpApi.model.Status.*;

/**
 * @author Shulnyaev Roman
 */
public final class StatusJsonSerializer extends JsonSerializer<Status> {

    @Override
    public void serialize(
        Status status,
        JsonGenerator writer,
        SerializerProvider provider) throws IOException {

        writer.writeStartObject();
        writer.writeNumberField(CODE_JSON_ATTR_NAME, status.getCode().getCode());

        if (status.hasMessage()) {
            writer.writeStringField(MESSAGE_JSON_ATTR_NAME, status.getMessage());
        }
        else if (status.getCode().hasDefaultMessage()) {
            writer.writeStringField(MESSAGE_JSON_ATTR_NAME, status.getCode().getDefaultMessage());
        }
        else {
            writer.writeNullField(MESSAGE_JSON_ATTR_NAME);
        }

        writer.writeEndObject();
    }
}
