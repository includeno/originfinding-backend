# MySQL环境

## image
```
docker pull mysql:5.7.33
```

## run
```
mkdir -p /docker/mysql/{data,conf.d}

docker run --name mysql_container -p 3306:3306 \
-e MYSQL_ROOT_PASSWORD=root \
-e MYSQL_USER=test \
-e MYSQL_PASS=123456 \
-e MYSQL_DATABASE=demo \
-d mysql:5.7.33
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
docker image rm mysql:5.7.33
```



