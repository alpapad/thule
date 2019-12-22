#!/bin/bash

function updateConfiguration() {
  # Input parameters
  kubernetesConfigurationFile=$1

  echo ""
  echo "Downloading configuration..."

  serviceName=$(awk '/app: /{print $NF;exit;}' ${kubernetesConfigurationFile})
  tempDirectory=$(mktemp -d --suffix "${serviceName}")

  # Locate the service version
  serviceVersion=$(grep "${NEXUS_HOST}:${NEXUS_PORT_DOCKER}/${serviceName}:" "${kubernetesConfigurationFile}" | sed "s/.*${NEXUS_HOST}:${NEXUS_PORT_DOCKER}\/${serviceName}:\(\S*\).*/\1/")
  if [[ -z ${serviceVersion} ]]; then
    echo ""
    echo "ERROR: Could not determine the version of ${serviceName}"
    echo "Hint: Ensure a version has been explicitly defined in ${kubernetesConfigurationFile}"
    echo "================================================================================"
    exit 255
  fi

  # Derive the service download url
  if [[ ${serviceVersion} == *"SNAPSHOT" ]]; then
    # Download the artifact metadata from nexus for the service using the Nexus REST api
    metadataUrl="http://${NEXUS_HOST}:${NEXUS_PORT_MAVEN}/repository/maven-public/uk/co/serin/thule/${serviceName}/${serviceVersion}/maven-metadata.xml"
    echo "Downloading ${serviceName} metadata using ${metadataUrl} from Nexus..."

    mkdir -p "${tempDirectory}/metadata"
    pushd "${tempDirectory}/metadata"
    curlResponseCode=$(curl -L -o maven-metadata.xml -w "%{http_code}" "${metadataUrl}")
    popd
    if [[ ${curlResponseCode} -lt 200 || ${curlResponseCode} -gt 299 ]]; then
      echo ""
      echo "ERROR: Received a HTTP status code of ${curlResponseCode} while trying to download ${metadataUrl}"
      echo "================================================================================"
      exit 255
    fi

    # Extract the latest version of the required snapshot
    timestampedSnapshotVersion=$(grep -m 1 \<value\> "${tempDirectory}/metadata/maven-metadata.xml" | sed "s/.*<value>\(.*\)<\/value>.*/\1/")
    serviceDownloadUrl="http://${NEXUS_HOST}:${NEXUS_PORT_MAVEN}/repository/maven-public/uk/co/serin/thule/${serviceName}/${serviceVersion}/${serviceName}-${timestampedSnapshotVersion}.jar"
  else
    serviceDownloadUrl="http://${NEXUS_HOST}:${NEXUS_PORT_MAVEN}/repository/maven-public/uk/co/serin/thule/${serviceName}/${serviceVersion}/${serviceName}-${serviceVersion}.jar"
  fi

  # Download the service jar file using the Nexus REST api
  echo ""
  echo "Downloading ${serviceDownloadUrl} from Nexus..."

  mkdir -p "${tempDirectory}/jar"
  pushd "${tempDirectory}/jar"
  curlResponseCode=$(curl -L -O -w "%{http_code}" "${serviceDownloadUrl}")
  popd
  if [[ ${curlResponseCode} -lt 200 || ${curlResponseCode} -gt 299 ]]; then
    echo ""
    echo "ERROR: Received a HTTP status code of ${curlResponseCode} while trying to download ${serviceDownloadUrl}"
    echo "================================================================================"
    exit 255
  fi

  # Update the config files
  if [[ "${serviceName}" == "thule-configuration-service" ]]; then
    configDirectory=${CONFIGRATION_SERVICE_CONFIG_DIRECTORY}
    rm -f "${configDirectory}/*.yml"
  else
    configDirectory=${CONFIGRATION_SERVICE_CONFIG_DIRECTORY}/${serviceName}
    rm -fr "${configDirectory}"
  fi

  echo ""
  echo -n "Replacing old config with the new config ..."

  rm -fr "${configDirectory}"
  mkdir -p "${configDirectory}"
  unzip -jq -d "${configDirectory}" "${tempDirectory}/jar/*.jar" BOOT-INF/classes/config/application*.yml

  echo -e "\033[32m done \033[0m"
}
