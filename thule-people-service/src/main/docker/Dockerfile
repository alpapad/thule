FROM java:8-alpine

################################  Update the base image  ################################
RUN \
    apk add --update bash

################################  Create user  ################################
ENV GID 1000
ENV UID 1000
ENV USER app

RUN \
    addgroup -g $GID $USER && \
    adduser -D -G $USER -s /bin/bash -u $UID $USER

################################  Grab gosu for easy step-down from root  ################################
ENV GOSU_VERSION 1.7
RUN set -x \
    && apk add --no-cache --virtual .gosu-deps \
        dpkg \
        gnupg \
        openssl \
    && wget -O /usr/local/bin/gosu "https://github.com/tianon/gosu/releases/download/$GOSU_VERSION/gosu-$(dpkg --print-architecture)" \
    && wget -O /usr/local/bin/gosu.asc "https://github.com/tianon/gosu/releases/download/$GOSU_VERSION/gosu-$(dpkg --print-architecture).asc" \
    && export GNUPGHOME="$(mktemp -d)" \
    && gpg --keyserver ha.pool.sks-keyservers.net --recv-keys B42F6819007F00F88E364FD4036A9C25BF357DD4 \
    && gpg --batch --verify /usr/local/bin/gosu.asc /usr/local/bin/gosu \
    && rm -r "$GNUPGHOME" /usr/local/bin/gosu.asc \
    && chmod +x /usr/local/bin/gosu \
    && gosu nobody true \
    && apk del .gosu-deps

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
