package net.devromik.app;

import org.joda.time.*;
import org.joda.time.format.*;
import com.fasterxml.jackson.annotation.JsonGetter;
import static com.google.common.base.Preconditions.*;
import static org.joda.time.DateTime.now;

/**
 * @author Shulnyaev Roman
 */
public final class AppInstanceInfo {

    public static final String APP_NAME = "App";
    public static final String APP_DESCRIPTION = "App";
    public static final String APP_VERSION = "1.0";
    public static final String DEV_BUILD_NUMBER = "dev build";
    public static final String APP_LOCAL_START_UP_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static final String[] APP_AUTHORS = {
        "Roman Shulnyaev"
    };

    public static final String APP_HTTP_API_URI_PREFIX = "/api";

    // ****************************** //

    public AppInstanceInfo() {
        this(now(), DEV_BUILD_NUMBER);
    }

    public AppInstanceInfo(DateTime localStartUpTime, String buildNumber) {
        this.localStartUpTime = checkNotNull(localStartUpTime);
        this.buildNumber = checkNotNull(buildNumber);
    }

    // ****************************** //

    @JsonGetter("name")
    public String name() {
        return APP_NAME;
    }

    @JsonGetter("description")
    public String description() {
        return APP_DESCRIPTION;
    }

    @JsonGetter("version")
    public String version() {
        return APP_VERSION;
    }

    @JsonGetter("buildNumber")
    public String buildNumber() {
        return buildNumber;
    }

    @JsonGetter("localStartUpTime")
    public String localStartUpTime() {
        return localStartUpTime.toString(DateTimeFormat.forPattern(APP_LOCAL_START_UP_DATE_TIME_FORMAT));
    }

    @JsonGetter("authors")
    public String[] authors() {
        return APP_AUTHORS;
    }

    // ****************************** //

    final DateTime localStartUpTime;
    final String buildNumber;
}
