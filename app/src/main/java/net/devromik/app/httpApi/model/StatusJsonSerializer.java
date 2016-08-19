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

        writeCode(status, writer);
        writeMessage(status, writer);

        writer.writeEndObject();
    }

    void writeCode(Status status, JsonGenerator writer) throws IOException {
        writer.writeNumberField(CODE_JSON_ATTR_NAME, status.code().code());
    }

    void writeMessage(Status status, JsonGenerator writer) throws IOException {
        if (status.hasMessage()) {
            writer.writeStringField(MESSAGE_JSON_ATTR_NAME, status.message());
        }
        else if (status.code().hasDefaultMessage()) {
            writer.writeStringField(MESSAGE_JSON_ATTR_NAME, status.code().defaultMessage());
        }
        else {
            writer.writeNullField(MESSAGE_JSON_ATTR_NAME);
        }
    }
}
