FROM openjdk:11.0.10-jdk

EXPOSE 8080
WORKDIR /app

#Version
ENV VERSION="1.0-SNAPSHOT"
ENV NAME="originfinding-sql"

#Environment
ENV consul_host="127.0.0.1"
ENV consul_port="8500"
ENV consul_service_name="api"
ENV consul_service_id="01"
ENV server_port="8080"
ENV kafka_server="127.0.0.1:9092"

#add jar file
ADD ${NAME}/target/${NAME}-${VERSION}.jar /app/application.jar
#ENTRYPOINT ["java","-jar","/app/application.jar","--dockerconfig=true"]
CMD java -jar /app/application.jar --server.port=${server_port} \
 --spring.cloud.consul.host=${consul_host} --spring.cloud.consul.port=${consul_port} \
 --spring.cloud.consul.discovery.service-name=${consul_service_name} --spring.cloud.consul.discovery.instance-id=${consul_service_id} \
 --spring.kafka.bootstrap-servers=${kafka_server}