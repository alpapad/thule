FROM adoptopenjdk/openjdk12:slim

################################  Create user  ################################
ENV GID 1000
ENV UID 1000
ENV USER spring

RUN \
    addgroup --gid $GID $USER && \
    adduser --disabled-password --gecos "" --gid $GID --shell /bin/bash --uid $UID $USER

################################  Add SSL certificates to Java trust store  ################################
COPY /ssl-certificates /ssl-certificates
COPY import-ssl-certificates.sh /
RUN chmod +x /import-ssl-certificates.sh && \
    /import-ssl-certificates.sh

################################  Install Spring Boot App ################################
# Copy the app's jar file to the container
ARG JAR_FILE
COPY ${JAR_FILE} /
# Save the jar file ARG as an environment variable so it can be accessed by the ENTRYPOINT
ENV JAR_FILE ${JAR_FILE}

################################  Expose private ports ################################
# Tomcat port
EXPOSE 8080

################################  Create entrypoint  ################################
# Use a non-blocking entropy source (i.e. /dev/urandom) instead of the default blocking entropy source (i.e. /dev/random)
# to reduce the startup time of Tomcat which is used by Spring boot as the default web container
# See http://wiki.apache.org/tomcat/HowTo/FasterStartUp#Entropy_Source
ENTRYPOINT chroot --skip-chdir --userspec $USER:$USER / java -Djava.security.egd=file:/dev/./urandom -jar ${JAR_FILE}
