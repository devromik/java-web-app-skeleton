package net.devromik.app.httpApi;

import javax.servlet.http.HttpServletRequest;
import org.junit.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import com.fasterxml.jackson.core.*;
import net.devromik.app.httpApi.model.*;
import net.devromik.app.AppException;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class GlobalExceptionHandlerTest {

    @Before
    public void beforeTest() {
        request = mock(HttpServletRequest.class);
        when(request.getRequestURL()).thenReturn(new StringBuffer("url"));

        JsonParseException jsonParseException = mock(JsonParseException.class);
        when(jsonParseException.getOriginalMessage()).thenReturn("oh...");

        httpMessageNotReadableException = mock(HttpMessageNotReadableException.class);
        when(httpMessageNotReadableException.getCause()).thenReturn(jsonParseException);
    }

    // ****************************** //

    @Test
    public void handles_AppExceptions() throws Exception {
        Status status = new Status(StatusCode.SUCCESS, "ok");
        Exception exception = new AppException(status);
        Response response = handler.handle(request, exception);
        assertThat(response.getStatus(), is(status));
    }

    @Test
    public void handles_InvalidRequestBodyJsonExceptions() throws Exception {
        Status status = new Status(StatusCode.REQUEST_BODY_NOT_VALID_JSON, "oh...");
        Response response = handler.handle(request, httpMessageNotReadableException);
        assertThat(response.getStatus(), is(status));
    }

    @Test
    public void handles_UnknownExceptions() throws Exception {
        Response response = handler.handle(request, new Exception());
        assertThat(response.getStatus(), is(new Status(StatusCode.UNKNOWN_ERROR)));
    }

    // ****************************** //

    private GlobalExceptionHandler handler = new GlobalExceptionHandler();
    private HttpServletRequest request;
    private HttpMessageNotReadableException httpMessageNotReadableException;
}