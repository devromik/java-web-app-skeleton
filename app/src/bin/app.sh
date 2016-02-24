#!/bin/bash

APP_NAME="App"
APP_VERSION="1.0"

##### Paths #####

CURRENT_DIR=$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)

HOME_DIR="$(dirname ${CURRENT_DIR})"
BIN_DIR="${HOME_DIR}"/bin
LIB_DIR="${HOME_DIR}"/lib
CONFIG_DIR="${HOME_DIR}"/config
LOG_DIR="${HOME_DIR}"/log

STARTUP_LOG_FILE="${LOG_DIR}"/startUp.log

PID_FILE="${HOME_DIR}"/app.pid

##### JVM properties #####

JVM_PROPERTIES="-DhomeDir=\"${HOME_DIR}\""
JVM_PROPERTIES="${JVM_PROPERTIES} -DbinDir=\"${BIN_DIR}\""
JVM_PROPERTIES="${JVM_PROPERTIES} -DlibDir=\"${LIB_DIR}\""
JVM_PROPERTIES="${JVM_PROPERTIES} -DconfigDir=\"${CONFIG_DIR}\""
JVM_PROPERTIES="${JVM_PROPERTIES} -DlogDir=\"${LOG_DIR}\""

JVM_PROPERTIES="${JVM_PROPERTIES} -Dcom.sun.management.jmxremote"
JVM_PROPERTIES="${JVM_PROPERTIES} -Dcom.sun.management.jmxremote.local.only=false"
JVM_PROPERTIES="${JVM_PROPERTIES} -Dcom.sun.management.jmxremote.authenticate=false"
JVM_PROPERTIES="${JVM_PROPERTIES} -Dcom.sun.management.jmxremote.ssl=false"

##############################

CLASSPATH=".:${LIB_DIR}/*:${CONFIG_DIR}"
MAIN_CLASS="net.devromik.app.Launcher"

##############################

printHelp() {
    echo "Usage: app.sh (command) [JVM properties]"
    echo "Possible commands:"
    echo "  run               start application in the current window"
    echo "  start             start application in a separate window"
    echo "  stop              stop application, waiting up to 5 seconds for the process to end"
    echo "  version           what version of application are you running?"
    echo "  help              print this help"
}

case "$1" in
'run')
    shift
    eval exec nohup java -classpath "${CLASSPATH}" ${JVM_PROPERTIES} "$@" ${MAIN_CLASS} > "${STARTUP_LOG_FILE}" 2>"${STARTUP_LOG_FILE}"
;;
'start')
    shift
    
    if [ ! -z "${PID_FILE}" ]; then
        if [ -f "${PID_FILE}" ]; then
            if [ -s "${PID_FILE}" ]; then
                echo "Existing PID file found during start"
                
                if [ -r "${PID_FILE}" ]; then
                    PID=`cat "${PID_FILE}"`
                    ps -p ${PID} > /dev/null 2>&1
                    
                    if [ $? -eq 0 ]; then
                        echo "${APP_NAME} appears to still be running with PID ${PID}. Start aborted"
                        exit 1
                    else
                        echo "Removing/clearing stale PID file"
                        rm -f "${PID_FILE}" > /dev/null 2>&1
                        
                        if [ $? != 0 ]; then
                            if [ -w "${PID_FILE}" ]; then
                                cat /dev/null > "${PID_FILE}"
                            else
                                echo "Unable to remove or clear stale PID file. Start aborted"
                                exit 1
                            fi
                        fi
                    fi
                else
                    echo "Unable to read PID file. Start aborted"
                    exit 1
                fi
            else
                rm -f "${PID_FILE}" > /dev/null 2>&1
                
                if [ $? != 0 ]; then
                    if [ ! -w "${PID_FILE}" ]; then
                        echo "Unable to remove or write to empty PID file. Start aborted"
                        exit 1
                    fi
                fi
            fi
        fi
    fi
        
    eval exec nohup java -classpath "${CLASSPATH}" ${JVM_PROPERTIES} "$@" ${MAIN_CLASS} > "${STARTUP_LOG_FILE}" 2>"${STARTUP_LOG_FILE}" "&"
    
    if [ ! -z "${PID_FILE}" ]; then
        echo $! > "${PID_FILE}"
    fi
       
    echo "${APP_NAME} started"
;;
'stop') 
    shift
    SLEEP=5
        
    if [ ! -z "${PID_FILE}" ]; then
        if [ -f "${PID_FILE}" ]; then
            if [ -s "${PID_FILE}" ]; then
                kill -0 `cat "${PID_FILE}"` > /dev/null 2>&1
                
                if [ $? -gt 0 ]; then
                    echo "PID file found but no matching process was found. Stop aborted"
                    exit 1
                fi
            else
                echo "PID file is empty and has been ignored"
            fi
        else
            echo "\${APP_PID_FILE}=${PID_FILE} was set but the specified file does not exist. Is ${APP_NAME} running? Stop aborted"
            exit 1
        fi
    fi
    
    if [ ! -z "${PID_FILE}" ]; then
        kill -15 `cat "${PID_FILE}"` > /dev/null 2>&1
    fi

    if [ ! -z "${PID_FILE}" ]; then
        if [ -f "${PID_FILE}" ]; then
            while [ ${SLEEP} -ge 0 ]; do
                kill -0 `cat "${PID_FILE}"` > /dev/null 2>&1
            
                if [ $? -gt 0 ]; then
                    rm -f "${PID_FILE}" > /dev/null 2>&1

                    if [ $? != 0 ]; then
                        if [ -w "${PID_FILE}" ]; then
                            cat /dev/null > "${PID_FILE}"
                        else
                            echo "The PID file could not be removed or cleared"
                        fi
                    fi

                    echo "${APP_NAME} stopped"
                    break
                fi

                if [ ${SLEEP} -gt 0 ]; then
                    sleep 1
                fi

                if [ ${SLEEP} -eq 0 ]; then
                    echo "${APP_NAME} did not stop in time. PID file was not removed. To aid diagnostics a thread dump has been written to standard out"
                    kill -3 `cat "${PID_FILE}"`
                fi

                SLEEP=`expr ${SLEEP} - 1`
            done
        fi
    fi    
;;
'version') 
    echo ${APP_VERSION}
;;
'help')
    printHelp;
;;
*)
    printHelp;
    exit 1;
;;
esac;