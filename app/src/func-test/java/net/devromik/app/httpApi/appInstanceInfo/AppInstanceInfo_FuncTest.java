package net.devromik.app.httpApi.appInstanceInfo;

import org.junit.*;
import com.fasterxml.jackson.databind.*;
import net.devromik.app.*;
import static net.devromik.app.Launcher.localStartUpTime;
import net.devromik.app.httpApi.HttpApi_FuncTest;
import static net.devromik.app.httpApi.model.Status.SUCCESS;
import static net.devromik.app.utils.json.JsonUtils.fromNode;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 * @author Shulnyaev Roman
 */
public class AppInstanceInfo_FuncTest extends HttpApi_FuncTest {

    @Test
    public void youCanGetAppInstanceInfo() throws Exception {
        JsonNode responseJsonNode = getForJsonNode("appInstanceInfo");
        AppInstanceInfoResponse response = fromNode(responseJsonNode, AppInstanceInfoResponse.class);
        assertThat(response.status(), is(SUCCESS));

        AppInstanceInfo info = response.appInstanceInfo();
        AppInstanceInfo expectedInfo = new AppInstanceInfo(localStartUpTime(), "func-test");

        assertThat(info.name(), is(expectedInfo.name()));
        assertThat(info.description(), is(expectedInfo.description()));
        assertThat(info.version(), is(expectedInfo.version()));
        assertThat(info.buildNumber(), is(expectedInfo.buildNumber()));
        assertThat(info.localStartUpTime(), is(expectedInfo.localStartUpTime()));
        assertThat(info.authors(), is(expectedInfo.authors()));
    }
}