######### dockerfile #############
FROM maven:3.6.3-aliyun
RUN mkdir -p /hello-world
COPY . /hello-world
WORKDIR /hello-world
RUN mvn -s /usr/share/maven/conf/settings.xml clean package
EXPOSE 8080
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "target/hello-world.jar"]

######### build #############
# docker build -t demo:1.0.0 -f /Users/miven/Project/github/demo-parent/hello-world/docker/Dockerfile /Users/miven/Project/github/demo-parent/hello-world
