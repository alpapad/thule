#!/bin/bash

function updateConfiguration() {
  # Input parameters
  serviceName=$1

  echo ""
  echo "Updating configuration..."

  configDirectoryExpectedByConfigurationService=$(configDirectoryExpectedByConfigurationService)
  tempConfigDirectory=${configDirectoryExpectedByConfigurationService}/temp/${serviceName}

  if [[ "${serviceName}" == "thule-configuration-service" ]]; then
    configDirectory=${configDirectoryExpectedByConfigurationService}
    rm -f "${configDirectory}/*.yml"
  else
    configDirectory=${configDirectoryExpectedByConfigurationService}/${serviceName}
    rm -fr "${configDirectory}"
  fi

  echo -n "Replacing old config ${configDirectory} with the new config ${tempConfigDirectory} ..."

  mkdir -p "${configDirectory}"
  mv "${tempConfigDirectory}"/* "${configDirectory}"
  rm -fr "${tempConfigDirectory}"

  echo -e "\033[32m done \033[0m"
}
