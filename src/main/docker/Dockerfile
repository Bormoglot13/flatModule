FROM adoptopenjdk:15-jre-hotspot
ARG JAR_FILE=*.jar
COPY ${JAR_FILE} application.jar
ENTRYPOINT ["java", "-jar", "application.jar"]

#FROM postgres:13
#COPY postgres/init.sql /docker-entrypoint-initdb.d/10-init.sql
#CMD ["docker-entrypoint.sh", "postgres"]