FROM openjdk:11.0.13-jdk

EXPOSE 8080
WORKDIR /app

#Version
ENV VERSION="1.0-SNAPSHOT"
ENV NAME="originfinding-sql"

#server
ENV server_port="8080"

#mysql
ENV datasource='jdbc:mysql://127.0.0.1:3306/demo?useUnicode=true&characterEncoding=utf8&serverTimezone=UTC+8'
ENV datasource_username='root'
ENV datasource_password='root'

#redis
ENV redis_host="127.0.0.1"
ENV redis_port="6379"
ENV redis_password="redis123456"
ENV redis_database="1"

#add jar file
ADD ${NAME}/target/${NAME}-${VERSION}.jar /app/application.jar
#ENTRYPOINT ["java","-jar","/app/application.jar","--dockerconfig=true"]
CMD java -jar /app/application.jar --server.port=${server_port} \
 --spring.kafka.bootstrap-servers=${kafka_server} \
 --spring.datasource.url="${datasource}" --spring.datasource.username=${datasource_username} --spring.datasource.password="${datasource_password}" \
 --spring.redis.host=${redis_host} --spring.redis.port=${redis_port} --spring.redis.password=${redis_password} --spring.redis.database=${redis_database}