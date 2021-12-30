FROM openjdk:11.0.10-jdk

EXPOSE 8080
WORKDIR /app

#Version
ENV VERSION="1.0-SNAPSHOT"
ENV NAME="originfinding-sql"

#server
ENV server_port="8080"

#consul
ENV consul_host="127.0.0.1"
ENV consul_port="8500"
ENV consul_service_name="api"
ENV consul_service_id="service"

#kafka
ENV kafka_server="127.0.0.1:9092"

#mysql
ENV datasource='jdbc:mysql://rm-uf646cc87yqoi7lu6lo.mysql.rds.aliyuncs.com:3306/demo?useUnicode=true&characterEncoding=utf8&serverTimezone=UTC'
ENV datasource_username='root'
ENV datasource_password='E3!ZivFF9gbQZ'

#redis
ENV redis_host="127.0.0.1"
ENV redis_port="6379"
ENV redis_password="redis123456"
ENV redis_database="1"

#add jar file
ADD ${NAME}/target/${NAME}-${VERSION}.jar /app/application.jar
#ENTRYPOINT ["java","-jar","/app/application.jar","--dockerconfig=true"]
CMD java -jar /app/application.jar --server.port=${server_port} \
 --spring.cloud.consul.host=${consul_host} --spring.cloud.consul.port=${consul_port} \
 --spring.cloud.consul.discovery.service-name=${consul_service_name} --spring.cloud.consul.discovery.instance-id=${consul_service_id} \
 --spring.kafka.bootstrap-servers=${kafka_server} \
 --spring.datasource.url="${datasource}" --spring.datasource.username=${datasource_username} --spring.datasource.password="${datasource_password}" \
 --spring.redis.host=${redis_host} --spring.redis.port=${redis_port} --spring.redis.password=${redis_password} --spring.redis.database=${redis_database}