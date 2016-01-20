package net.devromik.app.deploy;

import java.io.File;
import java.nio.file.*;
import org.slf4j.*;
import static java.lang.System.exit;
import static java.text.MessageFormat.format;
import net.devromik.app.deploy.config.*;
import static net.devromik.app.deploy.config.ConfigLoader.loadConfig;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author Shulnyaev Roman
 */
public final class Driver {

    public static final String CONFIG_DIR_JVM_PROPERTY_NAME = "configDir";
    public static final String CONFIG_FILE_NAME = "deploy.xml";
    public static final String ANSIBLE_DIR_JVM_PROPERTY_NAME = "ansibleDir";

    // ****************************** //

    public static void main(String[] args) throws Exception {
        try {
            File configFile = getConfigFile();
            Config config = loadConfig(configFile);
            Deployer deployer = new Deployer(config, getAnsibleDir());
            deployer.deploy();

            logger.info("App has been successfully deployed");
        }
        catch (Exception exception) {
            logger.error("Error occurred during App deployment: {}", exception.getMessage());
            exit(-1);
        }
    }

    // ****************************** //

    private static File getConfigFile() {
        Path configFilePath = Paths.get(System.getProperty(CONFIG_DIR_JVM_PROPERTY_NAME), CONFIG_FILE_NAME);
        logger.info("Config file: {}", configFilePath);
        File configFile = configFilePath.toFile();

        if (!configFile.exists()) {
            throw new RuntimeException(format("Cannot find config file {0}", CONFIG_FILE_NAME));
        }

        return configFile;
    }

    private static File getAnsibleDir() {
        Path ansibleDirPath = Paths.get(System.getProperty(ANSIBLE_DIR_JVM_PROPERTY_NAME));
        logger.info("Ansible directory: {}", ansibleDirPath);
        File ansibleDir = ansibleDirPath.toFile();

        if (!ansibleDir.exists() || !ansibleDir.isDirectory()) {
            throw new RuntimeException("Cannot find ansible directory");
        }

        return ansibleDir;
    }

    // ****************************** //

    private static final Logger logger = getLogger(Driver.class);
}
