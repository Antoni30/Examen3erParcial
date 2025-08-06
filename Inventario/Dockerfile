# Usar una imagen base con OpenJDK 21
FROM openjdk:21-jdk-slim

# Establecer el directorio de trabajo
WORKDIR /app

# Copiar el archivo pom.xml y el wrapper de Maven
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

# Dar permisos de ejecución al wrapper de Maven
RUN chmod +x mvnw

# Descargar las dependencias (esto se cachea si el pom.xml no cambia)
RUN ./mvnw dependency:go-offline -B

# Copiar el código fuente
COPY src src

# Construir la aplicación
RUN ./mvnw clean package -DskipTests

# Crear un usuario no-root para seguridad
RUN addgroup --system spring && adduser --system spring --ingroup spring
USER spring:spring

# Exponer el puerto 8081
EXPOSE 8081

# Configurar variables de entorno para la base de datos y RabbitMQ
ENV SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/bd_distri
ENV SPRING_DATASOURCE_USERNAME=postgres
ENV SPRING_DATASOURCE_PASSWORD=adminadmin123
ENV SPRING_RABBITMQ_HOST=rabbitmq
ENV SPRING_RABBITMQ_PORT=5672
ENV SPRING_RABBITMQ_USERNAME=admin
ENV SPRING_RABBITMQ_PASSWORD=admin

# Configurar JVM para contenedores
ENV JAVA_OPTS="-Xmx512m -Xms256m -XX:+UseContainerSupport"

# Ejecutar la aplicación
CMD ["sh", "-c", "java $JAVA_OPTS -jar target/Inventario-0.0.1-SNAPSHOT.jar"]
