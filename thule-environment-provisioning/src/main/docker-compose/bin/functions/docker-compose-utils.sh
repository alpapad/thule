#!/bin/bash

function createService() {
  # Input parameters
  dockerComposeFile=$1
  serviceName=$2

  echo ""
  docker-compose -f "${dockerComposeFile}" pull "${serviceName}"
  docker-compose -f "${dockerComposeFile}" up -d "${serviceName}"
}

function deleteService() {
  # Input parameters
  dockerComposeFile=$1
  serviceName=$2

  echo ""
  docker-compose -f "${dockerComposeFile}" rm -fsv "${serviceName}"
}
