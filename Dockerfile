FROM maven:3.9-eclipse-temurin-21

WORKDIR /app

COPY . .

RUN git submodule update --init --recursive


RUN mvn -f extremexp-dsl-framework/eu.extremexp.dsl.parent/pom.xml  clean install

# 4) Build the DMS Spring Boot app
RUN mvn -f eu.extremexp.dms/pom.xml  clean install

ENV JAVA_OPTS="" \
    SERVER_PORT=8866

EXPOSE 8866

# 5) Run the packaged Spring Boot jar on port 8866
CMD ["sh", "-c", "set -e; exec java $JAVA_OPTS -Dserver.port=${SERVER_PORT} -jar eu.extremexp.dms/target/eu.extremexp.dms-1.0-SNAPSHOT.jar"]


