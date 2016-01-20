package net.devromik.app.utils.json;

import org.joda.time.*;
import org.junit.Test;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.*;
import static net.devromik.app.utils.json.JsonUtils.*;
import static org.hamcrest.CoreMatchers.*;
import static org.joda.time.DateTime.now;
import static org.joda.time.DateTimeZone.UTC;
import static org.junit.Assert.*;

public class JsonUtilsTest {

    public static class TestObject {

        @JsonCreator
        public TestObject(@JsonProperty("dateTime") DateTime dateTime) {
            this.dateTime = dateTime;
        }

        // ****************************** //

        public DateTime dateTime;
    }

    // ****************************** //

    @Test
    public void serializesAndDeserializesJodaDateTimeAsUnixTime() {
        TestObject object = new TestObject(now(UTC));
        String json = toJson(object);
        assertThat(json, is("{\"dateTime\":" + object.dateTime.getMillis() + "}"));

        TestObject deserializedObject = fromJson(json, TestObject.class);
        assertThat(deserializedObject.dateTime, is(object.dateTime));
    }

    @Test
    public void canDeserializeObjectFromJsonUsingObjectClass() {
        DateTime now = now(UTC);
        String json = "{\"dateTime\":" + now.getMillis() + "}";
        TestObject object = fromJson(json, TestObject.class);
        assertThat(object.dateTime, is(now));
    }

    @Test
    public void canDeserializeObjectFromJsonNodeUsingObjectClass() {
        TestObject object = new TestObject(now(UTC));
        JsonNode jsonNode = toJsonNode(object);
        assertThat(fromNode(jsonNode, TestObject.class).dateTime, is(object.dateTime));
    }

    @Test
    public void canConvertJsonToJsonNode() {
        DateTime now = now(UTC);
        JsonNode jsonNode = jsonToNode("{\"dateTime\":" + now.getMillis() + "}");
        assertThat(jsonNode.size(), is(1));
        assertThat(jsonNode.get("dateTime").asLong(), is(now.getMillis()));
    }

    @Test
    public void canConvertJsonNodeToLong() {
        JsonNode jsonNode = new ShortNode((short)1);
        assertThat(toLong(jsonNode), is(1L));

        jsonNode = new IntNode(1);
        assertThat(toLong(jsonNode), is(1L));

        jsonNode = new LongNode(1);
        assertThat(toLong(jsonNode), is(1L));

        jsonNode = new TextNode("1");
        assertThat(toLong(jsonNode), is(1L));

        jsonNode = new TextNode("V");
        assertNull(toLong(jsonNode));

        jsonNode = jsonToNode("[1, 2, 3]");
        assertNull(toLong(jsonNode));

        String bigInt =
            "12345678912345678912345678912345678912345678" +
            "91234567891234567891234567891234567891234567" +
            "89123456789123456789123456789123456789123456" +
            "78912345678924545893534574395749534757345734" +
            "78912345678924545893534574395749534757345734";
        jsonNode = jsonToNode(bigInt);
        assertThat(jsonNode, instanceOf(BigIntegerNode.class));
        assertNull(toLong(jsonNode));
    }
}