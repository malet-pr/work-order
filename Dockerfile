FROM amazoncorretto:21
WORKDIR /workorder
COPY ./target/wo-0.0.1.jar .
CMD apt-get update -y
ENTRYPOINT ["java", "-Xmx2048M", "-jar", "wo-0.0.1.jar"]
EXPOSE 8091