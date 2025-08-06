#!/bin/bash
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
API_DIR="$SCRIPT_DIR/../api"
UI_DIR="$SCRIPT_DIR/../ui"

$API_DIR/mvnw -B --file $API_DIR/pom.xml \
    resources:resources \
    compiler:compile \
    jar:jar

java -jar $API_DIR/target/api-1.jar &
API_PID=$!

sleep 1
until curl -fsS http://localhost:8080 | grep -q true; do sleep 1; done

cd "$UI_DIR"
npx ng e2e || {
    kill $API_PID
    wait $API_PID 2>/dev/null || true
    exit 1
}

kill $API_PID
wait $API_PID 2>/dev/null || true
