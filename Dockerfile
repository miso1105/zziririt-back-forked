FROM amazoncorretto:17
ARG JAR_PATH=build/libs/*.jar
COPY ${JAR_PATH} /home/server.jar
ENV LOG_DIR=/home/logs
RUN mkdir -p ${LOG_DIR}
ENTRYPOINT ["java","-jar","-Dspring.profiles.active=prod","-Dlogging.file.name=${LOG_DIR}/spring.log","/home/server.jar"]