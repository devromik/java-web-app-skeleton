package net.devromik.app.monitoring;

import java.util.*;
import javax.management.*;
import javax.management.openmbean.TabularData;
import org.junit.*;
import static com.google.common.base.Preconditions.*;
import static javax.management.MBeanServerFactory.*;
import net.devromik.app.*;
import static net.devromik.app.Launcher.WEB_APP_CONTEXT_CONFIG_LOCATION;

/**
 * @author Shulnyaev Roman
 */
public abstract class MBeanTest {

    @After
    public void afterEachTest() {
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
        ObjectName mBeanObjectName = mBeanObjectNameForMBeanName(mBeanName);

        try {
            Class mBeanInterface = mBeanInterfaceOf(mBean);
            StandardMBean standardMBean = new StandardMBean(mBean, mBeanInterface);
            mBeanServer.registerMBean(standardMBean, mBeanObjectName);
            mBeanNameToMBeanObjectName.put(mBeanName, mBeanObjectName);
        }
        catch (Exception exception) {
            throw new AppException(exception);
        }
    }

    Class mBeanInterfaceOf(Object mBean) {
        Class mBeanInterface = null;

        for (Class mBeanInterfaceCandidate : mBean.getClass().getInterfaces()) {
            if (mBeanInterfaceCandidate.getSimpleName().endsWith("MBean")) {
                mBeanInterface = mBeanInterfaceCandidate;
                break;
            }
        }

        return checkNotNull(mBeanInterface);
    }

    // ****************************** //

    protected final Object mBeanAttributeValue(String mBeanName, String mBeanAttrName) {
        ObjectName mBeanObjectName = mBeanObjectNameForMBeanName(mBeanName);

        try {
            return mBeanServer.getAttribute(mBeanObjectName, mBeanAttrName);
        }
        catch (Exception exception) {
            throw new AppException(exception);
        }
    }

    // ****************************** //

    protected final int mBeanIntAttributeValue(String mBeanName, String mBeanAttrName) {
        return (int)mBeanAttributeValue(mBeanName, mBeanAttrName);
    }

    protected final int[] mBeanIntArrayAttributeValue(String mBeanName, String mBeanAttrName) {
        return (int[])mBeanAttributeValue(mBeanName, mBeanAttrName);
    }

    // ****************************** //

    protected final long mBeanLongAttributeValue(String mBeanName, String mBeanAttrName) {
        return (long)mBeanAttributeValue(mBeanName, mBeanAttrName);
    }

    protected final long[] mBeanLongArrayAttributeValue(String mBeanName, String mBeanAttrName) {
        return (long[])mBeanAttributeValue(mBeanName, mBeanAttrName);
    }

    // ****************************** //

    protected final String mBeanStringAttributeValue(String mBeanName, String mBeanAttrName) {
        return (String)mBeanAttributeValue(mBeanName, mBeanAttrName);
    }

    protected final String[] mBeanStringArrayAttributeValue(String mBeanName, String mBeanAttrName) {
        return (String[])mBeanAttributeValue(mBeanName, mBeanAttrName);
    }

    // ****************************** //

    protected final TabularData mBeanTableAttributeValue(String mBeanName, String mBeanAttrName) {
        return (TabularData)mBeanAttributeValue(mBeanName, mBeanAttrName);
    }

    // ****************************** //

    static ObjectName mBeanObjectNameForMBeanName(String mBeanName) {
        try {
            return new ObjectName(WEB_APP_CONTEXT_CONFIG_LOCATION, "name", mBeanName);
        }
        catch (MalformedObjectNameException exception) {
            throw new AppException(exception);
        }
    }

    // ****************************** //

    static MBeanServer mBeanServer = newMBeanServer();
    Map<String, ObjectName> mBeanNameToMBeanObjectName = new HashMap<>();
}
