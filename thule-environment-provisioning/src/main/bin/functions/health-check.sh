#!/bin/bash

function checkHealth {
    # Input parameters
    dockerComposeFile=$1

    echo ""
    echo "================================================================================"
    echo "About to check health of all services..."

    _checkHealthForAllServices $dockerComposeFile
    healthResponseCode=$?
    echo "================================================================================"

    if [[ $healthResponseCode -eq 0 ]]; then
        echo ""
        echo "================================================================================"
        echo "Healthcheck has completed and found that all the services are healthy"
        echo "================================================================================"
    else
        echo ""
        echo "================================================================================"
        echo "Healthcheck has completed and found $healthResponseCode service(s) ****FAILED***"
        echo "================================================================================"
        return $healthResponseCode
    fi
}

function _checkHealthForAllServices {
    # Input parameters
    dockerComposeFile=$1

    serviceNames=($(cat $dockerComposeFile | grep "thule.*.service:$" | sed "s/://g"))

    countOfServicesFailingHealthcheck=0
    for serviceName in "${serviceNames[@]}"
    do
        servicePort=$(cat $dockerComposeFile | sed -n "/$serviceName/,/:8080/p" | sed -n "s/[^0-9]*\([0-9]*\):8080.*/\1/p")
        healthCheckUrl=http://localhost:$servicePort/actuator/health

        _checkHealthForSingleService $serviceName $healthCheckUrl
        healthResponseCode=$?
        if [[ $healthResponseCode -ne 200 ]]; then
            ((countOfServicesFailingHealthcheck++))
        fi
    done

    return $countOfServicesFailingHealthcheck
}

function _checkHealthForSingleService {
    # Input parameters
    serviceName=$1
    healthCheckUrl=$2

    healthCheckStartTime=$(date +%s)
    elapsedSeconds=$(($(date +%s) - $healthCheckStartTime))
    maxElapsedSeconds=300

    echo ""
    printf "Waiting for $serviceName on $healthCheckUrl (up to a maximum of $maxElapsedSeconds seconds)."
    until [ $elapsedSeconds -ge $maxElapsedSeconds ] || \
          [[ healthResponseCode="$(curl -L -o /dev/null -s -w '%{http_code}' $healthCheckUrl)" -eq 200 ]]; do
        printf "."
        sleep 5
        elapsedSeconds=$(($(date +%s) - $healthCheckStartTime))
    done
    printf "\n"

    if [ $elapsedSeconds -lt $maxElapsedSeconds ]; then
        echo "Healthcheck for $serviceName on $healthCheckUrl succeeded and took $elapsedSeconds second(s)"
    else
        echo "Healthcheck failed for $serviceName on $healthCheckUrl within $elapsedSeconds second(s)"
        if [ $healthResponseCode -ne 0 ]; then
            echo "REASON: HTTP status code $healthResponseCode"
        else
            echo "REASON: $(curl -sS $healthCheckUrl 2>&1)"
        fi
        return 999
    fi

    return $healthResponseCode
}
