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

SCRIPT_DIR_NAME=$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)
SERVICE_NAMES=(thule-configuration-service $(find ${SCRIPT_DIR_NAME}/../../../../../ -name "*-service" -type d -printf "%f\n" | sort | sed "s/thule-configuration-service//g"))

for serviceName in "${SERVICE_NAMES[@]}"; do
  ${SCRIPT_DIR_NAME}/deploy.sh ${COMMAND_OPTIONS} ${SCRIPT_DIR_NAME}/../../../../../${serviceName}/k8s/${ENVIRONMENT_NAME}.yml
done
