package net.devromik.app;

import org.junit.Test;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class MandatoryJvmPropertyNotDefinedExceptionTest {

    @Test
    public void messageShowsNotDefinedProperty() {
        MandatoryJvmPropertyNotDefinedException exception = new MandatoryJvmPropertyNotDefinedException("propertyName");
        assertThat(exception.getMessage(), is("Mandatory JVM property \"propertyName\" is not defined"));
    }
}