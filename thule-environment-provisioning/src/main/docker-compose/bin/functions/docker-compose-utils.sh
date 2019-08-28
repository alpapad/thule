#!/bin/bash

function createService() {
  # Input parameters
  serviceName=$1

  echo ""
  echo "Creating service..."

  docker-compose -f "${DOCKER_COMPOSE_FILE}" pull "${serviceName}"
  docker-compose -f "${DOCKER_COMPOSE_FILE}" up -d "${serviceName}"
}

function deleteService() {
  # Input parameters
  serviceName=$1

  echo ""
  echo "Deleting service..."

  docker-compose -f "${DOCKER_COMPOSE_FILE}" rm -fsv "${serviceName}"
}
