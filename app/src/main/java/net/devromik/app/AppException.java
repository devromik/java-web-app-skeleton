package net.devromik.app;

import net.devromik.app.httpApi.model.*;
import static net.devromik.app.httpApi.model.StatusCode.UNKNOWN_ERROR;

/**
 * @author Shulnyaev Roman
 */
public class AppException extends RuntimeException {

    public AppException(Status status) {
        super(status.getMessage());
        this.status = status;
    }

    public AppException(Exception cause) {
        super(cause);
        this.status =
            cause instanceof AppException ?
            ((AppException)cause).getStatus() :
            new Status(UNKNOWN_ERROR, cause.getMessage());
    }

    public Status getStatus() {
        return status;
    }

    // ****************************** //

    private final Status status;
}
