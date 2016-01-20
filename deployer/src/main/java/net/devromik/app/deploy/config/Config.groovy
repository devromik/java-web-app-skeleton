package net.devromik.app.deploy.config

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

/**
 * @author Shulnyaev Roman
 */
@EqualsAndHashCode
@ToString
class Config {
    AnsibleConfig ansibleConfig
    DistrConfig distrConfig
    List<InstanceConfig> instanceConfigs = []
}
