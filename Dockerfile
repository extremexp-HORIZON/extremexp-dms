FROM maven:3.9-eclipse-temurin-21

WORKDIR /app

# 1) Copy the repo into the image
COPY . .

# Ensure GNU make and git are available (useful if you want to invoke `make` inside the container)
RUN apt-get update && \
	apt-get install -y --no-install-recommends make git && \
	rm -rf /var/lib/apt/lists/*

# 2) Init submodules (best-effort)
RUN make


ENV JAVA_OPTS="" \
	SERVER_PORT=8866

EXPOSE 8866

# 5) Run the packaged Spring Boot jar on port 8866
CMD ["sh", "-c", "set -e; JAR=$(ls eu.extremexp.dms/target/*.jar | grep -v original | head -n1); exec java $JAVA_OPTS -Dserver.port=${SERVER_PORT} -jar \"$JAR\""]

