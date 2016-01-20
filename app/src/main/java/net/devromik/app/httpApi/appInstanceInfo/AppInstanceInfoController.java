package net.devromik.app.httpApi.appInstanceInfo;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import net.devromik.app.AppInstanceInfo;
import static net.devromik.app.AppInstanceInfo.APP_HTTP_API_URI_PREFIX;
import static net.devromik.app.springAppConfig.AppConfig.BUILD_NUMBER_JVM_PROPERTY_NAME;
import static org.joda.time.DateTime.now;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@Controller
@Lazy(false)
@RequestMapping(value = APP_HTTP_API_URI_PREFIX + "/appInstanceInfo")
@SuppressWarnings("UnusedDeclaration")
public class AppInstanceInfoController {

    @PostConstruct
    public void init() {
        String buildNumber = environment.getProperty(BUILD_NUMBER_JVM_PROPERTY_NAME);
        response = new AppInstanceInfoResponse(new AppInstanceInfo(now(), buildNumber));
    }

    @RequestMapping(method = GET)
    @ResponseBody
    public AppInstanceInfoResponse getAppInstanceInfo() {
        return response;
    }

    // ****************************** //

    @Autowired
    Environment environment;

    private AppInstanceInfoResponse response;
}
