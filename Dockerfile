FROM maven:3.6.3-jdk-8
VOLUME /tmp
RUN mkdir -p /opt/demo-parent
COPY . /opt/demo-parent
WORKDIR /opt/demo-parent
RUN mvn package
EXPOSE 8080
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "hello-world/target/hello-world.jar"]