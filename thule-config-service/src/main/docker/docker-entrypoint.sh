#! /bin/bash
set -e

if [ "$1" = "app" ]; then
    # Start the app
    jarFileName=$(basename *.jar)
    echo "Starting $jarFileName..."
    echo ""

    # Use a non-blocking entropy source (i.e. /dev/urandom) instead of the default blocking entropy source (i.e. /dev/random)
    # to reduce the startup time of Tomcat which is used by Spring boot as the default web container
    # See http://wiki.apache.org/tomcat/HowTo/FasterStartUp#Entropy_Source
    ls -al /
    ls -al /dev/./urandom
    exec chroot --skip-chdir --userspec $USER:$USER / java -Djava.security.egd=file:/dev/./urandom -jar $jarFileName
fi

exec "$@"
