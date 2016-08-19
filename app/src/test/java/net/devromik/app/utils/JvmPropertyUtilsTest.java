package net.devromik.app.utils;

import org.junit.Test;
import net.devromik.app.MandatoryJvmPropertyNotDefinedException;
import static net.devromik.app.utils.JvmPropertyUtils.valueOfMandatoryJvmProperty;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class JvmPropertyUtilsTest {

    @Test(expected = MandatoryJvmPropertyNotDefinedException.class)
    public void canProvideValueOfMandatoryJvmProperty() {
        System.setProperty("name", "value");
        assertThat(valueOfMandatoryJvmProperty("name"), is("value"));

        System.clearProperty("name");
        valueOfMandatoryJvmProperty("name");
    }
}