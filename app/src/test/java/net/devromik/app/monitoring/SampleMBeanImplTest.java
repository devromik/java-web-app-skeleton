package net.devromik.app.monitoring;

import java.util.List;
import javax.management.openmbean.*;
import org.junit.*;
import static net.devromik.app.monitoring.SampleMBean.MBEAN_NAME;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

public class SampleMBeanImplTest extends MBeanTest {

    @Before
    public void beforeEachTest() throws OpenDataException {
        sample = new SampleMBeanImpl();
        registerMBean(MBEAN_NAME, sample);
    }

    // ****************************** //

    @Test
    public void provides_Int() {
        sample.setInt(1);
        assertThat(mBeanIntAttributeValue(MBEAN_NAME, "Int"), is(1));
    }

    @Test
    public void provides_IntArray() {
        sample.setIntArray(1, 2, 3);
        assertThat(mBeanIntArrayAttributeValue(MBEAN_NAME, "IntArray"), is(new int[] {1, 2, 3}));
    }

    // ****************************** //

    @Test
    public void provides_Long() {
        sample.setLong(1L);
        assertThat(mBeanLongAttributeValue(MBEAN_NAME, "Long"), is(1L));
    }

    @Test
    public void provides_LongArray() {
        sample.setLongArray(1L, 2L, 3L);
        assertThat(mBeanLongArrayAttributeValue(MBEAN_NAME, "LongArray"), is(new long[] {1L, 2L, 3L}));
    }

    // ****************************** //

    @Test
    public void provides_String() {
        sample.setString("A");
        assertThat(mBeanStringAttributeValue(MBEAN_NAME, "String"), is("A"));
    }

    @Test
    public void provides_StringArray() {
        sample.setStringArray("A", "B", "C");
        assertThat(mBeanStringArrayAttributeValue(MBEAN_NAME, "StringArray"), is(new String[] {"A", "B", "C"}));
    }

    // ****************************** //

    @Test
    public void provides_Table() throws OpenDataException {
        sample.setTableRowIds(1L, 2L, 3L, 4L, 5L);
        TabularData table = mBeanTableAttributeValue(MBEAN_NAME, "Table");
        assertThat(table.size(), is(5));
        int rowId = 1;

        for (Object rowKeyObject : table.keySet()) {
            Object[] rowKey = ((List)rowKeyObject).toArray();
            CompositeData row = table.get(rowKey);

            assertThat(row.get("id"), is((long)rowId));
            assertThat(row.get("value"), is("V_" + rowId));

            ++rowId;
        }
    }

    // ****************************** //

    SampleMBeanImpl sample;
}