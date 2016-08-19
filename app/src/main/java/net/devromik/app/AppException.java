package net.devromik.app;

import net.devromik.app.httpApi.model.*;
import static net.devromik.app.httpApi.model.StatusCode.UNKNOWN_ERROR;

/**
 * @author Shulnyaev Roman
 */
public class AppException extends RuntimeException {

    public AppException(Status status) {
        super(status.message());
        this.status = status;
    }

    public AppException(Exception cause) {
        super(cause);

        this.status =
            cause instanceof AppException ?
            ((AppException)cause).status() :
            new Status(UNKNOWN_ERROR, cause.getMessage());
    }

    public Status status() {
        return status;
    }

    // ****************************** //

    private final Status status;
}
