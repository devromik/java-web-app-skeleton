package net.devromik.app.httpApi;

import javax.servlet.http.HttpServletRequest;
import org.slf4j.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.core.JsonParseException;
import static net.devromik.slf4jUtils.Slf4jUtils.logException;
import net.devromik.app.httpApi.model.*;
import net.devromik.app.AppException;
import static net.devromik.app.httpApi.model.StatusCode.REQUEST_BODY_NOT_VALID_JSON;
import static org.slf4j.LoggerFactory.*;

/**
 * @author Shulnyaev Roman
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Response handle(HttpServletRequest request, Exception exception) {
        logException(logger, exception, "HTTP request error [url = {}]", request.getRequestURL());

        return
            exception instanceof AppException ?
            handleApp((AppException)exception) :
            handleUnknown(exception);
    }

    // ****************************** //

    static Response handleApp(AppException exception) {
        return new Response(exception.status());
    }

    static Response handleUnknown(Exception exception) {
        if (exception instanceof HttpMessageNotReadableException &&
            exception.getCause() instanceof JsonParseException) {

            return new Response(new Status(
                REQUEST_BODY_NOT_VALID_JSON,
                ((JsonParseException)exception.getCause()).getOriginalMessage()));
        }
        else {
            return new Response(new Status(StatusCode.UNKNOWN_ERROR, exception.getMessage()));
        }
    }

    // ****************************** //

    static final Logger logger = getLogger(GlobalExceptionHandler.class);
}