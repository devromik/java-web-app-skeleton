package net.devromik.app.deploy.config

import static org.apache.commons.io.FileUtils.readFileToString

/**
 * @author Shulnyaev Roman
 */
public final class ConfigLoader {

    public static Config loadConfig(File configFile) throws Exception {
        def root = new XmlParser().parseText(readFileToString(configFile))

        /* ***** Ansible. ***** */

        shouldHaveSingle(root.ansible)
        def ansibleConfig = new AnsibleConfig()

        root.ansible."*".each {
            ansibleConfig.addParam(it.name(), it.text())
        }

        /* ***** App distr. ***** */

        shouldHaveSingle(root.distrs."app")
        shouldHaveSingle(root.distrs."app".from)
        shouldHaveSingle(root.distrs."app".to)

        def distrConfig = new DistrConfig(
            from: root.distrs."app".from.text(),
            to: root.distrs."app".to.text())

        /* ***** App instances. ***** */

        def config = new Config(
            ansibleConfig: ansibleConfig,
            distrConfig: distrConfig)

        root.hosts.group.each {
            if (it["@name"] == "app") {
                it.host.each {
                    config.instanceConfigs.add(
                        new InstanceConfig(
                                hostName: it["@name"],
                                port: it["@port"]?.toInteger() ?: 51235))
                }
            }
        }

        config
    }

    // ****************************** //

    private static def shouldHaveSingle(def node) {
        if (node.size() != 1) {
            throw new InvalidConfigException("Exactly one element is required but was '$node'");
        }
    }
}
