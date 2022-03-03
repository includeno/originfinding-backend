# dockercompose command for app

cd app/docker-compose

## pull
```
docker pull openjdk:11.0.13-jdk
docker pull includeno/jdk11chrome:openjdk-11.0.13-jdkchrome99.0.4844.51
```

## run
```
docker-compose --env-file app.env -f docker-compose-app-withenv.yml  up -d
```

## inspect
```
docker-compose -f docker-compose-app-withenv.yml logs -f api

docker-compose -f docker-compose-app-withenv.yml logs spider1
docker-compose -f docker-compose-app-withenv.yml logs spider2
docker-compose -f docker-compose-app-withenv.yml logs api
docker-compose -f docker-compose-app-withenv.yml logs api > /root/api.log

docker-compose -f docker-compose-app-withenv.yml exec api /bin/bash
docker-compose -f docker-compose-app-withenv.yml exec spider1 /bin/bash
docker-compose -f docker-compose-app-withenv.yml exec spider2 /bin/bash
```

## stop&remove container
```
docker-compose -f docker-compose-app-withenv.yml stop
docker-compose -f docker-compose-app-withenv.yml down
```

## remove image
```
docker image rm includeno/originfinding-sql
docker image rm includeno/originfinding-spider
```