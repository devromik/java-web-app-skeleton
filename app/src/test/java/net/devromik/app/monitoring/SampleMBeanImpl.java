package net.devromik.app.monitoring;

import javax.management.openmbean.*;
import org.springframework.jmx.export.annotation.*;
import org.springframework.stereotype.Component;
import static com.google.common.base.Preconditions.*;
import static javax.management.openmbean.SimpleType.*;

/**
 * @author Shulnyaev Roman
 */
@Component("sampleMBean")
@ManagedResource(
    objectName="net.devromik.app:name=sample",
    description="sample")
public class SampleMBeanImpl implements SampleMBean {

    public SampleMBeanImpl() throws OpenDataException {
        makeTableType();
    }

    // ****************************** //

    public void setInt(int intValue) {
        this.intValue = intValue;
    }

    @ManagedAttribute(description = "Int")
    @Override
    public int getInt() {
        return intValue;
    }

    public void setIntArray(int... intArray) {
        this.intArray = checkNotNull(intArray);
    }

    @ManagedAttribute(description = "IntArray")
    @Override
    public int[] getIntArray() throws OpenDataException {
        return intArray;
    }

    // ****************************** //

    public void setLong(long longValue) {
        this.longValue = longValue;
    }

    @ManagedAttribute(description = "Long")
    @Override
    public long getLong() throws OpenDataException {
        return longValue;
    }

    public void setLongArray(long... longArray) {
        this.longArray = checkNotNull(longArray);
    }

    @ManagedAttribute(description = "LongArray")
    @Override
    public long[] getLongArray() throws OpenDataException {
        return longArray;
    }

    // ****************************** //

    public void setString(String stringValue) {
        this.stringValue = stringValue;
    }

    @ManagedAttribute(description = "String")
    @Override
    public String getString() throws OpenDataException {
        return stringValue;
    }

    public void setStringArray(String... stringArray) {
        this.stringArray = checkNotNull(stringArray);
    }

    @ManagedAttribute(description = "StringArray")
    @Override
    public String[] getStringArray() throws OpenDataException {
        return stringArray;
    }

    /* ***** Table (TabularType). ***** */

    private final static String[] TABLE_COLUMN_NAMES = {
        "id",
        "value"
    };

    private final static OpenType[] TABLE_COLUMN_TYPES = {
        LONG,
        STRING
    };

    private final static String[] TABLE_COLUMN_DESCRIPTIONS = {
        "Identifier",
        "Value"
    };

    private final static String[] TABLE_INDEX = {
        "id"
    };

    // ****************************** //

    public void setTableRowIds(long... tableRowIds) {
        this.tableRowIds = checkNotNull(tableRowIds);
    }

    @ManagedAttribute(description = "Table")
    @Override
    public TabularData getTable() throws OpenDataException {
        TabularDataSupport table = new TabularDataSupport(tableType);

        for (long id : tableRowIds) {
            String value = "V_" + id;
            Object[] row = {id, value};
            table.put(new CompositeDataSupport(tableRowType, TABLE_COLUMN_NAMES, row));
        }

        return table;
    }

    // ****************************** //

    void makeTableType() throws OpenDataException {
        tableRowType = new CompositeType(
            "SampleTableRowType",
            "Row type of sample table",
            TABLE_COLUMN_NAMES,
            TABLE_COLUMN_DESCRIPTIONS,
            TABLE_COLUMN_TYPES);

        tableType = new TabularType(
            "SampleTableType",
            "Type of sample table",
            tableRowType,
            TABLE_INDEX);
    }

    // ****************************** //

    int intValue;
    int[] intArray = new int[] {};

    long longValue;
    long[] longArray = new long[] {};

    String stringValue;
    String[] stringArray = new String[] {};

    CompositeType tableRowType;
    TabularType tableType;
    long[] tableRowIds = new long[] {};
}
