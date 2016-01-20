package net.devromik.app.monitoring;

import javax.management.openmbean.*;

/**
 * @author Shulnyaev Roman
 */
public interface SampleMBean {

    String MBEAN_NAME = "sample";

    // ****************************** //

    int getInt() throws OpenDataException;
    int[] getIntArray() throws OpenDataException;

    long getLong() throws OpenDataException;
    long[] getLongArray() throws OpenDataException;

    String getString() throws OpenDataException;
    String[] getStringArray() throws OpenDataException;

    TabularData getTable() throws OpenDataException;
}
