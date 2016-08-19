package net.devromik.app.monitoring.generalAppInstanceInfo;

import javax.management.openmbean.*;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import net.devromik.app.AppInstanceInfo;
import net.devromik.app.monitoring.MBeanTest;
import static net.devromik.app.monitoring.generalAppInstanceInfo.GeneralAppInstanceInfoMBean.MBEAN_NAME;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@ContextConfiguration("/contexts/monitoring/general-app-instance-info-test-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class GeneralAppInstanceInfoMBeanTest extends MBeanTest {

    @Before
    public void beforeEachTest() throws OpenDataException {
        registerMBean(MBEAN_NAME, generalAppInstanceInfoMBean);
    }

    // ****************************** //

    @Test
    public void provides_Name() {
        String actualName = mBeanStringAttributeValue(MBEAN_NAME, "Name");
        assertThat(actualName, is(AppInstanceInfo.APP_NAME));
    }

    @Test
    public void provides_Description() {
        String actualDescription = mBeanStringAttributeValue(MBEAN_NAME, "Description");
        assertThat(actualDescription, is(AppInstanceInfo.APP_DESCRIPTION));
    }

    @Test
    public void provides_Version() {
        String actualVersion = mBeanStringAttributeValue(MBEAN_NAME, "Version");
        assertThat(actualVersion, is(AppInstanceInfo.APP_VERSION));
    }

    @Test
    public void provides_Authors() {
        String[] actualAuthors = mBeanStringArrayAttributeValue(MBEAN_NAME, "Authors");
        assertThat(actualAuthors, is(AppInstanceInfo.APP_AUTHORS));
    }

    // ****************************** //

    @Autowired GeneralAppInstanceInfoMBeanImpl generalAppInstanceInfoMBean;
}