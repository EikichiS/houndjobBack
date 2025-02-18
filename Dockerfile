# Usar una imagen base con JDK 11 y Maven para construir
FROM maven:3.9.6-eclipse-temurin-17 AS build

# Establecer un directorio de trabajo
WORKDIR /app

# Copiar todos los archivos del proyecto al directorio de trabajo
COPY . /app

# Ejecutar Maven para construir el proyecto
RUN mvn clean package

# Crear una nueva imagen basada en OpenJDK 11
FROM eclipse-temurin:17-jre

# Exponer el puerto que utilizará la aplicación
EXPOSE 8001

# Copiar el archivo JAR construido desde la etapa anterior
COPY --from=build /app/target/houndjobApi-0.0.1-SNAPSHOT.jar /app/houndjobApi-0.0.1-SNAPSHOT.jar

# Establecer el punto de entrada para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "/app/houndjobApi-0.0.1-SNAPSHOT.jar"]
