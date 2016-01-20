#!/bin/bash
CURRENT_DIR=$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)
HOME_DIR="$(dirname ${CURRENT_DIR})"
LIB_DIR="${HOME_DIR}"/lib
CONFIG_DIR="${HOME_DIR}"/config
ANSIBLE_DIR="${HOME_DIR}"/ansible
LOG_DIR="${HOME_DIR}"/log

CLASSPATH=".:${LIB_DIR}/*:${CONFIG_DIR}"
JVM_PROPERTIES="-DconfigDir=\"${CONFIG_DIR}\" -DansibleDir=\"${ANSIBLE_DIR}\" -DlogDir=\"${LOG_DIR}\" "
MAIN_CLASS="net.devromik.app.deploy.Driver"

eval java -classpath "${CLASSPATH}" ${JVM_PROPERTIES} "$@" ${MAIN_CLASS}