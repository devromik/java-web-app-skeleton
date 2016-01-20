package net.devromik.app.httpApi.appInstanceInfo;

import org.junit.*;
import com.fasterxml.jackson.databind.*;
import net.devromik.app.AppInstanceInfo;
import net.devromik.app.httpApi.HttpApi_FuncTest;
import net.devromik.app.httpApi.model.Status;
import static net.devromik.app.utils.json.JsonUtils.fromNode;
import static org.hamcrest.CoreMatchers.*;
import static org.joda.time.DateTime.now;
import static org.junit.Assert.*;

/**
 * @author Shulnyaev Roman
 */
public class AppInstanceInfo_FuncTest extends HttpApi_FuncTest {

    @Test
    public void youCanGetAppInstanceInfo() throws Exception {
        JsonNode responseJsonNode = getForJsonNode("appInstanceInfo");
        AppInstanceInfoResponse response = fromNode(responseJsonNode, AppInstanceInfoResponse.class);
        assertThat(response.getStatus(), is(Status.SUCCESS));

        AppInstanceInfo info = new AppInstanceInfo(now(), "func-test");

        assertThat(response.getAppInstanceInfo().getName(), is(info.getName()));
        assertThat(response.getAppInstanceInfo().getDescription(), is(info.getDescription()));
        assertThat(response.getAppInstanceInfo().getVersion(), is(info.getVersion()));
        assertThat(response.getAppInstanceInfo().getBuildNumber(), is(info.getBuildNumber()));
        assertThat(response.getAppInstanceInfo().getAuthors(), is(info.getAuthors()));
    }
}