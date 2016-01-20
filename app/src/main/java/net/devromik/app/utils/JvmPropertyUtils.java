package net.devromik.app.utils;

import static com.google.common.base.Preconditions.*;
import net.devromik.app.MandatoryJvmPropertyNotDefinedException;

/**
 * @author Shulnyaev Roman
 */
public final class JvmPropertyUtils {

    public static String getMandatoryJvmPropertyValue(String propertyName) {
        String propertyValue = System.getProperty(checkNotNull(propertyName));

        if (propertyValue == null) {
            throw new MandatoryJvmPropertyNotDefinedException(propertyName);
        }

        return propertyValue;
    }
}
