package net.devromik.app.monitoring.generalAppInstanceInfo;

import javax.management.openmbean.OpenDataException;
import org.springframework.jmx.export.annotation.*;
import org.springframework.stereotype.Component;
import static net.devromik.app.AppInstanceInfo.*;

/**
 * We can not use a more suitable name "GeneralAppInstanceInfo",
 * because in this case Spring ignores the "description" attribute of the @ManagedAttribute annotation.
 * More generally: if a MBean interface is called "XMBean" then
 * a name of a class that implements the interface must not be "X".
 *
 * @author Shulnyaev Roman
 */
@Component("generalAppInstanceInfoMBean")
@ManagedResource(
    objectName="net.devromik.app:name=generalAppInstanceInfo",
    description="Provides general info on the application instance")
public final class GeneralAppInstanceInfoMBeanImpl implements GeneralAppInstanceInfoMBean {

    @ManagedAttribute(description = "Application name")
    @Override
    public String getName() throws OpenDataException {
        return APP_NAME;
    }

    @ManagedAttribute(description = "Application description")
    @Override
    public String getDescription() throws OpenDataException {
        return APP_DESCRIPTION;
    }

    @ManagedAttribute(description = "Application version")
    @Override
    public String getVersion() throws OpenDataException {
        return APP_VERSION;
    }

    @ManagedAttribute(description = "Application authors")
    @Override
    public String[] getAuthors() throws OpenDataException {
        return APP_AUTHORS;
    }
}