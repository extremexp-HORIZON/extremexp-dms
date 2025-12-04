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

### 1. POST /api/workflow2dsl

Convert workflow JSON to DSL format.

- **Consumes**: `application/json`
- **Produces**: `text/plain`
- **Query Parameter**: `name` (optional) - Name for the generated workflow
- **Request Body**: Workflow JSON (graphical format like examples/med.json)
- **Response**: DSL text content (.xxp format)

Example:

```powershell
curl -X POST "http://localhost:8866/api/workflow2dsl?name=myflow" `
  -H "Content-Type: application/json" `
  --data-binary "@examples/med.json"
```

### 2. POST /api/workflow2json

Convert workflow DSL to JSON format.

- **Consumes**: `text/plain`
- **Produces**: `application/json`
- **Request Body**: DSL text content (.xxp format)
- **Response**: Workflow JSON (graphical format)

Example:

```powershell
curl -X POST "http://localhost:8866/api/workflow2json" `
  -H "Content-Type: text/plain" `
  --data-binary "@examples/med.xxp"
```

### 3. POST /api/experiment2dsl

Convert experiment JSON to DSL format.

- **Consumes**: `application/json`
- **Produces**: `text/plain`
- **Query Parameter**: `scope` (optional) - Scope of conversion: `root` (default), `experiment`, or `workflow`
- **Request Body**: Experiment JSON
- **Response**: DSL text content (.xxp format)

Example:

```powershell
curl -X POST "http://localhost:8866/api/experiment2dsl?scope=root" `
  -H "Content-Type: application/json" `
  --data-binary "@examples/experiment.json"
```

### 4. POST /api/experiment2json

Convert experiment DSL to JSON format.

- **Consumes**: `text/plain`
- **Produces**: `application/json`
- **Request Body**: DSL text content (.xxp format)
- **Response**: Experiment JSON
- **Status**: Work in progress

Example:

```powershell
curl -X POST "http://localhost:8866/api/experiment2json" `
  -H "Content-Type: text/plain" `
  --data-binary "@examples/experiment.xxp"
```

## Notes

- The DSL framework is in the `extremexp-dsl-framework` submodule. Ensure it is present before local or Docker builds.
- The Spring Boot app binds to 0.0.0.0 by default and listens on port 8866 unless overridden.