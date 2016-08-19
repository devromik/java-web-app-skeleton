package net.devromik.app.utils.json;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.google.common.primitives.Longs;
import net.devromik.app.AppException;

/**
 * @author Shulnyaev Roman
 */
public final class JsonUtils {

    public static <T> T fromJson(String json, Class<T> objectClass) {
        try {
            return instance().mapper.readValue(json, objectClass);
        }
        catch (Exception exception) {
            throw new AppException(exception);
        }
    }

    public static String toJson(Object object) {
        try {
            return instance().mapper.writeValueAsString(object);
        }
        catch (Exception exception) {
            throw new AppException(exception);
        }
    }

    public static <T> T fromNode(JsonNode node, Class<T> objectClass) {
        try {
            return instance().mapper.treeToValue(node, objectClass);
        }
        catch (Exception exception) {
            throw new AppException(exception);
        }
    }

    public static JsonNode toJsonNode(Object object) {
        return jsonToNode(toJson(object));
    }

    public static JsonNode jsonToNode(String json) {
        try {
            return instance().mapper.readTree(json);
        }
        catch (Exception exception) {
            throw new AppException(exception);
        }
    }

    public static Long toLong(JsonNode jsonNode) {
        if (jsonNode.canConvertToLong()) {
            return jsonNode.asLong();
        }
        else if (jsonNode instanceof TextNode && Longs.tryParse(jsonNode.asText()) != null) {
            return jsonNode.asLong();
        }
        else {
            return null;
        }
    }

    // ****************************** //

    static class InstanceHolder {
        static final JsonUtils INSTANCE = new JsonUtils();
    }

    JsonUtils() {
        mapper = new ObjectMapper();
        mapper.registerModule(new JodaModule());
    }

    static JsonUtils instance() {
        return InstanceHolder.INSTANCE;
    }

    // ****************************** //

    ObjectMapper mapper;
}