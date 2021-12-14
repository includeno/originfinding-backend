# MySQL环境

## image
```
docker pull mysql:8.0.27
```

## run
```
mkdir -p /docker/mysql/{data,conf.d}

docker run --name mysql_container -p 3306:3306 \
-v /docker/mysql/conf.d:/etc/mysql/conf.d \
-v /docker/mysql/data:/var/lib/mysql \
-e MYSQL_ROOT_PASSWORD=root \
-e MYSQL_USER=test \
-e MYSQL_PASS=123456 \
-e MYSQL_DATABASE=demo \
-d mysql:8.0.27
```


## inspect
```
docker logs mysql_container
```

## stop&remove container
```
docker stop mysql_container
docker rm mysql_container
```

## remove image
```
docker image rm mysql:8.0.27
```



