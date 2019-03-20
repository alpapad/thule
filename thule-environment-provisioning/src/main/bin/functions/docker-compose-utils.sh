#!/bin/bash

function downloadDockerImage {
    # Input parameters
    dockerComposeFile=$1
    serviceName=$2

    if [[ -z ${serviceName} ]]; then
        # All services
        docker-compose -f ${dockerComposeFile} pull
    else
        # Single service
        docker-compose -f ${dockerComposeFile} pull ${serviceName}
    fi
}

function startService {
    # Input parameters
    dockerComposeFile=$1
    serviceName=$2

    if [[ -z ${serviceName} ]]; then
        # All services
        docker-compose -f ${dockerComposeFile} up -d
    else
        # Single service
        docker-compose -f ${dockerComposeFile} up -d ${serviceName}
    fi
}

function stopService {
    # Input parameters
    dockerComposeFile=$1
    serviceName=$2

    if [[ -z ${serviceName} ]]; then
        # All services
        docker-compose -f ${dockerComposeFile} down -v
    else
        # Single service
        docker-compose -f ${dockerComposeFile} rm -fsv ${serviceName}
    fi
}