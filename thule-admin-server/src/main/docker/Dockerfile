FROM openjdk:8-jre-alpine

################################  Create user  ################################
ENV GID 1000
ENV UID 1000
ENV USER app

RUN \
    addgroup -g $GID $USER && \
    adduser -D -G $USER -s /bin/bash -u $UID $USER

################################  Install Spring Boot App ################################
# Add a VOLUME pointing to "/tmp" because that is where a Spring Boot application creates working directories for Tomcat by default.
VOLUME /tmp

# Copy the app's jar file to the container
COPY *.jar /

################################  Create entrypoint  ################################
COPY docker-entrypoint.sh /
RUN chmod +x /docker-entrypoint.sh
ENTRYPOINT ["/docker-entrypoint.sh"]
CMD ["app"]
