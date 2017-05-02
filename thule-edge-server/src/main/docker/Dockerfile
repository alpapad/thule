FROM java:8-alpine

################################  Update the base image  ################################
# su-exec as an alternative to chroot which is not available in alpine for easy step-down from root
RUN apk add --update bash su-exec

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

# Touch the jar file so that it has a file modification time (Docker creates all container files in an "unmodified" state by default).
# Any static content (e.g. "index.html") would require the file to have a modification time.
RUN touch *.jar

################################  Create entrypoint  ################################
COPY docker-entrypoint.sh /
RUN chmod +x /docker-entrypoint.sh
ENTRYPOINT ["/docker-entrypoint.sh"]
CMD ["app"]
