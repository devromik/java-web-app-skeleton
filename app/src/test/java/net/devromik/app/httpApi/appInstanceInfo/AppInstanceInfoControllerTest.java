package net.devromik.app.httpApi.appInstanceInfo;

import org.junit.*;
import org.springframework.core.env.Environment;
import net.devromik.app.httpApi.model.Status;
import net.devromik.app.*;
import net.devromik.app.springAppConfig.AppConfig;
import static org.hamcrest.CoreMatchers.is;
import static org.joda.time.DateTime.now;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class AppInstanceInfoControllerTest {

    @Test
    public void provides_AppInstanceInfo() throws Exception {
        Environment environment = mock(Environment.class);
        when(environment.getProperty(AppConfig.BUILD_NUMBER_JVM_PROPERTY_NAME)).thenReturn("App X.X");

        AppInstanceInfoController controller = new AppInstanceInfoController();
        controller.environment = environment;
        controller.init();

        AppInstanceInfoResponse response = controller.getAppInstanceInfo();
        assertThat(response.getStatus(), is(Status.SUCCESS));

        AppInstanceInfo expectedInfo = new AppInstanceInfo(now() /* is not taken into account */, "App X.X");

        assertThat(response.getAppInstanceInfo().getName(), is(expectedInfo.getName()));
        assertThat(response.getAppInstanceInfo().getDescription(), is(expectedInfo.getDescription()));
        assertThat(response.getAppInstanceInfo().getVersion(), is(expectedInfo.getVersion()));
        assertThat(response.getAppInstanceInfo().getBuildNumber(), is(expectedInfo.getBuildNumber()));
        assertThat(response.getAppInstanceInfo().getAuthors(), is(expectedInfo.getAuthors()));
    }
}