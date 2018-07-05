FROM pooh:8082/oracle-java:8u162-jre

################################  Create user  ################################
ENV GID 1000
ENV UID 1000
ENV USER spring

RUN \
    addgroup --gid $GID $USER && \
    adduser --disabled-password --gecos "" --gid $GID --shell /bin/bash --uid $UID $USER

################################  Install Spring Boot App ################################
# Copy the app's jar file to the container
ARG JAR_FILE
COPY ${JAR_FILE} /

################################  Expose private ports ################################
# Tomcat port
EXPOSE 8080

################################  Create entrypoint  ################################
COPY docker-entrypoint.sh /
RUN chmod +x /docker-entrypoint.sh
ENTRYPOINT ["/docker-entrypoint.sh"]
CMD ["app"]

