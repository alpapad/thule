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
  echo "- Download the service configuration jar from Nexus"
  echo "- Update the old configuration with the new one"
  echo "- Apply kubernetes apply file"
  echo "- Check the health to ensure that the service has started successfully"
  echo ""
  echo ""
  echo "Usage: $scriptName [OPTION...] K8S_FILE"
  echo ""
  echo "Options Summary:"
  echo ""
  echo "  -e, --environment                    Environment to deploy to, defaults to DEV"
  echo "  -h, --help                           Show this help"
  echo "  -l, --deploy-locally                 Don't ship this script to the deployment host, deploy on the current host (localhost) instead"
  echo "  -r, --reset                          Reset microk8s"
  echo ""
  echo ""
  exit
}

################################################################################
# Process command line options
################################################################################
DEPLOY_LOCALLY=false
ENVIRONMENT_NAME=dev
RESET_ENVIRONMENT=false

COMMAND_OPTIONS=$*
getoptResults=$(getopt -s bash -o e:hlr --long environment:,deploy-locally,help,reset -- "$@")
eval set -- "$getoptResults"
while true; do
  case "$1" in
  --deploy-locally | -l)
    DEPLOY_LOCALLY=true
    shift
    ;;
  --help | -h)
    usage
    exit 0
    ;;
  --environment | -e)
    ENVIRONMENT_NAME=$2
    shift 2
    ;;
  --reset | -r)
    RESET_ENVIRONMENT=true
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

# k8s-file
K8S_FILE="$1"
K8S_FILE_NAME=$(basename "$K8S_FILE")

# K8S_FILE must exist
if [[ ! -f "${K8S_FILE}" ]]; then
  echo ""
  echo "================================================================================"
  echo "ERROR: Kubernetes file ${K8S_FILE} does not exist"
  echo "Hint: Have you mistyped the file name?"
  echo "================================================================================"
  exit 255
fi

################################################################################
# Load config properties
################################################################################
SERVICE_NAME=$(awk '/app: /{print $NF;exit;}' ${K8S_FILE})
CONFIGS_DIRECTORY=${SCRIPT_DIR_NAME}/../config

# Load default config properties
source ${CONFIGS_DIRECTORY}/default.sh

# Load environment specific config properties
if [[ -f "${CONFIGS_DIRECTORY}/${ENVIRONMENT_NAME}.sh" ]]; then source ${CONFIGS_DIRECTORY}/${ENVIRONMENT_NAME}.sh; fi

# Validate config properties
if [[ -z "${K8S_HOST}" ]]; then
  echo ""
  echo "================================================================================"
  echo "ERROR: Configuration attribute K8S_HOST has not been defined"
  echo "Hint: Have you mistyped K8S_HOST in ${CONFIGS_DIRECTORY}/${ENVIRONMENT_NAME}.sh?"
  echo "================================================================================"
  exit 255
fi

################################################################################
# Execute this script on the target environments host
################################################################################
if [[ ${DEPLOY_LOCALLY} != "true" ]]; then
  echo ""
  echo "================================================================================"
  echo "About to ship the deployment scripts to ${K8S_HOST}..."
  echo ""

  if ! rsync -a --delete --progress --rsync-path="mkdir -p ${K8S_HOST_TARGET_DIRECTORY} && rsync" "$SCRIPT_DIR_NAME/../../../../../thule-microservice-deployment" "${K8S_HOST_USERID}@${K8S_HOST}:${K8S_HOST_TARGET_DIRECTORY}"; then
    echo ""
    echo "ERROR: Could not ship the deployment scripts to ${K8S_HOST_USERID}@${K8S_HOST}:${K8S_HOST_TARGET_DIRECTORY}"
    echo "Hint: Ensure user ${K8S_HOST_USERID} has sufficient permissions to ${K8S_HOST_TARGET_DIRECTORY}"
    echo "================================================================================"
    exit 255
  fi

  if ! rsync -a --delete --progress --rsync-path="mkdir -p ${K8S_HOST_TARGET_DIRECTORY_K8S} && rsync" "$K8S_FILE" "${K8S_HOST_USERID}@${K8S_HOST}:${K8S_HOST_TARGET_DIRECTORY_K8S}/${K8S_FILE_NAME}"; then
    echo ""
    echo "ERROR: Could not ship the k8s file to ${K8S_HOST_USERID}@${K8S_HOST}:${K8S_HOST_TARGET_DIRECTORY_K8S}"
    echo "Hint: Ensure user ${K8S_HOST_USERID} has sufficient permissions to ${K8S_HOST_TARGET_DIRECTORY_K8S}"
    echo "================================================================================"
    exit 255
  fi

  echo ""
  echo "Have successfully shipped the deployment scripts to ${K8S_HOST}"
  echo "================================================================================"

  COMMAND_OPTIONS="${COMMAND_OPTIONS/$K8S_FILE/$K8S_HOST_TARGET_DIRECTORY_K8S/$K8S_FILE_NAME}"
  ssh -o StrictHostKeyChecking=no "${K8S_HOST_USERID}@${K8S_HOST}" -tt -C "bash -cl '${K8S_HOST_TARGET_DIRECTORY}/thule-microservice-deployment/src/main/k8s/bin/deploy.sh -l $COMMAND_OPTIONS'"
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
CONFIGRATION_SERVICE_CONFIG_DIRECTORY is ${CONFIGRATION_SERVICE_CONFIG_DIRECTORY}\n\
CONFIGS_DIRECTORY is ${CONFIGS_DIRECTORY}\n\
DEPLOY_LOCALLY is ${DEPLOY_LOCALLY}\n\
ENVIRONMENT_NAME is ${ENVIRONMENT_NAME}\n\
K8S_HOST is ${K8S_HOST}\n\
K8S_HOST_USERID is ${K8S_HOST_USERID}\n\
K8S_HOST_TARGET_DIRECTORY is ${K8S_HOST_TARGET_DIRECTORY}\n\
K8S_FILE is ${K8S_FILE}\n\
NEXUS_HOST is ${NEXUS_HOST}\n\
NEXUS_PORT_DOCKER is ${NEXUS_PORT_DOCKER}\n\
NEXUS_PORT_MAVEN is ${NEXUS_PORT_MAVEN}\n\
RESET_ENVIRONMENT is ${RESET_ENVIRONMENT}\n\
SERVICE_NAME is ${SERVICE_NAME}"

echo ""
echo "================================================================================"
echo -e "${settings}"
echo "================================================================================"

################################################################################
# Reset
################################################################################
if [[ "${RESET_ENVIRONMENT}" == "true" ]]; then
  echo ""
  echo "================================================================================"
  echo "About to reset the environment..."

  sudo microk8s.reset

  echo ""
  echo "Have successfully reset the environment"
  echo "================================================================================"
fi

################################################################################
# Deploy
################################################################################
configureMicrok8s
configureThule

echo ""
echo "================================================================================"
echo "About to deploy ${SERVICE_NAME}..."

isSpringBootService=$(sed -n "s/.*- name\s*:\s*SPRING_PROFILES_ACTIVE.*/true/p" "${K8S_FILE}")
isSpringBootService=${isSpringBootService:-false}

if [[ ${isSpringBootService} == "true" ]]; then
  updateConfiguration "${K8S_FILE}"
fi
if kubectlApply "${K8S_FILE}"; then
  echo ""
  echo "Have successfully deployed ${SERVICE_NAME}"
else
  echo ""
  echo "ERROR: Have failed to deploy ${SERVICE_NAME}"
  exit 255
fi
echo "================================================================================"

################################################################################
# Health check
################################################################################
echo ""
echo "================================================================================"
echo "Checking health of ${SERVICE_NAME}..."

if checkHealth "${K8S_FILE}"; then
  echo ""
  echo "Healthcheck has completed and found no problems"
else
  echo ""
  echo "ERROR: Healthcheck has ****FAILED*** for ${SERVICE_NAME}"
  exit 255
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
