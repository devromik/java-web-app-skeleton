package net.devromik.app.httpApi.model;

/**
 * @author Shulnyaev Roman
 */
public enum StatusCode {
    UNKNOWN_ERROR(-1, null),
    SUCCESS(0, "Success"),

    REQUEST_BODY_NOT_VALID_JSON(1, "Request body is not a valid JSON"),
    INVALID_JSON_FORMAT(2, "Invalid JSON format");

    // ****************************** //

    public static StatusCode forCode(int code) {
        for (StatusCode statusCode : values()) {
            if (statusCode.code == code) {
                return statusCode;
            }
        }

        return null;
    }

    // ****************************** //

    StatusCode(int code, String defaultMessage) {
        this.code = code;
        this.defaultMessage = defaultMessage;
    }

    public int code() {
        return code;
    }

    public boolean hasDefaultMessage() {
        return defaultMessage != null;
    }

    public String defaultMessage() {
        return defaultMessage;
    }

    // ****************************** //

    final int code;
    final String defaultMessage;
}
