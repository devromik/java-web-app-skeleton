package net.devromik.app.deploy.config

import org.junit.Test
import static ConfigLoader.loadConfig
import static net.devromik.app.deploy.utils.ResourceUtils.getResourceFile

/**
 * @author Shulnyaev Roman
 */
class ConfigLoaderTest extends GroovyTestCase {

    @Test
    void test_CanLoadConfig() {
        def configFile = getResourceFile("deploy.xml");

        def ansibleConfig = new AnsibleConfig()
        ansibleConfig.addParam("ansible_ssh_user", "userName")
        ansibleConfig.addParam("ansible_ssh_pass", "password")

        def distrConfig = new DistrConfig(
            from: "/usr/distrs/app.zip",
            to: "/usr/share/app")

        def expectedConfig = new Config(
            ansibleConfig: ansibleConfig,
            distrConfig: distrConfig)

        expectedConfig.instanceConfigs.add(new InstanceConfig(hostName: "192.168.1.101", port: 51235))
        expectedConfig.instanceConfigs.add(new InstanceConfig(hostName: "192.168.1.102", port: 51237))
        expectedConfig.instanceConfigs.add(new InstanceConfig(hostName: "192.168.1.103", port: 51239))

        assert loadConfig(configFile).equals(expectedConfig)
    }
}
