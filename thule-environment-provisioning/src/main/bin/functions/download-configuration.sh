#!/bin/bash

function downloadConfiguration {
    # Input parameters
    dockerComposeFile=$1
    serviceName=$2

    if [[ -z ${serviceName} ]]; then
        _downloadConfigurationForAllServices ${dockerComposeFile}
    else
        _downloadConfigurationForSingleService ${dockerComposeFile} ${serviceName}
    fi

    echo ""
}

function _downloadConfigurationForAllServices {
    # Input parameters
    dockerComposeFile=$1

    serviceNames=($(cat ${dockerComposeFile} | grep "^\s*thule.*.service:$" | sed "s/://g"))
    for serviceName in "${serviceNames[@]}"
    do
        _downloadConfigurationForSingleService ${dockerComposeFile} ${serviceName}
    done

}

function _downloadConfigurationForSingleService {
    # Input parameters
    dockerComposeFile=$1
    serviceName=$2

    # Derive the config name
    configName=$(echo ${serviceName} | sed 's/\(.*\)service/\1config/')

    echo ""
    echo "================================================================================"
    echo "About to download ${configName} from Nexus..."

    tempDirectory=$(mktemp -d --suffix ${configName})

    # Locate the service version
    serviceVersion=$(grep ${NEXUS_HOST}:${NEXUS_PORT_DOCKER}/${serviceName}: ${dockerComposeFile} | sed "s/.*${NEXUS_HOST}:${NEXUS_PORT_DOCKER}\/${serviceName}:\(\S*\).*/\1/")
    if [[ -z ${serviceVersion} ]]; then
        echo ""
        echo "Error: Could not determine the version of ${serviceName}"
        echo "Hint: Ensure a version has been explicitly defined in ${dockerComposeFile}"
        echo "================================================================================"
        exit -1
    fi

    # Derive the config download url
    if [[ ${serviceVersion} = *"SNAPSHOT" ]]; then
        # Download the artifact metadata from nexus for the config using the Nexus REST api
        metadataUrl="http://${NEXUS_HOST}:${NEXUS_PORT_MAVEN}/repository/maven-public/uk/co/serin/thule/${configName}/${serviceVersion}/maven-metadata.xml"
        echo ""
        echo "Downloading ${configName} metadata using ${metadataUrl} from Nexus..."

        mkdir -p ${tempDirectory}/metadata
        pushd ${tempDirectory}/metadata
        curlResponseCode=$(curl -L -o maven-metadata.xml -w "%{http_code}" "${metadataUrl}")
        popd
        if [[ ${curlResponseCode} < 200 || ${curlResponseCode} > 299 ]]; then
            echo ""
            echo "Error: Received a HTTP status code of ${curlResponseCode} while trying to download ${metadataUrl}"
            echo "================================================================================"
            exit -1
        fi

        # Extract the latest version of the required snapshot
        timestampedSnapshotVersion=$(grep -m 1 \<value\> ${tempDirectory}/metadata/maven-metadata.xml | sed "s/.*<value>\(.*\)<\/value>.*/\1/")
        configDownloadUrl="http://${NEXUS_HOST}:${NEXUS_PORT_MAVEN}/repository/maven-public/uk/co/serin/thule/${configName}/${serviceVersion}/${configName}-${timestampedSnapshotVersion}.jar"
    else
        configDownloadUrl="http://${NEXUS_HOST}:${NEXUS_PORT_MAVEN}/repository/maven-public/uk/co/serin/thule/${configName}/${serviceVersion}/${configName}-${serviceVersion}.jar"
    fi

    # Download the config jar file using the Nexus REST api
    echo ""
    echo "Downloading ${configDownloadUrl} from Nexus..."

    mkdir -p ${tempDirectory}/jar
    pushd ${tempDirectory}/jar
    curlResponseCode=$(curl -L -O -w "%{http_code}" "${configDownloadUrl}")
    popd
    if [[ ${curlResponseCode} < 200 || ${curlResponseCode} > 299 ]]; then
        echo ""
        echo "Error: Received a HTTP status code of ${curlResponseCode} while trying to download ${configDownloadUrl}"
        echo "================================================================================"
        exit -1
    fi

    configJarFile=$(echo "" ${configDownloadUrl} | sed 's/.*\(${serviceName}-.*.jar\).*/\1/')
    echo ""
    echo "Unzipping ${configJarFile} to ${configDirectoryExpectedByDockerCompose}/temp/${serviceName}..."

    # Unzip the config files
    configDirectoryExpectedByDockerCompose=$(configDirectoryExpectedByDockerCompose ${dockerComposeFile})
    rm -fr ${configDirectoryExpectedByDockerCompose}/temp/${serviceName}
    mkdir -p ${configDirectoryExpectedByDockerCompose}/temp/${serviceName}
    unzip -jq -d ${configDirectoryExpectedByDockerCompose}/temp/${serviceName} ${tempDirectory}/jar/*.jar config/application*.yml

    echo ""
    echo "Have downloaded ${configName} from Nexus"
    echo "================================================================================"
}

function configDirectoryExpectedByDockerCompose {
    # Input parameters
    dockerComposeFile=$1

    # Determine where docker expects the config files to reside on the filesystem
    configDirectory=$(grep :/config ${dockerComposeFile} | sed 's/.* - \(\S*\):\/config.*/\1/')
    # Substitute any environments variables or special characters , e.g. $HOME, ~
    configDirectory=$(eval echo -E "${configDirectory}")
    # Ensure that the config directory can be created
    mkdir -p ${configDirectory}
    if [[ $? != 0 ]]; then
        echo ""
        echo "Error: Could not create config directory ${configDirectory} as specified in ${dockerComposeFile}"
        echo "Hint: Ensure user ${PROVISIONING_HOST_USERID} has sufficient permissions to create ${configDirectory}"
        echo "================================================================================"
        exit -1
    fi

    echo ${configDirectory}
}