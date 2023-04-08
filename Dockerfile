FROM openjdk:17
ADD target/maven-test.jar maven-test.jar
ENTRYPOINT ["java", "-jar","maven-test.jar"]
