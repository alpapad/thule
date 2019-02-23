#!/bin/bash

usage () {
    scriptName=$(basename $0)
    echo "Provisions an environment (e.g. QA, PROD) by shipping this script and supporting config to that environments host and then executing it as follows:"
    echo ""
    echo "If the -l option has not been specified:"
    echo "- Ships this script to the environments host"
    echo "- Executes this script on the environments host with the -l option"
    echo ""
    echo "Following steps are executed regardless of the -l option:"
    echo "- Download the thule-configuration-config jar from Nexus"
    echo "- Unzip the thule-configuration-config jar to a temporary location"
    echo "- Pull the docker images for all micro-services from Nexus"
    echo "- Stop all micro-service containers"
    echo "- Replace the old thule-configuration-config with the new one that is in a temporary location (see above)"
    echo "- Start all micro-service containers"
    echo "- Check the health of all micro-services to ensure they have all started successfully"
    echo "- if the -s option was specified, stop all micro-service containers"
    echo ""
    echo ""
    echo "Usage: $scriptName [OPTION...] ENVIRONMENT_NAME"
    echo ""
    echo "Options Summary:"
    echo ""
    echo "  -h, --help                           Show this help"
    echo "  -l, --provision-locally              Don't ship this script to the environment host, provision on the current host (localhost) instead"
    echo "  -s, --stop                           Stop containers once provisioning has completed (typically used for continuous integration testing environments, e.g. TeamCity)"
    echo "  -t, --target-directory=DIRECTORY     Target directory on the environment host used to store the provisioning scripts prior to execution, defaults to ~/thule-environment-provisioning"
    echo "  -u, --userid=USERID                  Userid ti use when shipping the provisioning scripts via SSH, defaults to current logged in userid"
    echo ""
    echo ""
    exit
}


################################################################################
# Global variables
################################################################################
SCRIPT_DIR_NAME=$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )
SCRIPT_NAME=$(basename $0)


################################################################################
# Load shared functions
################################################################################
for functionScriptFile in $(find $SCRIPT_DIR_NAME/functions/*.sh -maxdepth 0 -type f)
do
    source $functionScriptFile
done


################################################################################
# Process command options
################################################################################
COMMAND_OPTIONS=$@
getoptResults=$(getopt -s bash -o hlst:u: --long help,provision-locally,stop,target-directory:,userid -- "$@")
eval set -- "$getoptResults"
while true
do
    case "$1" in
        --help | -h)
            usage
            exit 0
            ;;
        --provision-locally | -l)
            PROVISION_LOCALLY=true;
            shift;
            ;;
        --stop | -s)
            STOP_CONTAINERS=true;
            shift;
            ;;
        --target-directory | -t)
            TARGET_DIRECTORY=$2;
            shift 2;
            ;;
        --userid | -u)
            USERID=$2;
            shift 2;
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
# Validate command options
################################################################################
# environment-name is mandatory
ENVIRONMENT_NAME=$(toLowerCase "$1") # Convert to lowercase
if [ -z "$ENVIRONMENT_NAME" ]; then  usage; exit 1; fi

# docker-compose.yml must exist for the environment-name
ENVIRONMENTS_DIRECTORY=$SCRIPT_DIR_NAME/../environments
DOCKER_COMPOSE_FILE=$ENVIRONMENTS_DIRECTORY/$ENVIRONMENT_NAME/docker-compose.yml
if [[ ! -f "$DOCKER_COMPOSE_FILE" ]]; then
    echo
    echo "================================================================================"
    echo "Docker compose file for environment $ENVIRONMENT_NAME [$DOCKER_COMPOSE_FILE] does not exist...exiting"
    echo "Hint: Have you mistyped the environment name?"
    echo "================================================================================"
    exit -1
fi


################################################################################
# Configure settings
################################################################################
# Load default config properties
source $ENVIRONMENTS_DIRECTORY/default/config.sh

# Load environment specific config properties
if [ -f "$ENVIRONMENTS_DIRECTORY/$ENVIRONMENT_NAME/config.sh" ]; then source $ENVIRONMENTS_DIRECTORY/$ENVIRONMENT_NAME/config.sh; fi


################################################################################
# Set options specified on the command line using defaults when option has not been specified
################################################################################
PROVISION_LOCALLY=${PROVISION_LOCALLY:=${PROVISION_LOCALLY_DEFAULT}}
STOP_CONTAINERS=${STOP_CONTAINERS:=${STOP_CONTAINERS_DEFAULT}}
TARGET_DIRECTORY=${TARGET_DIRECTORY:=${TARGET_DIRECTORY_DEFAULT}}
USERID=${USERID:=${USERID_DEFAULT}}


################################################################################
# Execute this script on the target environments host
################################################################################
if [[ $PROVISION_LOCALLY != "true" ]]; then
    echo ""
    echo "================================================================================"
    echo "About to ship the provisioning scripts to $PROVISIONING_HOST..."
    echo ""

    rsync -a --delete --progress $SCRIPT_DIR_NAME/../../../ $USERID@$PROVISIONING_HOST:$TARGET_DIRECTORY
    if [[ $? != 0 ]]; then
        echo ""
        echo "Error: Could not ship the provisioning scripts to $USERID@$PROVISIONING_HOST:$TARGET_DIRECTORY"
        echo "Hint: Ensure user $USERID has sufficient permissions to $TARGET_DIRECTORY"
        echo "================================================================================"
        exit -1
    fi

    echo ""
    echo "Have successfully shipped the provisioning scripts to $PROVISIONING_HOST"
    echo "================================================================================"

    ssh -o StrictHostKeyChecking=no $USERID@$PROVISIONING_HOST -C "$TARGET_DIRECTORY/src/main/bin/provision-environment.sh -l $COMMAND_OPTIONS"

    exit
fi


################################################################################
# Log the start/end of this scripts execution
################################################################################
startTime=$(date +%s)

echo
echo "================================================================================"
echo "$SCRIPT_NAME starting at $(date '+%F %T ')"
echo "================================================================================"


################################################################################
# Show settings
################################################################################
settings="Settings used are as follows...\n\\n\
DOCKER_COMPOSE_FILE is $DOCKER_COMPOSE_FILE\n\
ENVIRONMENTS_DIRECTORY is $ENVIRONMENTS_DIRECTORY\n\
ENVIRONMENT_NAME is $ENVIRONMENT_NAME\n\
PROVISION_LOCALLY is $PROVISION_LOCALLY\n\
STOP_CONTAINERS is $STOP_CONTAINERS"

echo
echo "================================================================================"
echo -e $settings
echo "================================================================================"


################################################################################
# Download the thule-configuration-config from Nexus
################################################################################
echo ""
echo "================================================================================"
echo "About to download the thule-configuration-config from Nexus for the $ENVIRONMENT_NAME environment..."

tempDirectory=$(mktemp -d --suffix thule-configuration-config)

# Locate the thule-configuration-service version, defaulting to "latest" if the version is not explicitly defined
configurationServiceVersion=$(grep $NEXUS_HOST:$NEXUS_PORT_DOCKER/thule-configuration-service: $DOCKER_COMPOSE_FILE | sed "s/.*$NEXUS_HOST:$NEXUS_PORT_DOCKER\/thule-configuration-service:\(\S*\).*/\1/")
if [[ -z $configurationServiceVersion ]]; then
    echo ""
    echo "Error: Could not determine the configuration service version"
    echo "Hint: Ensure a version been explicitly defined in $DOCKER_COMPOSE_FILE"
    echo "================================================================================"
    exit -1
fi

# Derive the configurationServiceConfigDownloadUrl
if [[ $configurationServiceVersion = *"SNAPSHOT" ]]; then
    # Download the artifact metadata from nexus for thule-configuration-config using the Nexus REST api
    metadataUrl="http://$NEXUS_HOST:$NEXUS_PORT_MAVEN/repository/maven-public/uk/co/serin/thule/thule-configuration-config/$configurationServiceVersion/maven-metadata.xml"
    echo ""
    echo "Downloading thule-configuration-config metadata using $metadataUrl from Nexus..."

    mkdir -p $tempDirectory/metadata
    pushd $tempDirectory/metadata
    curlResponseCode=$(curl -L -o maven-metadata.xml -w "%{http_code}" "$metadataUrl")
    popd
    if [[ $curlResponseCode < 200 || $curlResponseCode > 299 ]]; then
        echo ""
        echo "Error: Received a HTTP status code of $curlResponseCode while trying to download $metadataUrl"
        echo "================================================================================"
        exit -1
    fi

    # Extract the latest version of the required snapshot
    timestampedSnapshotVersion=$(grep -m 1 \<value\> $tempDirectory/metadata/maven-metadata.xml | sed "s/.*<value>\(.*\)<\/value>.*/\1/")
    configurationServiceConfigDownloadUrl="http://$NEXUS_HOST:$NEXUS_PORT_MAVEN/repository/maven-public/uk/co/serin/thule/thule-configuration-config/$configurationServiceVersion/thule-configuration-config-$timestampedSnapshotVersion.jar"
else
    configurationServiceConfigDownloadUrl="http://$NEXUS_HOST:$NEXUS_PORT_MAVEN/repository/maven-public/uk/co/serin/thule/thule-configuration-config/$configurationServiceVersion/thule-configuration-config-$configurationServiceVersion.jar"
fi

# Download the thule-configuration-config jar file using the Nexus REST api
echo ""
echo "Downloading $configurationServiceConfigDownloadUrl from Nexus..."

mkdir -p $tempDirectory/jar
pushd $tempDirectory/jar
curlResponseCode=$(curl -L -O -w "%{http_code}" "$configurationServiceConfigDownloadUrl")
popd
if [[ $curlResponseCode < 200 || $curlResponseCode > 299 ]]; then
    echo ""
    echo "Error: Received a HTTP status code of $curlResponseCode while trying to download $configurationServiceConfigDownloadUrl"
    echo "================================================================================"
    exit -1
fi

# Determine where docker expects the config files to reside on the filesystem
configDirectory=$(grep :/config $DOCKER_COMPOSE_FILE | sed 's/.* - \(\S*\):\/config.*/\1/')
# Substitute any environmenst variables or special characters , e.g. $HOME, ~
configDirectory=$(eval echo -E "$configDirectory")
# Ensure that the config directory can be created
mkdir -p $configDirectory
if [[ $? != 0 ]]; then
    echo ""
    echo "Error: Could not create config directory $configDirectory as specified in $DOCKER_COMPOSE_FILE"
    echo "Hint: Ensure user $USERID has sufficient permissions to create $configDirectory"
    echo "================================================================================"
    exit -1
fi

configurationServiceConfigJarFile=$(echo "" $configurationServiceConfigDownloadUrl | sed 's/.*\(thule-configuration-config-.*.jar\).*/\1/')
echo ""
echo "Unzipping $configurationServiceConfigJarFile to $tempDirectory/jar/unzipped..."

# Unzip the config files
unzip -q -d $tempDirectory/jar/unzipped $tempDirectory/jar/*.jar

echo ""
echo "Have downloaded the thule-configuration-config from Nexus for the $ENVIRONMENT_NAME environment"
echo "================================================================================"


################################################################################
# Start all the services
################################################################################
echo ""
echo "================================================================================"
echo "About to provision the $ENVIRONMENT_NAME environment..."

# Pull docker images from docker repository
echo ""
echo "Pulling docker images from Nexus via $DOCKER_COMPOSE_FILE..."
docker-compose -f $DOCKER_COMPOSE_FILE pull

# Stop all thule micro-services
echo ""
echo "Stopping docker containers via $DOCKER_COMPOSE_FILE..."
docker-compose -f $DOCKER_COMPOSE_FILE down -v --remove-orphans

# Replace old config with the new config
echo ""
echo "Replacing old config [$configDirectory] with the new config [$tempDirectory/jar/unzipped/config/*]..."
rm -fr $configDirectory
mkdir -p $configDirectory
mv $tempDirectory/jar/unzipped/config/* $configDirectory

# Start all thule micro-services
echo ""
echo "Starting docker containers via $DOCKER_COMPOSE_FILE..."
docker-compose -f $DOCKER_COMPOSE_FILE up -d

echo ""
echo "Have finished provisioning the $ENVIRONMENT_NAME environment"
echo "================================================================================"


################################################################################
# Check health of all services
################################################################################

checkHealth $DOCKER_COMPOSE_FILE
healthReturnCode=$?


################################################################################
# Stop all provisioned containers
################################################################################
if [[ $STOP_CONTAINERS == "true" ]]; then
    echo ""
    echo "================================================================================"
    echo "About to stop docker containers for environment $ENVIRONMENT_NAME..."
    echo ""
    docker-compose -f $DOCKER_COMPOSE_FILE down -v --remove-orphans

    echo ""
    echo "Have stopped docker containers for environment $ENVIRONMENT_NAME"
    echo "================================================================================"
fi


################################################################################
# Log the start/end of this scripts execution
################################################################################
elapsedSeconds=$(($(date +%s) - $startTime))
echo
echo "================================================================================"
echo "$SCRIPT_NAME finished at $(date '+%F %T') taking $elapsedSeconds seconds"
echo "================================================================================"

exit $healthReturnCode