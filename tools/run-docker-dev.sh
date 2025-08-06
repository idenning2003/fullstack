#!/bin/bash
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
ROOT_DIR="$SCRIPT_DIR/../"

# Building containers (mostly generating Open API code for the frontend)
docker compose -f "$ROOT_DIR/compose.yml" -f "$ROOT_DIR/compose.dev.yml" down
docker compose -f "$ROOT_DIR/compose.yml" -f "$ROOT_DIR/compose.dev.yml" up --build -d api
echo "Waiting for API to be healthy..."
until [ "$(docker inspect -f '{{.State.Health.Status}}' "$(docker compose ps -q api)")" = "healthy" ]; do
    sleep 1
done
docker compose -f "$ROOT_DIR/compose.yml" build --build-arg CACHEBUST=$(date +%s) ui

# Start up production application
docker compose -f "$ROOT_DIR/compose.yml" -f "$ROOT_DIR/compose.dev.yml" up -d
