# build-and-push.ps1 - Script PowerShell pour builder et pousser sur Docker Hub
# Équivalent du script bash pour Windows

# Variables (à modifier avec tes infos)
$DOCKER_USERNAME = "sooulrich933"
$APP_NAME = "soosmart-facts-api"
$VERSION = Get-Date -Format "yyyyMMdd-HHmmss"  # Timestamp pour versionner

# Fonction pour afficher des messages colorés
function Write-ColorOutput {
    param(
        [string]$Message,
        [string]$Color = "White"
    )
    Write-Host $Message -ForegroundColor $Color
}

Write-ColorOutput "🚀 Début du déploiement..." "Cyan"

# Revenir au répertoire parent
# Set-Location ..+

# 1. Build l'application Spring Boot
Write-ColorOutput "`n📦 Build de l'application Spring Boot..." "Cyan"
& ./mvnw.cmd clean package -DskipTests

if ($LASTEXITCODE -ne 0) {
    Write-ColorOutput "❌ Erreur lors du build Maven" "Red"
    exit 1
}

Write-ColorOutput "✅ Build Maven réussi" "Green"

# 2. Build l'image Docker avec tags
Write-ColorOutput "`n🐳 Build de l'image Docker..." "Cyan"

# Build avec version
docker build -t "${DOCKER_USERNAME}/${APP_NAME}:${VERSION}" .
if ($LASTEXITCODE -ne 0) {
    Write-ColorOutput "❌ Erreur lors du build Docker (version)" "Red"
    exit 1
}

# Build avec tag latest
docker build -t "${DOCKER_USERNAME}/${APP_NAME}:latest" .
if ($LASTEXITCODE -ne 0) {
    Write-ColorOutput "❌ Erreur lors du build Docker (latest)" "Red"
    exit 1
}

Write-ColorOutput "✅ Images Docker créées" "Green"

# 3. Login Docker Hub (si pas déjà connecté)
Write-ColorOutput "`n🔐 Connexion à Docker Hub..." "Cyan"
docker login

if ($LASTEXITCODE -ne 0) {
    Write-ColorOutput "❌ Erreur de connexion Docker Hub" "Red"
    exit 1
}

Write-ColorOutput "✅ Connexion réussie" "Green"

# 4. Push sur Docker Hub
Write-ColorOutput "`n⬆️  Push vers Docker Hub..." "Cyan"

# Push version
Write-ColorOutput "  → Pushing version ${VERSION}..." "Yellow"
docker push "${DOCKER_USERNAME}/${APP_NAME}:${VERSION}"
if ($LASTEXITCODE -ne 0) {
    Write-ColorOutput "❌ Erreur lors du push (version)" "Red"
    exit 1
}

# Push latest
Write-ColorOutput "  → Pushing latest..." "Yellow"
docker push "${DOCKER_USERNAME}/${APP_NAME}:latest"
if ($LASTEXITCODE -ne 0) {
    Write-ColorOutput "❌ Erreur lors du push (latest)" "Red"
    exit 1
}

# 5. Résumé et commandes pour la production
Write-ColorOutput "`n✅ Image poussée avec succès !" "Green"
Write-ColorOutput "📍 Image disponible: ${DOCKER_USERNAME}/${APP_NAME}:${VERSION}" "Green"
Write-ColorOutput "📍 Image latest: ${DOCKER_USERNAME}/${APP_NAME}:latest" "Green"

Write-ColorOutput "`n📋 Commandes pour ton serveur de production:" "Cyan"
Write-ColorOutput @"

# Sur ton serveur de production, exécute:
docker pull ${DOCKER_USERNAME}/${APP_NAME}:latest

# Avec variables d'environnement depuis un fichier .env
docker run -d \
  --name $APP_NAME \
  --env-file .env \
  -p 8080:4000 \
  --restart unless-stopped \
  ${DOCKER_USERNAME}/${APP_NAME}:latest

# OU avec variables en ligne de commande
docker run -d \
  --name $APP_NAME \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://host:5432/db \
  -e SPRING_DATASOURCE_USERNAME=user \
  -e SPRING_DATASOURCE_PASSWORD=pass \
  -p 8080:4000 \
  --restart unless-stopped \
  ${DOCKER_USERNAME}/${APP_NAME}:latest

# Voir les logs
docker logs -f $APP_NAME

# Arrêter et supprimer le conteneur
docker stop $APP_NAME && docker rm $APP_NAME

"@ "White"

Write-ColorOutput "🎉 Déploiement terminé !" "Green"

# Afficher la taille des images
Write-ColorOutput "`n📊 Taille des images:" "Cyan"
docker images | Select-String "$APP_NAME"

# Proposer de nettoyer les images de build
Write-ColorOutput "`n🧹 Voulez-vous nettoyer les images de build intermédiaires? (O/N)" "Yellow"
$response = Read-Host
if ($response -eq "O" -or $response -eq "o") {
    docker image prune -f --filter label=stage=builder
    Write-ColorOutput "✅ Images de build nettoyées" "Green"
}
