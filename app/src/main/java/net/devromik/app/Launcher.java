package net.devromik.app;

import java.io.IOException;
import javax.servlet.DispatcherType;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.servlet.*;
import org.joda.time.DateTime;
import org.slf4j.*;
import org.springframework.web.context.*;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.DispatcherServlet;
import static com.google.common.base.Charsets.UTF_8;
import static com.google.common.primitives.Ints.tryParse;
import static java.util.EnumSet.allOf;
import static net.devromik.slf4jUtils.Slf4jUtils.logException;
import static net.devromik.app.AppInstanceInfo.APP_NAME;
import static net.devromik.app.utils.net.LocalhostInfo.getLocalhost;
import static org.eclipse.jetty.util.resource.Resource.newClassPathResource;
import static org.joda.time.DateTime.now;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author Shulnyaev Roman
 */
public final class Launcher {

    public static final String WEB_APP_CONTEXT_CONFIG_LOCATION = Launcher.class.getPackage().getName();
    public static final String WEB_APP_CONTEXT_PATH = "/";

    public static final String JETTY_PORT_JVM_PROPERTY_NAME = "port";
    public static final int DEFAULT_JETTY_PORT = 51235;
    public static final int ANY_AVAILABLE_JETTY_PORT = 0;

    // ****************************** //

    public static void main(String[] args) {
        String jettyPortJvmPropertyValue = System.getProperty(JETTY_PORT_JVM_PROPERTY_NAME);

        if (jettyPortJvmPropertyValue != null) {
            Integer port = tryParse(jettyPortJvmPropertyValue);

            if (port != null) {
                launch(port, true);
            }
            else {
                logger.error("Invalid port {}. {} stopped", jettyPortJvmPropertyValue, APP_NAME);
            }
        }
        else {
            launch(DEFAULT_JETTY_PORT, true);
        }
    }

    public static synchronized void launch(int port, boolean needWaitForJettyStopped) {
        if (server != null) {
            return;
        }

        logger.info("{} starting...", APP_NAME);
        server = new Server(port);

        try {
            server.setHandler(buildServletContextHandler());
            server.start();
            logger.info("{} started at port = {}", APP_NAME, jettyPort());

            if (needWaitForJettyStopped) {
                waitForJettyStopped();
            }
        }
        catch (Exception exception) {
            handleLaunch(exception);
        }
    }

    public static DateTime localStartUpTime() {
        return localStartUpTime;
    }

    static void handleLaunch(Exception exception) {
        logException(logger, exception, "Error occurred during {} starting", APP_NAME);
        stopJettySilently();
    }

    // ****************************** //

    public static String jettyHostName() {
        return getLocalhost();
    }

    public static synchronized int jettyPort() {
        return jettyConnector().getLocalPort();
    }

    static ServerConnector jettyConnector() {
        return (ServerConnector)server.getConnectors()[0];
    }

    // ****************************** //

    static ServletContextHandler buildServletContextHandler() throws IOException {
        ServletContextHandler handler = new ServletContextHandler();

        handler.setContextPath(WEB_APP_CONTEXT_PATH);
        handler.setBaseResource(newClassPathResource("webapp"));

        WebApplicationContext webAppContext = buildWebAppContext();
        handler.addServlet(new ServletHolder(new DispatcherServlet(webAppContext)), "/*");
        handler.addEventListener(new ContextLoaderListener(webAppContext));

        handler.setErrorHandler(null);
        addUtf8EncodingFilterTo(handler);

        return handler;
    }

    static WebApplicationContext buildWebAppContext() {
        AnnotationConfigWebApplicationContext webAppContext = new AnnotationConfigWebApplicationContext();
        webAppContext.setConfigLocation(WEB_APP_CONTEXT_CONFIG_LOCATION);

        return webAppContext;
    }

    static void addUtf8EncodingFilterTo(ServletContextHandler handler) {
        CharacterEncodingFilter filter = new CharacterEncodingFilter();
        filter.setEncoding(UTF_8.name());
        handler.addFilter(new FilterHolder(filter), "/*", allOf(DispatcherType.class));
    }

    // ****************************** //

    static void stopJettySilently() {
        try {
            server.stop();
        }
        catch (Exception exception) {
            logException(logger, exception);
        }
    }

    static void waitForJettyStopped() {
        try {
            server.join();
            server = null;
        }
        catch (InterruptedException exception) {
            // Application has been stopped normally (e.g. via Ctrl+C).
        }
        catch (Exception exception) {
            logException(logger, exception);
        }
    }

    // ****************************** //

    static Server server;
    static final DateTime localStartUpTime = now();

    static final Logger logger = getLogger(Launcher.class);
}