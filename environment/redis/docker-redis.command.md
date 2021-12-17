# Redis环境

## image
```
docker pull redis:6.0.16
docker pull redis:6.2.6
```

## run
```
docker run -itd --name redis_container -p 6379:6379 redis:6.0.16 redis-server --appendonly yes --requirepass "redis123456"
```


## inspect
```
docker logs redis_container
docker logs -ft redis_container
```

## stop&remove container
```
docker stop redis_container
docker rm redis_container
```

## remove image
```
docker image rm redis:6.0.16
docker image rm redis:6.2.6
```



