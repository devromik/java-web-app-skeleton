package net.devromik.app.utils;

import org.junit.Test;
import net.devromik.app.MandatoryJvmPropertyNotDefinedException;
import static net.devromik.app.utils.JvmPropertyUtils.getMandatoryJvmPropertyValue;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class JvmPropertyUtilsTest {

    @Test(expected = MandatoryJvmPropertyNotDefinedException.class)
    public void canCheckAndReturnValueOfMandatoryJvmProperty() {
        System.setProperty("name", "value");
        assertThat(getMandatoryJvmPropertyValue("name"), is("value"));

        System.clearProperty("name");
        getMandatoryJvmPropertyValue("value");
    }
}