FROM openjdk:11.0.10-jdk

EXPOSE 8080
WORKDIR /app

#Version
ENV VERSION="1.0-SNAPSHOT"
ENV NAME="originfinding-spider"

#Environment
ENV consul_host="127.0.0.1"
ENV consul_port="8500"
ENV consul_service_name="api"
ENV consul_service_id="01"
ENV server_port="8080"
ENV datasource="jdbc:mysql://127.0.0.1:3306/demo?useUnicode=true&characterEncoding=utf8&serverTimezone=UTC"
ENV username="root"
ENV password="root"

#add jar file
ADD ${NAME}/target/${NAME}-${VERSION}.jar /app/application.jar
#ENTRYPOINT ["java","-jar","/app/application.jar","--dockerconfig=true"]
CMD java -jar /app/application.jar --server.port=${server_port} \
 --spring.cloud.consul.host=${consul_host} --spring.cloud.consul.port=${consul_port} \
 --spring.cloud.consul.discovery.service-name=${consul_service_name} --spring.cloud.consul.discovery.instance-id=${consul_service_id} \
 --spring.datasource.url=${datasource} --spring.datasource.username=${username} spring.datasource.password=${password}