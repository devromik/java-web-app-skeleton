package net.devromik.app.httpApi.model;

import com.fasterxml.jackson.annotation.*;

/**
 * @author Shulnyaev Roman
 */
public class Response {

    public static final String STATUS_JSON_ATTR_NAME = "status";

    // ****************************** //

    @JsonCreator
    public Response(@JsonProperty(STATUS_JSON_ATTR_NAME) Status status) {
        this.status = status;
    }

    @JsonGetter(STATUS_JSON_ATTR_NAME)
    public Status status() {
        return status;
    }

    // ****************************** //

    final Status status;
}
