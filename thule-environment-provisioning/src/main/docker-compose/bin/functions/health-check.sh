#!/bin/bash

function checkHealth() {
  # Input parameters
  dockerComposeFile=$1
  serviceName=$2

  echo ""
  echo "================================================================================"
  echo "Checking health of ${serviceName}..."

  dockerServiceName=$(docker-compose -f ${dockerComposeFile} ps ${serviceName} | grep ${serviceName} | cut -d" " -f1)
  healthCheckStartTime=$(date +%s)
  healthResponseCode=0
  elapsedSeconds=$(($(date +%s) - healthCheckStartTime))
  maxElapsedSeconds=300

  stateHealth=$(docker inspect --format=\{\{.State.Health\}\} ${dockerServiceName})
  echo ""
  if [[ "${stateHealth}" == "<nil>" ]]; then
    echo "WARNING: Unable to check health for ${serviceName} because a healthcheck has not been defined"
  else
    printf "Waiting for %s (up to a maximum of %s seconds)." "${serviceName}" ${maxElapsedSeconds}
    healthStatus=$(docker inspect --format=\{\{.State.Health.Status\}\} ${dockerServiceName})
    until [[ ${elapsedSeconds} -ge ${maxElapsedSeconds} ]] || [[ "${healthStatus}" == "healthy" ]]; do
      printf "."
      sleep 5
      elapsedSeconds=$(($(date +%s) - healthCheckStartTime))
      healthStatus=$(docker inspect --format=\{\{.State.Health.Status\}\} ${dockerServiceName})
    done
    printf "\n"

    if [[ ${elapsedSeconds} -lt ${maxElapsedSeconds} ]]; then
      echo "Healthcheck for ${serviceName} succeeded and took ${elapsedSeconds} second(s)"
    else
      echo "Healthcheck failed for ${serviceName} within ${elapsedSeconds} second(s)"
      echo "REASON: Health status is ${healthStatus}"
      echo "HINT: Use the following command to obtain further diagnostics: docker inspect --format='{{json .State.Health}}' ${dockerServiceName}"
      healthResponseCode=255
    fi
  fi

  echo "================================================================================"

  return ${healthResponseCode}
}
