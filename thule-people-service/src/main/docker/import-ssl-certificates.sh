#! /bin/sh
set -e

for sslCertificate in $(find /ssl-certificates/* -type f)
do
    # Extract the certificate name without the .extension
    alias=$(echo $(basename $sslCertificate) | sed 's/\(.*\)\..*/\1/g')
    keytool -importcert -cacerts -noprompt -trustcacerts -alias $alias -file $sslCertificate -storepass changeit
    if [[ $? != 0 ]]; then
        exit -1
    fi
    keytool -list -cacerts -alias $alias -storepass changeit
done
