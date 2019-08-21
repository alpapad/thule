#!/bin/bash

function updateConfiguration {
    # Input parameters
    dockerComposeFile=$1
    serviceName=$2

    if [[ -z ${serviceName} ]]; then
        _updateConfigurationForAllServices ${dockerComposeFile}
    else
        _updateConfigurationForSingleService ${dockerComposeFile} ${serviceName}
    fi
}

function _updateConfigurationForAllServices {
    # Input parameters
    dockerComposeFile=$1

    serviceNames=($(cat ${dockerComposeFile} | grep "^\s*thule.*.service:$" | sed "s/://g"))
    for serviceName in "${serviceNames[@]}"
    do
        _updateConfigurationForSingleService ${dockerComposeFile} ${serviceName}
    done
}

function _updateConfigurationForSingleService {
    # Input parameters
    dockerComposeFile=$1
    serviceName=$2

    configDirectoryExpectedByDockerCompose=$(configDirectoryExpectedByDockerCompose ${dockerComposeFile})
    tempConfigDirectory=${configDirectoryExpectedByDockerCompose}/temp/${serviceName}

    if [[ "${serviceName}" == "thule-configuration-service" ]]; then
        configDirectory=${configDirectoryExpectedByDockerCompose}
        rm -f ${configDirectory}/*.yml
    else
        configDirectory=${configDirectoryExpectedByDockerCompose}/${serviceName}
        rm -fr ${configDirectory}
    fi

    printf "Replacing old config ${configDirectory} with the new config ${tempConfigDirectory} ..."

    mkdir -p ${configDirectory}
    mv ${tempConfigDirectory}/* ${configDirectory}
    rm -fr ${tempConfigDirectory}

    printf "\033[32m done \033[0m \n"
}
