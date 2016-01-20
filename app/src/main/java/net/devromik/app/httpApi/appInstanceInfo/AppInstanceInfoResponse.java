package net.devromik.app.httpApi.appInstanceInfo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import net.devromik.app.AppInstanceInfo;
import net.devromik.app.httpApi.model.*;

/**
 * @author Shulnyaev Roman
 */
@JsonDeserialize(using = AppInstanceInfoResponseJsonDeserializer.class)
public final class AppInstanceInfoResponse extends Response {

    public AppInstanceInfoResponse(AppInstanceInfo appInstanceInfo) {
        super(Status.SUCCESS);
        this.appInstanceInfo = appInstanceInfo;
    }

    public AppInstanceInfo getAppInstanceInfo() {
        return appInstanceInfo;
    }

    // ****************************** //

    private final AppInstanceInfo appInstanceInfo;
}
