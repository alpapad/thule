#!/bin/bash

SCRIPT_DIR_NAME=$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)

${SCRIPT_DIR_NAME}/deploy.sh -k piglet ${SCRIPT_DIR_NAME}/../../../../../thule-configuration-service/k8s/cit.yml
for microServiceDirectory in $(find ${SCRIPT_DIR_NAME}/../../../../../ -name "*-service" -type d -printf "%f\n" | sort | sed "s/thule-configuration-service//g"); do
  ${SCRIPT_DIR_NAME}/deploy.sh -k piglet ${SCRIPT_DIR_NAME}/../../../../../${microServiceDirectory}/k8s/cit.yml
done
