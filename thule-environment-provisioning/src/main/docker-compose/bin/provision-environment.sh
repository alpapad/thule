#!/bin/bash

################################################################################
# Usage documentation
################################################################################
usage() {
  scriptName=$(basename "$0")
  echo "Provisions a service (e.g. CONFIGURATION-SERVICE) in an environment (e.g. QA, PROD) by shipping this script and supporting config to that environments host and then executing it as follows:"
  echo ""
  echo "If the -l option has not been specified:"
  echo "- Ships this script to the environments host"
  echo "- Executes this script on the environments host with the -l option"
  echo ""
  echo "Following steps are executed regardless of the -l option:"
  echo "- Download the service configuration jar from Nexus into a temporary location"
  echo "- Pull the docker image from Nexus"
  echo "- Stop docker container"
  echo "- Update the old configuration with the new one that is in a temporary location (see above)"
  echo "- Start docker container"
  echo "- Check the health to ensure that the service has started successfully"
  echo ""
  echo ""
  echo "Usage: $scriptName [OPTION...] SERVICE_NAMES"
  echo ""
  echo "Options Summary:"
  echo ""
  echo "  -c, --clean                          Remove all resources using docker-compose"
  echo "  -e, --environment                    Environment to provision, defaults to DEV"
  echo "  -h, --help                           Show this help"
  echo "  -l, --provision-locally              Don't ship this script to the provisioning host, provision on the current host (localhost) instead"
  echo ""
  echo ""
  exit
}

################################################################################
# Process command line options
################################################################################
CLEAN_ENVIRONMENT=false
ENVIRONMENT_NAME=dev
PROVISION_LOCALLY=false

COMMAND_OPTIONS=$*
getoptResults=$(getopt -s bash -o ce:hl --long clean,environment:,help,provision-locally -- "$@")
eval set -- "$getoptResults"
while true; do
  case "$1" in
  --clean | -c)
    CLEAN_ENVIRONMENT=true
    shift
    ;;
  --environment | -e)
    ENVIRONMENT_NAME=$2
    shift 2
    ;;
  --help | -h)
    usage
    exit 0
    ;;
  --provision-locally | -l)
    PROVISION_LOCALLY=true
    shift
    ;;
  --)
    shift
    break
    ;;
  *)
    # Should not occur
    echo "$0: unknown option $1"
    exit 1
    ;;
  esac
done

################################################################################
# Load functions
################################################################################
SCRIPT_DIR_NAME=$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)
SCRIPT_NAME=$(basename "$0")
for functionScriptFile in $(find ${SCRIPT_DIR_NAME}/functions/*.sh -maxdepth 0 -type f); do
  source ${functionScriptFile}
done

################################################################################
# Validate command line options
################################################################################
# environment-name must be in lowercase
ENVIRONMENT_NAME=$(toLowerCase "${ENVIRONMENT_NAME}") # Convert to lowercase

# default service-name to all services
ENVIRONMENTS_DIRECTORY=${SCRIPT_DIR_NAME}/../environments
DOCKER_COMPOSE_FILE=${ENVIRONMENTS_DIRECTORY}/${ENVIRONMENT_NAME}/docker-compose.yml
SERVICE_NAMES=$(toLowerCase "$@") # Convert to lowercase
if [[ -z "${SERVICE_NAMES}" ]]; then
  SERVICE_NAMES=($(grep "^\s*.*service:$" "${DOCKER_COMPOSE_FILE}" | sed "s/://g"))
else
  SERVICE_NAMES=(${SERVICE_NAMES})
fi

# docker-compose.yml must exist for the environment-name
if [[ ! -f "${DOCKER_COMPOSE_FILE}" ]]; then
  echo ""
  echo "================================================================================"
  echo "ERROR: Docker compose file for environment [${ENVIRONMENT_NAME}] ${DOCKER_COMPOSE_FILE} does not exist"
  echo "Hint: Have you mistyped the environment name?"
  echo "================================================================================"
  exit 255
fi

# service must exist in the docker-compose.yml
for serviceName in "${SERVICE_NAMES[@]}"; do
  serviceNameFound=$(cat "${DOCKER_COMPOSE_FILE}" | grep "^\s*${serviceName}:$" | sed "s/://g")
  if [[ -z ${serviceNameFound} ]]; then
    echo ""
    echo "================================================================================"
    echo "ERROR: Service with name [${serviceName}] does not exist in ${DOCKER_COMPOSE_FILE}"
    echo "Hint: Have you mistyped the service name?"
    echo "================================================================================"
    exit 255
  fi
done

################################################################################
# Load config properties
################################################################################
# Load default config properties
source ${ENVIRONMENTS_DIRECTORY}/default/config.sh

# Load environment specific config properties
if [[ -f "${ENVIRONMENTS_DIRECTORY}/${ENVIRONMENT_NAME}/config.sh" ]]; then source ${ENVIRONMENTS_DIRECTORY}/${ENVIRONMENT_NAME}/config.sh; fi

# Validate config properties
if [[ -z "${PROVISIONING_HOST}" ]]; then
  echo ""
  echo "================================================================================"
  echo "Configuration attribute PROVISIONING_HOST has not been defined"
  echo "Hint: Have you mistyped PROVISIONING_HOST in ${ENVIRONMENTS_DIRECTORY}/${ENVIRONMENT_NAME}/config.sh?"
  echo "================================================================================"
  exit 255
fi

################################################################################
# Execute this script on the target environments host
################################################################################
if [[ ${PROVISION_LOCALLY} != "true" ]]; then
  echo ""
  echo "================================================================================"
  echo "About to ship the provisioning scripts to ${PROVISIONING_HOST}..."
  echo ""

  rsync -a --delete --progress "$SCRIPT_DIR_NAME/../../../../" "${PROVISIONING_HOST_USERID}@${PROVISIONING_HOST}:${PROVISIONING_HOST_TARGET_DIRECTORY}"
  if [[ $? != 0 ]]; then
    echo ""
    echo "ERROR: Could not ship the provisioning scripts to ${PROVISIONING_HOST_USERID}@${PROVISIONING_HOST}:${PROVISIONING_HOST_TARGET_DIRECTORY}"
    echo "Hint: Ensure user ${PROVISIONING_HOST_USERID} has sufficient permissions to ${PROVISIONING_HOST_TARGET_DIRECTORY}"
    echo "================================================================================"
    exit 255
  fi

  echo ""
  echo "Have successfully shipped the provisioning scripts to ${PROVISIONING_HOST}"
  echo "================================================================================"

  ssh -o StrictHostKeyChecking=no "${PROVISIONING_HOST_USERID}@${PROVISIONING_HOST}" -tt -C "bash -cl '${PROVISIONING_HOST_TARGET_DIRECTORY}/src/main/docker-compose/bin/provision-environment.sh -l $COMMAND_OPTIONS'"
  exit
fi

################################################################################
# Log the start/end of this scripts execution
################################################################################
startTime=$(date +%s)

echo ""
echo "================================================================================"
echo "$SCRIPT_NAME starting at $(date '+%F %T ')"
echo "================================================================================"

################################################################################
# Show settings
################################################################################
settings="Settings used are as follows...\n\\n\
CLEAN_ENVIRONMENT is ${CLEAN_ENVIRONMENT}\n\
DOCKER_COMPOSE_FILE is ${DOCKER_COMPOSE_FILE}\n\
ENVIRONMENTS_DIRECTORY is ${ENVIRONMENTS_DIRECTORY}\n\
ENVIRONMENT_NAME is ${ENVIRONMENT_NAME}\n\
NEXUS_HOST is ${NEXUS_HOST}\n\
NEXUS_PORT_DOCKER is ${NEXUS_PORT_DOCKER}\n\
NEXUS_PORT_MAVEN is ${NEXUS_PORT_MAVEN}\n\
PROVISION_LOCALLY is ${PROVISION_LOCALLY}\n\
PROVISIONING_HOST is ${PROVISIONING_HOST}\n\
PROVISIONING_HOST_USERID is ${PROVISIONING_HOST_USERID}\n\
PROVISIONING_HOST_TARGET_DIRECTORY is ${PROVISIONING_HOST_TARGET_DIRECTORY}\n\
SERVICE_NAMES are ${SERVICE_NAMES[*]}"

echo ""
echo "================================================================================"
echo -e "${settings}"
echo "================================================================================"

################################################################################
# Clean
################################################################################
if [[ "${CLEAN_ENVIRONMENT}" == "true" ]]; then
  echo ""
  echo "================================================================================"
  echo "About to clean the environment..."

  docker-compose -f "${DOCKER_COMPOSE_FILE}" rm -fsv

  echo ""
  echo "Have successfully cleaned the environment"
  echo "================================================================================"
fi

################################################################################
# Provision
################################################################################
for serviceName in "${SERVICE_NAMES[@]}"; do
  echo ""
  echo "================================================================================"
  echo "About to provision ${serviceName}..."

  isSpringBootService=$(cat "${DOCKER_COMPOSE_FILE}" | sed -n "/${serviceName}:/,/#####/p" | sed -n "s/.*SPRING_PROFILES_ACTIVE:.*/true/p")
  isSpringBootService=${isSpringBootService:-false}

  if [[ ${isSpringBootService} == "true" ]]; then
    downloadConfiguration "${serviceName}"
  fi
  deleteService "${serviceName}"
  if [[ ${isSpringBootService} == "true" ]]; then
    updateConfiguration "${serviceName}"
  fi
  createService "${serviceName}"

  echo ""
  echo "Have successfully provisioned ${serviceName}"
  echo "================================================================================"
done

################################################################################
# Health check
################################################################################
countOfServicesFailingHealthcheck=0
for serviceName in "${SERVICE_NAMES[@]}"; do
  echo ""
  echo "================================================================================"
  echo "Checking health of ${serviceName}..."

  if ! checkHealth "${serviceName}"; then
    ((countOfServicesFailingHealthcheck++))
  fi

  echo "================================================================================"
done

echo ""
echo "================================================================================"
if [[ ${countOfServicesFailingHealthcheck} -eq 0 ]]; then
  echo "Healthcheck has completed and found no problems"
else
  echo "Healthcheck has completed and found ${countOfServicesFailingHealthcheck} service(s) ****FAILED***"
fi
echo "================================================================================"

################################################################################
# Log the start/end of this scripts execution
################################################################################
elapsedSeconds=$(($(date +%s) - startTime))
echo ""
echo "================================================================================"
echo "$SCRIPT_NAME finished at $(date '+%F %T') taking $elapsedSeconds seconds"
echo "================================================================================"

exit ${countOfServicesFailingHealthcheck}
