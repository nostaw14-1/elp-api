FROM eclipse-temurin:17-jre

WORKDIR /app

COPY target/*.jar app.jar

EXPOSE 1924

ENTRYPOINT ["java","-Xms256m","-Xmx512m","-jar","app.jar"]