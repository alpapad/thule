#!/bin/bash

function checkHealth() {
  # Input parameters
  kubernetesConfigurationFile=$1

  healthCheckStartTime=$(date +%s)
  elapsedSeconds=$(($(date +%s) - healthCheckStartTime))
  maxElapsedSeconds=300
  serviceName=$(basename "${kubernetesConfigurationFile}" | sed "s/.yml.*//g")

  echo ""
  # jsonpath filters return a list so convert to an array and just cut the first element
  readinessProbePath=$(kubectl get pods --output=jsonpath="{..containers[?(@.name==\"${serviceName}\")].readinessProbe.httpGet.path}" 2>/dev/null | cut -d" " -f1)
  if [[ "${readinessProbePath}" == "" ]]; then
    echo "WARNING: Unable to perform a healthcheck because a readiness probe has not been defined"
  else
    nodePort=$(kubectl get services --output=jsonpath="{.spec.ports[*].nodePort}" "${serviceName}")
    healthCheckUrl="http://127.0.0.1:${nodePort}${readinessProbePath}"

    echo -en "\rWaiting for health check to succeed on ${healthCheckUrl} (up to a maximum of ${maxElapsedSeconds} seconds)..."
    httpStatusCode=$(curl -L -m2 -o /dev/null -s -w '%{http_code}' "${healthCheckUrl}")
    until [[ ${elapsedSeconds} -ge ${maxElapsedSeconds} ]] || [[ ${httpStatusCode} -eq 200 ]]; do
      echo -en "\rWaiting for health check to succeed on ${healthCheckUrl} (up to a maximum of ${maxElapsedSeconds} seconds)...${elapsedSeconds}s"
      sleep 5
      elapsedSeconds=$(($(date +%s) - healthCheckStartTime))
      httpStatusCode=$(curl -L -m2 -o /dev/null -s -w '%{http_code}' "${healthCheckUrl}")
    done

    if [[ ${elapsedSeconds} -lt ${maxElapsedSeconds} ]]; then
      echo -e "\rWaiting for health check to succeed on ${healthCheckUrl} (up to a maximum of ${maxElapsedSeconds} seconds)...\033[32m done \033[0m"
      echo "Healthcheck on ${healthCheckUrl} succeeded and took ${elapsedSeconds} second(s)"
      healthCheckResponseCode=0
    else
      echo -e "\rWaiting for health check to succeed on ${healthCheckUrl} (up to a maximum of ${maxElapsedSeconds} seconds)...\033[31m done \033[0m"
      echo "ERROR: Healthcheck failed on ${healthCheckUrl} within ${elapsedSeconds} second(s)"
      if [[ ${httpStatusCode} -ne 0 ]]; then
        echo "REASON: HTTP status code ${httpStatusCode}"
      else
        echo "REASON: $(curl -sS ${healthCheckUrl} 2>&1)"
      fi
      healthCheckResponseCode=255
    fi
  fi

  return ${healthCheckResponseCode}
}
