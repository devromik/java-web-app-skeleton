package net.devromik.app;

import org.joda.time.DateTime;
import org.junit.Test;
import static net.devromik.app.AppInstanceInfo.*;
import static org.hamcrest.CoreMatchers.is;
import static org.joda.time.DateTime.now;
import static org.junit.Assert.*;

public class AppInstanceInfoTest {

    @Test
    public void provides_Name() {
        assertThat(new AppInstanceInfo().name(), is(APP_NAME));
    }

    @Test
    public void provides_Description() {
        assertThat(new AppInstanceInfo().description(), is(APP_DESCRIPTION));
    }

    @Test
    public void provides_Version() {
        assertThat(new AppInstanceInfo().version(), is(AppInstanceInfo.APP_VERSION));
    }

    @Test
    public void provides_BuildNumber() {
        assertThat(new AppInstanceInfo().buildNumber(), is(DEV_BUILD_NUMBER));
        assertThat(new AppInstanceInfo(now(), "42").buildNumber(), is("42"));
    }

    @Test
    public void provides_LocalStartUpTime() {
        DateTime localStartUpTime = new DateTime(2005, 5, 7, 8, 9, 10);
        assertThat(new AppInstanceInfo(localStartUpTime, DEV_BUILD_NUMBER).localStartUpTime(), is("2005-05-07 08:09:10"));
    }

    @Test
    public void provides_Authors() {
        assertThat(new AppInstanceInfo().authors(), is(APP_AUTHORS));
    }
}