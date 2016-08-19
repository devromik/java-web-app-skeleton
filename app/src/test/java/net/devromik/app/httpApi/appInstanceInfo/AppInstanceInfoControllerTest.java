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

        AppInstanceInfoResponse response = controller.appInstanceInfo();
        assertThat(response.getStatus(), is(Status.SUCCESS));

        AppInstanceInfo expectedInfo = new AppInstanceInfo(now() /* is not taken into account */, "App X.X");

        assertThat(response.appInstanceInfo().name(), is(expectedInfo.name()));
        assertThat(response.appInstanceInfo().description(), is(expectedInfo.description()));
        assertThat(response.appInstanceInfo().version(), is(expectedInfo.version()));
        assertThat(response.appInstanceInfo().buildNumber(), is(expectedInfo.buildNumber()));
        assertThat(response.appInstanceInfo().authors(), is(expectedInfo.authors()));
    }
}