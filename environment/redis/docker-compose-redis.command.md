# dockercompose command for redis

## image
```
docker pull redis:6.0.16
docker pull redis:6.2.6
```

## run
```
docker-compose -f docker-compose-redis.yml  up -d
```

## inspect
```
docker-compose -f docker-compose-redis.yml logs redis
```

## stop&remove container
```
docker-compose -f docker-compose-redis.yml stop
docker-compose -f docker-compose-redis.yml down
```

## remove image
```
docker image rm redis:6.0.16
docker image rm redis:6.2.6
```
