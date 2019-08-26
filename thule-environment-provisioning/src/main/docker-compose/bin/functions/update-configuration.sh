#!/bin/bash

function updateConfiguration() {
  # Input parameters
  dockerComposeFile=$1
  serviceName=$2

  configDirectoryExpectedByConfigurationService=$(configDirectoryExpectedByConfigurationService "${dockerComposeFile}")
  tempConfigDirectory=${configDirectoryExpectedByConfigurationService}/temp/${serviceName}

  if [[ "${serviceName}" == "thule-configuration-service" ]]; then
    configDirectory=${configDirectoryExpectedByConfigurationService}
    rm -f "${configDirectory}/*.yml"
  else
    configDirectory=${configDirectoryExpectedByConfigurationService}/${serviceName}
    rm -fr "${configDirectory}"
  fi

  echo ""
  printf "Replacing old config %s with the new config %s ..." "${configDirectory}" "${tempConfigDirectory}"

  mkdir -p "${configDirectory}"
  mv "${tempConfigDirectory}"/* "${configDirectory}"
  rm -fr "${tempConfigDirectory}"

  printf "\033[32m done \033[0m \n"
}
