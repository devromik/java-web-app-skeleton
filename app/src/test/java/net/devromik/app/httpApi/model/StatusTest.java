package net.devromik.app.httpApi.model;

import java.util.Iterator;
import org.junit.Test;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.IntNode;
import static net.devromik.app.httpApi.model.StatusCode.*;
import net.devromik.app.utils.json.JsonUtils;
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
        assertThat(statusJsonNode.get("message").asText(), is(SUCCESS.getDefaultMessage()));

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
        String statusJson = JsonUtils.toJson(status);
        status = fromJson(statusJson, Status.class);

        assertThat(status.getCode(), is(SUCCESS));
        assertThat(status.getMessage(), is("ok"));

        // ****************************** //

        status = new Status(UNKNOWN_ERROR, "Unknown error");
        statusJson = JsonUtils.toJson(status);
        status = fromJson(statusJson, Status.class);

        assertThat(status.getCode(), is(UNKNOWN_ERROR));
        assertThat(status.getMessage(), is("Unknown error"));

        // ****************************** //

        status = new Status(UNKNOWN_ERROR);
        statusJson = JsonUtils.toJson(status);
        status = fromJson(statusJson, Status.class);

        assertThat(status.getCode(), is(UNKNOWN_ERROR));
        assertNull(status.getMessage());
    }
}



















