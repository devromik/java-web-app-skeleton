package net.devromik.app.deploy.config

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

/**
 * @author Shulnyaev Roman
 */
@EqualsAndHashCode
@ToString
class AnsibleConfig {

    def addParam(String name, String value) {
        params[name] = value
    }

    def Collection<String> getParamNames() {
        params.keySet()
    }

    def getParamValue(String name) {
        params[name]
    }

    // ****************************** //

    def params = [:]
}
