# Usa una imagen base de OpenJDK 17
FROM openjdk:17-jdk-slim

# Argumento para el JAR_FILE
ARG JAR_FILE=target/*.jar

# Copia el archivo .jar de la aplicación al contenedor
COPY ${JAR_FILE} app.jar

# Expone el puerto 8080 para que la aplicación sea accesible
EXPOSE 8080

# Comando para ejecutar la aplicación cuando el contenedor inicie
ENTRYPOINT ["java","-jar","/app.jar"]
