# Usa una imagen base con Java 17 y Alpine Linux
FROM adoptopenjdk:17-jdk-hotspot-alpine3.15

# Establece el directorio de trabajo en /app
WORKDIR /app

# Copia el JAR construido en el contenedor
COPY target/*.jar app.jar

# Expone el puerto 8080
EXPOSE 8080

# Comando para ejecutar la aplicación al iniciar el contenedor
CMD ["java", "-jar", "app.jar"]
