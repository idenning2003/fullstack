#!/bin/bash
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
ROOT_DIR="$SCRIPT_DIR/../"

docker compose -f "$ROOT_DIR/compose.yml" down
docker compose -f "$ROOT_DIR/compose.yml" up --build -d db api
echo "Waiting for API to be healthy..."
until [ "$(docker inspect -f '{{.State.Health.Status}}' "$(docker compose ps -q api)")" = "healthy" ]; do
    sleep 1
done

docker compose -f "$ROOT_DIR/compose.yml" build --build-arg CACHEBUST=$(date +%s) ui
docker compose -f "$ROOT_DIR/compose.yml" up -d ui
