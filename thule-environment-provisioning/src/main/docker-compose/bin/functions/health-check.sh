#!/bin/bash

function checkHealth() {
  # Input parameters
  serviceName=$1

  dockerServiceName=$(docker-compose -f "${DOCKER_COMPOSE_FILE}" ps "${serviceName}" | grep "${serviceName}" | cut -d" " -f1)
  healthCheckStartTime=$(date +%s)
  elapsedSeconds=$(($(date +%s) - healthCheckStartTime))
  maxElapsedSeconds=300

  echo ""
  stateHealth=$(docker inspect --format=\{\{.State.Health\}\} "${dockerServiceName}")
  if [[ "${stateHealth}" == "<nil>" ]]; then
    echo "WARNING: Unable to perform a healthcheck because a healthcheck has not been defined"
  else
    echo -en "\rWaiting for health check to succeed (up to a maximum of ${maxElapsedSeconds} seconds)..."
    healthStatus=$(docker inspect --format=\{\{.State.Health.Status\}\} "${dockerServiceName}")
    until [[ ${elapsedSeconds} -ge ${maxElapsedSeconds} ]] || [[ "${healthStatus}" == "healthy" ]]; do
      echo -en "\rWaiting for health check to succeed (up to a maximum of ${maxElapsedSeconds} seconds)...${elapsedSeconds}s"
      sleep 5
      elapsedSeconds=$(($(date +%s) - healthCheckStartTime))
      healthStatus=$(docker inspect --format=\{\{.State.Health.Status\}\} "${dockerServiceName}")
    done
    echo ""

    if [[ ${elapsedSeconds} -lt ${maxElapsedSeconds} ]]; then
      echo "Healthcheck succeeded and took ${elapsedSeconds} second(s)"
      healthCheckResponseCode=0
    else
      echo "ERROR: Healthcheck failed within ${elapsedSeconds} second(s)"
      echo "REASON: Health status is ${healthStatus}"
      echo "HINT: Use the following command to obtain further diagnostics: docker inspect --format='{{json .State.Health}}' ${dockerServiceName}"
      healthCheckResponseCode=255
    fi
  fi

  return ${healthCheckResponseCode}
}
