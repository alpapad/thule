#!/bin/bash

function checkHealth() {
  # Input parameters
  kubernetesConfigurationFile=$1

  serviceName=$(basename "${kubernetesConfigurationFile}" | sed "s/.yml.*//g")

  echo ""
  echo "================================================================================"
  echo "Checking health of ${serviceName}..."

  healthCheckStartTime=$(date +%s)
  healthResponseCode=0
  elapsedSeconds=$(($(date +%s) - healthCheckStartTime))
  maxElapsedSeconds=300

  nodePort=$(kubectl get services --output=jsonpath="{.spec.ports[*].nodePort}" "${serviceName}")
  # jsonpath filters return a list so convert to an array and just take first element
  readinessProbePath=($(kubectl get pods --output=jsonpath="{..containers[?(@.name==\"${serviceName}\")].readinessProbe.httpGet.path}"))
  healthCheckUrl=http://127.0.0.1:${nodePort}${readinessProbePath}

  echo ""
  if [[ "${readinessProbePath}" == "" ]]; then
    echo "WARNING: Unable to check health for ${serviceName} because a healthcheck has not been defined"
  else
    printf "Waiting for %s on %s (up to a maximum of %s seconds)." "${serviceName}" "${healthCheckUrl}" ${maxElapsedSeconds}
    httpStatusCode=$(curl -L -m2 -o /dev/null -s -w '%{http_code}' "${healthCheckUrl}")
    until [[ ${elapsedSeconds} -ge ${maxElapsedSeconds} ]] || [[ ${httpStatusCode} -eq 200 ]]; do
      printf "."
      sleep 5
      elapsedSeconds=$(($(date +%s) - healthCheckStartTime))
      httpStatusCode=$(curl -L -m2 -o /dev/null -s -w '%{http_code}' "${healthCheckUrl}")
    done
    printf "\n"

    if [[ ${elapsedSeconds} -lt ${maxElapsedSeconds} ]]; then
      echo "Healthcheck for ${serviceName} on ${healthCheckUrl} succeeded and took ${elapsedSeconds} second(s)"
    else
      echo "ERROR: Healthcheck failed for ${serviceName} on ${healthCheckUrl} within ${elapsedSeconds} second(s)"
      if [[ ${httpStatusCode} -ne 0 ]]; then
        echo "REASON: HTTP status code ${httpStatusCode}"
      else
        echo "REASON: $(curl -sS ${healthCheckUrl} 2>&1)"
      fi
      healthResponseCode=255
    fi
  fi

  echo "================================================================================"

  return ${healthResponseCode}
}
