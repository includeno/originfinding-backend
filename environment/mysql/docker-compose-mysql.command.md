# dockercompose command for mysql

## image
```
docker pull mysql:5.7.33
```

## run
```
docker-compose --env-file app.env -f docker-compose-mysql.yml  up -d
```

## inspect
```
docker-compose -f docker-compose-mysql.yml logs mysql
```

## stop&remove container
```
docker-compose -f docker-compose-mysql.yml stop
docker-compose -f docker-compose-mysql.yml down
```

## remove image
```
docker image rm mysql:5.7.33
```
