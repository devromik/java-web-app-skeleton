package net.devromik.app.httpApi.model;

import java.util.Objects;
import com.fasterxml.jackson.databind.annotation.*;
import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.*;

/**
 * @author Shulnyaev Roman
 */
@JsonSerialize(using = StatusJsonSerializer.class)
@JsonDeserialize(using = StatusJsonDeserializer.class)
public final class Status {

    public static final Status SUCCESS = new Status(StatusCode.SUCCESS);

    public static final String CODE_JSON_ATTR_NAME = "code";
    public static final String MESSAGE_JSON_ATTR_NAME = "message";

    // ****************************** //

    public Status(StatusCode code) {
        this(code, code.getDefaultMessage());
    }

    public Status(StatusCode code, String message) {
        this.code = checkNotNull(code);
        this.message = message;
    }

    public StatusCode getCode() {
        return code;
    }

    public boolean hasMessage() {
        return message != null;
    }

    public String getMessage() {
        return message;
    }

    // ****************************** //

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (other == null) {
            return false;
        }

        if (other.getClass() != getClass()) {
            return false;
        }

        Status otherStatus = (Status)other;

        return
            code.equals(otherStatus.code) &&
            Objects.equals(message, otherStatus.message);
    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }

    @Override
    public String toString() {
        return toStringHelper(this)
            .add("code", code)
            .add("message", message)
            .toString();
    }

    // ****************************** //

    private final StatusCode code;
    private final String message;
}
