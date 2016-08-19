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
        AppInstanceInfo deserializedInfo = deserializedResponse.appInstanceInfo();

        assertThat(deserializedInfo.name(), is(info.name()));
        assertThat(deserializedInfo.description(), is(info.description()));
        assertThat(deserializedInfo.version(), is(info.version()));
        assertThat(deserializedInfo.buildNumber(), is(info.buildNumber()));
        assertThat(deserializedInfo.localStartUpTime(), is(info.localStartUpTime()));
        assertThat(deserializedInfo.authors(), is(info.authors()));
    }
}