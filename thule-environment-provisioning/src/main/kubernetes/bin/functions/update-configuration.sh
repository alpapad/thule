#!/bin/bash

function updateConfiguration() {
  # Input parameters
  kubernetesConfigurationFile=$1

  echo ""
  echo "Updating configuration..."

  configDirectoryExpectedByConfigurationService=$(configDirectoryExpectedByConfigurationService)
  serviceName=$(basename "${kubernetesConfigurationFile}" | sed "s/.yml.*//g")
  tempConfigDirectory=${configDirectoryExpectedByConfigurationService}/temp/${serviceName}

  if [[ "${serviceName}" == "thule-configuration-service" ]]; then
    configDirectory=${configDirectoryExpectedByConfigurationService}
    rm -f "${configDirectory}/*.yml"
  else
    configDirectory=${configDirectoryExpectedByConfigurationService}/${serviceName}
    rm -fr "${configDirectory}"
  fi

  printf "Replacing old config %s with the new config %s ..." "${configDirectory}" "${tempConfigDirectory}"

  mkdir -p "${configDirectory}"
  mv "${tempConfigDirectory}"/* "${configDirectory}"
  rm -fr "${tempConfigDirectory}"

  printf "\033[32m done \033[0m \n"
}
