FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY . .

RUN chmod +x mvnw && ./mvnw clean install -DskipTests

CMD ["./mvnw", "spring-boot:run", "-DskipTests"]
