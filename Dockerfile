FROM maven:3.9-amazoncorretto-21-al2023

WORKDIR /app

COPY pom.xml .

COPY src ./src

RUN mvn clean package

EXPOSE 4000

CMD ["java", "-jar", "target/stock-0.0.1.jar"]