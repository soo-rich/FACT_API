#!/bin/bash
# build-and-push.sh - Script pour builder et pousser sur Docker Hub
# premiere etape
cd ..

# Variables (à modifier avec tes infos)
DOCKER_USERNAME="sooulrich933"
APP_NAME="soosmart-facts-api"
VERSION=$(date +%Y%m%d-%H%M%S)  # ou utilise $(date +%Y%m%d-%H%M%S) pour un timestamp

# Couleurs pour les messages
GREEN='\033[0;32m'
BLUE='\033[0;34m'
RED='\033[0;31m'
NC='\033[0m' # No Color

echo -e "${BLUE}🚀 Début du déploiement...${NC}"

# 1. Build l'application Spring Boot
echo -e "${BLUE}📦 Build de l'application Spring Boot...${NC}"
mvn clean package -DskipTests
if [ $? -ne 0 ]; then
    echo -e "${RED}❌ Erreur lors du build Maven${NC}"
    exit 1
fi

# 2. Build l'image Docker avec tag
echo -e "${BLUE}🐳 Build de l'image Docker...${NC}"
docker build -t $DOCKER_USERNAME/$APP_NAME:"$VERSION" .
docker build -t $DOCKER_USERNAME/$APP_NAME:latest .

if [ $? -ne 0 ]; then
    echo -e "${RED}❌ Erreur lors du build Docker${NC}"
    exit 1
fi

# 3. Login Docker Hub (si pas déjà connecté)
echo -e "${BLUE}🔐 Connexion à Docker Hub...${NC}"
docker login
if [ $? -ne 0 ]; then
    echo -e "${RED}❌ Erreur de connexion Docker Hub${NC}"
    exit 1
fi

# 4. Push sur Docker Hub
echo -e "${BLUE}⬆️  Push vers Docker Hub...${NC}"
docker push $DOCKER_USERNAME/$APP_NAME:$VERSION
docker push $DOCKER_USERNAME/$APP_NAME:latest

if [ $? -ne 0 ]; then
    echo -e "${RED}❌ Erreur lors du push${NC}"
    exit 1
fi

echo -e "${GREEN}✅ Image poussée avec succès !${NC}"
echo -e "${GREEN}📍 Image disponible: ${DOCKER_USERNAME}/${APP_NAME}:${VERSION}${NC}"
echo -e "${GREEN}📍 Image latest: ${DOCKER_USERNAME}/${APP_NAME}:latest${NC}"

# 5. Afficher les commandes pour la production
echo -e "${BLUE}📋 Commandes pour ton serveur de production:${NC}"
echo ""
echo "# Sur ton serveur de production, execute:"
echo "docker pull $DOCKER_USERNAME/$APP_NAME:latest"
echo "docker run -d --name $APP_NAME --env-file .env -p 8080:8080 --restart unless-stopped $DOCKER_USERNAME/$APP_NAME:latest"
echo ""
echo -e "${GREEN}🎉 Déploiement terminé !${NC}"