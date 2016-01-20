package net.devromik.app;

import net.devromik.app.httpApi.model.*;
import static net.devromik.app.httpApi.model.StatusCode.UNKNOWN_ERROR;

/**
 * @author Shulnyaev Roman
 */
public final class MandatoryJvmPropertyNotDefinedException extends AppException {

    public MandatoryJvmPropertyNotDefinedException(String propertyName) {
        super(new Status(
            UNKNOWN_ERROR,
            "Mandatory JVM property \"" + propertyName + "\" is not defined"));
    }
}