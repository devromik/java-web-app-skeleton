package net.devromik.app.httpApi.model;

import com.fasterxml.jackson.annotation.*;

/**
 * @author Shulnyaev Roman
 */
public class Response {

    @JsonCreator
    public Response(@JsonProperty("status") Status status) {
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }

    // ****************************** //

    private final Status status;
}
