## Install and run

You can run the DMS API locally with Make (Maven) or with Docker.

### Option A — Local (Make + Maven)

Requirements:
- Java 21 (JDK)
- Maven 3.9+
- Git (for submodules)

Steps (PowerShell):

```powershell
# 1) Ensure submodules are checked out
git submodule update --init --recursive

# 2) Build framework parent and DMS (outputs the JAR to eu.extremexp.dms/target)
make

# 3) Run the Spring Boot app (defaults to 0.0.0.0:8866)
java -jar eu.extremexp.dms-1.0-SNAPSHOT.jar
```

Server defaults can be overridden via Spring Boot args:

```powershell
mvn -f eu.extremexp.dms/pom.xml spring-boot:run -Dspring-boot.run.arguments="--server.address=127.0.0.1 --server.port=8080"
```

### Option B — Docker

Requirements:

**docker compose**

```bash
docker compose build
docker compose up -d
```

Environment variables:
- SERVER_PORT (default 8866)
- JAVA_OPTS (e.g. -Xms256m -Xmx1g)

## API endpoints

Base URL defaults to http://localhost:8866 (change if you override the port).

- POST /api/workflow2dsl?name={optionalName}
	- Body: JSON blob (graph JSON like examples/med.json)
	- Response: text/plain DSL content (placeholder format for now)

Example:

```powershell
curl -X POST "http://localhost:8866/api/workflow2dsl?name=myflow"^
	-H "Content-Type: application/json"^
	--data-binary @examples/med.json
```

- POST /api/dsl2workflow
	- Body: text/plain (DSL)
	- Response: application/json (placeholder; 501 Not Implemented)

- POST /api/experiment2dsl
	- Body: application/json (experiment JSON)
	- Response: text/plain (placeholder; 501 Not Implemented)

- POST /api/dsl2experiment
	- Body: text/plain (DSL)
	- Response: application/json (placeholder; 501 Not Implemented)

## Notes

- The DSL framework is in the `extremexp-dsl-framework` submodule. Ensure it is present before local or Docker builds.
- The Spring Boot app binds to 0.0.0.0 by default and listens on port 8866 unless overridden.