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
  echo "- Delete kubernetes service"
  echo "- Update the old configuration with the new one that is in a temporary location (see above)"
  echo "- Create kubernetes service"
  echo "- Check the health to ensure that the service has started successfully"
  echo ""
  echo ""
  echo "Usage: $scriptName [OPTION...] SERVICE_NAMES"
  echo ""
  echo "Options Summary:"
  echo ""
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
ENVIRONMENT_NAME=dev
PROVISION_LOCALLY=false

COMMAND_OPTIONS=$*
getoptResults=$(getopt -s bash -o e:hl --long environment:,help,provision-locally -- "$@")
eval set -- "$getoptResults"
while true; do
  case "$1" in
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
SERVICE_NAMES=$(toLowerCase "$@") # Convert to lowercase
if [[ -z "${SERVICE_NAMES}" ]]; then
  SERVICE_NAMES=($(find "${ENVIRONMENTS_DIRECTORY}" -name "*.yml" -type f -printf "%f\n" | sort | sed "s/.yml.*//g"))
else
  SERVICE_NAMES=(${SERVICE_NAMES})
fi

# ${serviceName}.yml must exist for the environment-name
KUBERNETES_CONFIGURATION_DIRECTORY=${ENVIRONMENTS_DIRECTORY}/${ENVIRONMENT_NAME}/services
for serviceName in "${SERVICE_NAMES[@]}"; do
  kubernetesConfigurationFile=${KUBERNETES_CONFIGURATION_DIRECTORY}/${serviceName}.yml
  if [[ ! -f "${kubernetesConfigurationFile}" ]]; then
    echo ""
    echo "================================================================================"
    echo "ERROR: Kubernetes file for environment [${ENVIRONMENT_NAME}] ${kubernetesConfigurationFile} does not exist"
    echo "Hint: Have you mistyped the environment name?"
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
  echo "ERROR: Configuration attribute PROVISIONING_HOST has not been defined"
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

  if ! rsync -a --delete --progress "$SCRIPT_DIR_NAME/../../../../" "${PROVISIONING_HOST_USERID}@${PROVISIONING_HOST}:${PROVISIONING_HOST_TARGET_DIRECTORY}"; then
    echo ""
    echo "ERROR: Could not ship the provisioning scripts to ${PROVISIONING_HOST_USERID}@${PROVISIONING_HOST}:${PROVISIONING_HOST_TARGET_DIRECTORY}"
    echo "Hint: Ensure user ${PROVISIONING_HOST_USERID} has sufficient permissions to ${PROVISIONING_HOST_TARGET_DIRECTORY}"
    echo "================================================================================"
    exit 255
  fi

  echo ""
  echo "Have successfully shipped the provisioning scripts to ${PROVISIONING_HOST}"
  echo "================================================================================"

  ssh -o StrictHostKeyChecking=no "${PROVISIONING_HOST_USERID}@${PROVISIONING_HOST}" -t -C "bash -cl '${PROVISIONING_HOST_TARGET_DIRECTORY}/src/main/kubernetes/bin/provision-environment.sh -l $COMMAND_OPTIONS'"
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
ENVIRONMENTS_DIRECTORY is ${ENVIRONMENTS_DIRECTORY}\n\
ENVIRONMENT_NAME is ${ENVIRONMENT_NAME}\n\
KUBERNETES_CONFIGURATION_DIRECTORY is ${KUBERNETES_CONFIGURATION_DIRECTORY}\n\
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
# Provision
################################################################################
installMicrok8s
for serviceName in "${SERVICE_NAMES[@]}"; do
  echo ""
  echo "================================================================================"
  echo "About to provision ${serviceName}..."

  kubernetesConfigurationFile=${KUBERNETES_CONFIGURATION_DIRECTORY}/${serviceName}.yml
  isSpringBootService=$(sed -n "s/.*- name\s*:\s*SPRING_PROFILES_ACTIVE.*/true/p" "${kubernetesConfigurationFile}")
  isSpringBootService=${isSpringBootService:-false}

  if [[ ${isSpringBootService} == "true" ]]; then
    downloadConfiguration "${kubernetesConfigurationFile}"
  fi
  deleteService "${kubernetesConfigurationFile}"
  if [[ ${isSpringBootService} == "true" ]]; then
    updateConfiguration "${kubernetesConfigurationFile}"
  fi
  createService "${kubernetesConfigurationFile}"

  echo ""
  echo "Have successfully provisioned ${serviceName}"
  echo "================================================================================"
done

################################################################################
# Health check
################################################################################
sleep 5
countOfServicesFailingHealthcheck=0
for serviceName in "${SERVICE_NAMES[@]}"; do
  kubernetesConfigurationFile=${KUBERNETES_CONFIGURATION_DIRECTORY}/${serviceName}.yml
  if ! checkHealth "${kubernetesConfigurationFile}"; then
    ((countOfServicesFailingHealthcheck++))
  fi
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
