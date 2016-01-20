package net.devromik.app;

import org.joda.time.*;
import org.joda.time.format.*;
import static com.google.common.base.Preconditions.*;
import static org.joda.time.DateTime.now;

/**
 * @author Shulnyaev Roman
 */
public final class AppInstanceInfo {

    public static final String APP_NAME = "App";
    public static final String APP_DESCRIPTION = "App";
    public static final String APP_VERSION = "1.0";
    public static final String DEV_BUILD = "dev build";

    public static final String[] APP_AUTHORS = {
        "Roman Shulnyaev"
    };

    public static final String APP_LOCAL_START_UP_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String APP_HTTP_API_URI_PREFIX = "/api";

    // ****************************** //

    public AppInstanceInfo() {
        this(now(), DEV_BUILD);
    }

    public AppInstanceInfo(DateTime localStartUpTime, String buildNumber) {
        this.localStartUpTime = checkNotNull(localStartUpTime);
        this.buildNumber = checkNotNull(buildNumber);
    }

    // ****************************** //

    public String getName() {
        return APP_NAME;
    }

    public String getDescription() {
        return APP_DESCRIPTION;
    }

    public String getVersion() {
        return APP_VERSION;
    }

    public String getBuildNumber() {
        return buildNumber;
    }

    public String getLocalStartUpTime() {
        return localStartUpTime.toString(DateTimeFormat.forPattern(APP_LOCAL_START_UP_DATE_TIME_FORMAT));
    }

    public String[] getAuthors() {
        return APP_AUTHORS;
    }

    // ****************************** //

    private final DateTime localStartUpTime;
    private final String buildNumber;
}
