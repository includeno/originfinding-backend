# dockercompose command for app

root /originfinding-backend

## pull
```
docker pull openjdk:11.0.10-jdk
docker pull includeno/javachrome:openjdk-11.0.10-jdkchrome92.0.4515.43
```

## run
```
docker-compose --env-file app.env -f docker-compose-app-withenv.yml  up -d
```

## inspect
```
docker-compose -f docker-compose-app-withenv.yml logs spider1
docker-compose -f docker-compose-app-withenv.yml logs spider2
docker-compose -f docker-compose-app-withenv.yml logs api
docker-compose -f docker-compose-app-withenv.yml logs api > /root/api.log
```

## stop&remove container
```
docker-compose -f docker-compose-app-withenv.yml stop
docker-compose -f docker-compose-app-withenv.yml down
```

## remove image
```
docker image rm includeno/originfinding-api
docker image rm includeno/originfinding-spider
```