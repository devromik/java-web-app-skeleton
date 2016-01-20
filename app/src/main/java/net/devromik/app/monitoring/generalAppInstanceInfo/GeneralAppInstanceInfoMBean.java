package net.devromik.app.monitoring.generalAppInstanceInfo;

import javax.management.openmbean.*;

/**
 * @author Shulnyaev Roman
 */
public interface GeneralAppInstanceInfoMBean {

    String MBEAN_NAME = "generalAppInstanceInfo";

    // ****************************** //

    String getName() throws OpenDataException;
    String getDescription() throws OpenDataException;
    String getVersion() throws OpenDataException;
    String[] getAuthors() throws OpenDataException;
}
