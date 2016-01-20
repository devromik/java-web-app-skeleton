package net.devromik.app.deploy;

import java.io.*;
import org.slf4j.Logger;
import static java.lang.Runtime.getRuntime;
import static java.text.MessageFormat.format;
import net.devromik.app.deploy.config.*;
import static org.apache.commons.io.Charsets.UTF_8;
import static org.apache.commons.io.IOUtils.write;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author Shulnyaev Roman
 */
public final class Deployer {

    public Deployer(Config config, File ansibleDir) throws Exception {
        this.config = config;
        this.ansibleDir = ansibleDir;
    }

    /**
     * ------------------------------
     * 1. hosts:
     * ------------------------------
     * [defaults]
     * host_key_checking=False
     * sudo_user=root
     * [app]
     * 192.168.1.101 app_port=1
     * 192.168.1.102 app_port=2
     * 192.168.1.103 app_port=3
     * ------------------------------
     * 2. group_vars/all.yml:
     * ------------------------------
     * ansible_ssh_user: user_name
     * ansible_ssh_pass: password
     *
     * app_distr: "/usr/distrs/app.zip"
     * app_install_dir: "/usr/share/app"
     * ------------------------------
     * 3. Deploy App.
     */
    public void deploy() throws Exception {
        // 1. hosts.
        // 2. group_vars/all.yml.
        StringBuilder hostsBuilder = new StringBuilder();
        StringBuilder groupVarsAllBuilder = new StringBuilder();
        buildAnsibleConfig(hostsBuilder, groupVarsAllBuilder);

        writeToFile(new File(ansibleDir, "hosts"), hostsBuilder.toString());
        writeToFile(new File(ansibleDir, "group_vars/all.yml"), groupVarsAllBuilder.toString());

        // 3. Deploy App.
        invokeAnsible("update");
    }

    // ****************************** //

    void buildAnsibleConfig(StringBuilder hostsBuilder, StringBuilder groupVarsAllBuilder) throws Exception {
        hostsBuilder.append("[defaults]");
        hostsBuilder.append("\n");
        hostsBuilder.append("host_key_checking=False");
        hostsBuilder.append("\n");
        hostsBuilder.append("sudo_user=root");
        hostsBuilder.append("\n");

        addAnsibleParams(groupVarsAllBuilder);
        addAppParams(hostsBuilder, groupVarsAllBuilder);
    }

    private void addAnsibleParams(StringBuilder groupVarsAllBuilder) throws IOException {
        for (String paramName : config.getAnsibleConfig().getParamNames()) {
            groupVarsAllBuilder.append(paramName);
            groupVarsAllBuilder.append(": ");
            groupVarsAllBuilder.append(config.getAnsibleConfig().getParamValue(paramName));
            groupVarsAllBuilder.append("\n");
        }

        groupVarsAllBuilder.append("\n");
    }

    private void addAppParams(StringBuilder hostsBuilder, StringBuilder groupVarsAllBuilder) {
        // hosts.
        hostsBuilder.append("[app]");
        hostsBuilder.append("\n");

        for (InstanceConfig instanceConfig : config.getInstanceConfigs()) {
            hostsBuilder.append(instanceConfig.getHostName());

            hostsBuilder.append(" app_port=");
            hostsBuilder.append(instanceConfig.getPort());

            hostsBuilder.append(" app_jmx_port=");
            hostsBuilder.append(instanceConfig.getPort() + 1);

            hostsBuilder.append("\n");
        }

        // groupVars/all.yml.
        groupVarsAllBuilder.append("app_distr: ");
        groupVarsAllBuilder.append("'");
        groupVarsAllBuilder.append(config.getDistrConfig().getFrom());
        groupVarsAllBuilder.append("'\n");

        groupVarsAllBuilder.append("app_install_dir: ");
        groupVarsAllBuilder.append("'");
        groupVarsAllBuilder.append(config.getDistrConfig().getTo());
        groupVarsAllBuilder.append("'\n");
    }

    // ****************************** //

    private void writeToFile(File file, String data) throws IOException {
        BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file));
        write(data, outputStream, UTF_8);
        outputStream.close();
    }

    private void invokeAnsible(String playbook) throws Exception {
        String command = format("ansible-playbook -i hosts ./{0}.yml", playbook);
        logger.info("Executing {}", command);
        Process process = getRuntime().exec(command, new String[] {}, ansibleDir);

        BufferedReader processOutputReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        StringBuilder processOutputBuilder = new StringBuilder();
        String processOutputLine;

        while ((processOutputLine = processOutputReader.readLine()) != null) {
            processOutputBuilder.append(processOutputLine);
            processOutputBuilder.append("\n");
        }

        if (process.waitFor() == 0) {
            logger.info(processOutputBuilder.toString());
        }
        else {
            logger.error(processOutputBuilder.toString());
            throw new RuntimeException(format("Cannot execute command {0}", command));
        }
    }

    // ****************************** //

    private final Config config;
    private final File ansibleDir;

    private static final Logger logger = getLogger(Deployer.class);
}
