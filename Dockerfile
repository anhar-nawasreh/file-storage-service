FROM bellsoft/liberica-runtime-container:jre-17-stream-musl

WORKDIR /app

COPY target/file-storage-service-0.0.1-SNAPSHOT.jar /app/file-storage-service-0.0.1-SNAPSHOT.jar

EXPOSE 8083

CMD ["java", "-jar", "file-storage-service-0.0.1-SNAPSHOT.jar"]
