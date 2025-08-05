#!/bin/bash
# deploy.sh - Script simple pour déployer
cd ..

# 1. Build ton application Spring Boot
echo "🔨 Build de l'application..."
mvn clean package -DskipTests

# 2. Build l'image Docker
echo "🐳 Build de l'image Docker..."
docker build -t soosmartfact_api:latest .

# 3. Arrêter l'ancien conteneur (si il existe)
echo "🛑 Arrêt de l'ancien conteneur..."
docker stop soosmartfact_api 2>/dev/null || true
docker rm soosmartfact_api 2>/dev/null || true

# 4. Démarrer le nouveau conteneur avec tes variables
echo "🚀 Démarrage du nouveau conteneur..."
docker run -d \
  --name soosmartfact_api \
  -p 9000:8080 \
  soosmartfact_api:latest

echo "✅ Application déployée"

# Voir les logs
echo "📋 Logs de l'application:"
docker logs -f soosmartfact_api