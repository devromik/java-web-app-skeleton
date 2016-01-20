package net.devromik.app.httpApi.appInstanceInfo;

import org.joda.time.DateTime;
import org.junit.Test;
import net.devromik.app.*;
import static net.devromik.app.utils.json.JsonUtils.*;
import static org.hamcrest.CoreMatchers.is;
import static org.joda.time.DateTime.now;
import static org.junit.Assert.*;

public class AppInstanceInfoResponseTest {

    @Test
    public void canBeDeserializedFromJson() throws Exception {
        DateTime localStartUpTime = now();
        AppInstanceInfo info = new AppInstanceInfo(localStartUpTime, "dev build");
        AppInstanceInfoResponse response = new AppInstanceInfoResponse(info);
        AppInstanceInfoResponse deserializedResponse = fromJson(toJson(response), AppInstanceInfoResponse.class);
        AppInstanceInfo deserializedInfo = deserializedResponse.getAppInstanceInfo();

        assertThat(deserializedInfo.getName(), is(info.getName()));
        assertThat(deserializedInfo.getDescription(), is(info.getDescription()));
        assertThat(deserializedInfo.getVersion(), is(info.getVersion()));
        assertThat(deserializedInfo.getBuildNumber(), is(info.getBuildNumber()));
        assertThat(deserializedInfo.getLocalStartUpTime(), is(info.getLocalStartUpTime()));
        assertThat(deserializedInfo.getAuthors(), is(info.getAuthors()));
    }
}