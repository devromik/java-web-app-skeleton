package net.devromik.app.httpApi.model;

import java.util.Iterator;
import org.junit.Test;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.IntNode;
import static net.devromik.app.httpApi.model.StatusCode.*;
import static net.devromik.app.utils.json.JsonUtils.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class StatusTest {

    @Test
    public void canBeSerializedIntoJson() throws Exception {
        Status status = new Status(SUCCESS, "ok");
        JsonNode statusJsonNode = toJsonNode(status);
        Iterator<String> statusJsonNodeAttrNamesIter = statusJsonNode.fieldNames();

        assertThat(statusJsonNodeAttrNamesIter.next(), is("code"));
        assertThat(statusJsonNodeAttrNamesIter.next(), is("message"));
        assertFalse(statusJsonNodeAttrNamesIter.hasNext());

        assertThat(statusJsonNode.get("code"), instanceOf(IntNode.class));
        assertThat(forCode(statusJsonNode.get("code").asInt()), is(SUCCESS));
        assertThat(statusJsonNode.get("message").asText(), is("ok"));

        // ****************************** //

        status = Status.SUCCESS;
        statusJsonNode = toJsonNode(status);
        statusJsonNodeAttrNamesIter = statusJsonNode.fieldNames();

        assertThat(statusJsonNodeAttrNamesIter.next(), is("code"));
        assertThat(statusJsonNodeAttrNamesIter.next(), is("message"));
        assertFalse(statusJsonNodeAttrNamesIter.hasNext());

        assertThat(statusJsonNode.get("code"), instanceOf(IntNode.class));
        assertThat(forCode(statusJsonNode.get("code").asInt()), is(SUCCESS));
        assertThat(statusJsonNode.get("message").asText(), is(SUCCESS.defaultMessage()));

        // ****************************** //

        status = new Status(UNKNOWN_ERROR);
        statusJsonNode = toJsonNode(status);
        statusJsonNodeAttrNamesIter = statusJsonNode.fieldNames();

        assertThat(statusJsonNodeAttrNamesIter.next(), is("code"));
        assertThat(statusJsonNodeAttrNamesIter.next(), is("message"));
        assertFalse(statusJsonNodeAttrNamesIter.hasNext());

        assertThat(statusJsonNode.get("code"), instanceOf(IntNode.class));
        assertThat(forCode(statusJsonNode.get("code").asInt()), is(UNKNOWN_ERROR));
        assertTrue(statusJsonNode.get("message").isNull());

        // ****************************** //

        status = new Status(UNKNOWN_ERROR, "Unknown error");
        statusJsonNode = toJsonNode(status);
        statusJsonNodeAttrNamesIter = statusJsonNode.fieldNames();

        assertThat(statusJsonNodeAttrNamesIter.next(), is("code"));
        assertThat(statusJsonNodeAttrNamesIter.next(), is("message"));
        assertFalse(statusJsonNodeAttrNamesIter.hasNext());

        assertThat(statusJsonNode.get("code"), instanceOf(IntNode.class));
        assertThat(forCode(statusJsonNode.get("code").asInt()), is(UNKNOWN_ERROR));
        assertThat(statusJsonNode.get("message").asText(), is("Unknown error"));
    }

    @Test
    public void canBeDeserializedFromJson() throws Exception {
        Status status = new Status(SUCCESS, "ok");
        String statusJson = toJson(status);
        status = fromJson(statusJson, Status.class);

        assertThat(status.code(), is(SUCCESS));
        assertThat(status.message(), is("ok"));

        // ****************************** //

        status = new Status(UNKNOWN_ERROR, "Unknown error");
        statusJson = toJson(status);
        status = fromJson(statusJson, Status.class);

        assertThat(status.code(), is(UNKNOWN_ERROR));
        assertThat(status.message(), is("Unknown error"));

        // ****************************** //

        status = new Status(UNKNOWN_ERROR);
        statusJson = toJson(status);
        status = fromJson(statusJson, Status.class);

        assertThat(status.code(), is(UNKNOWN_ERROR));
        assertNull(status.message());
    }
}



















