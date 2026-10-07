FROM node:20-alpine AS frontend-builder

WORKDIR /build/frontend

COPY apps/react-app/package.json apps/react-app/package-lock.json ./
RUN npm ci

COPY apps/react-app/ ./
RUN npx vite build


FROM maven:3.9-eclipse-temurin-17 AS backend-builder

WORKDIR /build/backend

COPY apps/SE-form-backend/pom.xml ./
RUN mvn --batch-mode --no-transfer-progress dependency:go-offline

COPY apps/SE-form-backend/src ./src
COPY --from=frontend-builder /build/frontend/dist ./src/main/resources/static

RUN mvn --batch-mode --no-transfer-progress -DskipTests package


FROM eclipse-temurin:17-jre

WORKDIR /app

COPY --from=backend-builder /build/backend/target/SE_form_backend-0.0.1-SNAPSHOT.jar ./app.jar

EXPOSE 9090

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
