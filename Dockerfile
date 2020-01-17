FROM openjdk:8
VOLUME /tmp
ADD hello-world/target/hello-world.jar hello-world.jar
EXPOSE 8080
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "hello-world.jar"]