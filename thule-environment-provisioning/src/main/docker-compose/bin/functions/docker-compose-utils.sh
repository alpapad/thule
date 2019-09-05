#!/bin/bash

function createService() {
  # Input parameters
  serviceName=$1

  echo ""
  echo "Creating service..."

  docker-compose -f "${DOCKER_COMPOSE_FILE}" pull "${serviceName}"
  docker-compose -f "${DOCKER_COMPOSE_FILE}" up -d "${serviceName}"

  dockerServiceName=$(docker-compose -f "${DOCKER_COMPOSE_FILE}" ps "${serviceName}" | grep "${serviceName}" | cut -d" " -f1)
  startupStartTime=$(date +%s)
  elapsedSeconds=$(($(date +%s) - startupStartTime))
  maxElapsedSeconds=300

  echo ""
  echo -n "Waiting for service to start (up to a maximum of ${maxElapsedSeconds} seconds)..."
  serviceInfo=$(docker inspect "${dockerServiceName}" 2>/dev/null)
  until [[ ${elapsedSeconds} -ge ${maxElapsedSeconds} ]] || [[ "${serviceInfo}" != "[]" ]]; do
    echo -en "\rWaiting for service to start (up to a maximum of ${maxElapsedSeconds} seconds)...${elapsedSeconds}s"
    sleep 5
    elapsedSeconds=$(($(date +%s) - startupStartTime))
    serviceInfo=$(docker inspect "${dockerServiceName}" 2>/dev/null)
  done

  if [[ ${elapsedSeconds} -lt ${maxElapsedSeconds} ]]; then
    echo -e "\rWaiting for service to start (up to a maximum of ${maxElapsedSeconds} seconds)...\033[32m done \033[0m"
    echo "Service started successfully and took ${elapsedSeconds} second(s)"
    startupResponseCode=0
  else
    echo -e "\rWaiting for service to start (up to a maximum of ${maxElapsedSeconds} seconds)...\033[31m failed \033[0m"
    echo "ERROR: Service failed to start within ${elapsedSeconds} second(s)"
    startupResponseCode=255
  fi

  return ${startupResponseCode}
}

function deleteService() {
  # Input parameters
  serviceName=$1

  echo ""
  echo "Deleting service..."

  docker-compose -f "${DOCKER_COMPOSE_FILE}" rm -fsv "${serviceName}"

  dockerServiceName=$(docker-compose -f "${DOCKER_COMPOSE_FILE}" ps "${serviceName}" | grep "${serviceName}" | cut -d" " -f1)
  shutdownStartTime=$(date +%s)
  elapsedSeconds=$(($(date +%s) - shutdownStartTime))
  maxElapsedSeconds=300

  echo ""
  echo -n "Waiting for service to shutdown (up to a maximum of ${maxElapsedSeconds} seconds)..."
  serviceInfo=$(docker inspect "${dockerServiceName}" 2>/dev/null)
  until [[ ${elapsedSeconds} -ge ${maxElapsedSeconds} ]] || [[ "${serviceInfo}" == "[]" ]]; do
    echo -en "\rWaiting for service to shutdown (up to a maximum of ${maxElapsedSeconds} seconds)...${elapsedSeconds}s"
    sleep 5
    elapsedSeconds=$(($(date +%s) - shutdownStartTime))
    serviceInfo=$(docker inspect "${dockerServiceName}" 2>/dev/null)
  done

  if [[ ${elapsedSeconds} -lt ${maxElapsedSeconds} ]]; then
    echo -e "\rWaiting for service to shutdown (up to a maximum of ${maxElapsedSeconds} seconds)...\033[32m done \033[0m"
    echo "Service shutdown successfully and took ${elapsedSeconds} second(s)"
    shutdownResponseCode=0
  else
    echo -e "\rWaiting for service to shutdown (up to a maximum of ${maxElapsedSeconds} seconds)...\033[31m failed \033[0m"
    echo "ERROR: Service failed to shutdown within ${elapsedSeconds} second(s)"
    shutdownResponseCode=255
  fi

  return ${shutdownResponseCode}
}
