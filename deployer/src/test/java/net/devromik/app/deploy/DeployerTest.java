package net.devromik.app.deploy;

import org.junit.Test;
import net.devromik.app.deploy.config.*;
import static net.devromik.app.deploy.config.ConfigLoader.loadConfig;
import static net.devromik.app.deploy.utils.ResourceUtils.getResourceFile;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class DeployerTest {

    @Test
    public void buildsAnsibleConfig() throws Exception {
        Config config = loadConfig(getResourceFile("deploy.xml"));

        Deployer deployer = new Deployer(
            config,
            // fake.
            getResourceFile("deploy.xml"));

        StringBuilder hostsBuilder = new StringBuilder();
        StringBuilder groupVarsAllBuilder = new StringBuilder();
        deployer.buildAnsibleConfig(hostsBuilder, groupVarsAllBuilder);

        String expectedHosts =
            "[defaults]\n" +
            "host_key_checking=False\n" +
            "sudo_user=root\n" +
            "[app]\n" +
            "192.168.1.101 app_port=51235 app_jmx_port=51236\n" +
            "192.168.1.102 app_port=51237 app_jmx_port=51238\n" +
            "192.168.1.103 app_port=51239 app_jmx_port=51240\n";
        assertThat(hostsBuilder.toString(), is(expectedHosts));

        String expectedGroupVarsAll =
            "ansible_ssh_user: userName\n" +
            "ansible_ssh_pass: password\n" +
            "\n" +
            "app_distr: '/usr/distrs/app.zip'\n" +
            "app_install_dir: '/usr/share/app'\n";

        assertThat(groupVarsAllBuilder.toString(), is(expectedGroupVarsAll));
    }
}