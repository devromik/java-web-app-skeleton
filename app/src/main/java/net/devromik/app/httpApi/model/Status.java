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
        this(code, code.defaultMessage());
    }

    public Status(StatusCode code, String message) {
        this.code = checkNotNull(code);
        this.message = message;
    }

    public StatusCode code() {
        return code;
    }

    public boolean hasMessage() {
        return message != null;
    }

    public String message() {
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

        Status that = (Status)other;

        return
            code.equals(that.code) &&
            Objects.equals(message, that.message);
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

    final StatusCode code;
    final String message;
}
