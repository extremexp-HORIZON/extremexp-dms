# Multi-stage build for Extremexp DMS
# 1) Build Tycho-based DSL artifacts and the Spring Boot DMS app with Maven (JDK 21)
# 2) Copy the runnable fat JAR into a lightweight JRE 21 image

FROM maven:3.9-eclipse-temurin-21 AS builder

WORKDIR /workspace

# Leverage Docker layer caching by copying only POMs first
COPY extremexp-dsl-framework/eu.extremexp.dsl.parent/pom.xml extremexp-dsl-framework/eu.extremexp.dsl.parent/pom.xml
COPY extremexp-dsl-framework/eu.extremexp.dsl.parent/eu.extremexp.dsl/pom.xml extremexp-dsl-framework/eu.extremexp.dsl.parent/eu.extremexp.dsl/pom.xml
COPY extremexp-dsl-framework/eu.extremexp.dsl.parent/eu.extremexp.dsl.ide/pom.xml extremexp-dsl-framework/eu.extremexp.dsl.parent/eu.extremexp.dsl.ide/pom.xml
COPY extremexp-dsl-framework/eu.extremexp.dsl.parent/eu.extremexp.dsl.ui/pom.xml extremexp-dsl-framework/eu.extremexp.dsl.parent/eu.extremexp.dsl.ui/pom.xml
COPY extremexp-dsl-framework/eu.extremexp.dsl.parent/eu.extremexp.dsl.target/pom.xml extremexp-dsl-framework/eu.extremexp.dsl.parent/eu.extremexp.dsl.target/pom.xml
COPY eu.extremexp.dms/pom.xml eu.extremexp.dms/pom.xml

# Pre-fetch dependencies for better cache hits
RUN --mount=type=cache,target=/root/.m2 \
    mvn -q -f extremexp-dsl-framework/eu.extremexp.dsl.parent/pom.xml -DskipTests -Dtycho.disableP2Mirrors=true dependency:go-offline || true && \
    mvn -q -f eu.extremexp.dms/pom.xml -DskipTests dependency:go-offline || true

# Now add the full sources
COPY extremexp-dsl-framework/eu.extremexp.dsl.parent extremexp-dsl-framework/eu.extremexp.dsl.parent
COPY eu.extremexp.dms eu.extremexp.dms

# Build the DSL stack first (Tycho) so the artifacts are installed to the local repo
RUN --mount=type=cache,target=/root/.m2 \
    mvn -f extremexp-dsl-framework/eu.extremexp.dsl.parent/pom.xml -DskipTests clean install

# Build the Spring Boot app (produces a fat jar via spring-boot-maven-plugin)
RUN --mount=type=cache,target=/root/.m2 \
    mvn -f eu.extremexp.dms/pom.xml -DskipTests clean package && \
    bash -lc 'set -euo pipefail; JAR=$(ls eu.extremexp.dms/target/*.jar | grep -v "original" | head -n1); echo "Using JAR: $JAR"; cp "$JAR" /tmp/app.jar'


# --- Runtime image ---
FROM eclipse-temurin:21-jre

ENV JAVA_OPTS="" \
    SERVER_PORT=8080 \
    TZ=UTC

WORKDIR /app
COPY --from=builder /tmp/app.jar /app/app.jar

EXPOSE 8080

# Healthcheck (optional): tries to hit actuator if available, otherwise the root
HEALTHCHECK --interval=30s --timeout=5s --start-period=30s --retries=3 \
  CMD sh -c 'wget -qO- http://127.0.0.1:${SERVER_PORT}/actuator/health || wget -qO- http://127.0.0.1:${SERVER_PORT}/ || exit 1'

ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -Dserver.port=${SERVER_PORT} -jar /app/app.jar"]
