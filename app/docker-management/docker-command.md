# docker command for app

## pull
```
docker pull openjdk:11.0.10-jdk
docker pull includeno/javachrome:openjdk-11.0.10-jdkchrome92.0.4515.43
docker pull includeno/originfinding-api
docker pull includeno/originfinding-spider

```

## run
```
docker run -it --name originfinding-api-container\
    -e consul_service_name=api \
    -e consul_host=101.43.20.192 \
    -e consul_port=8500 \
    -e machine_id=tencent \
    -e datasource="jdbc:mysql://rm-uf646cc87yqoi7lu6lo.mysql.rds.aliyuncs.com:3306/demo?useUnicode=true&characterEncoding=utf8&serverTimezone=UTC" \
    -e datasource_username=user \
    -e datasource_password=E3*Ziv$FF9gbQZ$ \
    -e kafka_server=101.43.20.192:9092 \
    -e redis_host=101.43.20.192 \
    -e redis_port=6379 \
    -e redis_password=redis123456 \
    -e redis_database=1 \
    -d includeno/originfinding-sql
```


## stop

docker stop originfinding-api-container
docker rm originfinding-api-container

## remove image
```
docker image rm includeno/originfinding-api
docker image rm includeno/originfinding-spider
```