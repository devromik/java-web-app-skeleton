package net.devromik.app.httpApi.appInstanceInfo;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import net.devromik.app.AppInstanceInfo;
import net.devromik.app.httpApi.model.*;
import static net.devromik.app.httpApi.model.Status.SUCCESS;

/**
 * @author Shulnyaev Roman
 */
@JsonDeserialize(using = AppInstanceInfoResponseJsonDeserializer.class)
public final class AppInstanceInfoResponse extends Response {

    public AppInstanceInfoResponse(AppInstanceInfo appInstanceInfo) {
        super(SUCCESS);
        this.appInstanceInfo = appInstanceInfo;
    }

    @JsonGetter("appInstanceInfo")
    public AppInstanceInfo appInstanceInfo() {
        return appInstanceInfo;
    }

    // ****************************** //

    final AppInstanceInfo appInstanceInfo;
}