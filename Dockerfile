FROM maven:3.9-eclipse-temurin-21
# Ensure GNU make and git are available (useful if you want to invoke `make` inside the container)
RUN apt-get update && \
	apt-get install -y --no-install-recommends make git && \
	rm -rf /var/lib/apt/lists/*


WORKDIR /app

# 2) Init submodules (best-effort)
RUN git clone --recurse-submodules https://github.com/extremexp-HORIZON/extremexp-dms.git 

WORKDIR /app/extremexp-dms/extremexp-dsl-framework/eu.extremexp.dsl.parent
RUN mvn clean install

WORKDIR /app/extremexp-dms/eu.extremexp.dms
RUN mvn clean install

ENV JAVA_OPTS="" \
	SERVER_PORT=8866

EXPOSE 8866

# 5) Run the packaged Spring Boot jar on port 8866
CMD ["sh", "-c", "set -e; exec java $JAVA_OPTS -Dserver.port=${SERVER_PORT} -jar /app/extremexp-dms/eu.extremexp.dms/target/eu.extremexp.dms-1.0-SNAPSHOT.jar"]


