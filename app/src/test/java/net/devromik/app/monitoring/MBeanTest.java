package net.devromik.app.monitoring;

import java.util.*;
import javax.management.*;
import javax.management.openmbean.TabularData;
import org.junit.*;
import static com.google.common.base.Preconditions.*;
import static javax.management.MBeanServerFactory.*;
import net.devromik.app.*;
import static net.devromik.app.Launcher.SPRING_CONTEXT_CONFIG_LOCATION;

/**
 * @author Shulnyaev Roman
 */
public abstract class MBeanTest {

    @After
    public void afterTest() {
        for (ObjectName mBeanObjectName : mBeanNameToMBeanObjectName.values()) {
            try {
                mBeanServer.unregisterMBean(mBeanObjectName);
            }
            catch (Exception exception) {
                throw new AppException(exception);
            }
        }

        mBeanNameToMBeanObjectName.clear();
    }

    // ****************************** //

    protected final void registerMBean(String mBeanName, Object mBean) {
        checkState(!mBeanNameToMBeanObjectName.containsKey(mBeanName));
        ObjectName mBeanObjectName = makeMBeanObjectNameForMBeanName(mBeanName);

        try {
            Class mBeanInterface = null;

            for (Class mBeanInterfaceCandidate : mBean.getClass().getInterfaces()) {
                if (mBeanInterfaceCandidate.getSimpleName().endsWith("MBean")) {
                    mBeanInterface = mBeanInterfaceCandidate;
                    break;
                }
            }

            StandardMBean standardMBean = new StandardMBean(mBean, mBeanInterface);
            mBeanServer.registerMBean(standardMBean, mBeanObjectName);
            mBeanNameToMBeanObjectName.put(mBeanName, mBeanObjectName);
        }
        catch (Exception exception) {
            throw new AppException(exception);
        }
    }

    // ****************************** //

    protected final Object getMBeanAttributeValue(String mBeanName, String mBeanAttrName) {
        ObjectName mBeanObjectName = makeMBeanObjectNameForMBeanName(mBeanName);

        try {
            return mBeanServer.getAttribute(mBeanObjectName, mBeanAttrName);
        }
        catch (Exception exception) {
            throw new AppException(exception);
        }
    }

    // ****************************** //

    protected final int getMBeanIntAttributeValue(String mBeanName, String mBeanAttrName) {
        return (int)getMBeanAttributeValue(mBeanName, mBeanAttrName);
    }

    protected final int[] getMBeanIntArrayAttributeValue(String mBeanName, String mBeanAttrName) {
        return (int[])getMBeanAttributeValue(mBeanName, mBeanAttrName);
    }

    // ****************************** //

    protected final long getMBeanLongAttributeValue(String mBeanName, String mBeanAttrName) {
        return (long)getMBeanAttributeValue(mBeanName, mBeanAttrName);
    }

    protected final long[] getMBeanLongArrayAttributeValue(String mBeanName, String mBeanAttrName) {
        return (long[])getMBeanAttributeValue(mBeanName, mBeanAttrName);
    }

    // ****************************** //

    protected final String getMBeanStringAttributeValue(String mBeanName, String mBeanAttrName) {
        return (String)getMBeanAttributeValue(mBeanName, mBeanAttrName);
    }

    protected final String[] getMBeanStringArrayAttributeValue(String mBeanName, String mBeanAttrName) {
        return (String[])getMBeanAttributeValue(mBeanName, mBeanAttrName);
    }

    // ****************************** //

    protected final TabularData getMBeanTableAttributeValue(String mBeanName, String mBeanAttrName) {
        return (TabularData)getMBeanAttributeValue(mBeanName, mBeanAttrName);
    }

    // ****************************** //

    private static ObjectName makeMBeanObjectNameForMBeanName(String mBeanName) {
        try {
            return new ObjectName(SPRING_CONTEXT_CONFIG_LOCATION, "name", mBeanName);
        }
        catch (MalformedObjectNameException exception) {
            throw new AppException(exception);
        }
    }

    // ****************************** //

    private static MBeanServer mBeanServer = newMBeanServer();
    private Map<String, ObjectName> mBeanNameToMBeanObjectName = new HashMap<>();
}
