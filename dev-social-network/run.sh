#!/usr/bin/env bash
# Arranca la aplicación cargando las variables del archivo .env (NO subido a Git).
set -euo pipefail

cd "$(dirname "$0")"

ENV_FILE=".env"

if [ -f "$ENV_FILE" ]; then
  echo "Cargando variables desde $ENV_FILE"
  # Exporta cada línea del .env (ignora comentarios y líneas vacías)
  set -a
  # shellcheck disable=SC1090
  source "$ENV_FILE"
  set +a
else
  echo "⚠️  No existe $ENV_FILE. Crea uno a partir de .env.example:"
  echo "    cp .env.example .env"
  exit 1
fi

# Evita arranques duplicados: comprueba si el puerto 8080 ya está en uso
if (ss -tln 2>/dev/null | grep -q ':8080 ') || (netstat -tln 2>/dev/null | grep -q ':8080 '); then
  echo "⚠️  El puerto 8080 ya está en uso. La aplicación ya debe estar corriendo."
  echo "    Para detenerla:  pkill -f spring-boot:run"
  echo "    Para reiniciarla: detener y volver a ejecutar ./run.sh"
  exit 1
fi

echo "▶ Arrancando la aplicación (puerto 8080)..."
exec ./mvnw spring-boot:run