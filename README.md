## Run with Docker

This repository includes a Dockerfile and docker-compose.yml to build the DSL artifacts and run the DMS Spring Boot API.

Requirements:
- Docker Desktop

Build and run (PowerShell):

```powershell
# Build the image (multi-stage: builds DSL parent and DMS)
docker compose build

# Run the API on http://localhost:8080
docker compose up -d

# View logs
docker compose logs -f dms-api

# Stop
docker compose down
```

Environment variables:
- SERVER_PORT (default 8080)
- JAVA_OPTS (e.g. -Xms256m -Xmx1g)
# design-model-storage