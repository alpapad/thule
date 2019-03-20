#!/bin/bash

function checkHealth {
    # Input parameters
    dockerComposeFile=$1
    serviceName=$2

    echo ""
    echo "================================================================================"
    echo "About to check health of service(s).."

    if [[ -z ${serviceName} ]]; then
        _checkHealthForAllServices ${dockerComposeFile}
        healthResponseCode=$?
    else
        _checkHealthForSingleService ${dockerComposeFile} ${serviceName}
        healthResponseCode=$?
        if [[ ${healthResponseCode} -ne 0 ]]; then
            echo ""
            echo "================================================================================"
            echo "WARNING: ${serviceName} has failed its healthcheck"
            echo ""
            echo "HINT: If the config service is not up, no other services will start"
            echo "Checking the health of the configuration service for your convenience :-)"
            echo "================================================================================"

            _checkHealthForSingleService ${dockerComposeFile} thule-configuration-service
        fi
    fi

    echo "================================================================================"

    return ${healthResponseCode}
}

function _checkHealthForAllServices {
    # Input parameters
    dockerComposeFile=$1

    serviceNames=($(cat ${dockerComposeFile} | grep "^\s*thule.*.service:$" | sed "s/://g"))

    countOfServicesFailingHealthcheck=0
    for serviceName in "${serviceNames[@]}"
    do
        _checkHealthForSingleService ${dockerComposeFile} ${serviceName}
        httpStahealthResponseCodetusCode=$?
        if [[ ${healthResponseCode} -ne 0 ]]; then
            ((countOfServicesFailingHealthcheck++))
        fi
    done

    echo ""
    if [[ ${countOfServicesFailingHealthcheck} -eq 0 ]]; then
        echo "Healthcheck has completed and found that all the services are healthy"
    else
        echo "Healthcheck has completed and found ${countOfServicesFailingHealthcheck} service(s) ****FAILED***"
    fi

    return ${countOfServicesFailingHealthcheck}
}

function _checkHealthForSingleService {
    # Input parameters
    dockerComposeFile=$1
    serviceName=$2

    servicePort=$(cat ${dockerComposeFile} | sed -n "/${serviceName}/,/:8080/p" | sed -n "s/[^0-9]*\([0-9]*\):8080.*/\1/p")
    healthCheckUrl=http://localhost:${servicePort}/actuator/health

    healthCheckStartTime=$(date +%s)
    elapsedSeconds=$(($(date +%s) - ${healthCheckStartTime}))
    maxElapsedSeconds=300

    echo ""
    printf "Waiting for ${serviceName} on ${healthCheckUrl} (up to a maximum of ${maxElapsedSeconds} seconds)."
    until [[ ${elapsedSeconds} -ge ${maxElapsedSeconds} ]] || \
          [[ httpStatusCode="$(curl -L -o /dev/null -s -w '%{http_code}' ${healthCheckUrl})" -eq 200 ]]; do
        printf "."
        sleep 5
        elapsedSeconds=$(($(date +%s) - $healthCheckStartTime))
    done
    printf "\n"

    if [[ ${elapsedSeconds} -lt ${maxElapsedSeconds} ]]; then
        echo "Healthcheck for ${serviceName} on ${healthCheckUrl} succeeded and took ${elapsedSeconds} second(s)"
        healthResponseCode=0
    else
        echo "Healthcheck failed for ${serviceName} on ${healthCheckUrl} within ${elapsedSeconds} second(s)"
        healthResponseCode=999
        if [[ ${httpStatusCode} -ne 0 ]]; then
            echo "REASON: HTTP status code ${httpStatusCode}"
        else
            echo "REASON: $(curl -sS ${healthCheckUrl} 2>&1)"
        fi
    fi

    return ${healthResponseCode}
}
