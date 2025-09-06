# Dockerfile optimisé pour la production
FROM openjdk:17-jdk-slim

# Installer curl pour les health checks
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

# Créer un utilisateur non-root pour la sécurité
RUN groupadd -r spring && useradd -r -g spring spring

WORKDIR /app

# Créer les dossiers nécessaires avec les bonnes permissions
RUN mkdir -p /app/uploads /app/logs && \
    chown -R spring:spring /app

# Copier le jar
COPY target/*.jar app.jar

# Changer le propriétaire du jar
RUN chown spring:spring app.jar

# Basculer vers l'utilisateur non-root
USER spring

# Configuration JVM optimisée pour la production
ENV JAVA_OPTS="-Xms256m -Xmx1g -XX:+UseG1GC -XX:MaxGCPauseMillis=200 -Dspring.profiles.active=prod"

EXPOSE 4000

# Health check pour Docker
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:4000/actuator/health || exit 1

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]