package net.devromik.app;

import java.io.IOException;
import javax.servlet.DispatcherType;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.servlet.*;
import org.slf4j.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.context.*;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.DispatcherServlet;
import static com.google.common.primitives.Ints.tryParse;
import static java.util.EnumSet.allOf;
import static net.devromik.slf4jUtils.Slf4jUtils.logException;
import static net.devromik.app.AppInstanceInfo.APP_NAME;
import static net.devromik.app.utils.net.LocalhostInfo.getLocalhost;
import static org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace;
import static org.eclipse.jetty.util.resource.Resource.newClassPathResource;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author Shulnyaev Roman
 */
public final class Launcher {

    public static final String SPRING_CONTEXT_CONFIG_LOCATION = Launcher.class.getPackage().getName();
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
            logger.info("{} started [port = {}]", APP_NAME, getJettyPort());

            if (needWaitForJettyStopped) {
                waitForJettyStopped();
            }
        }
        catch (Exception exception) {
            handleLaunchError(exception);
        }
    }

    public static synchronized String getJettyHostName() {
        return getLocalhost();
    }

    public static synchronized int getJettyPort() {
        return getJettyConnector().getLocalPort();
    }

    // ****************************** //

    private static void handleLaunchError(Exception launchException) {
        logException(logger, launchException, "Error occurred during {} starting", APP_NAME);
        stopJettySilently();
    }

    private static ServletContextHandler buildServletContextHandler() throws IOException {
        WebApplicationContext webAppContext = buildWebAppContext();
        ServletContextHandler servletContextHandler = new ServletContextHandler();
        servletContextHandler.setContextPath(WEB_APP_CONTEXT_PATH);
        servletContextHandler.setResourceBase(new ClassPathResource("webapp").getURI().toString());
        servletContextHandler.setBaseResource(newClassPathResource("webapp"));
        servletContextHandler.addServlet(new ServletHolder(new DispatcherServlet(webAppContext)), "/*");
        servletContextHandler.addEventListener(new ContextLoaderListener(webAppContext));
        servletContextHandler.setErrorHandler(null);
        addUtf8EncodingFilter(servletContextHandler);

        return servletContextHandler;
    }

    private static WebApplicationContext buildWebAppContext() {
        AnnotationConfigWebApplicationContext webAppContext = new AnnotationConfigWebApplicationContext();
        webAppContext.setConfigLocation(SPRING_CONTEXT_CONFIG_LOCATION);

        return webAppContext;
    }

    private static void addUtf8EncodingFilter(ServletContextHandler servletContextHandler) {
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding("UTF-8");
        servletContextHandler.addFilter(new FilterHolder(characterEncodingFilter), "/*", allOf(DispatcherType.class));
    }

    private static ServerConnector getJettyConnector() {
        return (ServerConnector)server.getConnectors()[0];
    }

    private static void waitForJettyStopped() {
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

    private static void stopJettySilently() {
        try {
            server.stop();
        }
        catch (Exception exception) {
            logger.debug(getStackTrace(exception));
        }
    }

    // ****************************** //

    private static Server server;
    private static final Logger logger = getLogger(Launcher.class);
}

