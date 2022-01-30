FROM includeno/javachrome:openjdk-11.0.10-jdkchrome92.0.4515.43

EXPOSE 8080
WORKDIR /app

#Version
ENV VERSION="1.0-SNAPSHOT"
ENV NAME="originfinding-spider"

#Environment
ENV consul_host="127.0.0.1"
ENV consul_port="8500"
ENV consul_service_name="spider"
ENV consul_service_id="service"
ENV server_port="8080"
ENV spider_count="4"

#kafka
ENV kafka_server="127.0.0.1:9092"

#mysql
ENV datasource='jdbc:mysql://127.0.0.1:3306/demo?useUnicode=true&characterEncoding=utf8&serverTimezone=UTC+8'
ENV datasource_username='root'
ENV datasource_password='root'

#add jar file
ADD ${NAME}/target/${NAME}-${VERSION}.jar /app/application.jar
#ENTRYPOINT ["java","-jar","/app/application.jar","--dockerconfig=true"]
CMD java -jar /app/application.jar --server.port=${server_port} --spider.count=${spider_count} \
 --spring.cloud.consul.host=${consul_host} --spring.cloud.consul.port=${consul_port} \
 --spring.kafka.bootstrap-servers=${kafka_server} \
 --spring.datasource.url="${datasource}" --spring.datasource.username=${datasource_username} --spring.datasource.password="${datasource_password}" \
 --spring.cloud.consul.discovery.service-name=${consul_service_name} --spring.cloud.consul.discovery.instance-id=${consul_service_id}