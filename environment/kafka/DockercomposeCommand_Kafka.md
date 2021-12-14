# dockercompose command for kafka

## image
```
docker pull zookeeper:3.5.9
docker pull wurstmeister/kafka:2.13-2.6.0
```

## run
```
docker-compose --env-file app.env -f docker-compose-kafka.yml  up -d
```

## inspect
```
docker-compose -f docker-compose-kafka.yml logs zookeeper
docker-compose -f docker-compose-kafka.yml logs kafka01
```

## stop&remove container
```
docker-compose -f docker-compose-kafka.yml stop
docker-compose -f docker-compose-kafka.yml down
```

## remove image
```
docker image rm wurstmeister/kafka:2.13-2.6.0
```
