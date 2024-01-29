FROM openjdk:17-jdk
ARG JAR_FILE=target/*.jar
ADD ${JAR_FILE}  VigiDrive-0.0.1-SNAPSHOT.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/VigiDrive-0.0.1-SNAPSHOT.jar"]