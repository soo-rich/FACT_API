# syntax=docker/dockerfile:1

################################################################################
# Dockerfile optimisé pour FACTS API - Spring Boot 3.3.5 / Java 17
# Multi-stage build pour réduire la taille de l'image finale
################################################################################

# Stage 1: Résolution et téléchargement des dépendances
FROM eclipse-temurin:17-jdk-jammy AS deps

LABEL stage=builder
LABEL description="Dependency resolution stage"

WORKDIR /build

# Copier le wrapper Maven avec permissions exécutables
COPY --chmod=0755 mvnw mvnw
COPY .mvn/ .mvn/

# Télécharger les dépendances (avec cache Docker pour optimiser les builds)
RUN --mount=type=bind,source=pom.xml,target=pom.xml \
    --mount=type=cache,target=/root/.m2 \
    ./mvnw dependency:go-offline -DskipTests

################################################################################

# Stage 2: Build de l'application
FROM deps AS package

LABEL stage=builder
LABEL description="Application build stage"

WORKDIR /build

# Copier le code source
COPY ./src src/

# Builder l'application (uber JAR) avec optimisations
RUN --mount=type=bind,source=pom.xml,target=pom.xml \
    --mount=type=cache,target=/root/.m2 \
    ./mvnw clean package -DskipTests -Dmaven.test.skip=true && \
    mv target/$(./mvnw help:evaluate -Dexpression=project.artifactId -q -DforceStdout)-$(./mvnw help:evaluate -Dexpression=project.version -q -DforceStdout).jar target/app.jar

################################################################################

# Stage 3: Extraction en layers (optimisation du cache Docker)
# Spring Boot Layer Tools permet de séparer l'application en couches
# pour optimiser les rebuilds Docker (seules les couches modifiées sont reconstruites)
FROM package AS extract

LABEL stage=builder
LABEL description="JAR extraction stage"

WORKDIR /build

# Extraire le JAR en layers séparés
RUN java -Djarmode=layertools -jar target/app.jar extract --destination target/extracted

################################################################################

# Stage 4: Image finale de production (JRE seulement, image minimale)
FROM eclipse-temurin:17-jre-jammy AS final

# Metadata de l'image
LABEL maintainer="SooSmart Team"
LABEL application="FACTS API"
LABEL version="0.0.1-SNAPSHOT"
LABEL description="API de gestion des factures FACTS"
LABEL java.version="17"
LABEL spring-boot.version="3.3.5"

# Installation de curl pour le healthcheck
RUN apt-get update && \
    apt-get install -y --no-install-recommends curl && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# Créer un utilisateur non-privilégié pour la sécurité
ARG UID=10001
ARG GID=10001
RUN groupadd -g "${GID}" appgroup && \
    adduser \
    --disabled-password \
    --gecos "" \
    --home "/nonexistent" \
    --shell "/sbin/nologin" \
    --no-create-home \
    --uid "${UID}" \
    --gid "${GID}" \
    appuser

# Créer le répertoire de travail avec les bonnes permissions
WORKDIR /app
RUN chown -R appuser:appgroup /app

USER appuser

# Variables d'environnement
ENV SPRING_PROFILES_ACTIVE=prod \
    JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -XX:+UseG1GC" \
    SERVER_PORT=4000

# Copier les layers extraits (ordre optimisé pour le cache)
COPY --from=extract --chown=appuser:appgroup /build/target/extracted/dependencies/ ./
COPY --from=extract --chown=appuser:appgroup /build/target/extracted/spring-boot-loader/ ./
COPY --from=extract --chown=appuser:appgroup /build/target/extracted/snapshot-dependencies/ ./
COPY --from=extract --chown=appuser:appgroup /build/target/extracted/application/ ./

# Exposition du port
EXPOSE ${SERVER_PORT}

# Healthcheck amélioré
HEALTHCHECK --interval=30s --timeout=10s --start-period=90s --retries=3 \
    CMD curl -f http://localhost:${SERVER_PORT}/api/actuator/health || exit 1

# Point d'entrée avec support des variables JAVA_OPTS
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} org.springframework.boot.loader.launch.JarLauncher"]
